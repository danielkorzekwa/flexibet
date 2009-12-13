package dk.bot.webconsole.panels.statestat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.math.util.MathUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import dk.flexibet.server.mbean.accountstatement.model.StateMachineStat;
import dk.flexibet.server.mbean.accountstatement.model.StatesStat;

public class StateStatPanel extends Panel{

	public StateStatPanel(String id, List<StateMachineStat> thisWeekStateMachineStats,List<StateMachineStat> lastWeekStateMachineStats) {
		super(id);
		
		add(getStatesStat(thisWeekStateMachineStats,lastWeekStateMachineStats));
	}
	
	private ListView getStatesStat(List<StateMachineStat> thisWeekStateMachineStats,List<StateMachineStat> lastWeekStateMachineStats) {
		
		/**Creates data model for states stat table.*/
		ArrayList<Object> stateStatList = new ArrayList<Object>();
		for(StateMachineStat stateMachineStat: thisWeekStateMachineStats) {
			List<StatesStat> statesStat = new ArrayList<StatesStat>();
			statesStat.addAll(stateMachineStat.getStatesStat().values());
			Collections.sort(statesStat);
			
			stateStatList.add(stateMachineStat);
			stateStatList.addAll(statesStat);
		}
		
		/**prepare last week states map. Key - stateMachineId*/
		final HashMap<String, StateMachineStat> lastWeekStateMachineStatsMap = new HashMap<String,StateMachineStat>();
		for(StateMachineStat stateMachineStat: lastWeekStateMachineStats) {
			lastWeekStateMachineStatsMap.put(stateMachineStat.getStateMachineId(), stateMachineStat);
		}

		ListView listView = new ListView("statesStat", stateStatList) {

			@Override
			protected void populateItem(ListItem item) {
				
				if(item.getModelObject() instanceof StateMachineStat) {
					item.add(new AttributeModifier("class",true,new Model("highlighted")));
					
					StateMachineStat stateMachineStat = (StateMachineStat) item.getModelObject();
					item.add(new Label("state", stateMachineStat.getStateMachineId()!=null ? "" + stateMachineStat.getStateMachineId() : "Unknown machine"));
					item.add(new Label("green", "" + stateMachineStat.getGreenRunners() + " ("
							+ MathUtils.round(stateMachineStat.getProfitLossGreen(),2) + ")"));
					item.add(new Label("white", "" + stateMachineStat.getWhiteRunners() + " (0)"));
					item.add(new Label("red", "" + stateMachineStat.getRedRunners() + " ("
							+ MathUtils.round(stateMachineStat.getProfitLossRed(),2) + ")"));
					item.add(new Label("total", "" + stateMachineStat.getTotalRunners() + " ("
							+ MathUtils.round(stateMachineStat.getProfitLossTotal(),2) + ")"));
					
					StateMachineStat lastWeekstateMachineStat = lastWeekStateMachineStatsMap.get(stateMachineStat.getStateMachineId());
					if(lastWeekstateMachineStat!=null) {
						item.add(new Label("lastWeekGreen", "" + lastWeekstateMachineStat.getGreenRunners() + " ("
								+ MathUtils.round(lastWeekstateMachineStat.getProfitLossGreen(),2) + ")"));
						item.add(new Label("lastWeekWhite", "" + lastWeekstateMachineStat.getWhiteRunners() + " (0)"));
						item.add(new Label("lastWeekRed", "" + lastWeekstateMachineStat.getRedRunners() + " ("
								+ MathUtils.round(lastWeekstateMachineStat.getProfitLossRed(),2) + ")"));
						item.add(new Label("lastWeekTotal", "" + lastWeekstateMachineStat.getTotalRunners() + " ("
								+ MathUtils.round(lastWeekstateMachineStat.getProfitLossTotal(),2) + ")"));
					} else {
						item.add(new Label("lastWeekGreen", "none"));
						item.add(new Label("lastWeekWhite", "none"));
						item.add(new Label("lastWeekRed", "none"));
						item.add(new Label("lastWeekTotal", "none"));

					}
					
					
				}else if(item.getModelObject() instanceof StatesStat) {
				
					StatesStat statesStat = (StatesStat) item.getModelObject();
					item.add(new Label("state", statesStat.getStateName()!=null ? "" + statesStat.getStateName() : "Unknown state"));
					item.add(new Label("green", "" + statesStat.getGreenRunners() + " ("
							+ MathUtils.round(statesStat.getProfitLossGreen(),2) + ")"));
					item.add(new Label("white", "" + statesStat.getWhiteRunners() + " (0)"));
					item.add(new Label("red", "" + statesStat.getRedRunners() + " ("
							+ MathUtils.round(statesStat.getProfitLossRed(),2) + ")"));
					item.add(new Label("total", "" + statesStat.getTotalRunners() + " ("
							+ MathUtils.round(statesStat.getProfitLossTotal(),2) + ")"));

					StateMachineStat lastWeekstateMachineStat = lastWeekStateMachineStatsMap.get(statesStat.getStateMachineId());
					if(lastWeekstateMachineStat!=null) {
						
						StatesStat lastWeekStatesStat = lastWeekstateMachineStat.getStatesStat().get(statesStat.getStateName());
						if (lastWeekStatesStat != null) {
							item.add(new Label("lastWeekGreen", "" + lastWeekStatesStat.getGreenRunners() + " ("
									+ MathUtils.round(lastWeekStatesStat.getProfitLossGreen(),2) + ")"));
							item.add(new Label("lastWeekWhite", "" + lastWeekStatesStat.getWhiteRunners() + " (0)"));
							item.add(new Label("lastWeekRed", "" + lastWeekStatesStat.getRedRunners() + " ("
									+ MathUtils.round(lastWeekStatesStat.getProfitLossRed(),2) + ")"));
							item.add(new Label("lastWeekTotal", "" + lastWeekStatesStat.getTotalRunners() + " ("
									+ MathUtils.round(lastWeekStatesStat.getProfitLossTotal(),2) + ")"));
						} else {
							item.add(new Label("lastWeekGreen", "none"));
							item.add(new Label("lastWeekWhite", "none"));
							item.add(new Label("lastWeekRed", "none"));
							item.add(new Label("lastWeekTotal", "none"));

						}
					}
					else {
						item.add(new Label("lastWeekGreen", "none"));
						item.add(new Label("lastWeekWhite", "none"));
						item.add(new Label("lastWeekRed", "none"));
						item.add(new Label("lastWeekTotal", "none"));

					}
				}
			}
		};

		return listView;
	}
}
