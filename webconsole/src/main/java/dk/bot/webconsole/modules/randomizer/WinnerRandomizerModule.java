package dk.bot.webconsole.modules.randomizer;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.AjaxLazyLoadPanel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.SimpleFormComponentLabel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.NumberValidator;

import dk.flexibet.server.mbean.accountstatement.AccountStatementMBean;
import dk.flexibet.server.mbean.accountstatement.model.AccountStatementStat;

public class WinnerRandomizerModule extends Panel {

	@SpringBean(name="accountStatementMBean")
	private AccountStatementMBean accountStatementMBean;
	
	public WinnerRandomizerModule(String id) {
		super(id);
		
		build();
	}

	private void build() {

		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		feedback.setOutputMarkupId(true);
		add(feedback);

		Form form = new Form("form");
		form.setOutputMarkupId(true);
		add(form);

		/** Random qty field*/
		final RequiredTextField randomQtyInput = new RequiredTextField("randomQty", new Model(1), Integer.class);
		randomQtyInput.setLabel(new Model("Random iterations"));
		randomQtyInput.add(NumberValidator.range(1, 1000));
		form.add(randomQtyInput);
		form.add(new SimpleFormComponentLabel("randomQtyLabel", randomQtyInput));

		/**Commission field*/
		final RequiredTextField commissionInput = new RequiredTextField("commission", new Model(0.05), Double.class);
		commissionInput.setLabel(new Model("Commission"));
		commissionInput.add(NumberValidator.range(0d,1d));
		form.add(commissionInput);
		form.add(new SimpleFormComponentLabel("commissionLabel", commissionInput));

		/**Statistic panel*/
		form.add(getStatesStat((Integer)randomQtyInput.getModelObject(),(Double)commissionInput.getModelObject()));

		/**Refresh button*/
		form.add(new AjaxButton("submit", form) {
			protected void onSubmit(org.apache.wicket.ajax.AjaxRequestTarget target, Form form) {
				int randomQty = (Integer) randomQtyInput.getModelObject();
				double commission = (Double)commissionInput.getModelObject();
				form.remove("randomizerStatsPanel");
				form.add(getStatesStat(randomQty,commission));
				target.addComponent(form);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form form) {
				target.addComponent(feedback);
			}
		});

	}

	private Panel getStatesStat(final int randomQty, final double commission) {
		AjaxLazyLoadPanel ajaxLazyLoadPanel = new AjaxLazyLoadPanel("randomizerStatsPanel") {
			@Override
			public Component getLazyLoadComponent(String markupId) {
			//	AccountStatementStat[] accountStatementStat = accountStatementMBean.getAccountStatementStat(randomQty,commission);
				AccountStatementStat[] accountStatementStat = new AccountStatementStat[0];
				return new RandomizerStatsPanel(markupId, accountStatementStat);
			}
		};

		return ajaxLazyLoadPanel;
	}
}
