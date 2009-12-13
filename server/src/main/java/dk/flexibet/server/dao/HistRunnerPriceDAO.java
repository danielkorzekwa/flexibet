package dk.flexibet.server.dao;

import java.util.Date;
import java.util.List;

import dk.flexibet.server.model.HistRunnerPrice;

/**DAO object for runner prices and other fast changing data such as: totalMatched, lastMatchedPrice
 * 
 * @author daniel
 *
 */
public interface HistRunnerPriceDAO {

	public void saveRunnerPrices(List<HistRunnerPrice> runnerPrices);
	
	/**
	 * 
	 * @param marketId
	 * @return list of runner price records sorted by time (from oldest to the earliest)
	 */
	public List<HistRunnerPrice> findRunnerPrices(int marketId);
	
	/**
	 * 
	 * @param marketId
	 * @return list of runner price records sorted by time (from oldest to the earliest)
	 */
	public List<HistRunnerPrice> findRunnerPrices(int marketId,int selectionId);
	
	/**
	 * 
	 * @param marketId
	 * @param from
	 * @param to
	 * @return list of runner price records sorted by time (from oldest to the earliest)
	 */
	public List<HistRunnerPrice> findRunnerPrices(int marketId, Date from, Date to);
}
