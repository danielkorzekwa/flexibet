package dk.flexibet.server.marketwinnergenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dk.bot.marketobserver.MarketObserver;
import dk.bot.marketobserver.model.Market;
import dk.bot.marketobserver.model.MarketRunner;

/**Random generator of market winners for all markets
 * 
 * @author daniel
 *
 */
public class MarketWinnerGeneratorImpl implements MarketWinnerGenerator{

	private final Log log = LogFactory.getLog(MarketWinnerGeneratorImpl.class.getSimpleName());

	private Random random = new Random(System.currentTimeMillis());
	
	private MarketObserver marketObserver;

	public List<MarketWinner> generateMarketWinners() {
		/** Get latest state of markets. */
		Map<Integer, Market> compositeMarkets = marketObserver.getCompleteMarkets();

		/** Generate winners for all markets. */
		/**key - marketId, value - selectionWinnerId*/
		List<MarketWinner> marketWinners = new ArrayList<MarketWinner>();
		for (Integer marketId : compositeMarkets.keySet()) {
			Market completeMarket = compositeMarkets.get(marketId);
			List<MarketRunner> runners = completeMarket.getMarketRunners().getMarketRunners();
			if (runners.size() < 2) {
				log.error("Runners size < 2. MarketId: " + marketId + ", Runners: " + runners.size());
			} else {
				double[] prices = new double[runners.size()];
				for (int i = 0; i < runners.size(); i++) {
					prices[i] = runners.get(i).getLastPriceToBack() > 0 ? runners.get(i).getLastPriceToBack() : runners
							.get(i).getPriceToBack();
				}

				int winnerSelectionIndex = RandomMarketWinner.generateMarketWinner(random, prices);
				int winnerSelectionId = runners.get(winnerSelectionIndex).getSelectionId();
				
				marketWinners.add(new MarketWinner(marketId,winnerSelectionId,completeMarket.getMarketData().getEventDate()));
			}
		}
		
		return marketWinners;

	}
	
	public void setMarketObserver(MarketObserver marketObserver) {
		this.marketObserver = marketObserver;
	}

}
