package dk.flexibet.server.simulator.bettingengine;

import java.util.Date;

import dk.bot.bettingengine.dao.model.RunnerBet;
import dk.bot.marketobserver.model.BetType;

/**Part of a betting simulations. Dao object for last bet on runner state.
 * 
 * @author daniel
 *
 */
public interface SimLastBetDao {

	public RunnerBet findLastBet(int runnerStateId, BetType betType);
	public void saveLastBet(RunnerBet runnerBet);
	public void updateLastBet(long betId, double avgPriceMatched, double sizeMatched, Date matchedDate);
	public void cancelBet(long betId, double sizeCancelled, double sizeMatched);
	public RunnerBet findBet(long betId);
}
