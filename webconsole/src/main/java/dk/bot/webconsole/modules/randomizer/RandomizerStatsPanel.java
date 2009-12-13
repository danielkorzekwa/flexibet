package dk.bot.webconsole.modules.randomizer;

import org.apache.wicket.markup.html.panel.Panel;

import dk.bot.webconsole.panels.marketstat.MarketStatPanel;
import dk.bot.webconsole.panels.statestat.StateStatPanel;
import dk.flexibet.server.mbean.accountstatement.model.AccountStatementStat;

/** Statistics panel with profit per states and profit per week.
 * 
 * @author daniel
 *
 */
public class RandomizerStatsPanel extends Panel{

	private final AccountStatementStat[] accountStatementStat;

	public RandomizerStatsPanel(String id,AccountStatementStat[] accountStatementStat) {
		super(id);
		this.accountStatementStat = accountStatementStat;
		
		build();
	}
	
	private void build() {
	
		add(new StateStatPanel("statesStat", accountStatementStat[0].getProfitPerStat(), accountStatementStat[1]
				.getProfitPerStat()));
		
		add(new MarketStatPanel("MarketStat", accountStatementStat));
	}
}
