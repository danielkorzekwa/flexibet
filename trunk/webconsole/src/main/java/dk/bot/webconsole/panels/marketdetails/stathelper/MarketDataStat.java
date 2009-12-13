package dk.bot.webconsole.panels.marketdetails.stathelper;

import java.io.Serializable;

/**Data model for market statistic.
 * 
 * @author daniel
 *
 */
public class MarketDataStat implements Serializable{

	private String marketId="";
	private String betFairDeepLink="";
	private String marketName="";
	private String menuPath="";
	private String inPlayDelay="";
	private String numberOfRunners="";
	private String numberOfWinners="";
	private String marketTime="";
	/**When market runners were updated from BetFair.*/
	private String marketRunnersTimestamp="";
	
	
	public String getMarketId() {
		return marketId;
	}
	public void setMarketId(String marketId) {
		this.marketId = marketId;
	}
	public String getMarketName() {
		return marketName;
	}
	public void setMarketName(String marketName) {
		this.marketName = marketName;
	}
	public String getMenuPath() {
		return menuPath;
	}
	public void setMenuPath(String menuPath) {
		this.menuPath = menuPath;
	}
	public String getInPlayDelay() {
		return inPlayDelay;
	}
	public void setInPlayDelay(String inPlayDelay) {
		this.inPlayDelay = inPlayDelay;
	}
	public String getMarketRunnersTimestamp() {
		return marketRunnersTimestamp;
	}
	public void setMarketRunnersTimestamp(String marketRunnersTimestamp) {
		this.marketRunnersTimestamp = marketRunnersTimestamp;
	}
	public String getMarketTime() {
		return marketTime;
	}
	public void setMarketTime(String marketTime) {
		this.marketTime = marketTime;
	}
	public String getNumberOfRunners() {
		return numberOfRunners;
	}
	public void setNumberOfRunners(String numberOfRunners) {
		this.numberOfRunners = numberOfRunners;
	}
	public String getNumberOfWinners() {
		return numberOfWinners;
	}
	public void setNumberOfWinners(String numberOfWinners) {
		this.numberOfWinners = numberOfWinners;
	}
	public String getBetFairDeepLink() {
		return betFairDeepLink;
	}
	public void setBetFairDeepLink(String betFairDeepLink) {
		this.betFairDeepLink = betFairDeepLink;
	}
}
