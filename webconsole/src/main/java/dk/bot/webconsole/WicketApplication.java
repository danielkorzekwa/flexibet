package dk.bot.webconsole;

import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;

import dk.bot.webconsole.pages.BetFairConsolePage;
import dk.bot.webconsole.pages.MarketDetailsPage;
import dk.bot.webconsole.pages.VirtualConsolePage;
import dk.bot.webconsole.pages.histpricechart.HistRunnerPricesChartPage;
import dk.bot.webconsole.pages.test.TestPage;

/**
 * Application object for your web application. If you want to run this
 * application without deploying, run the Start class.
 * 
 * @see wicket.myproject.BettingConsoleStart#main(String[])
 */
public class WicketApplication extends WebApplication {
	
	/**
	 * Constructor
	 */
	public WicketApplication() {
		
	}
	
	/**wicket init method*/
	public void init() {
		super.init();
		addComponentInstantiationListener(new SpringComponentInjector(this));
		
		mountBookmarkablePage("BetFairConsole", BetFairConsolePage.class);
		mountBookmarkablePage("VirtualConsole", VirtualConsolePage.class);
		mountBookmarkablePage("MarketDetails", MarketDetailsPage.class);
		mountBookmarkablePage("HistRunnerPricesChart", HistRunnerPricesChartPage.class);
		mountBookmarkablePage("Test", TestPage.class);
	}

	/**
	 * @see wicket.Application#getHomePage()
	 */
	public Class getHomePage() {
		return BetFairConsolePage.class;
	}
}
