package dk.flexibet.server.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;
import org.junit.Test;

import com.betfair.publicapi.types.exchange.v5.GetAccountFundsResp;

import dk.bot.betfairservice.BetFairServiceInfo;
import dk.bot.betfairservice.model.BFAccountStatementItem;
import dk.bot.betfairservice.model.BFBetPlaceResult;
import dk.bot.betfairservice.model.BFBetStatus;
import dk.bot.betfairservice.model.BFBetType;
import dk.bot.betfairservice.model.BFMUBet;
import dk.bot.betfairservice.model.BFMarketData;
import dk.bot.betfairservice.model.BFMarketDetails;
import dk.bot.betfairservice.model.BFMarketRunners;

public class BetDaqBetFairServiceImplIntegrationTest {

	private BetDaqBetFairServiceImpl testBetService = new BetDaqBetFairServiceImpl();

	@Test
	public void testGetMarkets() {
		/** hr, soccer, tennis: 100004,100003, 100005 */
		Set<Integer> eventIds = new HashSet<Integer>(Arrays.asList(100004,100003,100005));

		List<BFMarketData> markets = testBetService.getMarkets(new DateTime().toDate(), new DateTime().plusDays(1)
				.toDate(), eventIds);

		assertTrue("No markets", markets.size() > 0);
	}

	@Test
	public void testGetMarketDetails() {
		/** hr, soccer, tennis: 100004,100003, 100005 */
		Set<Integer> eventIds = new HashSet<Integer>(Arrays.asList(100004));
		List<BFMarketData> markets = testBetService.getMarkets(new DateTime().toDate(), new DateTime().plusDays(1)
				.toDate(), eventIds);
		assertTrue("Can't run test - no markets.", markets.size() > 0);

		BFMarketData marketData = markets.get(0);
		BFMarketDetails marketDetails = testBetService.getMarketDetails(marketData.getMarketId());

		assertEquals(marketData.getMarketId(), marketDetails.getMarketId());
		assertEquals(marketData.getEventDate().getTime(), marketDetails.getMarketTime().getTime());

		assertNotNull("Market details runners are null", marketDetails.getRunners());
		assertTrue("No runners for market details", marketDetails.getRunners().size() > 0);
	}

	@Test
	public void testGetMUBets() {
		List<BFMUBet> bets = testBetService.getMUBets(BFBetStatus.MU);

		assertNotNull(bets);
	}

	@Test
	public void testGetMarketRunners() {
		/** hr, soccer, tennis: 100004,100003, 100005 */
		Set<Integer> eventIds = new HashSet<Integer>(Arrays.asList(100004));
		List<BFMarketData> markets = testBetService.getMarkets(new DateTime().toDate(), new DateTime().plusDays(1)
				.toDate(), eventIds);
		assertTrue("Can't run test - no markets.", markets.size() > 0);

		BFMarketData marketData = markets.get(0);

		BFMarketRunners marketRunners = testBetService.getMarketRunners(marketData.getMarketId());
		assertTrue("No market runners", marketRunners.getMarketRunners().size() > 0);
	}

	@Test
	public void testGetBetFairServiceInfo() {
		BetFairServiceInfo betFairServiceInfo = testBetService.getBetFairServiceInfo();
		assertNotNull("BetFairServiceInfo is null", betFairServiceInfo);
		assertNotNull("BetFairServiceInfo.txCounterState is null", betFairServiceInfo.getTxCounterState());
		assertEquals(0, betFairServiceInfo.getMaxDataRequestPerSecond());
	}

	@Test
	public void testPlaceBet() {
		/** hr, soccer, tennis: 100004,100003, 100005 */
		Set<Integer> eventIds = new HashSet<Integer>(Arrays.asList(100004));
		List<BFMarketData> markets = testBetService.getMarkets(new DateTime().toDate(), new DateTime().plusDays(1)
				.toDate(), eventIds);
		assertTrue("Can't run test - no markets.", markets.size() > 0);

		BFMarketData marketData = markets.get(0);
		BFMarketDetails marketDetails = testBetService.getMarketDetails(marketData.getMarketId());
		int selectionId = marketDetails.getRunners().get(0).getSelectionId();

		BFBetPlaceResult betResults = testBetService.placeBet(marketData.getMarketId(), selectionId, BFBetType.L, 1.01, 2d,true);
		
		assertEquals("L",betResults.getBetType());
		assertEquals(2,betResults.getSize(),0);
		assertEquals(1.01,betResults.getPrice(),0);
		assertEquals(0,betResults.getSizeMatched(),0);
		assertEquals(0,betResults.getAvgPriceMatched(),0);
		
		testBetService.cancelBet(betResults.getBetId());
	}
	
	@Test
	public void testPlaceLayBetWrongPriceRoundUp() {
		/** hr, soccer, tennis: 100004,100003, 100005 */
		Set<Integer> eventIds = new HashSet<Integer>(Arrays.asList(100004));
		List<BFMarketData> markets = testBetService.getMarkets(new DateTime().toDate(), new DateTime().plusDays(1)
				.toDate(), eventIds);
		assertTrue("Can't run test - no markets.", markets.size() > 0);

		BFMarketData marketData = markets.get(0);
		BFMarketDetails marketDetails = testBetService.getMarketDetails(marketData.getMarketId());
		int selectionId = marketDetails.getRunners().get(0).getSelectionId();

		BFBetPlaceResult betResults = testBetService.placeBet(marketData.getMarketId(), selectionId, BFBetType.L, 1.012, 2d,true);
		
		assertEquals("L",betResults.getBetType());
		assertEquals(2,betResults.getSize(),0);
		assertEquals(1.02,betResults.getPrice(),0);
		assertEquals(0,betResults.getSizeMatched(),0);
		assertEquals(0,betResults.getAvgPriceMatched(),0);
		
		testBetService.cancelBet(betResults.getBetId());
	}

	@Test
	public void testPlaceBackBetWrongPriceRoundDown() {
		/** hr, soccer, tennis: 100004,100003, 100005 */
		Set<Integer> eventIds = new HashSet<Integer>(Arrays.asList(100004));
		List<BFMarketData> markets = testBetService.getMarkets(new DateTime().toDate(), new DateTime().plusDays(1)
				.toDate(), eventIds);
		assertTrue("Can't run test - no markets.", markets.size() > 0);

		BFMarketData marketData = markets.get(0);
		BFMarketDetails marketDetails = testBetService.getMarketDetails(marketData.getMarketId());
		int selectionId = marketDetails.getRunners().get(0).getSelectionId();

		BFBetPlaceResult betResults = testBetService.placeBet(marketData.getMarketId(), selectionId, BFBetType.B, 999, 2d,true);
		
		assertEquals("B",betResults.getBetType());
		assertEquals(2,betResults.getSize(),0);
		assertEquals(995,betResults.getPrice(),0);
		assertEquals(0,betResults.getSizeMatched(),0);
		assertEquals(0,betResults.getAvgPriceMatched(),0);
		
		testBetService.cancelBet(betResults.getBetId());
	}
	
	@Test
	public void testGetAccountFunds() {
		GetAccountFundsResp accountFunds = testBetService.getAccountFunds();
		
		assertNotNull(accountFunds);
	}
	
	@Test
	public void testGetAccountStatement() {
		List<BFAccountStatementItem> items = testBetService.getAccountStatement(new Date(System.currentTimeMillis()-1000*3600*24*14), new Date(System.currentTimeMillis()), Integer.MAX_VALUE);
		assertNotNull(items);
	}
}
