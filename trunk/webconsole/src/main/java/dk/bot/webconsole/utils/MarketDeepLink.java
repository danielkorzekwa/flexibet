package dk.bot.webconsole.utils;

/**
 * Generates market deep link
 * 
 * @author daniel
 * 
 */
public class MarketDeepLink {

	public static String generateMarketDeepLink(int marketId) {
		return "http://sports.betfair.com/Index.do?mi=" + marketId + "&ex=1";
	}
}
