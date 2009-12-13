package dk.flexibet.server.simulator;

/**
 * Simulates betting based on a historical data.
 * 
 * @author daniel
 * 
 */
public interface BettingSimulator {

	/**
	 * Runs the simulation and returns betting report.
	 * 
	 * @param marketDataSqlQuery
	 *            sql query to get markets from database to run simulation for, e.g. select * from market where
	 *            market_time>='2009-08-01' and market_time<='2009-08-30'
	 *            
	 * @param listener
	 * @return
	 */
	public BettingSimulationReport simulate(String marketDataSqlQuery,BettingSimulationListener listener);

}
