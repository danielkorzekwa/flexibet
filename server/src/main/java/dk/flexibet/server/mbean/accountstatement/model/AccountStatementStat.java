package dk.flexibet.server.mbean.accountstatement.model;

import java.io.Serializable;
import java.util.List;

/** Statistics for account statement
 * 
 * @author daniel
 *
 */
public class AccountStatementStat implements Serializable{

	/**Profit per states. Key - state name*/
	private List<StateMachineStat> profitPerStat;
	
	/**Profit per green/white/red market*/
	private MarketStat marketStat;

	public List<StateMachineStat> getProfitPerStat() {
		return profitPerStat;
	}

	public void setProfitPerStat(List<StateMachineStat> profitPerStat) {
		this.profitPerStat = profitPerStat;
	}

	public MarketStat getMarketStat() {
		return marketStat;
	}

	public void setMarketStat(MarketStat marketStat) {
		this.marketStat = marketStat;
	}
		
}
