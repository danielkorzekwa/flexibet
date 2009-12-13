package dk.flexibet.server.model;

import java.io.Serializable;
import java.util.Date;

/**Data model for runner_state_bet table.
 * 
 * @author daniel
 *
 */
public class RunnerStateBet implements Serializable{

	private final long betId;
	private final int runnerStateId;
	private final Date placedDate;
	private final double price;

	public RunnerStateBet(long betId,int runnerStateId,Date placedDate,double price) {
		this.betId = betId;
		this.runnerStateId = runnerStateId;
		this.placedDate = placedDate;
		this.price = price;
	}

	public long getBetId() {
		return betId;
	}

	public int getRunnerStateId() {
		return runnerStateId;
	}

	public Date getPlacedDate() {
		return placedDate;
	}

	public double getPrice() {
		return price;
	}
	
	
	
	
}
