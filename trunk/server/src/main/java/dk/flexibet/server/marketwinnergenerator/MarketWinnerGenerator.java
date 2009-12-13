package dk.flexibet.server.marketwinnergenerator;

import java.util.List;

/**Random generator of market winners for all markets.
 * 
 * @author daniel
 *
 */
public interface MarketWinnerGenerator {

	/**
	 * 
	 * @return list of market winners.
	 */
	public List<MarketWinner> generateMarketWinners();
}
