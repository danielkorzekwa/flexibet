package dk.flexibet.server.marketlistener;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;

import dk.bot.bettingengine.BettingEngine;
import dk.bot.bettingengine.BettingEngineResult;
import dk.bot.bettingengine.betapi.BetCancelResult;
import dk.bot.bettingengine.betapi.BetCancelResultEnum;
import dk.bot.bettingengine.betapi.BetPlaceResult;
import dk.bot.bettingengine.betapi.BetResult;
import dk.bot.bettingengine.betapi.SPBetPlaceResult;
import dk.bot.bettingengine.dao.model.RunnerBet;
import dk.bot.bettingengine.statemachine.StateMachineResult;
import dk.bot.marketobserver.model.BetStatus;
import dk.bot.marketobserver.model.MUBet;
import dk.bot.marketobserver.model.MUBets;
import dk.bot.marketobserver.model.Market;
import dk.bot.marketobserver.model.MarketData;
import dk.bot.marketobserver.model.MarketRunner;
import dk.bot.marketobserver.runnerservice.MarketRunnerListener;
import dk.flexibet.server.betexarchive.BetexArchivePublisher;
import dk.flexibet.server.dao.BetsDAO;
import dk.flexibet.server.dao.MarketDAO;
import dk.flexibet.server.dao.RunnerStateDAO;
import dk.flexibet.server.dao.RunnerStateLastBetDAO;
import dk.flexibet.server.model.HistRunnerPrice;

/**
 * Listens for a new market data from market observer and delegates it to the betting engine and betexArchivePublisher
 * 
 * All methods are synced on a lock.
 * 
 * @author daniel
 * 
 */
public class MarketRunnerListenerImpl implements MarketRunnerListener {

	private final Log log = LogFactory.getLog(MarketRunnerListenerImpl.class.getSimpleName());

	private final BettingEngine bettingEngine;
	private final boolean sendPricesToBetexArchivePublisher;
	private final BetsDAO betsDao;
	private final RunnerStateDAO runnerStateDao;
	private final RunnerStateLastBetDAO runnerStateLastBetDao;
	private final BetexArchivePublisher betexArchivePublisher;

	private final MarketDAO marketDao;

	private ReentrantLock lock = new ReentrantLock();

	public MarketRunnerListenerImpl(BettingEngine bettingEngine, BetsDAO betsDao,
			RunnerStateLastBetDAO runnerStateLastBetDao, RunnerStateDAO runnerStateDao, MarketDAO marketDao,
			BetexArchivePublisher betexArchivePublisher, boolean sendPricesToBetexArchivePublisher) {
		this.bettingEngine = bettingEngine;
		this.betsDao = betsDao;
		this.runnerStateLastBetDao = runnerStateLastBetDao;
		this.runnerStateDao = runnerStateDao;
		this.marketDao = marketDao;
		this.betexArchivePublisher = betexArchivePublisher;
		this.sendPricesToBetexArchivePublisher = sendPricesToBetexArchivePublisher;
	}

	public void onMUBetsData(MUBets muBets) {
		lock.lock();
		try {

			/** Update last bets in db using getMUBets from BetFair API. */
			List<MUBet> matchedBets = muBets.getBets(BetStatus.M).getMuBets();

			for (MUBet bet : matchedBets) {
				RunnerBet lastBet = runnerStateLastBetDao.findBet(bet.getBetId());
				if (lastBet != null) {
					runnerStateLastBetDao
							.updateBet(bet.getBetId(), bet.getPrice(), bet.getSize(), bet.getMatchedDate());
				}
			}
		} finally {
			lock.unlock();
		}
	}

	public void onMarketPrices(Market completeMarket) {

		lock.lock();
		try {

			/** Save runner prices to the database, only for market currently in play and HR sp markets with 10 min or less to market time.. */
			if (sendPricesToBetexArchivePublisher && betexArchivePublisher != null) {
				DateTime now = new DateTime();
				if ((completeMarket.getMarketData().getEventHierarchy().startsWith("/7/") && completeMarket
						.getMarketData().getEventDate().getTime()
						- now.toDate().getTime() <= 1000 * 60 * 120 && completeMarket.getMarketData().isBsbMarket())
						|| completeMarket.getMarketRunners().getInPlayDelay() > 0) {
					for (MarketRunner marketRunner : completeMarket.getMarketRunners().getMarketRunners()) {

						HistRunnerPrice histRunnerPrice = new HistRunnerPrice();
						histRunnerPrice.setMarketId(completeMarket.getMarketData().getMarketId());
						histRunnerPrice.setSelectionId(marketRunner.getSelectionId());
						histRunnerPrice.setRecordTime(completeMarket.getMarketRunners().getTimestamp());
						histRunnerPrice.setInPlayDelay(completeMarket.getMarketRunners().getInPlayDelay());
						histRunnerPrice.setLastPriceMatched(marketRunner.getLastPriceMatched());
						histRunnerPrice.setPriceToBack(marketRunner.getPriceToBack());
						histRunnerPrice.setPriceToLay(marketRunner.getPriceToLay());

						betexArchivePublisher.publish(histRunnerPrice);
					}
				}
			}
					
			BettingEngineResult fireEventResult = bettingEngine.fireEvent(completeMarket);

			/** Store bets and runner states in database. */
			for (StateMachineResult result : fireEventResult.getBetOperations()) {

				/** Store runner state only if was changed */
				if (!result.getInputStateName().equals(result.getOutputStateName())) {
					runnerStateDao.saveRunnerState(result.getStateMachineId(), result.getMarketId(), result
							.getMarketRunner().getSelectionId(), result.getOutputStateName());
				}

				for (BetResult betResult : result.getBetResults()) {
					if (betResult instanceof BetPlaceResult) {
						BetPlaceResult betPlaceResult = (BetPlaceResult) betResult;
						betsDao.addRunnerStateBet(result.getRunnerStateId(), betPlaceResult.getBetId(), betPlaceResult
								.getBetDate(), betPlaceResult.getPrice());

						runnerStateLastBetDao.addBet(result.getRunnerStateId(), betPlaceResult.getBetId(),
								betPlaceResult.getBetDate(), betPlaceResult.getBetType(), betPlaceResult.getPrice(),
								betPlaceResult.getSize(), betPlaceResult.getAvgPriceMatched(), betPlaceResult
										.getSizeMatched(), result.getMarketRunner(), completeMarket.getMarketRunners()
										.getInPlayDelay() > 0, result.getPrediction().getSlope(), result
										.getPrediction().getSlopeErr());

					} else if (betResult instanceof SPBetPlaceResult) {
						SPBetPlaceResult spBetPlaceResult = (SPBetPlaceResult) betResult;
						betsDao.addRunnerStateBet(result.getRunnerStateId(), spBetPlaceResult.getBetId(),
								spBetPlaceResult.getBetDate(), -1);

						runnerStateLastBetDao.addBet(result.getRunnerStateId(), spBetPlaceResult.getBetId(),
								spBetPlaceResult.getBetDate(), spBetPlaceResult.getBetType(), spBetPlaceResult
										.getLimit(), 0, 0, 0, result.getMarketRunner(), completeMarket
										.getMarketRunners().getInPlayDelay() > 0, result.getPrediction().getSlope(),
								result.getPrediction().getSlopeErr());
					} else if (betResult instanceof BetCancelResult) {
						BetCancelResult betCancelResult = (BetCancelResult) betResult;
						if (betCancelResult.getStatus() == BetCancelResultEnum.TAKEN_OR_LAPSED) {
							log.warn("cancelBet: " + betCancelResult.getStatus() + ",betId: "
									+ betCancelResult.getBetId());
							/** Setting sizeCancelled to 1 to indicate that bet was canceled. */
							runnerStateLastBetDao.cancelBet(betCancelResult.getBetId(), 1, betCancelResult
									.getSizeMatched());
						} else if (betCancelResult.getStatus() == BetCancelResultEnum.OK) {
							runnerStateLastBetDao.cancelBet(betCancelResult.getBetId(), betCancelResult
									.getSizeCancelled(), betCancelResult.getSizeMatched());
						} else {
							log.error("Bet cancel result not supported:" + betCancelResult.getStatus());
						}
					}
				}
			}

		} finally {
			lock.unlock();
		}
	}

	/** Store market with runners in a database. */
	private Set<Integer> marketsAlreadyInDb = new HashSet<Integer>();

	public void onMarketDiscovery(Set<MarketData> markets) {

		lock.lock();
		try {
			for (MarketData market : markets) {
				/** Store market in db only if not stored yet. */
				if (!marketsAlreadyInDb.contains(market.getMarketId())) {

					if (!marketDao.marketExists(market.getMarketId())) {
						marketDao.saveMarket(market);
					}
					marketsAlreadyInDb.add(market.getMarketId());
				}
			}
		} finally {
			lock.unlock();
		}
	}

}
