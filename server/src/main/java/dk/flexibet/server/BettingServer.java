package dk.flexibet.server;

import dk.flexibet.server.mbean.BettingServerMBean;

/**
 * The main server side component of a flexibet. Integrates other components such as marketObserver,
 * bettingEngine.
 * 
 * 
 * @author daniel
 * 
 */
public interface BettingServer {

	public BettingServerMBean getMBean();
}
