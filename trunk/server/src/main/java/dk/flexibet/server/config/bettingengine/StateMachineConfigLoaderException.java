package dk.flexibet.server.config.bettingengine;

/** Thrown when loading state machines configuration fails.
 * 
 * @author daniel
 *
 */
public class StateMachineConfigLoaderException extends RuntimeException{

	public StateMachineConfigLoaderException(Throwable t) {
		super(t);
	}
}
