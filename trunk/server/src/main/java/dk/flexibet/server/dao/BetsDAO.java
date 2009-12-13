package dk.flexibet.server.dao;

import java.util.Date;
import java.util.List;

import dk.flexibet.server.model.BetsStat;
import dk.flexibet.server.model.RunnerStateBet;

/**DAO object for bets.
 * 
 * @author daniel
 *
 */
public interface BetsDAO {

	/**Statistics about how many bets were placed in a time period.
	 * 
	 * @param dateFrom
	 * @param dateTo
	 * @return
	 */
	public List<BetsStat> getBetsStat(Date dateFrom, Date dateTo);
	
	/**Get bets added by runner stateMachine.*/
	public List<RunnerStateBet> getRunnerStateBets(int runnerStateId);
	
	/** Add to database bet added by runner's stateMachine.*/
	public void addRunnerStateBet(int runnerStateId, long betId, Date placedDate, double price);
}
