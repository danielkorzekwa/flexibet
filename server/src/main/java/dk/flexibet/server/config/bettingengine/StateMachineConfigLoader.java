package dk.flexibet.server.config.bettingengine;

import dk.bot.bettingengine.statemachine.StateMachineServiceConfig;

/**Loads state machines configuration
 * 
 * @author daniel
 *
 */
public interface StateMachineConfigLoader {

	public StateMachineServiceConfig load() throws StateMachineConfigLoaderException;
}
