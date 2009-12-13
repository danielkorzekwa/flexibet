package dk.bot.webconsole.utils;

import java.util.HashMap;
import java.util.Map;

import dk.bot.bettingengine.statemachine.executor.StateStat;
import dk.bot.bettingengine.statemachine.executor.TransitionStat;
import dk.bot.bettingengine.statemachine.executor.VisitedStatesStat;
import dk.bot.bettingengine.statemachine.manager.StateMachineStat;

/**
 * Returns text-based report for visited states.
 * 
 * @author daniel
 * 
 */
public class VisitedStatesReport {

	public static String buildReport(VisitedStatesStat visitedStates) {
		Map<String, StateMachineStat> statMap = new HashMap<String, StateMachineStat>();
		
		StateMachineStat stateMachineStat = new StateMachineStat();
		stateMachineStat.setVisitedStatesStat(visitedStates);
		
		statMap.put("default", stateMachineStat);
		
		return buildReport(statMap);
	}
	
	public static String buildReport(Map<String,StateMachineStat> stateMachineStat) {

		StringBuilder builder = new StringBuilder();

		for (String stateMachineId : stateMachineStat.keySet()) {

			StateMachineStat stat = stateMachineStat.get(stateMachineId);
			builder.append(stateMachineId + ":" + "\n\n");
				
			append(builder,"Visited states:\n",3);
			for (StateStat stateStat : stat.getVisitedStatesStat().getStateStats()) {

				append(builder,stateStat.getStateName() + ": " + stateStat.getVisits() + "\n",6);

				for (TransitionStat transitionStat : stateStat.getTransitionStats()) {

					append(builder," -> " + transitionStat.getTarget() + ": " + transitionStat.getExecutions() + "\n",9);
				}
			}

			append(builder, "\n", 0);
			append(builder,"Not visited states:\n",3);
			for (StateStat stateStat : stat.getVisitedStatesStat().getStateStats()) {
				if (stateStat.getVisits() == 0) {
					append(builder,stateStat.getStateName() + "\n",6);
				}

				for (TransitionStat transitionStat : stateStat.getTransitionStats()) {

					if (transitionStat.getExecutions() == 0) {
						append(builder,stateStat.getStateName() + " -> " + transitionStat.getTarget() + "\n",9);
					}
				}
			}
			append(builder, "\n", 0);
		}

		return builder.toString();
	}
	
	/**Adds text to string builder preceded by number of spaces.
	 * 
	 * @param builder
	 * @param text
	 * @param tabsNum
	 */
	private static void append(StringBuilder builder, String text, int spaces) {
		for(int i=0;i<spaces;i++) {
			builder.append("&nbsp;");
		}
		builder.append(text);
	}
}
