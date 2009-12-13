package dk.flexibet.server.dao;

import dk.bot.bettingengine.dao.model.RunnerState;


/** Manage the runner state in the persistence storage
 * 
 * @author daniel
 *
 */
public interface RunnerStateDAO {
	
	/**Find the runner state that the bet was placed for.
	 * 
	 * @param betId
	 * @return 
	 */
	public RunnerState findRunnerState(long betId);
	
	/** Save the current state of runner 
	 * 
	 * @param stateMachineId
	 * @param marketId
	 * @param selectionId
	 * @param stateName
	 */
	public void saveRunnerState(String stateMachineId, int marketId, int selectionId,String stateName);

	/**
	 * 
	 * @param stateMachineId
	 * @param marketId
	 * @param selectionId
	 * @return null if not exist
	 */
	public RunnerState findRunnerState(String stateMachineId, int marketId,int selectionId);
	
	
}
