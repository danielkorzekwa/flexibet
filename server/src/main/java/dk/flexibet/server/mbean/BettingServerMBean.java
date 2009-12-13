package dk.flexibet.server.mbean;

import java.util.Date;
import java.util.List;

import com.betfair.publicapi.types.exchange.v5.GetAccountFundsResp;

import dk.bot.betfairservice.BetFairServiceInfo;
import dk.bot.marketobserver.util.BeanStat;
import dk.flexibet.server.model.BetsStat;
import dk.flexibet.server.model.HistRunnerPrice;
import dk.flexibet.server.model.RunnerStateBet;

/**JMX interface for a flexibet server.
 * 
 * @author daniel
 *
 */
public interface BettingServerMBean {

	public List<BeanStat> getScheduledTaskStat();
	
	/** Check status of scheduler: running or stopped
	 * 
	 * @return true if running
	 */
	public boolean isSchedulerRunning();
	
	/**Stop scheduler. Waits until all currently running tasks are finished.
	 * 
	 */
	public void stopScheduler();
	
	/**Statistics about how many bets were placed in a time period.
	 * 
	 * @param dateFrom
	 * @param dateTo
	 * @return
	 */
	public List<BetsStat> getBetsStat(Date dateFrom, Date dateTo);
	
	public List<RunnerStateBet> getRunnerStateBets(int runnerStateId);
	
public GetAccountFundsResp getAccountFunds();
	
	/** Returns BetFairService usage statistics. */
	public BetFairServiceInfo getBetFairServiceInfo();
	
	/**
	 * 
	 * @param marketId
	 * @param selectionId
	 * @return list of runner price records sorted by time (from oldest to the earliest)
	 */
	public List<HistRunnerPrice> findRunnerPrices(int marketId,int selectionId);
}
