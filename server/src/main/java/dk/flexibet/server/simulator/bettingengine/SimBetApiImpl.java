package dk.flexibet.server.simulator.bettingengine;

import dk.bot.bettingengine.betapi.BetApi;
import dk.bot.bettingengine.betapi.BetCancelResult;
import dk.bot.bettingengine.betapi.BetPlaceResult;
import dk.bot.bettingengine.betapi.SPBetPlaceResult;
import dk.bot.marketobserver.model.BetType;
import dk.flexibet.server.simulator.betex.BettingExchange;

/** Part of a betting simulation. All bets are immediately matched.
 * 
 * @author daniel
 *
 */
public class SimBetApiImpl implements BetApi{

	private final BettingExchange bettingExchange;

	public SimBetApiImpl(BettingExchange bettingExchange) {
		this.bettingExchange = bettingExchange;
	}
	
	public BetCancelResult cancelBet(long betId) {
		return bettingExchange.cancelBet(betId);
	}

	public BetPlaceResult placeBet(int marketId, int selectionId, BetType betType, double price, double size,
			boolean checkTxLimit) {
		return bettingExchange.placeBet(marketId, selectionId, betType, price, size, checkTxLimit);
	}

	public SPBetPlaceResult placeSPBet(int marketId, int selectionId, BetType betType, double liability, Double limit) {
		throw new UnsupportedOperationException("Place SP bet is not supported");
	}

}
