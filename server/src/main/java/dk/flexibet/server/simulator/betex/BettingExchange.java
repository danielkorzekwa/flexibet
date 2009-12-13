package dk.flexibet.server.simulator.betex;

import java.util.List;

import dk.bot.bettingengine.betapi.BetCancelResult;
import dk.bot.bettingengine.betapi.BetPlaceResult;
import dk.bot.marketobserver.model.BetType;
import dk.bot.marketobserver.model.MUBet;
import dk.bot.marketobserver.model.MarketRunners;

/**Represents Betting Exchange 
 * 
 * @author daniel
 *
 */
public interface BettingExchange {

	public BetPlaceResult placeBet(int marketId, int selectionId, BetType betType, double price, double size,
			boolean checkTxLimit);
		
	public BetCancelResult cancelBet(long betId);
	public List<MUBet> getMUBets(int marketId);
	public List<MUBet> getMUBets();
	
	/**Returns number of matched bets
	 * 
	 * @return
	 */
	public int getNumOfMatchedBets();
	
	/**Matched bets for market - algorithm depends on implementation of a betting exchange.*/
	public void matchBets(MarketRunners marketRunners);
	
}
