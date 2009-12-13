package dk.bot.webconsole.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.PageParameters;
import org.apache.wicket.extensions.ajax.markup.html.AjaxLazyLoadPanel;
import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import dk.bot.birt.BirtService;
import dk.bot.marketobserver.mbean.MarketObserverMBean;
import dk.bot.webconsole.modules.bets.BetsModule;
import dk.bot.webconsole.modules.dashboard.DashboardModule;
import dk.bot.webconsole.modules.markets.MarketsPanel;
import dk.bot.webconsole.modules.randomizer.WinnerRandomizerModule;
import dk.bot.webconsole.modules.statement.AccountStatementModule;
import dk.bot.webconsole.modules.statesvisits.StatesVisitsPanel;
import dk.bot.webconsole.modules.statistics.StatsPanel;
import dk.bot.webconsole.modules.system.SystemModuleIndex;
import dk.flexibet.server.mbean.accountstatement.AccountStatementMBean;

/**
 * Homepage
 */
public class BetFairConsolePage extends TemplatePage {

	private static final long serialVersionUID = 1L;

	@SpringBean(name = "birtService")
	private BirtService birtService;

	@SpringBean(name="marketObserverMBean")
	MarketObserverMBean marketObserverMBean;
		
	@SpringBean(name="accountStatementMBean")
	private AccountStatementMBean accountStatementMBean;
	
	/**
	 * Constructor that is invoked when page is invoked without a session.
	 * 
	 * @param parameters
	 *            Page parameters
	 */
	public BetFairConsolePage(final PageParameters parameters) {
		super(parameters);

		List<ITab> tabs = new ArrayList<ITab>();
		tabs.add(new AbstractTab(new Model("Dashboard")) {

			public Panel getPanel(String panelId) {
				return new AjaxLazyLoadPanel(panelId) {
					@Override
					public Component getLazyLoadComponent(String id) {
						if (marketObserverMBean.getNewestRunnersSummary() != null) {
							DashboardModule betFairPanel = new DashboardModule(id, accountStatementMBean.getAccountStatementStat(), marketObserverMBean
									.getMarkets());
							return betFairPanel;
						} else {
							return new Label(id, "Data not available: First execution not finished yet.");
						}
					}
				};
			}
		});

		tabs.add(new AbstractTab(new Model("Winner Randomizer")) {

			public Panel getPanel(String panelId) {

				WinnerRandomizerModule randomizerModule = new WinnerRandomizerModule(panelId);
				return randomizerModule;

			}

		});

		tabs.add(new AbstractTab(new Model("Markets")) {

			public Panel getPanel(String panelId) {
				MarketsPanel marketsPanel = new MarketsPanel(panelId);
				return marketsPanel;
			}

		});

		tabs.add(new AbstractTab(new Model("States visits")) {

			public Panel getPanel(String panelId) {
				StatesVisitsPanel statesPanel = new StatesVisitsPanel(panelId);
				return statesPanel;
			}

		});

		tabs.add(new AbstractTab(new Model("Statistics")) {

			public Panel getPanel(String panelId) {
				StatsPanel statsPanel = new StatsPanel(panelId, birtService);
				return statsPanel;
			}

		});

		tabs.add(new AbstractTab(new Model("Account Statement")) {

			public Panel getPanel(String panelId) {

				AccountStatementModule accountStatementModule = new AccountStatementModule(panelId);
				return accountStatementModule;

			}

		});
		tabs.add(new AbstractTab(new Model("Bets")) {

			public Panel getPanel(String panelId) {

				BetsModule betsModule = new BetsModule(panelId);
				return betsModule;

			}

		});

		
		tabs.add(new AbstractTab(new Model("System")) {

			public Panel getPanel(String panelId) {
				return new AjaxLazyLoadPanel(panelId) {
					@Override
					public Component getLazyLoadComponent(String id) {
						if (marketObserverMBean.getNewestRunnersSummary() != null) {

							SystemModuleIndex systemModule = new SystemModuleIndex(id);
							return systemModule;

						} else {
							return new Label(id, "Data not available: First execution not finished yet.");
						}
					}
				};
			}
		});

		add(new AjaxTabbedPanel("tabs", tabs));

	}

	@Override
	public String getView() {
		return "BetFair Console";
	}

}
