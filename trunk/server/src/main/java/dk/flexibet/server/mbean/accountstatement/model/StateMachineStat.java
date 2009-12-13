package dk.flexibet.server.mbean.accountstatement.model;

import java.io.Serializable;
import java.util.Map;

/**
 * Statistics model for state machine.
 * 
 * @author daniel
 * 
 */
public class StateMachineStat implements Serializable {

	private final String stateMachineId;

	/**
	 * Statistics for all states in state machine. Key - stateName , Value -
	 * statistic for state
	 */
	private final Map<String, StatesStat> statesStat;

	public StateMachineStat(String stateMachineId, Map<String, StatesStat> statesStat) {
		this.stateMachineId = stateMachineId;
		this.statesStat = statesStat;
	}

	public String getStateMachineId() {
		return stateMachineId;
	}

	public Map<String, StatesStat> getStatesStat() {
		return statesStat;
	}

	public double getProfitLossGreen() {
		double total = 0;
		for (StatesStat stat : statesStat.values()) {
			total = total + stat.getProfitLossGreen();
		}
		return total;

	}

	public double getProfitLossRed() {
		double total = 0;
		for (StatesStat stat : statesStat.values()) {
			total = total + stat.getProfitLossRed();
		}
		return total;
	}

	public int getGreenRunners() {
		int total = 0;
		for (StatesStat stat : statesStat.values()) {
			total = total + stat.getGreenRunners();
		}
		return total;
	}

	public int getWhiteRunners() {
		int total = 0;
		for (StatesStat stat : statesStat.values()) {
			total = total + stat.getWhiteRunners();
		}
		return total;
	}

	public int getRedRunners() {
		int total = 0;
		for (StatesStat stat : statesStat.values()) {
			total = total + stat.getRedRunners();
		}
		return total;
	}

	public int getTotalRunners() {
		int total = 0;
		for (StatesStat stat : statesStat.values()) {
			total = total + stat.getTotalRunners();
		}
		return total;

	}

	public double getProfitLossTotal() {
		double total = 0;
		for (StatesStat stat : statesStat.values()) {
			total = total + stat.getProfitLossTotal();
		}
		return total;
	}
}
