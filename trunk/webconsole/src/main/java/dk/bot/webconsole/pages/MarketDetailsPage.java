package dk.bot.webconsole.pages;

import org.apache.wicket.PageParameters;
import org.apache.wicket.extensions.breadcrumb.BreadCrumbBar;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.spring.injection.annot.SpringBean;

import dk.bot.marketobserver.mbean.MarketObserverMBean;
import dk.bot.webconsole.panels.marketdetails.MarketDetailsPanel;

/** Displays market details.
 * 
 * @author daniel
 *
 */
public class MarketDetailsPage extends WebPage{

	public static final String PAGE_PARAM_MARKETID="marketId";
	
	@SpringBean(name="marketObserverMBean")
	MarketObserverMBean marketObserverMBean;
	
	public MarketDetailsPage(final PageParameters parameters) {
		super();
		
		int marketId = parameters.getInt(PAGE_PARAM_MARKETID, -1);
		
		add(new MarketDetailsPanel("marketDetails",new BreadCrumbBar("breadCrumbBar"),marketObserverMBean.getCompleteMarket(marketId)));
	}
}
