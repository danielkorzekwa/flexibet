package dk.bot.webconsole.panels.marketdetails.stathelper;

import java.io.Serializable;

/**Data model for runner data statistic.
 * 
 * @author daniel
 *
 */
public class RunnerDataStat implements Serializable{

	private String runnerName;
	private double priceToBack;
	private double priceToBackWeighted;
	private double priceToLay;
	
	/**price from www.adrianmassey.com*/
	private double masseyPrice;
	/**price from www.racingpost.com*/
	private double racingPostPrice;
	
	/**Last prices before market is turn in play.*/
	private double lastPriceToBack;
	private double lastPriceToLay;
	
	private double farSP;
	private double nearSP;
	private double actualSP;
	
	private boolean virtWinner;
	
	/** Regression slope in degrees, if <0 then price is decreasing, if >0 then price growing.
	 *  Example:
	 *  For regression y = f(minutes) and probability of price growing 1% (price is decreasing) every minute then slope = -45%
	 * 
	 *  Double.NaN if cannot be estimated (observations <2)
	 */
	private double slope;
	
	/** Probability of slope error (between 0 and 1). 0 - no error probability. Double.NaN if cannot be estimated*/
	private double slopeErr;
	
	public double getMasseyPrice() {
		return masseyPrice;
	}
	public void setMasseyPrice(double masseyPrice) {
		this.masseyPrice = masseyPrice;
	}
	public String getRunnerName() {
		return runnerName;
	}
	public void setRunnerName(String runnerName) {
		this.runnerName = runnerName;
	}
	public double getPriceToBack() {
		return priceToBack;
	}
	public void setPriceToBack(double priceToBack) {
		this.priceToBack = priceToBack;
	}
	public double getPriceToBackWeighted() {
		return priceToBackWeighted;
	}
	public void setPriceToBackWeighted(double priceToBackWeighted) {
		this.priceToBackWeighted = priceToBackWeighted;
	}
	public double getPriceToLay() {
		return priceToLay;
	}
	public void setPriceToLay(double priceToLay) {
		this.priceToLay = priceToLay;
	}
	
	public double getLastPriceToBack() {
		return lastPriceToBack;
	}
	public void setLastPriceToBack(double lastPriceToBack) {
		this.lastPriceToBack = lastPriceToBack;
	}
	public double getLastPriceToLay() {
		return lastPriceToLay;
	}
	public void setLastPriceToLay(double lastPriceToLay) {
		this.lastPriceToLay = lastPriceToLay;
	}
	public double getFarSP() {
		return farSP;
	}
	public void setFarSP(double farSP) {
		this.farSP = farSP;
	}
	public double getNearSP() {
		return nearSP;
	}
	public void setNearSP(double nearSP) {
		this.nearSP = nearSP;
	}
	public double getActualSP() {
		return actualSP;
	}
	public void setActualSP(double actualSP) {
		this.actualSP = actualSP;
	}
	public boolean isVirtWinner() {
		return virtWinner;
	}
	public void setVirtWinner(boolean virtWinner) {
		this.virtWinner = virtWinner;
	}
	public double getSlope() {
		return slope;
	}
	public void setSlope(double slope) {
		this.slope = slope;
	}
	public double getSlopeErr() {
		return slopeErr;
	}
	public void setSlopeErr(double slopeErr) {
		this.slopeErr = slopeErr;
	}
	public double getRacingPostPrice() {
		return racingPostPrice;
	}
	public void setRacingPostPrice(double racingPostPrice) {
		this.racingPostPrice = racingPostPrice;
	}
}
