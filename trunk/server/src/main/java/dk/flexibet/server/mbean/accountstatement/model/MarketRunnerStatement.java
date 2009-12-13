package dk.flexibet.server.mbean.accountstatement.model;

import java.io.Serializable;

/**What is the profit for market runner.
 * 
 * @author daniel
 *
 */
public class MarketRunnerStatement implements Serializable{

	private final String stateMachineId;
	private final String stateName;
	private final int marketId;
	private final int selectionId;
	
	/** Negative if loss, Positive if profit (no commission is subtracted)*/
	private double amount;

	public MarketRunnerStatement(String stateMachineId,String stateName,int marketId,int selectionId) {
		this.stateMachineId=stateMachineId;
		this.stateName = stateName;
		this.marketId = marketId;
		this.selectionId = selectionId;
	}
	
	public String getStateMachineId() {
		return stateMachineId;
	}

	public String getStateName() {
		return stateName;
	}

	public int getMarketId() {
		return marketId;
	}

	public int getSelectionId() {
		return selectionId;
	}


	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
	
}
