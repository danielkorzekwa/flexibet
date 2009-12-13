package dk.flexibet.server.tasks.statemachine;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dk.bot.bettingengine.BettingEngine;
import dk.bot.marketobserver.util.AbstractTask;

/** Clean up task:
 *  - Remove state machines for markets with marketTime older than 4 hours.*/
public class CleanUpTaskImpl extends AbstractTask{

	private final Log log = LogFactory.getLog(CleanUpTaskImpl.class.getSimpleName());
	private final BettingEngine bettingEngine;

	public CleanUpTaskImpl(String taskName,BettingEngine bettingEngine) {
		super(taskName);
		this.bettingEngine = bettingEngine;
	}
	
	@Override
	public void doExecute() {
		/** Remove machines for marketTime older than 4 hours ago.*/
		int removedAmount = bettingEngine.removeMachines(new Date(System.currentTimeMillis()-(1000*3600*4)));
		log.info("Removed old machines. Amount of removed: " + removedAmount );
		
		int removedMarketFromRegressionCache = bettingEngine.removeMarketsFromRegressionCache(4);
		log.info("Removed markets from regression cache: " + removedMarketFromRegressionCache);
	}
	
}
