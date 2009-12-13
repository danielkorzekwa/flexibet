package dk.flexibet.server.simulator.bettingengine;

import java.util.HashMap;
import java.util.Map;

import dk.bot.bettingengine.dao.BettingEngineDAO;
import dk.bot.bettingengine.dao.model.RunnerBet;
import dk.bot.bettingengine.dao.model.RunnerState;
import dk.bot.marketobserver.model.BetType;

/** Part of a betting simulation, it's a dependency of a betting engine.
 * 
 * @author korzekwad
 *
 */
public class SimBettingEngineDao implements BettingEngineDAO{

	private int nextRunnerStateId=1;
	
	private Map<RunnerStateKey,RunnerState> runnerStates = new HashMap<RunnerStateKey,RunnerState>();

	private final SimLastBetDao lastBetDao;

	public SimBettingEngineDao(SimLastBetDao lastBetDao) {
		this.lastBetDao = lastBetDao;
	}
	
	public RunnerBet findBet(long betId) {
		return lastBetDao.findBet(betId);
	}

	public RunnerBet findLastBet(int runnerStateId, String betType) {
		return lastBetDao.findLastBet(runnerStateId, BetType.valueOf(betType));
	}

	public RunnerState findRunnerState(String stateMachineId, int marketId, int selectionId, String defaultStateName) {
		
		RunnerStateKey runnerStateKey = new RunnerStateKey(stateMachineId,marketId,selectionId);
		if(runnerStates.get(runnerStateKey)!=null) {
			throw new IllegalStateException("Runner state can be obtained only once.");
		}
		
		RunnerState runnerState = new RunnerState();
		runnerState.setId(nextRunnerStateId);
		runnerState.setMarketId(marketId);
		runnerState.setSelectionId(selectionId);
		runnerState.setStateMachineId(stateMachineId);
		runnerState.setStateName(defaultStateName);
		nextRunnerStateId++;
		
		runnerStates.put(runnerStateKey,runnerState);
		
		return runnerState;
	}
	
	private class RunnerStateKey {
		private String stateMachineId;
		
		private int marketId;
		
		private int selectionId;

		public RunnerStateKey(String stateMachineId,int marketId,int selectionId) {
			this.stateMachineId = stateMachineId;
			this.marketId = marketId;
			this.selectionId = selectionId;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + marketId;
			result = prime * result + selectionId;
			result = prime * result + ((stateMachineId == null) ? 0 : stateMachineId.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final RunnerStateKey other = (RunnerStateKey) obj;
			if (marketId != other.marketId)
				return false;
			if (selectionId != other.selectionId)
				return false;
			if (stateMachineId == null) {
				if (other.stateMachineId != null)
					return false;
			} else if (!stateMachineId.equals(other.stateMachineId))
				return false;
			return true;
		}
		
		
	}

}
