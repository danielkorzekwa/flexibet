package dk.flexibet.server.betexarchive;

/** JMX interface for a BetexArchivePublisher.*/
public interface BetexArchivePublisherMBean {

	/**Returns number of price elements in a queue.
	 * 
	 * @return
	 */
	public int getPriceQueueSize();
}
