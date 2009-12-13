package dk.flexibet.server.betexarchive;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;

import dk.flexibet.server.dao.HistRunnerPriceDAO;
import dk.flexibet.server.model.HistRunnerPrice;

/**
 * Stores time stamped runner prices in a database.
 * 
 * @author daniel
 * 
 */
@ManagedResource(objectName = "dk.flexibet.bettingapp:name=BetexArchivePublisher")
public class BetexArchivePublisherImpl implements BetexArchivePublisher, BetexArchivePublisherMBean, Runnable {

	private final Log log = LogFactory.getLog(BetexArchivePublisherImpl.class.getSimpleName());

	private BlockingQueue<HistRunnerPrice> runnerPricesQueue = new LinkedBlockingQueue<HistRunnerPrice>();
	private Thread publishPricesThread;

	private final HistRunnerPriceDAO histRunnerPriceDao;

	/**
	 * Url of a betex archive application starts with http://..
	 * 
	 * @param betexArchiveUrl
	 */
	public BetexArchivePublisherImpl(HistRunnerPriceDAO histRunnerPriceDao) {
		this.histRunnerPriceDao = histRunnerPriceDao;
		publishPricesThread = new Thread(this);
		publishPricesThread.start();
	}

	/**
	 * Put the last prices to the queue to be stored in a db by a background task.
	 * 
	 * @param histRunnerPrices
	 *            list of time stamped runner prices
	 */
	public void publish(List<HistRunnerPrice> histRunnerPrices) {
		runnerPricesQueue.addAll(histRunnerPrices);
	}

	/**
	 * Put the last prices to the queue to be stored in a db by a background task.
	 * 
	 * @param histRunnerPrice
	 *            time stamped runner price
	 */
	public void publish(HistRunnerPrice histRunnerPrice) {
		runnerPricesQueue.add(histRunnerPrice);
	}

	@ManagedAttribute
	public int getPriceQueueSize() {
		return runnerPricesQueue.size();
	}

	/**
	 * Store runner prices from the queue to the database.
	 * 
	 */
	public void run() {
		while (true) {
			try {
				int pricesQueueSize = runnerPricesQueue.size();
				if (pricesQueueSize > 0) {
					List<HistRunnerPrice> runnerPrices = new ArrayList<HistRunnerPrice>(pricesQueueSize);
					runnerPricesQueue.drainTo(runnerPrices, pricesQueueSize);
					histRunnerPriceDao.saveRunnerPrices(runnerPrices);
				}
				Thread.sleep(1000);
			} catch (Exception e) {
				log.error("Saving runner prices error.", e);
			}
		}
	}
}
