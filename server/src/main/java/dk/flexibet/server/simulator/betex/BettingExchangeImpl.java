package dk.flexibet.server.simulator.betex;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import dk.bot.bettingengine.betapi.BetCancelResult;
import dk.bot.bettingengine.betapi.BetPlaceResult;
import dk.bot.marketobserver.model.BetCategoryType;
import dk.bot.marketobserver.model.BetStatus;
import dk.bot.marketobserver.model.BetType;
import dk.bot.marketobserver.model.MUBet;
import dk.bot.marketobserver.model.MarketRunners;

/**
 * Represents Betting Exchange. It's a very simple implementation used by a betting simulator. All bets are
 * automatically matched.
 * 
 * @author daniel
 * 
 */
public class BettingExchangeImpl implements BettingExchange {

	private int lastBetId = 0;

	private List<MUBet> muBets = new ArrayList<MUBet>();

	/** All bets are automatically matched. */
	public BetPlaceResult placeBet(int marketId, int selectionId, BetType betType, double price, double size,
			boolean checkTxLimit) {
		int betId = getNextBetId();

		MUBet bet = new MUBet();
		bet.setBetId(betId);
		bet.setBetStatus(BetStatus.M);
		bet.setBetType(betType);
		bet.setBetCategoryType(BetCategoryType.E);
		bet.setMarketId(marketId);
		bet.setSelectionId(selectionId);
		bet.setPlacedDate(new DateTime().toDate());
		bet.setMatchedDate(bet.getPlacedDate());
		bet.setPrice(price);
		bet.setSize(size);

		muBets.add(bet);

		BetPlaceResult betPlaceResult = new BetPlaceResult(betId, bet.getPlacedDate(), betType.name(), price, size,
				price, size);
		return betPlaceResult;
	}

	public BetCancelResult cancelBet(long betId) {
		throw new UnsupportedOperationException("All bets are matched byh this impl, so cancelBet should not be called.");
	}
	
	public List<MUBet> getMUBets(int marketId) {

		List<MUBet> marketMUBets = new ArrayList<MUBet>();
		for (MUBet bet : muBets) {
			if (bet.getMarketId() == marketId) {
				marketMUBets.add(bet);
			}
		}

		return marketMUBets;
	}
	public List<MUBet> getMUBets() {
		return muBets;
	}
	
	public int getNumOfMatchedBets() {
		int numOfMatchedBets=0;
		for (MUBet bet : muBets) {
			if (bet.getBetStatus()==BetStatus.M) {
				numOfMatchedBets++;
			}
		}
		return numOfMatchedBets;
	}
	
	public void matchBets(MarketRunners marketRunners) {
		/**Don't do anything, because all bets are automatically matched.*/
	}

	private int getNextBetId() {
		lastBetId++;
		return lastBetId;
	}

}
