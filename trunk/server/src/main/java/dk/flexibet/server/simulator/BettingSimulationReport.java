package dk.flexibet.server.simulator;

/**Report from a betting simulation
 * 
 * @author daniel
 *
 */
public class BettingSimulationReport {

	/**List of all markets returned by markets sql query.*/
	private int numOfMarkets;
	
	private int numOfMatchedBets;
	
	/**List of analyzed markets (with at least one runner prices record).*/
	private int numOfAnalyzedMarkets;
	
	/**Betting report in a text format.*/
	private String report;

	private final double totalLiability;
	
	public BettingSimulationReport(int numOfMarkets,int numOfAnalyzedMarkets,int numOfMatchedBets,double totalLiability,String report) {
		this.numOfMarkets = numOfMarkets;
		this.numOfAnalyzedMarkets = numOfAnalyzedMarkets;
		this.numOfMatchedBets=numOfMatchedBets;
		this.totalLiability = totalLiability;
		this.report = report;
	}

	
	public double getTotalLiability() {
		return totalLiability;
	}


	public int getNumOfMatchedBets() {
		return numOfMatchedBets;
	}


	public void setNumOfMatchedBets(int numOfMatchedBets) {
		this.numOfMatchedBets = numOfMatchedBets;
	}


	public int getNumOfMarkets() {
		return numOfMarkets;
	}

	public void setNumOfMarkets(int numOfMarkets) {
		this.numOfMarkets = numOfMarkets;
	}
	
	public int getNumOfAnalyzedMarkets() {
		return numOfAnalyzedMarkets;
	}

	public void setNumOfAnalyzedMarkets(int numOfAnalyzedMarkets) {
		this.numOfAnalyzedMarkets = numOfAnalyzedMarkets;
	}

	public String getReport() {
		return report;
	}

	public void setReport(String report) {
		this.report = report;
	}
}
