package dk.bot.webconsole.modules.dashboard.sportstat;

import java.io.Serializable;

/** How many are betfair/bwin/matched markets for each sport.
 * 
 * @author daniel
 *
 */
public class SportStat implements Comparable<SportStat>,Serializable{

	private String sportName;
	
	private int betFairMarkets;
	
	private int bwinMarkets;
	
	private int bwinMMatchedMarkets;
	
	private int oddsCheckerMarkets;
	
	private int oddsCheckerMatchedMarkets;

	public String getSportName() {
		return sportName;
	}

	public void setSportName(String sportName) {
		this.sportName = sportName;
	}

	public int getBetFairMarkets() {
		return betFairMarkets;
	}

	public void setBetFairMarkets(int betFairMarkets) {
		this.betFairMarkets = betFairMarkets;
	}

	public int getBwinMarkets() {
		return bwinMarkets;
	}

	public void setBwinMarkets(int bwinMarkets) {
		this.bwinMarkets = bwinMarkets;
	}

	public int getBwinMMatchedMarkets() {
		return bwinMMatchedMarkets;
	}

	public void setBwinMMatchedMarkets(int bwinMMatchedMarkets) {
		this.bwinMMatchedMarkets = bwinMMatchedMarkets;
	}
	
	public int getOddsCheckerMarkets() {
		return oddsCheckerMarkets;
	}

	public void setOddsCheckerMarkets(int oddsCheckerMarkets) {
		this.oddsCheckerMarkets = oddsCheckerMarkets;
	}
	
	public int getOddsCheckerMatchedMarkets() {
		return oddsCheckerMatchedMarkets;
	}

	public void setOddsCheckerMatchedMarkets(int oddsCheckerMatchedMarkets) {
		this.oddsCheckerMatchedMarkets = oddsCheckerMatchedMarkets;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof SportStat && this.sportName.equals(((SportStat)obj).getSportName());
	}
	
	@Override
	public int hashCode() {
		return this.getSportName().hashCode();
	}
	
	public int compareTo(SportStat o) {
		return this.sportName.compareTo(o.sportName);
	}
}
