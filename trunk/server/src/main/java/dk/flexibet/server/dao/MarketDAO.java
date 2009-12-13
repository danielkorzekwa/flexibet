package dk.flexibet.server.dao;

import java.util.List;

import dk.bot.marketobserver.model.MarketData;

/**
 * DAO object for market with runners
 * 
 * @author daniel
 * 
 */
public interface MarketDAO {

	/**
	 * Save market to database
	 * 
	 * @param marketData
	 */
	public void saveMarket(MarketData marketData);

	/**
	 * Load market from database
	 * 
	 * @param marketId
	 * @return
	 */
	public MarketData findMarket(int marketId);

	/**
	 * Check if market exists in a database
	 * 
	 * @param marketId
	 * @return
	 */
	public boolean marketExists(int marketId);

	/**
	 * Returns a list of markets for a sql query, e.g. select * from market where market_time>='2009-08-01' and
	 * market_time<='2009-08-30'
	 */
	public List<MarketData> findMarkets(String sql);
}
