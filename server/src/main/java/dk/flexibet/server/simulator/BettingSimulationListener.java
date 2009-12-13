package dk.flexibet.server.simulator;

import dk.bot.marketobserver.model.MarketData;

/**Allows to monitor simulation progress.
 * 
 * @author daniel
 *
 */
public interface BettingSimulationListener {

	public void afterGetMarkets(int numOfMarkets);
	
	public void afterMarketAnalysis(int marketNumber,MarketData marketData);
}
