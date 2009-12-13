package dk.bot.webconsole.modules.statement.home.navigation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.joda.time.DateMidnight;

import dk.bot.bettingengine.statemachine.StateMachineServiceMBean;
import dk.bot.webconsole.modules.statement.home.stathelper.AccountStatementOrderEnum;
import dk.bot.webconsole.modules.statement.home.stathelper.TimePeriod;

public class NavigationPanel extends Panel {

	@SpringBean(name = "stateMachineServiceMBean")
	private StateMachineServiceMBean stateMachineServiceMBean;

	/** Time periods dropdown config */
	private DateMidnight dateMidnight = new DateMidnight();

	private Model selectedTimePeriod = new Model();
	private List<TimePeriod> timePeriodOptions = Arrays.asList(new TimePeriod("For Today", dateMidnight.toDate()),
			new TimePeriod("From Yesterday", dateMidnight.minusDays(1).toDate()), new TimePeriod("This week",
					dateMidnight.withDayOfWeek(1).toDate()),new TimePeriod("Last 30 days",dateMidnight.minusDays(30).toDate()),new TimePeriod("Last 3 months",dateMidnight.minusMonths(3).toDate()));
	private DropDownChoice periodsDropDown = new DropDownChoice("timePeriod", selectedTimePeriod, timePeriodOptions);;

	private Model selectedItemType = new Model();
	private List<SelectOption> itemTypeOptions = Arrays.asList(new SelectOption(0, "Bet"), new SelectOption(1,
			"Non-bet"));
	private DropDownChoice itemTypeDropDown = new DropDownChoice("itemType", selectedItemType, itemTypeOptions);

	private Model selectedStateMachineId = new Model();
	private List<SelectOption> stateMachineIdOptions = new ArrayList<SelectOption>();
	private DropDownChoice stateMachineDropDown = new DropDownChoice("stateMachineId", selectedStateMachineId,
			stateMachineIdOptions);

	private Model selectedStateName = new Model();
	private List<SelectOption> stateNameOptions = new ArrayList<SelectOption>();
	private DropDownChoice stateNameDropDown = new DropDownChoice("stateName", selectedStateName, stateNameOptions);

	private Model selectedOrder = new Model();
	private List<AccountStatementOrderEnum> orderByOptions = Arrays.asList(AccountStatementOrderEnum.values());
	private DropDownChoice orderDropDown = new DropDownChoice("orderBy", selectedOrder, orderByOptions);

	BetItemNavPanel betItemNav = new BetItemNavPanel("betItemNav","betItemNavFragment");
	
	private final NavigationPanelListener listener;
	private final Form form;

	public NavigationPanel(String id, Form form, NavigationPanelListener listener) {
		super(id);
		this.form = form;
		this.listener = listener;
		
		build(form);
	}

	public TimePeriod getDefaultPeriod() {
		return timePeriodOptions.get(0);
	}

	private void build(Form form) {

		periodsDropDown.setModelObject(timePeriodOptions.get(0));
		periodsDropDown.add(new NavigationListener(false));
		add(periodsDropDown);

		itemTypeDropDown.setModelObject(itemTypeOptions.get(0));
		itemTypeDropDown.add(new NavigationListener(false));
		add(itemTypeDropDown);
		
		add(betItemNav);

		final NavigationListener refreshButtonListener = new NavigationListener(false);
		AjaxButton refreshButton = new AjaxButton("submit") {
			protected void onSubmit(org.apache.wicket.ajax.AjaxRequestTarget target, Form form) {
				refreshButtonListener.onUpdate(target);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form form) {

			}
		};
		add(refreshButton);
	}

	/**
	 * Reloads account statement items if any of drop down fields is changed.
	 * 
	 * @author daniel
	 * 
	 */
	private class NavigationListener extends OnChangeAjaxBehavior {

		private final boolean reloadStates;

		public NavigationListener(boolean reloadStates) {
			this.reloadStates = reloadStates;
		}

		@Override
		protected void onUpdate(AjaxRequestTarget target) {
			TimePeriod period = (TimePeriod) selectedTimePeriod.getObject();

			SelectOption itemTypeSelection = (SelectOption) selectedItemType.getObject();
			boolean betItems = itemTypeSelection.getIndex() == 0 ? true : false;

			SelectOption stateMachineSelection = (SelectOption) selectedStateMachineId.getObject();
			String stateMachineId = null;
			String stateName = null;
			AccountStatementOrderEnum orderBy = (AccountStatementOrderEnum) selectedOrder.getObject();

			if (betItems) {
				betItemNav.setVisible(true);

				/** Set stateMachineId/stateName. */
				if (stateMachineSelection.getIndex() == 0) {
					/** Clear state names */
					stateNameOptions.clear();
					stateNameDropDown.setEnabled(false);
				} else {
					stateMachineId = stateMachineSelection.getValue();
					/** Load state names */
					if (reloadStates || !stateNameDropDown.isEnabled()) {
						stateNameOptions.clear();
						stateNameOptions.add(new SelectOption(0, "All state names"));
						List<String> stateNamesList = stateMachineServiceMBean.getStates(stateMachineId);
						for (int i = 0; i < stateNamesList.size(); i++) {
							stateNameOptions.add(new SelectOption(i + 1, stateNamesList.get(i)));
						}
						stateNameDropDown.setEnabled(true);
						stateNameDropDown.setModelObject(stateNameOptions.get(0));
					}
					SelectOption stateNameSelection = (SelectOption) selectedStateName.getObject();
					if (stateNameSelection.getIndex() > 0) {
						stateName = stateNameSelection.getValue();
					}
				}

			} else {
				betItemNav.setVisible(false);
			}

			listener.onUpdate(period, stateMachineId, stateName, orderBy, betItems);
			target.addComponent(form);
		}

	}
	
	private class BetItemNavPanel extends Fragment {

		public BetItemNavPanel(String id, String markupId) {
			super(id, markupId);
			
			/** State machine dropdown config. */
			stateMachineIdOptions.add(new SelectOption(0, "All state machines"));
			List<String> stateMachinesList = stateMachineServiceMBean.getStateMachines();
			for (int i = 0; i < stateMachinesList.size(); i++) {
				stateMachineIdOptions.add(new SelectOption(i + 1, stateMachinesList.get(i)));
			}
			stateMachineDropDown.setModelObject(stateMachineIdOptions.get(0));
			stateMachineDropDown.add(new NavigationListener(true));
			add(stateMachineDropDown);

			stateNameOptions.add(new SelectOption(0, "All state names"));
			stateNameDropDown.setModelObject(stateNameOptions.get(0));
			stateNameDropDown.add(new NavigationListener(false));
			add(stateNameDropDown);
			stateNameDropDown.setEnabled(false);

			orderDropDown.setModelObject(orderByOptions.get(0));
			orderDropDown.add(new NavigationListener(false));
			add(orderDropDown);
		}
		
	}

	/** Convenient class for dropdown list selections. */
	private class SelectOption implements Serializable {

		private final int index;
		private final String value;

		public SelectOption(int index, String value) {
			this.index = index;
			this.value = value;
		}

		public int getIndex() {
			return index;
		}

		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return value;
		}
	}

}
