package dk.bot.webconsole.panels.marketstat;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math.util.MathUtils;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;

import dk.flexibet.server.mbean.accountstatement.model.AccountStatementStat;
import dk.flexibet.server.mbean.accountstatement.model.MarketStat;

public class MarketStatPanel extends Panel{

	private final AccountStatementStat[] accountStatementStat;

	public MarketStatPanel(String id,AccountStatementStat[] accountStatementStat) {
		super(id);
		this.accountStatementStat = accountStatementStat;
		
		build();
	}
	
	private void build() {
		List<MarketStat> accountStatements = new ArrayList<MarketStat>();
		accountStatements.add(accountStatementStat[0].getMarketStat());
		accountStatements.add(accountStatementStat[1].getMarketStat());

		add(new ListView("marketStat", accountStatements) {

			@Override
			protected void populateItem(ListItem item) {
				MarketStat statement = (MarketStat) item.getModelObject();
				item.add(new Label("period", statement.getTimePeriod()));
				item.add(new Label("greenMarkets", statement.getGreenMarkets() + " ("
						+ MathUtils.round(statement.getProfitLossGreen(), 2) + ")"));
				item.add(new Label("whiteMarkets", statement.getWhiteMarkets() + " (0)"));
				item.add(new Label("redMarkets", statement.getRedMarkets() + " ("
						+ MathUtils.round(statement.getProfitLossRed(), 2) + ")"));
				item.add(new Label("totalMarkets", statement.getTotalMarkets() + " ("
						+ MathUtils.round(statement.getProfitLossTotal(), 2) + ")"));
			}
		});

	}
}
