package dk.bot.webconsole.modules.statement;

import org.apache.wicket.extensions.breadcrumb.BreadCrumbBar;
import org.apache.wicket.markup.html.panel.Panel;

import dk.bot.webconsole.modules.statement.home.StatementHome;

public class AccountStatementModule extends Panel {

	public AccountStatementModule(String id) {
		super(id);

//		String url = ";
//		Label lastMatchedPrices = new Label("lastMatchedPrices",
//				"<iframe src =\"" + url + "\" width=\"900px\" height=\"620px\"><p>Your browser does not support iframes.</p></iframe>");
//		lastMatchedPrices.setEscapeModelStrings(false);
//		
		//add(lastMatchedPrices);
		
		
		BreadCrumbBar breadCrumbBar = new BreadCrumbBar("breadCrumbBar");
		add(breadCrumbBar);
		StatementHome firstPanel = new StatementHome("panel", breadCrumbBar);
		add(firstPanel);
		breadCrumbBar.setActive(firstPanel);
	}

}
