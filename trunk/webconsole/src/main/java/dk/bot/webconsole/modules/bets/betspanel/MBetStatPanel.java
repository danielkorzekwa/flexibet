package dk.bot.webconsole.modules.bets.betspanel;

import java.util.Collections;
import java.util.List;

import org.apache.commons.math.util.MathUtils;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;

import com.ibm.icu.text.SimpleDateFormat;

import dk.bot.marketobserver.model.BetCategoryType;
import dk.bot.marketobserver.model.MUBet;
import dk.bot.webconsole.pages.MarketDetailsPage;
import dk.bot.webconsole.utils.MarketDeepLink;

/**
 * Table with bets.
 * 
 * @author daniel
 * 
 */
public class MBetStatPanel extends Panel {

	public MBetStatPanel(String id, List<MUBet> bets) {
		super(id);
		
		add(new Label("betsNum", "" + bets.size()));
		add(getBets(bets));
	}

	private ListView getBets(List<MUBet> bets) {
		Collections.sort(bets);

		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

		ListView listView = new ListView("item", bets) {

			@Override
			protected void populateItem(ListItem item) {
				final MUBet bet = (MUBet) item.getModelObject();

				item.add(new Label("market", bet.getFullMarketName()));
				
				/**Add marketDetailsLink*/
				PageParameters parameters = new PageParameters();	
				parameters.put(MarketDetailsPage.PAGE_PARAM_MARKETID,bet.getMarketId());
				BookmarkablePageLink marketDetailsLink = new BookmarkablePageLink("marketIdLink",MarketDetailsPage.class,parameters );
				marketDetailsLink.add(new Label("marketId", "" + bet.getMarketId()));
				item.add(marketDetailsLink);
				item.add(new ExternalLink("betFairLink",MarketDeepLink.generateMarketDeepLink(bet.getMarketId())));
				
				String selectionName = bet.getSelectionName();
				if (selectionName != null) {
					item.add(new Label("selection", selectionName));
				} else {
					item.add(new Label("selection", "" + bet.getSelectionId()));
				}
				
				if(bet.getBetCategoryType()== BetCategoryType.M || bet.getBetCategoryType()== BetCategoryType.L) {
					item.add(new Label("betType", "" + bet.getBetType().name() + " (SP)"));
				}
				else {
					item.add(new Label("betType", "" + bet.getBetType().name()));	
				}
				
				item.add(new Label("betId", "" + bet.getBetId()));
				item.add(new Label("betPlaced", simpleDateFormat.format(bet.getPlacedDate())));
				item.add(new Label("avgOdds", "" + MathUtils.round(bet.getPrice(), 2)));
				item.add(new Label("stake", "" + MathUtils.round(bet.getSize(), 2)));
				item.add(new Label("matchedDate", simpleDateFormat.format(bet.getMatchedDate())));
			}

		};

		return listView;
	}

	
}
