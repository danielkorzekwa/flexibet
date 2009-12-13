package dk.bot.webconsole.modules.dashboard;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import dk.bot.marketobserver.mbean.MarketObserverMBean;
import dk.bot.marketobserver.model.Market;
import dk.bot.webconsole.modules.dashboard.sportstat.SportStat;
import dk.bot.webconsole.modules.dashboard.sportstat.SportsStatHelper;
import dk.bot.webconsole.panels.marketstat.MarketStatPanel;
import dk.bot.webconsole.panels.statestat.StateStatPanel;
import dk.flexibet.server.mbean.accountstatement.model.AccountStatementStat;

/**
 * Betfair bot statistics report.
 * 
 * @author daniel
 * 
 */
public class DashboardModule extends Panel {

	@SpringBean(name="marketObserverMBean")
	MarketObserverMBean marketObserverMBean;
	
	private final List<Market> markets;
	private final AccountStatementStat[] accountStatementStat;

	public DashboardModule(String id, AccountStatementStat[] accountStatementStat,List<Market> markets) {
		super(id);
		this.accountStatementStat = accountStatementStat;
		
		this.markets = markets;
		
		build();
	}

	private void build() {
		add(new MarketStatPanel("MarketStat", accountStatementStat));
		add(new StateStatPanel("StatesStat", accountStatementStat[0].getProfitPerStat(), accountStatementStat
				[1].getProfitPerStat()));

		addSportsStat();
	}

	

	private void addSportsStat() {
		/** Sports statistic */
		List<SportStat> sportsStat = SportsStatHelper.getSportsStat(markets,marketObserverMBean.getBwinMarkets());
		add(new ListView("sportStat", sportsStat) {

			@Override
			protected void populateItem(ListItem item) {
				SportStat sportStat = (SportStat) item.getModelObject();
				item.add(new Label("sport", sportStat.getSportName().toLowerCase()));
				item.add(new Label("betfair", "" + sportStat.getBetFairMarkets()));
				item.add(new Label("bwin", "" + sportStat.getBwinMarkets()));
				item.add(new Label("bwinMatched", "" + sportStat.getBwinMMatchedMarkets()));
				item.add(new Label("oddsChecker", "" + sportStat.getOddsCheckerMarkets()));
				item.add(new Label("oddsCheckerMatched", "" + sportStat.getOddsCheckerMatchedMarkets()));
			}
		});
	}


}
