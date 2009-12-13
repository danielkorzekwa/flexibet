package dk.flexibet.server.dao.postgres;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import dk.bot.bettingengine.dao.model.RunnerState;
import dk.flexibet.server.dao.RunnerStateDAO;

public class PostgresRunnerStateDAO extends SimpleJdbcDaoSupport implements RunnerStateDAO {
	
	public RunnerState findRunnerState(long betId) {
		String SQL_FIND_RUNNER_STATE = "select * from runner_state_bet bet, runner_state rs where bet.bet_id=? and bet.runner_state_id=rs.id;";

		List<RunnerState> runnerStateList = getSimpleJdbcTemplate().query(SQL_FIND_RUNNER_STATE,
				new RunnerStateMapper(), betId);

		if (runnerStateList.size() == 0) {
			return null;
		} else {
			return runnerStateList.get(0);
		}
	}
	
	public RunnerState findRunnerState(String stateMachineId, int marketId, int selectionId) {
		String SQL_FIND_RUNNER_STATE = "select * from runner_state where state_machine_id=? and market_id=? and selection_id=?";

		List<RunnerState> runnerStateList = getSimpleJdbcTemplate().query(SQL_FIND_RUNNER_STATE,
				new RunnerStateMapper(), stateMachineId, marketId, selectionId);

		if (runnerStateList.size() == 0) {
			return null;
		} else {
			return runnerStateList.get(0);
		}
	}
	
	@Transactional
	public void saveRunnerState(String stateMachineId, int marketId, int selectionId, String stateName) {
		String SQL_UPDATE_RUNNER_STATE = "update runner_state set state_name = ? where state_machine_id=? and market_id=? and selection_id=?";
		String SQL_INSERT_RUNNER_STATE = "insert into runner_state(state_machine_id,market_id,selection_id,state_name) values(?,?,?,?)";

		RunnerState findRunnerState = findRunnerState(stateMachineId, marketId, selectionId);

		if (findRunnerState != null) {
			getSimpleJdbcTemplate().update(SQL_UPDATE_RUNNER_STATE, stateName, stateMachineId, marketId, selectionId);
		} else {
			getSimpleJdbcTemplate().update(SQL_INSERT_RUNNER_STATE, stateMachineId, marketId, selectionId, stateName);
		}
	}

	private class RunnerStateMapper implements ParameterizedRowMapper<RunnerState> {

		public RunnerState mapRow(ResultSet rs, int rowNum) throws SQLException {
			RunnerState runnerState = new RunnerState();
			runnerState.setId(rs.getInt("id"));
			runnerState.setStateMachineId(rs.getString("state_machine_id"));
			runnerState.setMarketId(rs.getInt("market_id"));
			runnerState.setSelectionId(rs.getInt("selection_id"));
			runnerState.setStateName(rs.getString("state_name"));
			return runnerState;
		}

	}
}
