package dk.bot.webconsole.modules.system.runnerbetspanel;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListView;

import dk.flexibet.server.model.RunnerStateBet;

public class RunnerBetsPanel extends BreadCrumbPanel {

	public RunnerBetsPanel(String id, final IBreadCrumbModel breadCrumbModel, List<RunnerStateBet> runnerBets) {
		super(id, breadCrumbModel);

		final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");

		ListView runnerBetsListView = new ListView("item", runnerBets) {
			protected void populateItem(org.apache.wicket.markup.html.list.ListItem item) {
				RunnerStateBet bet = (RunnerStateBet) item.getModelObject();
				item.add(new Label("betId", "" + bet.getBetId()));
				item.add(new Label("placedDate", df.format(bet.getPlacedDate())));
				item.add(new Label("price", "" + bet.getPrice()));
			};
		};

		add(runnerBetsListView);
	}

	public String getTitle() {
		return "Runner Bets";
	}
}
