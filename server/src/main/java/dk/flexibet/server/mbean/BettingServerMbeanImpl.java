package dk.flexibet.server.mbean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;

import com.betfair.publicapi.types.exchange.v5.GetAccountFundsResp;

import dk.bot.betfairservice.BetFairService;
import dk.bot.betfairservice.BetFairServiceInfo;
import dk.bot.marketobserver.util.BeanStat;
import dk.flexibet.server.dao.BetsDAO;
import dk.flexibet.server.dao.HistRunnerPriceDAO;
import dk.flexibet.server.model.BetsStat;
import dk.flexibet.server.model.HistRunnerPrice;
import dk.flexibet.server.model.RunnerStateBet;
import dk.flexibet.server.tasks.account.AccountStatementTaskImpl;
import dk.flexibet.server.tasks.statemachine.CleanUpTaskImpl;

/**JMX interface for a flexibet server.
 * 
 * @author daniel
 *
 */
@ManagedResource(objectName = "dk.flexibet.bettingserver:name=BettingServer")
public class BettingServerMbeanImpl implements BettingServerMBean{
	
	@Resource
	private BetFairService betFairService;
	
	@Resource
	private BetsDAO betsDao;
	
	@Resource
	private AccountStatementTaskImpl accountStatementTask;
	@Resource
	private CleanUpTaskImpl removeOldMachinesTask;
	
	@Resource
	private Scheduler bettingServerScheduler;
	
	@Resource
	private HistRunnerPriceDAO histRunnerPriceDao;
	
	/**JMX*/
	@ManagedAttribute
	public List<BeanStat> getScheduledTaskStat() {
		List<BeanStat> beanStats = new ArrayList<BeanStat>();
		beanStats.add(accountStatementTask.getBeanStat());
		beanStats.add(removeOldMachinesTask.getBeanStat());
		return beanStats;
	}
	
	@ManagedOperation
	@ManagedOperationParameters( { @ManagedOperationParameter(name = "dateFrom", description = ""),
			@ManagedOperationParameter(name = "dateTo", description = "") })
	public List<BetsStat> getBetsStat(Date dateFrom, Date dateTo) {
		return betsDao.getBetsStat(dateFrom, dateTo);
	}
	
	@ManagedOperation
	@ManagedOperationParameters({@ManagedOperationParameter(name="runnerStateId", description="")})
	public List<RunnerStateBet> getRunnerStateBets(int runnerStateId) {
		return betsDao.getRunnerStateBets(runnerStateId);
	}
	
	@ManagedAttribute
	public GetAccountFundsResp getAccountFunds() {
		return betFairService.getAccountFunds();
	}

	@ManagedAttribute
	public BetFairServiceInfo getBetFairServiceInfo() {
		return betFairService.getBetFairServiceInfo();
	}

	@ManagedAttribute
	public boolean isSchedulerRunning() {
		try {
			return !bettingServerScheduler.isInStandbyMode() && !bettingServerScheduler.isShutdown();
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}

	@ManagedOperation
	public void stopScheduler() {
		try {
		bettingServerScheduler.shutdown(true);
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@ManagedOperation
	@ManagedOperationParameters({@ManagedOperationParameter(name="marketId", description=""),@ManagedOperationParameter(name="selectionId", description="")})

	public List<HistRunnerPrice> findRunnerPrices(int marketId,int selectionId) {
		return histRunnerPriceDao.findRunnerPrices(marketId,selectionId);
	}
	
}
