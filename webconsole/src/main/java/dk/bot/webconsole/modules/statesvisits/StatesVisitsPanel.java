package dk.bot.webconsole.modules.statesvisits;

import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import dk.bot.bettingengine.statemachine.StateMachineServiceMBean;
import dk.bot.webconsole.utils.VisitedStatesReport;

public class StatesVisitsPanel extends Panel{

	@SpringBean(name="stateMachineServiceMBean")
	private StateMachineServiceMBean stateMachineServiceMBean;
	
	

	public StatesVisitsPanel(String id) {
		super(id);
		
		build();
	}
	
	private void build() {
		
		String reportString = VisitedStatesReport.buildReport(stateMachineServiceMBean.getStateMachineStat());
		
		MultiLineLabel multiLineLabel = new MultiLineLabel("visits",reportString);
		multiLineLabel.setEscapeModelStrings(false);
		add(multiLineLabel);
	}

}
