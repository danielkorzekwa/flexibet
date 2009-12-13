package dk.flexibet.server.model;

import java.util.Date;

/** Represents time stamped market runner price and other fast changing data such as: totalMatched, lastMatchedPrice 
 * 
 * @author daniel
 *
 */
public class HistRunnerPrice {

	private int marketId;
	private int selectionId;
	private Date recordTime;
	private int inPlayDelay;

	private double lastPriceMatched;
	
	private double priceToBack;
	private double priceToLay;
	public int getMarketId() {
		return marketId;
	}
	public void setMarketId(int marketId) {
		this.marketId = marketId;
	}
	public int getSelectionId() {
		return selectionId;
	}
	public void setSelectionId(int selectionId) {
		this.selectionId = selectionId;
	}
	public Date getRecordTime() {
		return recordTime;
	}
	public void setRecordTime(Date recordTime) {
		this.recordTime = recordTime;
	}
	public int getInPlayDelay() {
		return inPlayDelay;
	}
	public void setInPlayDelay(int inPlayDelay) {
		this.inPlayDelay = inPlayDelay;
	}
	public double getLastPriceMatched() {
		return lastPriceMatched;
	}
	public void setLastPriceMatched(double lastPriceMatched) {
		this.lastPriceMatched = lastPriceMatched;
	}
	public double getPriceToBack() {
		return priceToBack;
	}
	public void setPriceToBack(double priceToBack) {
		this.priceToBack = priceToBack;
	}
	public double getPriceToLay() {
		return priceToLay;
	}
	public void setPriceToLay(double priceToLay) {
		this.priceToLay = priceToLay;
	}		
}
