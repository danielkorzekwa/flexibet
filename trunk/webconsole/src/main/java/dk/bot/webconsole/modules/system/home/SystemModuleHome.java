package dk.bot.webconsole.modules.system.home;

import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.math.util.MathUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanelLink;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import dk.bot.betfairservice.counters.TransactionCounterState;
import dk.bot.marketobserver.cache.ObjectCacheInfo;
import dk.bot.marketobserver.mbean.MarketObserverMBean;
import dk.bot.marketobserver.model.BetStatus;
import dk.bot.marketobserver.model.BetType;
import dk.bot.marketobserver.model.MUBets;
import dk.bot.marketobserver.util.BeanStat;
import dk.bot.webconsole.modules.system.maxbetsdetails.MaxBetsDetails;
import dk.flexibet.server.mbean.BettingServerMBean;

public class SystemModuleHome extends BreadCrumbPanel {

	@SpringBean(name = "marketObserverMBean")
	private MarketObserverMBean marketObserverMBean;
	
	@SpringBean(name = "bettingServerMBean")
	private BettingServerMBean bettingServerMBean;
	
	/** Shows exception details. */
	private final ModalWindow modalWindow = new ModalWindow("modalWindow");
	private final SystemModuleData systemModuleData;

	public SystemModuleHome(String id,final IBreadCrumbModel breadCrumbModel, SystemModuleData systemModuleData) {
		super(id,breadCrumbModel);
		this.systemModuleData = systemModuleData;

		build();
	}

	private void build() {

		/**Add scheduler stop form.*/
		final Form form = new Form("form");
		form.setOutputMarkupId(true);
		add(form);
		
		form.add(new Label("schedulerStatus",getSchedulerStatus()));
		AjaxButton stopSchedulerButton = new IndicatingAjaxButton("stopScheduler") {
			protected void onSubmit(org.apache.wicket.ajax.AjaxRequestTarget target, Form form) {
				marketObserverMBean.stopScheduler();
				bettingServerMBean.stopScheduler();
				
				form.remove("schedulerStatus");
				form.add(new Label("schedulerStatus",getSchedulerStatus()));
				target.addComponent(form);
			}
			@Override
			protected void onError(AjaxRequestTarget target, Form form) {

			}
		};
		form.add(stopSchedulerButton);
		
		long now = System.currentTimeMillis();

		add(getBeansStat(now));
		
		TransactionCounterState txCounterState = systemModuleData.getBetFairServiceInfo().getTxCounterState();
		add(new Label("maxBets",txCounterState.getMaxTxNum() + "/"
				+ txCounterState.getLastHourTxNum() + "/"
				+ txCounterState.getCurrHourTxNum()));
		add(new BreadCrumbPanelLink("maxBetsLink",this.getBreadCrumbModel(),new MaxBetsDetailsFactory(txCounterState)));
		add(getParamsStat2(now));
		add(getParamsStat(now));
		add(getCacheStat());

		modalWindow.setContent(new Label(modalWindow.getContentId(), ""));
		modalWindow.setTitle("Error details");
		modalWindow.setCookieName("systemModalWindow");
		add(modalWindow);
	}

	private ListView getBeansStat(final long now) {

		final SimpleDateFormat dateFormat = new SimpleDateFormat();

		ListView listView = new ListView("beansStat", systemModuleData.getBeanStats()) {

			@Override
			protected void populateItem(ListItem item) {
				BeanStat beanStat = (BeanStat) item.getModelObject();
				item.add(new Label("beanName", beanStat.getBeanName()));
				item.add(new Label("successes", "" + beanStat.getSuccessExecutions()));
				item.add(new Label("failures", "" + beanStat.getFailureExecutions()));
				item.add(new Label("lastRun", "" + beanStat.getLastExecutionDuration()));
				Date currentExecutionStart = beanStat.getCurrentExecutionStart();
				if (currentExecutionStart != null) {
					item
							.add(new Label("currentRun", "" + (now - beanStat.getCurrentExecutionStart().getTime())
									/ 1000));
				} else {
					item.add(new Label("currentRun", "waiting"));

				}
				item.add(new Label("status", Boolean.toString(beanStat.isLastExecutionSuccess()).toString()));

				String lastError = "";
				String lastErrorDate = "";
				final Model lastErrorDetails = new Model("");
				if (beanStat.getLastExceptionDate() != null && beanStat.getLastException() != null) {

					Date lastExceptionDate = beanStat.getLastExceptionDate();
					lastError = beanStat.getLastException();
					lastErrorDetails.setObject(beanStat.getLastExceptionDetails());
					lastErrorDate = dateFormat.format(lastExceptionDate) + " (" + (now - lastExceptionDate.getTime())
							/ 1000 + " sec. ago)";

				}

				/** Ajax link to open exception details modal window. */
				item.add(new Label("lastError", lastError));
				AjaxLink ajaxLink = new AjaxLink("lastErrorDetails") {
					@Override
					public void onClick(AjaxRequestTarget target) {

						modalWindow.setContent(new Label(modalWindow.getContentId(), lastErrorDetails));
						modalWindow.show(target);
					}
				};
				if (lastErrorDetails.getObject().equals("")) {
					ajaxLink.setVisible(false);
				}
				item.add(ajaxLink);

				item.add(new Label("lastErrorDate", lastErrorDate));

			}

		};

		return listView;
	}

	private ListView getCacheStat() {
		final List<ObjectCacheInfo> objectCacheInfo = systemModuleData.getObjectCacheInfo();

		ListView listView = new ListView("cache", objectCacheInfo) {
			protected void populateItem(ListItem item) {
				ObjectCacheInfo cacheInfo = (ObjectCacheInfo)item.getModelObject();
				item.add(new Label("cacheName",cacheInfo.getCacheName()));
				item.add(new Label("cacheSize","" + cacheInfo.getSize()));
			
				item.add(new Label("cacheCreationTime","" + (System.currentTimeMillis() / 1000 - cacheInfo.getCreationTime().getTime() / 1000)
						+ " sec. ago" ));
				
				/**add expiry time*/
				if(cacheInfo.getExpiryTime()>0) {
					item.add(new Label("cacheExpiryTime", cacheInfo.getExpiryTime() + " sec."));
				}
				else {
					item.add(new Label("cacheExpiryTime", "never"));
				}
			
			}
		};

		return listView;
	}

	private ListView getParamsStat2(final long now) {
		/** [0] - param name, [1] - param value */
		List<String[]> paramStats = new ArrayList<String[]>();

		paramStats.add(new String[] { "Balance (available)",
				systemModuleData.getBalance() + "(" + systemModuleData.getAvailableBalance() + ")" });

		paramStats.add(new String[]{"All matched bets (betsNumber/liability)", systemModuleData.getTotalLiability().getAllMatchedBetsNumber() + " / " + MathUtils.round(systemModuleData.getTotalLiability().getAllMatchedBetsLiability(),2)});
		paramStats.add(new String[]{"All matched not inPlay bets (betsNumber/liability)", systemModuleData.getTotalLiability().getAllMatchedNotInPlayBetsNumber() + " / " + MathUtils.round(systemModuleData.getTotalLiability().getAllMatchedNotInPlayBetsLiability(),2)});
		
		
		MUBets muBets = systemModuleData.getRunnersSummary().getMuBets();
		String mBets, mlBets, mbBets, uBets;
		if (muBets != null) {

			mBets = "" + muBets.getBets(BetStatus.M, null).size();
			mlBets = "" + muBets.getBets(BetStatus.M, BetType.L).size();
			mbBets = "" + muBets.getBets(BetStatus.M, BetType.B).size();
			uBets = "" + muBets.getBets(BetStatus.U, null).size();
		} else {
			mBets = "not available";
			mlBets = "not available";
			mbBets = "not available";
			uBets = "not available";

		}
		paramStats.add(new String[] { "Bets [M (L/B) / U]", mBets + " (" + mlBets + "/" + mbBets + ") / " + uBets });
		
		paramStats.add(new String[] {
				"Max/last sec BF req.",
				"" + systemModuleData.getBetFairServiceInfo().getMaxDataRequestPerSecond() + "/"
						+ systemModuleData.getBetFairServiceInfo().getLastSecDataRequest() });

		SimpleDateFormat dateFormat = new SimpleDateFormat();
		Date lastPlacedBetDate = muBets.getLastPlacedBetDate();
		Date lastMatchedBetDate = muBets.getLastMatchedBetDate();

		paramStats.add(new String[] {
				"Last placed bet",
				"" + dateFormat.format(lastPlacedBetDate) + " (" + (now - lastPlacedBetDate.getTime()) / 1000
						+ " sec. ago)" });
		paramStats.add(new String[] {
				"Last matched bet",
				"" + dateFormat.format(lastMatchedBetDate) + " (" + (now - lastMatchedBetDate.getTime()) / 1000 / 60
						+ " min. ago)" });

		paramStats.add(new String[] {
				"Analyzed markets (All/InPlay)",
				"" + systemModuleData.getRunnersSummary().getAnalyzedMarketsAmount() + "/"
						+ systemModuleData.getRunnersSummary().getAnalyzedMarketsInPlayAmount() });
		paramStats.add(new String[] { "Analyzed runners",
				"" + systemModuleData.getRunnersSummary().getAnalyzedRunnersAmount() });

		ListView listView = new ListView("paramStat2", paramStats) {

			@Override
			protected void populateItem(ListItem item) {
				String[] param = (String[]) item.getModelObject();
				item.add(new Label("paramName", param[0]));
				item.add(new Label("paramValue", param[1]));
			}

		};

		return listView;
	}

	private ListView getParamsStat(final long now) {
		/** [0] - param name, [1] - param value */
		List<String[]> paramStats = new ArrayList<String[]>();

		SimpleDateFormat dateFormat = new SimpleDateFormat();

		paramStats.add(new String[] { "Cached state machines", "" + systemModuleData.getStateMachinesAmount() });

		paramStats.add(new String[] { "RegressionCache size", "" + systemModuleData.getRegressionCacheInfo().getCacheSize() });
		paramStats.add(new String[] { "RegressionCache max age",
				"" + systemModuleData.getRegressionCacheInfo().getMaxAgeInSeconds() / 60 + " min." });

		paramStats
				.add(new String[] { "CompleteMarketsCache size", "" + systemModuleData.getCompleteMarketsCacheSize() });

		paramStats.add(new String[] { "BetexArchivePublisher queue size", "" + systemModuleData.getBetexArchivePublisherQueueSize() });
		
		long totalMem = Runtime.getRuntime().totalMemory();
		long usedMemory = totalMem - Runtime.getRuntime().freeMemory();
		paramStats.add(new String[] { "Memory used (Total)", usedMemory / 1024 + "K (" + totalMem / 1024 + "K)" });

		Date appStartTime = new Date(now - ManagementFactory.getRuntimeMXBean().getUptime());
		paramStats.add(new String[] { "App start time", dateFormat.format(appStartTime) });

		ListView listView = new ListView("paramStat", paramStats) {

			@Override
			protected void populateItem(ListItem item) {
				String[] param = (String[]) item.getModelObject();
				item.add(new Label("paramName", param[0]));
				item.add(new Label("paramValue", param[1]));
			}

		};

		return listView;
	}

	public static void main(String[] args) {

		IllegalArgumentException exception = new IllegalArgumentException("dddd", new NumberFormatException("ffffff"));

		System.out.println(ExceptionUtils.getFullStackTrace(exception));
		System.out.println(ExceptionUtils.getMessage(exception));
		System.out.println(ExceptionUtils.getRootCauseMessage(exception));
		System.out.println(exception.getMessage());
	}

	public String getTitle() {
		return "Home";
	}
	
	private class MaxBetsDetailsFactory implements IBreadCrumbPanelFactory {

		private final TransactionCounterState txCounterState;

		public MaxBetsDetailsFactory(TransactionCounterState txCounterState) {
			this.txCounterState = txCounterState;
		}
		
		public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
			return new MaxBetsDetails(componentId,breadCrumbModel,txCounterState);
		}
		
	}
	
	/**
	 * 
	 * @return running or stopped
	 */
	private String getSchedulerStatus() {
		return marketObserverMBean.isSchedulerRunning() || bettingServerMBean.isSchedulerRunning() ? "running" : "stopped";
	}
}
