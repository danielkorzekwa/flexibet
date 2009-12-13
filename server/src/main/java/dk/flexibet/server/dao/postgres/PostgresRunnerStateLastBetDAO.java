package dk.flexibet.server.dao.postgres;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

import dk.bot.bettingengine.dao.model.RunnerBet;
import dk.bot.marketobserver.model.MarketRunner;
import dk.flexibet.server.dao.RunnerStateLastBetDAO;

public class PostgresRunnerStateLastBetDAO extends SimpleJdbcDaoSupport implements RunnerStateLastBetDAO{

	public RunnerBet findLastBet(int runnerStateId, String betType) {
		String SQL_FIND_LAST_BET = "select * from runner_state_last_bet where runner_state_id=? and bet_type=?";

		List<RunnerBet> bets = getSimpleJdbcTemplate().query(SQL_FIND_LAST_BET, new RunnerBetMapper(), runnerStateId,
				betType);
		if (bets.size() == 0) {
			return null;
		} else {
			return bets.get(0);
		}
	}
	
	public RunnerBet findBet(long betId) {
		String SQL_FIND_LAST_BET = "select * from runner_state_last_bet where bet_id=?";

		List<RunnerBet> bets = getSimpleJdbcTemplate().query(SQL_FIND_LAST_BET, new RunnerBetMapper(), betId);
		if (bets.size() == 0) {
			return null;
		} else {
			return bets.get(0);
		}
	}

	
	public void addBet(int runnerStateId, long betId, Date placedDate, String betType, double price, double size,
			double avgPriceMatched, double sizeMatched, MarketRunner marketRunner, boolean marketInPLay,
			double price_slope, double price_slope_err) {
		
		String SQL_ADD_BET = "insert into runner_state_last_bet(runner_state_id,bet_id,placed_date,bet_type,price,size,avg_price_matched,size_matched,matched_date,size_cancelled) values(?,?,?,?,?,?,?,?,?,?)";

		String SQL_ADD_BET_RUNNER = "insert into runner_state_last_bet_runner(bet_id,total_amount_matched,last_price_matched,total_to_back,total_to_lay,price_to_back,total_on_price_to_back,price_to_lay,total_on_price_to_lay,near_sp,far_sp,actual_sp,in_play,price_slope,price_slope_err) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		String SQL_DELETE_BET = "delete from runner_state_last_bet where runner_state_id=? and bet_type=?";

		RunnerBet lastBet = findLastBet(runnerStateId, betType);
		if (lastBet != null) {
			getSimpleJdbcTemplate().update(SQL_DELETE_BET,runnerStateId, betType);
		}

		getSimpleJdbcTemplate().update(SQL_ADD_BET, runnerStateId, betId, placedDate, betType, price, size,
				avgPriceMatched, sizeMatched, placedDate, 0);

		getSimpleJdbcTemplate().update(SQL_ADD_BET_RUNNER, betId, marketRunner.getTotalAmountMatched(),
				marketRunner.getLastPriceMatched(), marketRunner.getTotalToBack(), marketRunner.getTotalToLay(),
				marketRunner.getPriceToBack(), marketRunner.getTotalOnPriceToBack(), marketRunner.getPriceToLay(),
				marketRunner.getTotalOnPriceToLay(),marketRunner.getNearSP(),marketRunner.getFarSP(),marketRunner.getActualSP(),marketInPLay,price_slope,price_slope_err);
		
	}
	
	private class RunnerBetMapper implements ParameterizedRowMapper<RunnerBet> {
		public RunnerBet mapRow(ResultSet rs, int rowNum) throws SQLException {

			RunnerBet runnerBet = new RunnerBet();

			runnerBet.setRunnerStateId(rs.getInt("runner_state_id"));
			runnerBet.setBetId(rs.getLong("bet_id"));

			runnerBet.setPlacedDate(rs.getTimestamp("placed_date"));
			runnerBet.setBetType(rs.getString("bet_type"));
			runnerBet.setPrice(rs.getDouble("price"));
			runnerBet.setSize(rs.getDouble("size"));

			runnerBet.setAvgPriceMatched(rs.getDouble("avg_price_matched"));
			runnerBet.setSizeMatched(rs.getDouble("size_matched"));
			runnerBet.setMatchedDate(rs.getTimestamp("matched_date"));

			runnerBet.setSizeCancelled(rs.getDouble("size_cancelled"));

			return runnerBet;
		}
	}
	
	public void cancelBet(long betId, double sizeCancelled, double sizeMatched) {
		String SQL_UPDATE_BET = "update runner_state_last_bet set size_cancelled=?,size_matched=? where bet_id=?";

		getSimpleJdbcTemplate().update(SQL_UPDATE_BET, sizeCancelled, sizeMatched, betId);
	
	}
	
	public void updateBet(long betId, double avgPriceMatched, double sizeMatched, Date matchedDate) {
		String SQL_UPDATE_BET = "update runner_state_last_bet set avg_price_matched=?,size_matched=?,matched_date=? where bet_id=?";

		getSimpleJdbcTemplate().update(SQL_UPDATE_BET, avgPriceMatched, sizeMatched, matchedDate, betId);

	}

}
