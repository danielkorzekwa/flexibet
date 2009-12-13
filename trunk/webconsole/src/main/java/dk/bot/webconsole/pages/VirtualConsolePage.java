package dk.bot.webconsole.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.PageParameters;
import org.apache.wicket.extensions.ajax.markup.html.AjaxLazyLoadPanel;
import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import dk.bot.marketobserver.mbean.MarketObserverMBean;
import dk.bot.webconsole.modules.bets.BetsModule;
import dk.bot.webconsole.modules.dashboard.DashboardModule;
import dk.bot.webconsole.modules.randomizer.WinnerRandomizerModule;
import dk.bot.webconsole.modules.statement.AccountStatementModule;

/**
 * Homepage
 */
public class VirtualConsolePage extends TemplatePage {

	private static final long serialVersionUID = 1L;

	@SpringBean(name="marketObserverMBean")
	MarketObserverMBean marketObserverMBean;
	

	/**
	 * Constructor that is invoked when page is invoked without a session.
	 * 
	 * @param parameters
	 *            Page parameters
	 */
	public VirtualConsolePage(final PageParameters parameters) {
		super(parameters);

		List<ITab> tabs = new ArrayList<ITab>();
		tabs.add(new AbstractTab(new Model("Dashboard")) {

			public Panel getPanel(String panelId) {

				return new AjaxLazyLoadPanel(panelId) {
					@Override
					public Component getLazyLoadComponent(String id) {
						DashboardModule virtalDashboardPanel = new DashboardModule(id,
								null, marketObserverMBean.getMarkets());

						return virtalDashboardPanel;
					}

				};

			}

		});

		tabs.add(new AbstractTab(new Model("Winner Randomizer")) {

			public Panel getPanel(String panelId) {

				WinnerRandomizerModule virtRandomizerModule = new WinnerRandomizerModule(panelId);
				return virtRandomizerModule;

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

		add(new AjaxTabbedPanel("tabs", tabs));

	}
	
	@Override
	public String getView() {
		return "Virtual Console";
	}

}
