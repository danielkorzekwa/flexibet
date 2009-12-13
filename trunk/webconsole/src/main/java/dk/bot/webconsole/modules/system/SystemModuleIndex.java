package dk.bot.webconsole.modules.system;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.breadcrumb.BreadCrumbBar;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.betfair.publicapi.types.exchange.v5.GetAccountFundsResp;

import dk.bot.bettingengine.mbean.BettingEngineMBean;
import dk.bot.bettingengine.statemachine.StateMachineServiceMBean;
import dk.bot.marketobserver.mbean.MarketObserverMBean;
import dk.bot.marketobserver.util.BeanStat;
import dk.bot.webconsole.modules.system.home.SystemModuleData;
import dk.bot.webconsole.modules.system.home.SystemModuleHome;
import dk.flexibet.server.betexarchive.BetexArchivePublisherMBean;
import dk.flexibet.server.mbean.BettingServerMBean;

public class SystemModuleIndex extends Panel{

	@SpringBean(name="marketObserverMBean")
	private MarketObserverMBean marketObserverMBean;
	@SpringBean(name="stateMachineServiceMBean")
	private StateMachineServiceMBean stateMachineServiceMBean;
	@SpringBean(name="bettingEngineMBean")
	private BettingEngineMBean bettingEngineMBean;
	@SpringBean(name="betexArchivePublisherMBean")
	private BetexArchivePublisherMBean betexArchivePublisherMBean;
	@SpringBean(name="bettingServerMBean")
	private BettingServerMBean bettingServerMBean;
	
	public SystemModuleIndex(String id) {
		super(id);
		
		build();
	}
	
	private void build() {	
		    BreadCrumbBar breadCrumbBar = new BreadCrumbBar("breadCrumbBar");
	        add(breadCrumbBar);
	        SystemModuleHome firstPanel = new SystemModuleHome("panel", breadCrumbBar, getSystemModuleData());
	        add(firstPanel);
	        breadCrumbBar.setActive(firstPanel);
	}
	
	private SystemModuleData getSystemModuleData() {
		List<BeanStat> beanStats = new ArrayList<BeanStat>();
		beanStats.addAll(marketObserverMBean.getScheduledTaskStat());
		beanStats.addAll(bettingServerMBean.getScheduledTaskStat());

		SystemModuleData systemModuleData = new SystemModuleData();
		systemModuleData.setBeanStats(beanStats);

		systemModuleData.setRegressionCacheInfo(bettingEngineMBean.getRegressionCacheInfo());
		systemModuleData.setRunnersSummary(marketObserverMBean.getNewestRunnersSummary());
		systemModuleData.setCompleteMarketsCacheSize(marketObserverMBean.getCompleteMarketsCacheMarketsAmount());

		systemModuleData.setObjectCacheInfo(marketObserverMBean.getObjectCacheInfo());

		systemModuleData.setTotalLiability(marketObserverMBean.getMatchedBetsLiability());
		
		systemModuleData.setStateMachinesAmount(stateMachineServiceMBean.getMachinesAmount());

		GetAccountFundsResp accountFunds = bettingServerMBean.getAccountFunds();
		systemModuleData.setBalance(accountFunds.getBalance());
		systemModuleData.setAvailableBalance(accountFunds.getAvailBalance());
		systemModuleData.setBetFairServiceInfo(bettingServerMBean.getBetFairServiceInfo());

		systemModuleData.setBetexArchivePublisherQueueSize(betexArchivePublisherMBean.getPriceQueueSize());
		
		return systemModuleData;
	}
}
