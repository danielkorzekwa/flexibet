package dk.flexibet.server.mbean.accountstatement.model;

import java.io.Serializable;

/** Statistic about runner states vs profit&loss
 * 
 * @author daniel
 *
 */
public class StatesStat implements Serializable,Comparable<StatesStat>{

	private final String stateMachineId;
	private final String stateName;
	
	private double profitLossGreen;
	private double profitLossRed;
	
	private int greenRunners;
	private int whiteRunners;
	private int redRunners;

	public StatesStat(String stateMachineId,String stateName) {
		this.stateMachineId = stateMachineId;	
		this.stateName = stateName;
	}
	
	public String getStateMachineId() {
		return stateMachineId;
	}
	
	public String getStateName() {
		return stateName;
	}
	
	public double getProfitLossGreen() {
		return profitLossGreen;
	}
	public void setProfitLossGreen(double profitLossGreen) {
		this.profitLossGreen = profitLossGreen;
	}
	public double getProfitLossRed() {
		return profitLossRed;
	}
	public void setProfitLossRed(double profitLossRed) {
		this.profitLossRed = profitLossRed;
	}
	public int getGreenRunners() {
		return greenRunners;
	}
	public void setGreenRunners(int greenRunners) {
		this.greenRunners = greenRunners;
	}
	public int getWhiteRunners() {
		return whiteRunners;
	}
	public void setWhiteRunners(int whiteRunners) {
		this.whiteRunners = whiteRunners;
	}
	public int getRedRunners() {
		return redRunners;
	}
	public void setRedRunners(int redRunners) {
		this.redRunners = redRunners;
	}
	
	public int getTotalRunners() {
		return this.greenRunners + this.whiteRunners + this.redRunners;
	}
	
	public double getProfitLossTotal() {
		return this.profitLossGreen + this.profitLossRed;
	}
	
	public int compareTo(StatesStat o) {
		return this.getStateName().compareTo(o.getStateName());
	}
}
