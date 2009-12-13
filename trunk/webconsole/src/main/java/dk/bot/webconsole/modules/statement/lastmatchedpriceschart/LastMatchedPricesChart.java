package dk.bot.webconsole.modules.statement.lastmatchedpriceschart;

/**Chart with last matched prices for market runner.
 * 
 * @author daniel
 *
 */
import org.apache.wicket.PageParameters;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.markup.html.link.InlineFrame;

import dk.bot.webconsole.pages.histpricechart.HistRunnerPricesChartPage;

public class LastMatchedPricesChart extends BreadCrumbPanel{

	private final String selectionName;

	public LastMatchedPricesChart(String id,final IBreadCrumbModel breadCrumbModel,int marketId, int selectionId, String selectionName, int sportId) {
		super(id,breadCrumbModel);
		this.selectionName = selectionName;
		
		PageParameters parameters = new PageParameters();
		parameters.put("marketId", marketId);
		parameters.put("selectionId", selectionId);
		
		if(sportId==7) {
		parameters.put("inPlay", false);
		parameters.put("probs", false);
		}
		else {
			parameters.put("inPlay", true);
			parameters.put("probs", true);
		}
		
		HistRunnerPricesChartPage histRunnerPricesChartPage = new HistRunnerPricesChartPage(parameters );
		
		InlineFrame inlineFrame = new InlineFrame("lastMatchedPricesChart",histRunnerPricesChartPage);
		add(inlineFrame);
	}
	
	public String getTitle() {
		return "LastMatchedPrice for " + selectionName;
	}
}
