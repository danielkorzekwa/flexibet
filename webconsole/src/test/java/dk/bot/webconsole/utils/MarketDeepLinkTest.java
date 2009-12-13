package dk.bot.webconsole.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MarketDeepLinkTest {

	@Test
	public void testGenerateMarketDeepLink() {
		String deepLink = MarketDeepLink.generateMarketDeepLink(100610149);
		String expectedDeepLink = "http://sports.betfair.com/Index.do?mi=100610149&ex=1";
		
		assertEquals(expectedDeepLink, deepLink);
		
	}

}
