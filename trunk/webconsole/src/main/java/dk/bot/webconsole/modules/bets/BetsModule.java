package dk.bot.webconsole.modules.bets;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.extensions.ajax.markup.html.AjaxLazyLoadPanel;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import dk.bot.marketobserver.mbean.MarketObserverMBean;
import dk.bot.marketobserver.model.BetCategoryType;
import dk.bot.marketobserver.model.BetStatus;
import dk.bot.marketobserver.model.MUBet;
import dk.bot.marketobserver.model.MUBets;
import dk.bot.webconsole.modules.bets.betspanel.MBetStatPanel;
import dk.bot.webconsole.modules.bets.betspanel.SPBetStatPanel;
import dk.bot.webconsole.modules.bets.betspanel.UBetStatPanel;

/**Displays bets for bet status.
 * 
 * @author daniel
 *
 */
public class BetsModule extends Panel {

	private static final String BET_TYPE_UNMATCHED="Unmatched";
	private static final String BET_TYPE_MATCHED="Matched";
	private static final String BET_TYPE_SP="SP Placed";
	
	@SpringBean(name="marketObserverMBean")
	MarketObserverMBean marketObserverMBean;
	
	private List<String> betStatuses = new ArrayList<String>();
	
	public BetsModule(String id) {
		super(id);
		
		betStatuses.add(BET_TYPE_UNMATCHED);
		betStatuses.add(BET_TYPE_MATCHED);
		betStatuses.add(BET_TYPE_SP);
		
		build();
	}

	private void build() {
		final Form form = new Form("form");
		form.setOutputMarkupId(true);
		add(form);

		final Model selectedBetStatus = new Model();
		DropDownChoice betStatusDropDown = new DropDownChoice("betStatus", selectedBetStatus, betStatuses);
		betStatusDropDown.setModelObject(betStatuses.get(0));
		form.add(betStatusDropDown);

		final Panel betsPanel = getBets(betStatuses.get(0));
		form.add(betsPanel);

		OnChangeAjaxBehavior onChangeAjaxBehavior = new OnChangeAjaxBehavior() {

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				String betStatus = (String) selectedBetStatus.getObject();
				form.remove(betsPanel);
				form.add(getBets(betStatus));

				target.addComponent(form);
			}

		};

		betStatusDropDown.add(onChangeAjaxBehavior);
	}

	/**
	 * Gets bets for bet status
	 * 
	 * @param betStatus
	 * @return
	 */
	public Panel getBets(final String betstatus) {
		AjaxLazyLoadPanel ajaxLazyLoadPanel = new AjaxLazyLoadPanel("bets") {
			@Override
			public Component getLazyLoadComponent(String markupId) {
				
				if(betstatus==BET_TYPE_UNMATCHED) {
					MUBets bets = marketObserverMBean.getMUBets().getBets(BetStatus.U).getBets(BetCategoryType.E);
					return new UBetStatPanel(markupId, bets.getMuBets());
				}
				else if(betstatus==BET_TYPE_MATCHED) {
					MUBets bets = marketObserverMBean.getMUBets().getBets(BetStatus.M);
					return new MBetStatPanel(markupId, bets.getMuBets());
			
				}
				else if(betstatus==BET_TYPE_SP) {
					List<MUBet> spBets = marketObserverMBean.getMUBets().getBets(BetStatus.U).getSPBets();
					return new SPBetStatPanel(markupId, spBets);
			
				}
				else {
					throw new IllegalArgumentException("Not supported bet type: " + betstatus);
				}	
			}
		};

		return ajaxLazyLoadPanel;
		
		
		
	}

}
