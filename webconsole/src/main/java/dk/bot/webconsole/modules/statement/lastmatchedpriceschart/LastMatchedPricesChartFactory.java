package dk.bot.webconsole.modules.statement.lastmatchedpriceschart;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;

/**Create LastMatchedPricesChart panel
 * 
 * @author daniel
 *
 */
public class LastMatchedPricesChartFactory implements IBreadCrumbPanelFactory{

	private final int marketId;
	private final int selectionId;
	private final String selectionName;
	private final int sportId;

	public LastMatchedPricesChartFactory(int marketId,int selectionId, String selectionName, int sportId) {
		this.marketId = marketId;
		this.selectionId = selectionId;
		this.selectionName = selectionName;
		this.sportId = sportId;
	}
	
	public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
		return new LastMatchedPricesChart(componentId,breadCrumbModel,marketId,selectionId,selectionName,sportId);
	}

}
