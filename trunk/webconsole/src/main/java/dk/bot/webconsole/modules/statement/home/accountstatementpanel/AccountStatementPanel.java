package dk.bot.webconsole.modules.statement.home.accountstatementpanel;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math.util.MathUtils;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanelLink;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ibm.icu.text.SimpleDateFormat;

import dk.bot.marketobserver.mbean.MarketObserverMBean;
import dk.bot.webconsole.modules.statement.lastmatchedpriceschart.LastMatchedPricesChartFactory;
import dk.bot.webconsole.panels.marketdetails.MarketDetailsPanelFactory;
import dk.flexibet.server.model.DetailedAccountStatementItem;

public class AccountStatementPanel extends Panel {

	@SpringBean(name="marketObserverMBean")
	MarketObserverMBean marketObserverMBean;
	private final BreadCrumbPanel parent;
	
	public AccountStatementPanel(String id,BreadCrumbPanel parent,List<DetailedAccountStatementItem> accountStatement) {
		super(id);
		this.parent = parent;
		
		double profit=0;
		double commission=0;
		int numOfBets=0;
		List<DetailedAccountStatementItem> displayedItems = new ArrayList<DetailedAccountStatementItem>();
		for(DetailedAccountStatementItem item: accountStatement) {
			profit=profit+item.getAmount();
			if(item.isCommission()) {
				commission = commission + item.getAmount();
			}
			else {
				numOfBets++;
			}
			
			if(displayedItems.size()<300) {
				displayedItems.add(item);
			}
		}
		
		add(new Label("profit", "" + MathUtils.round(profit,2)));
		add(new Label("commission", "" + MathUtils.round(-commission,2)));
		add(new Label("numOfBets", "" + numOfBets));
		add(new Label("numOfDisplayedItems", "" + displayedItems.size()));
		add(new Label("numOfAllItems", "" + accountStatement.size()));
		add(getAccountStatement(displayedItems));

	}

	private ListView getAccountStatement(List<DetailedAccountStatementItem> accountStatement) {
		
		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

		ListView listView = new ListView("item", accountStatement) {

			@Override
			protected void populateItem(ListItem item) {
				final DetailedAccountStatementItem accountItem = (DetailedAccountStatementItem) item.getModelObject();
				item.add(new Label("settled", simpleDateFormat.format(accountItem.getSettledDate())));
				item.add(new Label("refId", "" + accountItem.getBetId()));
				item.add(new Label("placed", "" + simpleDateFormat.format(accountItem.getPlacedDate())));
			
				/**marketDetailsLink*/			
				IBreadCrumbPanelFactory marketDetailsFactory = new MarketDetailsPanelFactory(marketObserverMBean.getCompleteMarket(accountItem.getMarketId()));
				BreadCrumbPanelLink marketDetailsLink = new BreadCrumbPanelLink("marketIdLink", parent.getBreadCrumbModel(),
						marketDetailsFactory);
				
				/**marketId*/
				marketDetailsLink.add(new Label("marketId", "" + accountItem.getMarketId()));
				item.add(marketDetailsLink);
				
				item.add(new Label("description", "" + accountItem.getFullMarketName()));
				item.add(new Label("selection", "" + accountItem.getSelectionName()));
				
				/**lastMatchedPricesChartLink*/			
				IBreadCrumbPanelFactory lastMatchedPricesChartFactory = new LastMatchedPricesChartFactory(accountItem.getMarketId(),accountItem.getSelectionId(),accountItem.getSelectionName(),accountItem.getEventTypeId());
				BreadCrumbPanelLink lastMatchedPricesChartLink = new BreadCrumbPanelLink("lastMatchedPricesChartLink", parent.getBreadCrumbModel(),
						lastMatchedPricesChartFactory);
				item.add(lastMatchedPricesChartLink);
				
				/**betType*/
				if(accountItem.getBetCategoryType().equals("M") || accountItem.getBetCategoryType().equals("L")) {
					item.add(new Label("betType", "" + accountItem.getBetType() + " (SP)"));
				}
				else {
					item.add(new Label("betType", "" + accountItem.getBetType()));	
				}
				
				item.add(new Label("avgOdds", "" + MathUtils.round(accountItem.getAvgPrice(),2)));
				item.add(new Label("stake", "" + MathUtils.round(accountItem.getBetSize(),2)));
				item.add(new Label("amount", "" + MathUtils.round(accountItem.getAmount(),2)));
			}

		};

		return listView;
	}

}
