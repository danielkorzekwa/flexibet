package dk.flexibet.server.dao.postgres;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

import dk.flexibet.server.dao.BetsDAO;
import dk.flexibet.server.model.BetsStat;
import dk.flexibet.server.model.RunnerStateBet;

public class PostgresBetsDAO extends SimpleJdbcDaoSupport implements BetsDAO {

	/**
	 * Get bets statistics for time period >= dateFrom and <dateTo.
	 */
	public List<BetsStat> getBetsStat(Date dateFrom, Date dateTo) {
		String SQL = "select rs.state_machine_id,rs.id as runner_state_id,rs.market_id,md.menu_path, mdr.selection_name,count(*) as total from runner_state_bet b join runner_state rs on(b.runner_state_id=rs.id) join market_details md on(rs.market_id=md.market_id) join market_details_runner mdr on(rs.market_id=mdr.market_id and rs.selection_id=mdr.selection_id) where b.placed_date>=? and b.placed_date<? group by rs.state_machine_id,rs.id, rs.market_id,md.menu_path, mdr.selection_name order by total desc";

		ParameterizedRowMapper<BetsStat> mapper = new ParameterizedRowMapper<BetsStat>() {

			public BetsStat mapRow(ResultSet rs, int rowNum) throws SQLException {
				String stateMachineId = rs.getString("state_machine_id");
				int runnerStateId = rs.getInt("runner_state_id");
				int marketId = rs.getInt("market_id");
				String menuPath = rs.getString("menu_path");
				String selectionName = rs.getString("selection_name");
				int totalBets = rs.getInt("total");

				return new BetsStat(stateMachineId,runnerStateId, marketId,menuPath, selectionName, totalBets);
			}

		};
		;
		List<BetsStat> betsStat = getSimpleJdbcTemplate().query(SQL, mapper, dateFrom, dateTo);
		return betsStat;
	}
	
	public List<RunnerStateBet> getRunnerStateBets(int runnerStateId) {
		String SQL = "select * from runner_state_bet where runner_state_id=? order by placed_date desc";

		ParameterizedRowMapper<RunnerStateBet> mapper = new ParameterizedRowMapper<RunnerStateBet>() {

			public RunnerStateBet mapRow(ResultSet rs, int rowNum) throws SQLException {
				long betId = rs.getLong("bet_id");
				int runnerStateId = rs.getInt("runner_state_id");
				Date placedDate = rs.getTimestamp("placed_date");
				double price = rs.getDouble("price");
				
				RunnerStateBet runnerStateBet = new RunnerStateBet(betId,runnerStateId,placedDate,price);
				return runnerStateBet;
			}

		};
		return getSimpleJdbcTemplate().query(SQL, mapper, runnerStateId);
	}
	
	public void addRunnerStateBet(int runnerStateId, long betId, Date placedDate, double price) {
		String SQL = "insert into runner_state_bet(runner_state_id,bet_id,placed_date,price) values(?,?,?,?)";

		getSimpleJdbcTemplate().update(SQL, runnerStateId, betId,placedDate,price);
	}

}
