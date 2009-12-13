package dk.flexibet.server.model;

import java.io.Serializable;

/**Bets statistics model
 * 
 * @author daniel
 *
 */
public class BetsStat implements Serializable{

	private final String stateMachineId;
	/**Menu path to market*/
	private final int marketId;
	private final String menuPath;
	private final String selectionName;
	
	/**Number of bets placed on a market runner within a state machine.*/
	private final int betsAmount;
	private final int runnerStateId;

	public BetsStat(String stateMachineId, int runnerStateId,int marketId,String menuPath, String selectionName, int betsAmount) {
		this.stateMachineId = stateMachineId;
		this.runnerStateId = runnerStateId;
		this.marketId=marketId;
		this.menuPath = menuPath;
		this.selectionName = selectionName;
		this.betsAmount = betsAmount;
	}

	public String getStateMachineId() {
		return stateMachineId;
	}
	
	public int getMarketId() {
		return marketId;
	}

	public String getMenuPath() {
		return menuPath;
	}

	public String getSelectionName() {
		return selectionName;
	}

	public int getBetsAmount() {
		return betsAmount;
	}

	public int getRunnerStateId() {
		return runnerStateId;
	}
	
	
}
