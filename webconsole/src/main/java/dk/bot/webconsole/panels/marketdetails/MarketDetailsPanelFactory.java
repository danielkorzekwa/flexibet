package dk.bot.webconsole.panels.marketdetails;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;

import dk.bot.marketobserver.model.Market;

/**Create marketDetailsPanel.
 * 
 * @author daniel
 *
 */
public class MarketDetailsPanelFactory implements IBreadCrumbPanelFactory{

	private final Market completeMarket;

	public MarketDetailsPanelFactory(Market completeMarket) {
		this.completeMarket = completeMarket;
	}

	public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
		return new MarketDetailsPanel(componentId, breadCrumbModel, completeMarket);
	}
}
