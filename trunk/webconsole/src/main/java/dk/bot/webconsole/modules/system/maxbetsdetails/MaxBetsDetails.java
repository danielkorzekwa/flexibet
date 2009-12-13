package dk.bot.webconsole.modules.system.maxbetsdetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.AjaxLazyLoadPanel;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ibm.icu.text.SimpleDateFormat;

import dk.bot.betfairservice.counters.TransactionCounterState;
import dk.bot.bettingengine.mbean.BettingEngineMBean;
import dk.bot.webconsole.modules.system.maxbetsdetails.detailspanel.MaxBetsDetailsPanel;
import dk.flexibet.server.mbean.BettingServerMBean;
import dk.flexibet.server.model.BetsStat;

public class MaxBetsDetails extends BreadCrumbPanel {

	@SpringBean(name = "bettingEngineMBean")
	private BettingEngineMBean bettingEngineMBean;
	
	@SpringBean(name = "bettingServerMBean")
	private BettingServerMBean bettingServerMBean;

	private Form form = new Form("form");
	private Model selectedHour = new Model();

	private MaxBetsDetails me;

	public MaxBetsDetails(String id, final IBreadCrumbModel breadCrumbModel, TransactionCounterState txCounterState) {
		super(id, breadCrumbModel);
		me = this;

		add(form);

		List<HourSelection> hourOptions = getTimePeriods(txCounterState);
		DropDownChoice hourDropDown = new DropDownChoice("timePeriod", selectedHour, hourOptions);
		hourDropDown.setModelObject(hourOptions.get(2));
		form.add(hourDropDown);
		hourDropDown.add(new UpdateListener());

		final UpdateListener buttonListener = new UpdateListener();
		form.add(new AjaxButton("submit") {
			protected void onSubmit(org.apache.wicket.ajax.AjaxRequestTarget target, Form form) {
				buttonListener.onUpdate(target);
			};
		});

		form.add(getBetsStat(hourOptions.get(2).getHourTime()));
	}

	private Panel getBetsStat(final Date hourDate) {
		AjaxLazyLoadPanel panel = new AjaxLazyLoadPanel("betsStat") {

			@Override
			public Component getLazyLoadComponent(String markupId) {

				Date hourEnd = new Date(hourDate.getTime() + (1000 * 3600));
				List<BetsStat> betsStat = bettingServerMBean.getBetsStat(hourDate, hourEnd);
				return new MaxBetsDetailsPanel(markupId, me, betsStat);
			}

		};
		return panel;
	}

	private List<HourSelection> getTimePeriods(TransactionCounterState txCounterState) {
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		ArrayList<HourSelection> timePeriods = new ArrayList<HourSelection>();
		timePeriods.add(new HourSelection("Max hour : " + df.format(txCounterState.getMaxHourAsDate()), txCounterState
				.getMaxHourAsDate()));
		timePeriods.add(new HourSelection("Last hour : " + df.format(txCounterState.getLastHourAsDate()),
				txCounterState.getLastHourAsDate()));
		timePeriods.add(new HourSelection("Current hour : " + df.format(txCounterState.getCurrentHourAsDate()),
				txCounterState.getCurrentHourAsDate()));
		return timePeriods;
	}

	public String getTitle() {
		return "MaxBetsDetails";
	}

	private class UpdateListener extends OnChangeAjaxBehavior {

		@Override
		protected void onUpdate(AjaxRequestTarget target) {
			HourSelection selection = (HourSelection) selectedHour.getObject();
			form.remove("betsStat");
			form.add(getBetsStat(selection.getHourTime()));

			target.addComponent(form);
		}

	}

	private class HourSelection implements Serializable {

		private final String periodName;
		private final Date hourTime;

		public HourSelection(String periodName, Date hourTime) {
			this.periodName = periodName;
			this.hourTime = hourTime;

		}

		public String getPeriodName() {
			return periodName;
		}

		public Date getHourTime() {
			return hourTime;
		}

		@Override
		public String toString() {
			return periodName;
		}
	}
}
