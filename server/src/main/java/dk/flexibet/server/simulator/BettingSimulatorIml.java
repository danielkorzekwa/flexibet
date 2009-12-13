package dk.flexibet.server.simulator;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTimeUtils;

import dk.bot.bettingengine.BettingEngine;
import dk.bot.bettingengine.BettingEngineResult;
import dk.bot.bettingengine.betapi.BetCancelResult;
import dk.bot.bettingengine.betapi.BetPlaceResult;
import dk.bot.bettingengine.betapi.BetResult;
import dk.bot.bettingengine.betapi.SPBetPlaceResult;
import dk.bot.bettingengine.dao.model.RunnerBet;
import dk.bot.bettingengine.statemachine.StateMachineResult;
import dk.bot.bettingengine.statemachine.StateMachineServiceMBean;
import dk.bot.bettingengine.statemachine.manager.StateMachineStat;
import dk.bot.marketobserver.model.BetStatus;
import dk.bot.marketobserver.model.MUBet;
import dk.bot.marketobserver.model.Market;
import dk.bot.marketobserver.model.MarketData;
import dk.bot.marketobserver.model.MarketRunner;
import dk.bot.marketobserver.model.MarketRunners;
import dk.bot.marketobserver.model.RunnerPrice;
import dk.bot.marketobserver.util.liabilitycalc.LiabilityCalculatorImpl;
import dk.bot.marketobserver.util.liabilitycalc.MarketProbability;
import dk.flexibet.server.dao.HistRunnerPriceDAO;
import dk.flexibet.server.dao.MarketDAO;
import dk.flexibet.server.model.HistRunnerPrice;
import dk.flexibet.server.simulator.betex.BettingExchange;
import dk.flexibet.server.simulator.bettingengine.SimLastBetDao;

/**
 * Simulates betting based on a historical data.
 * 
 * @author daniel
 * 
 */
public class BettingSimulatorIml implements BettingSimulator {

	private final BettingEngine bettingEngine;
	private final MarketDAO marketDao;
	private final HistRunnerPriceDAO histRunnerPriceDao;
	private final StateMachineServiceMBean stateMachineServiceMBean;
	private final BettingExchange bettingExchange;
	private final SimLastBetDao lastBetDao;

	/** Market probabilities based on a last available prices */
	private Map<Integer, MarketProbability> marketProbs = new HashMap<Integer, MarketProbability>();

	public BettingSimulatorIml(BettingExchange bettingExchange, BettingEngine bettingEngine, SimLastBetDao lastBetDao,
			MarketDAO marketDao, HistRunnerPriceDAO histRunnerPriceDao,
			StateMachineServiceMBean stateMachineServiceMBean) {
		this.bettingExchange = bettingExchange;
		this.bettingEngine = bettingEngine;
		this.lastBetDao = lastBetDao;
		this.marketDao = marketDao;
		this.histRunnerPriceDao = histRunnerPriceDao;
		this.stateMachineServiceMBean = stateMachineServiceMBean;
	}

	/**
	 * Runs the simulation and returns betting report.
	 * 
	 * @param marketDataSqlQuery
	 *            sql query to get markets from database to run simulation for, e.g. select * from market where
	 *            market_time>='2009-08-01' and market_time<='2009-08-30'
	 * @param listener
	 * @return
	 */
	public BettingSimulationReport simulate(String marketDataSqlQuery, BettingSimulationListener listener) {

		List<MarketData> markets = marketDao.findMarkets(marketDataSqlQuery);
		if (listener != null) {
			listener.afterGetMarkets(markets.size());
		}
		/** List of analyzed markets (with at least one runner prices record). */
		int numOfAnalyzedMarkets = 0;

		/** Send market events to betting engine for all markets (market by market) */
		for (int marketNumber = 0; marketNumber < markets.size(); marketNumber++) {
			MarketData marketData = markets.get(marketNumber);
			/** Historical runer prices for market. */
			List<HistRunnerPrice> histRunnerPrices = histRunnerPriceDao.findRunnerPrices(marketData.getMarketId());
			if (histRunnerPrices.size() > 0) {
				numOfAnalyzedMarkets++;

				long previousRecordTime = 0;
				/** Runner prices for market that have the same record time */
				List<HistRunnerPrice> marketRunnerPrices = new ArrayList<HistRunnerPrice>();
				Market lastMarketEvent = null;
				for (int priceRecord = 0; priceRecord < histRunnerPrices.size(); priceRecord++) {
					HistRunnerPrice histRunnerPrice = histRunnerPrices.get(priceRecord);
					/** Create market record for previous record time and send to the betting engine. */
					if (previousRecordTime!=0 && histRunnerPrice.getRecordTime().getTime() > previousRecordTime) {
						lastMarketEvent = processNewData(marketData, marketRunnerPrices);
						marketRunnerPrices.clear();
					}

					/** Buffer runner prices for the same record time. */
					marketRunnerPrices.add(histRunnerPrice);
					previousRecordTime = histRunnerPrice.getRecordTime().getTime();
				}
				/**Fire last event*/
				if(marketRunnerPrices.size()>0) {
					lastMarketEvent = processNewData(marketData, marketRunnerPrices);
					marketRunnerPrices.clear();
				}

				/** Update market probability */
				/** key - selectionId */
				Map<Integer, Double> runnerProbs = new HashMap<Integer, Double>();
				for (MarketRunner marketRunner : lastMarketEvent.getMarketRunners().getMarketRunners()) {
					runnerProbs.put(marketRunner.getSelectionId(), 1 / marketRunner.getAvgPrice());
				}
				marketProbs.put(lastMarketEvent.getMarketData().getMarketId(), new MarketProbability(runnerProbs));
				
				if (listener != null) {
					listener.afterMarketAnalysis(numOfAnalyzedMarkets,marketData);
				}
			}
		}

		String bettingReport = createReport();

		double totalLiability = new LiabilityCalculatorImpl().calculateLiability(bettingExchange.getMUBets(),
				marketProbs);

		return new BettingSimulationReport(markets.size(), numOfAnalyzedMarkets, bettingExchange.getNumOfMatchedBets(),
				totalLiability, bettingReport);
	}

	private Market processNewData(MarketData marketData, List<HistRunnerPrice> marketRunnerPrices) {
		List<MUBet> marketMUBets = bettingExchange.getMUBets(marketData.getMarketId());		
		Market lastMarketEvent = createMarketEvent(marketData, marketRunnerPrices,marketMUBets);
		bettingExchange.matchBets(lastMarketEvent.getMarketRunners());
		lastMarketEvent.setMarketMUBets(marketMUBets);
		
		/**Update last bets*/
		for (MUBet bet : bettingExchange.getMUBets()) {
			if(bet.getBetStatus()==BetStatus.M) {
				lastBetDao.updateLastBet(bet.getBetId(),bet.getPrice(),bet.getSize(),bet.getMatchedDate());
			}		
		}
		
		fireEvent(lastMarketEvent);
		return lastMarketEvent;
	}
	
	/**
	 * Creates market record
	 * 
	 * @param marketData
	 * @param marketPrices
	 * @return
	 */
	private Market createMarketEvent(MarketData marketData, List<HistRunnerPrice> marketPrices,List<MUBet> marketMUBets) {
		if (marketPrices.size() == 0) {
			return null;
		}

		/**
		 * inPLayDelay and recordTime are the same for all runner price records, so take them from the first element in
		 * a list.
		 */
		int inPlayDelay = marketPrices.get(0).getInPlayDelay();
		Date recordTime = marketPrices.get(0).getRecordTime();
		/** Set current time. */
		DateTimeUtils.setCurrentMillisFixed(recordTime.getTime());

		/** Build marketRunner object based on a historical record. */
		List<MarketRunner> marketRunnersList = new ArrayList<MarketRunner>(marketPrices.size());
		for (HistRunnerPrice histRunnerPrice : marketPrices) {

			/** Some values are hard coded, because they are not not available in a historical data. */
			List<RunnerPrice> prices = new ArrayList<RunnerPrice>();
			prices.add(new RunnerPrice(histRunnerPrice.getPriceToBack(), 100, 0));
			prices.add(new RunnerPrice(histRunnerPrice.getPriceToLay(), 0, 100));
			MarketRunner marketRunner = new MarketRunner(histRunnerPrice.getSelectionId(), 10, histRunnerPrice
					.getLastPriceMatched(), 0, 0, 0, prices);
			marketRunnersList.add(marketRunner);
		}
		MarketRunners marketRunners = new MarketRunners(marketData.getMarketId(), marketRunnersList, inPlayDelay,
				recordTime);

		Market completeMarket = new Market(marketData, null, null, null);
		completeMarket.setMarketRunners(marketRunners);

		return completeMarket;
	}

	/**
	 * Fire market event and processes results
	 * 
	 * 
	 * @param market
	 */
	private void fireEvent(Market market) {
		if (market != null) {
			BettingEngineResult results = bettingEngine.fireEvent(market);

			for (StateMachineResult result : results.getBetOperations()) {

				for (BetResult betResult : result.getBetResults()) {
					if (betResult instanceof BetPlaceResult) {
						BetPlaceResult betPlaceResult = (BetPlaceResult) betResult;

						RunnerBet runnerBet = new RunnerBet();
						runnerBet.setRunnerStateId(result.getRunnerStateId());
						runnerBet.setBetId(betPlaceResult.getBetId());
						runnerBet.setPlacedDate(betPlaceResult.getBetDate());
						runnerBet.setBetType(betPlaceResult.getBetType());
						runnerBet.setPrice(betPlaceResult.getPrice());
						runnerBet.setSize(betPlaceResult.getSize());
						runnerBet.setSizeMatched(betPlaceResult.getSizeMatched());
						runnerBet.setAvgPriceMatched(betPlaceResult.getAvgPriceMatched());
						runnerBet.setMatchedDate(betPlaceResult.getBetDate());
						lastBetDao.saveLastBet(runnerBet);

					} else if (betResult instanceof SPBetPlaceResult) {
						throw new UnsupportedOperationException();
					} else if (betResult instanceof BetCancelResult) {
						BetCancelResult betCancelResult = (BetCancelResult) betResult;
						lastBetDao.cancelBet(betCancelResult.getBetId(),betCancelResult.getSizeCancelled(),betCancelResult.getSizeMatched());
					}
				}
			}
		}
	}

	/**
	 * Creates report from a betting simulation.
	 * 
	 * @return
	 */
	private String createReport() {
		StringBuilder report = new StringBuilder();

		Map<String, StateMachineStat> stateMachinesStat = stateMachineServiceMBean.getStateMachineStat();
		for (String stateMachineId : stateMachinesStat.keySet()) {
			report.append(stateMachineId + "\n");

			StateMachineStat stateMachineStat = stateMachinesStat.get(stateMachineId);
			for (String stateName : stateMachineStat.getCurrentStates().keySet()) {
				report.append("   - ").append(stateName).append(" ").append(
						stateMachineStat.getCurrentStates().get(stateName));
				report.append("\n");
			}
			report.append("\n");
		}

		return report.toString();
	}
}
