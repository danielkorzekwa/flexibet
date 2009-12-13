package dk.bot.webconsole.modules.system.maxbetsdetails.detailspanel;

import java.util.List;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanelLink;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import dk.bot.marketobserver.mbean.MarketObserverMBean;
import dk.bot.webconsole.modules.system.runnerbetspanel.RunnerBetsPanel;
import dk.bot.webconsole.panels.marketdetails.MarketDetailsPanelFactory;
import dk.flexibet.server.mbean.BettingServerMBean;
import dk.flexibet.server.model.BetsStat;
import dk.flexibet.server.model.RunnerStateBet;

public class MaxBetsDetailsPanel extends Panel {

	@SpringBean(name="marketObserverMBean")
	MarketObserverMBean marketObserverMBean;
		
	@SpringBean(name = "bettingServerMBean")
	private BettingServerMBean bettingServerMBean;

	public MaxBetsDetailsPanel(String id, final BreadCrumbPanel parent, List<BetsStat> betsStatItems) {
		super(id);
		betsStatItems.add(new BetsStat("default",4456, 34, "menuPath", "selection", 23));

		ListView betsStat = new ListView("item", betsStatItems) {

			@Override
			protected void populateItem(ListItem item) {
				BetsStat stat = (BetsStat) item.getModelObject();
				item.add(new Label("stateMachine", stat.getStateMachineId()));

				IBreadCrumbPanelFactory marketDetailsFactory = new MarketDetailsPanelFactory(marketObserverMBean.getCompleteMarket(stat.getMarketId()));
				BreadCrumbPanelLink menuPathLink = new BreadCrumbPanelLink("menuPathLink", parent.getBreadCrumbModel(),
						marketDetailsFactory);
				menuPathLink.add(new Label("menuPath", stat.getMenuPath()));
				item.add(menuPathLink);

				item.add(new Label("selectionName", stat.getSelectionName()));

				RunnerBetsFactory runnerBetsFactory = new RunnerBetsFactory(stat.getRunnerStateId());
				BreadCrumbPanelLink betsAmountLink = new BreadCrumbPanelLink("betsAmountLink", parent
						.getBreadCrumbModel(), runnerBetsFactory);
				betsAmountLink.add(new Label("betsAmount", "" + stat.getBetsAmount()));
				item.add(betsAmountLink);
			}

		};

		add(betsStat);
	}

	private class RunnerBetsFactory implements IBreadCrumbPanelFactory {

		private final int runnerStateId;

		public RunnerBetsFactory(int runnerStateId) {
			this.runnerStateId = runnerStateId;
		}

		public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
			List<RunnerStateBet> runnerBets = bettingServerMBean.getRunnerStateBets(runnerStateId);
			return new RunnerBetsPanel(componentId, breadCrumbModel, runnerBets);
		}

	}

}
