package dk.bot.webconsole.modules.markets;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.breadcrumb.BreadCrumbBar;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.SimpleFormComponentLabel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.NumberValidator;

import dk.bot.marketobserver.mbean.MarketObserverMBean;
import dk.bot.marketobserver.model.Market;
import dk.bot.webconsole.panels.marketdetails.MarketDetailsPanel;

public class MarketsPanel extends Panel {

	@SpringBean(name="marketObserverMBean")
	private MarketObserverMBean marketObserverMBean;

	public MarketsPanel(String id) {
		super(id);
		build();
	}

	private void build() {

		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		feedback.setOutputMarkupId(true);
		add(feedback);

		final Form form = new Form("form");
		form.setOutputMarkupId(true);
		add(form);

		final RequiredTextField marketIdInput = new RequiredTextField("marketId", new Model(), Integer.class);
		marketIdInput.setLabel(new Model("Market Id"));
		marketIdInput.add(NumberValidator.POSITIVE);
		form.add(marketIdInput);
		form.add(new SimpleFormComponentLabel("marketIdLabel", marketIdInput));

		form.add(new MarketDetailsPanel("marketDetails",new BreadCrumbBar("breadCrumbBar"),null));

		form.add(new AjaxButton("submit", form) {
			protected void onSubmit(org.apache.wicket.ajax.AjaxRequestTarget target, Form form) {

				int marketId = (Integer) marketIdInput.getModelObject();
				Market completeMarket = marketObserverMBean.getCompleteMarket(marketId);
				if (completeMarket != null) {
					form.remove("marketDetails");
					form.add(new MarketDetailsPanel("marketDetails",new BreadCrumbBar("breadCrumbBar"),completeMarket));
					target.addComponent(form);
				} else {
					feedback.error("No market");
				}

				target.addComponent(feedback);

			}

			@Override
			protected void onError(AjaxRequestTarget target, Form form) {
				target.addComponent(feedback);
			}
		});
		
	}

	
}
