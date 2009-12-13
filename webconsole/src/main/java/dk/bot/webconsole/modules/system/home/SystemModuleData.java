package dk.bot.webconsole.modules.system.home;

import java.io.Serializable;
import java.util.List;

import dk.bot.betfairservice.BetFairServiceInfo;
import dk.bot.bettingengine.regression.RegressionCacheInfo;
import dk.bot.marketobserver.cache.ObjectCacheInfo;
import dk.bot.marketobserver.mbean.TotalLiability;
import dk.bot.marketobserver.tasks.analyzerunners.RunnersSummary;
import dk.bot.marketobserver.util.BeanStat;

/**Data model for system module
 * 
 * @author daniel
 *
 */
public class SystemModuleData implements Serializable{

	private List<BeanStat> beanStats;
	
	/**Market Observer stats*/
	private RunnersSummary runnersSummary;
	private RegressionCacheInfo regressionCacheInfo;
	private int completeMarketsCacheSize;
	
	/**Liability for all matched bets based on current market probabilities.*/
	private TotalLiability totalLiability;
	
	private List<ObjectCacheInfo> objectCacheInfo;
	
	/**Betting Engine stats*/
	private int stateMachinesAmount;
	
	/**BetFair*/
	private double balance;
	private double availableBalance;
	private BetFairServiceInfo betFairServiceInfo;
	
	/**BetexArchivePublisher*/
	private int betexArchivePublisherQueueSize;
	
	public int getBetexArchivePublisherQueueSize() {
		return betexArchivePublisherQueueSize;
	}
	public void setBetexArchivePublisherQueueSize(int betexArchivePublisherQueueSize) {
		this.betexArchivePublisherQueueSize = betexArchivePublisherQueueSize;
	}
	public List<BeanStat> getBeanStats() {
		return beanStats;
	}
	public void setBeanStats(List<BeanStat> beanStats) {
		this.beanStats = beanStats;
	}
	public RunnersSummary getRunnersSummary() {
		return runnersSummary;
	}
	public void setRunnersSummary(RunnersSummary runnersSummary) {
		this.runnersSummary = runnersSummary;
	}
	
	public RegressionCacheInfo getRegressionCacheInfo() {
		return regressionCacheInfo;
	}
	public void setRegressionCacheInfo(RegressionCacheInfo regressionCacheInfo) {
		this.regressionCacheInfo = regressionCacheInfo;
	}
	public int getCompleteMarketsCacheSize() {
		return completeMarketsCacheSize;
	}
	public void setCompleteMarketsCacheSize(int completeMarketsCacheSize) {
		this.completeMarketsCacheSize = completeMarketsCacheSize;
	}
	
	public int getStateMachinesAmount() {
		return stateMachinesAmount;
	}
	public void setStateMachinesAmount(int stateMachinesAmount) {
		this.stateMachinesAmount = stateMachinesAmount;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public double getAvailableBalance() {
		return availableBalance;
	}
	public void setAvailableBalance(double availableBalance) {
		this.availableBalance = availableBalance;
	}
	public BetFairServiceInfo getBetFairServiceInfo() {
		return betFairServiceInfo;
	}
	public void setBetFairServiceInfo(BetFairServiceInfo betFairServiceInfo) {
		this.betFairServiceInfo = betFairServiceInfo;
	}
	public List<ObjectCacheInfo> getObjectCacheInfo() {
		return objectCacheInfo;
	}
	public void setObjectCacheInfo(List<ObjectCacheInfo> objectCacheInfo) {
		this.objectCacheInfo = objectCacheInfo;
	}
	public TotalLiability getTotalLiability() {
		return totalLiability;
	}
	public void setTotalLiability(TotalLiability totalLiability) {
		this.totalLiability = totalLiability;
	}
	
}
