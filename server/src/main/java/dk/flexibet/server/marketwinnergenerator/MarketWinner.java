package dk.flexibet.server.marketwinnergenerator;

import java.util.Date;

/** Represents a market winner.
 * 
 * @author daniel
 *
 */
public class MarketWinner {

	private final int marketId;
	private final int selectionWinnerId;
	private final Date marketTime;
	
	public MarketWinner(int marketId,int selectionWinnerId,Date marketTime) {
		this.marketId=marketId;
		this.selectionWinnerId=selectionWinnerId;
		this.marketTime=marketTime;
	}
	public int getMarketId() {
		return marketId;
	}
	public int getSelectionWinnerId() {
		return selectionWinnerId;
	}
	public Date getMarketTime() {
		return marketTime;
	}
	
	
}
