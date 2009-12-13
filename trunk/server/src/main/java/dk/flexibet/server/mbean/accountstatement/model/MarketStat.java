package dk.flexibet.server.mbean.accountstatement.model;

import java.io.Serializable;

/** Account statement summary for market
 * 
 * @author daniel
 *
 */
public class MarketStat implements Serializable{

	/** Period of time of account statement, e,g. last week*/
	private String timePeriod;
	
	private double profitLossGreen;
	private double profitLossRed;
	private double profitLossTotal;
	
	private int greenMarkets;
	private int whiteMarkets;
	private int redMarkets;
	
	public String getTimePeriod() {
		return timePeriod;
	}
	public void setTimePeriod(String timePeriod) {
		this.timePeriod = timePeriod;
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
	public double getProfitLossTotal() {
		return profitLossTotal;
	}
	public void setProfitLossTotal(double profitLossTotal) {
		this.profitLossTotal = profitLossTotal;
	}
	public int getGreenMarkets() {
		return greenMarkets;
	}
	public void setGreenMarkets(int greenMarkets) {
		this.greenMarkets = greenMarkets;
	}
	public int getWhiteMarkets() {
		return whiteMarkets;
	}
	public void setWhiteMarkets(int whiteMarkets) {
		this.whiteMarkets = whiteMarkets;
	}
	public int getRedMarkets() {
		return redMarkets;
	}
	public void setRedMarkets(int redMarkets) {
		this.redMarkets = redMarkets;
	}
	
	public int getTotalMarkets() {
		return this.greenMarkets + this.whiteMarkets + this.redMarkets;
	}
}
