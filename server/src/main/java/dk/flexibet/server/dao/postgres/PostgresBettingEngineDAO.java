package dk.flexibet.server.dao.postgres;

import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

import dk.bot.bettingengine.dao.BettingEngineDAO;
import dk.bot.bettingengine.dao.model.RunnerBet;
import dk.bot.bettingengine.dao.model.RunnerState;
import dk.flexibet.server.dao.RunnerStateDAO;
import dk.flexibet.server.dao.RunnerStateLastBetDAO;

public class PostgresBettingEngineDAO extends SimpleJdbcDaoSupport implements BettingEngineDAO{

	private final RunnerStateDAO runnetStateDao;
	private final RunnerStateLastBetDAO runnerStateLastBetDao;

	public PostgresBettingEngineDAO(RunnerStateDAO runnetStateDao, RunnerStateLastBetDAO runnerStateLastBetDao) {
		this.runnetStateDao = runnetStateDao;
		this.runnerStateLastBetDao = runnerStateLastBetDao;
	}
	
	public RunnerState findRunnerState(String stateMachineId, int marketId, int selectionId, String defaultStateName) {
		RunnerState runnerState = runnetStateDao.findRunnerState(stateMachineId,marketId,selectionId);
		if(runnerState==null) {
			runnetStateDao.saveRunnerState(stateMachineId,marketId,selectionId,defaultStateName);
			runnerState = runnetStateDao.findRunnerState(stateMachineId,marketId,selectionId);
			if(runnerState==null) {
				throw new RuntimeException("Can't create/get runnerState from a database.");
			}
		}
		return runnerState;
	}

	public RunnerBet findBet(long betId) {
		return runnerStateLastBetDao.findBet(betId);
	}

	public RunnerBet findLastBet(int runnerStateId, String betType) {
		return runnerStateLastBetDao.findLastBet(runnerStateId, betType);
	}
}
