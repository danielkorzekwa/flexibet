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

import dk.bot.marketobserver.model.BetType;
import dk.bot.marketobserver.model.MUBet;
import dk.bot.webconsole.pages.MarketDetailsPage;
import dk.bot.webconsole.utils.MarketDeepLink;

/**
 * Table with bets.
 * 
 * @author daniel
 * 
 */
public class SPBetStatPanel extends Panel {

	public SPBetStatPanel(String id, List<MUBet> bets) {
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
				item.add(new Label("betType", bet.getBetType().value()));
				item.add(new Label("betId", "" + bet.getBetId()));
				item.add(new Label("betPlaced", simpleDateFormat.format(bet.getPlacedDate())));

				if (bet.getPrice() > 0) {
					item.add(new Label("oddsReq", "" + MathUtils.round(bet.getPrice(), 2)));
				} else {
					item.add(new Label("oddsReq", ""));
				}
				if (bet.getBetType() == BetType.B) {
					item.add(new Label("stake", "" + MathUtils.round(bet.getBspLiability(), 2)));
				} else if (bet.getBetType() == BetType.L) {
					item.add(new Label("stake", ""));
				} else {
					throw new IllegalArgumentException("Bet type not supported: " + bet.getBetType());
				}
				item.add(new Label("liability", "" + MathUtils.round(bet.getBspLiability(), 2)));
			}

		};

		return listView;
	}

}
