package dk.flexibet.server.betexarchive;

import java.util.List;

import dk.flexibet.server.model.HistRunnerPrice;

/**Stores time stamped runner prices in a database.
 * 
 * @author daniel
 *
 */
public interface BetexArchivePublisher {

	/** Put the last prices to the queue to be stored in a db by a background task. 
	 * 
	 * @param histRunnerPrices list of time stamped runner prices
	 */
	public void publish(List<HistRunnerPrice> histRunnerPrices);
	
	/** Put the last prices to the queue to be stored in a db by a background task.  
	 * 
	 * @param histRunnerPrice time stamped runner price
	 */
	public void publish(HistRunnerPrice histRunnerPrice);
	
}
