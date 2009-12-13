package dk.flexibet.server.dao.postgres;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import dk.flexibet.server.dao.HistRunnerPriceDAO;
import dk.flexibet.server.model.HistRunnerPrice;

/**
 * DAO object for runner prices and other fast changing data such as: totalMatched, lastMatchedPrice
 * 
 * @author daniel
 * 
 */
public class PostgresHistRunnerPriceDAO extends SimpleJdbcDaoSupport implements HistRunnerPriceDAO {

	@Transactional
	public void saveRunnerPrices(List<HistRunnerPrice> runnerPrices) {
		String SQL = "insert into hist_runner_price(market_id,selection_id,record_time,in_play_delay,last_price_matched,price_to_back,price_to_lay) values(?,?,?,?,?,?,?)";

		for (HistRunnerPrice price : runnerPrices) {
			getSimpleJdbcTemplate().update(SQL, price.getMarketId(), price.getSelectionId(), price.getRecordTime(),
					price.getInPlayDelay(), price.getLastPriceMatched(), price.getPriceToBack(), price.getPriceToLay());
		}
	}

	public List<HistRunnerPrice> findRunnerPrices(int marketId, Date from, Date to) {
		String SQL = "select * from hist_runner_price where market_id=? and record_time>=? and record_time<=? order by record_time asc";

		List<HistRunnerPrice> histRunnerPrices = getSimpleJdbcTemplate().query(SQL, new RunnerPricesMapper(), marketId,
				from, to);

		return histRunnerPrices;
	}

	public List<HistRunnerPrice> findRunnerPrices(int marketId) {
		String SQL = "select * from hist_runner_price where market_id=? order by record_time asc";

		List<HistRunnerPrice> histRunnerPrices = getSimpleJdbcTemplate().query(SQL, new RunnerPricesMapper(), marketId);

		return histRunnerPrices;
	}
	
	public List<HistRunnerPrice> findRunnerPrices(int marketId, int selectionId) {
		String SQL = "select * from hist_runner_price where market_id=? and selection_id=? order by record_time asc";

		List<HistRunnerPrice> histRunnerPrices = getSimpleJdbcTemplate().query(SQL, new RunnerPricesMapper(), marketId,selectionId);

		return histRunnerPrices;
	}

	private class RunnerPricesMapper implements ParameterizedRowMapper<HistRunnerPrice> {

		public HistRunnerPrice mapRow(ResultSet rs, int rowNum) throws SQLException {
			HistRunnerPrice histRunnerPrice = new HistRunnerPrice();
			histRunnerPrice.setMarketId(rs.getInt("market_id"));
			histRunnerPrice.setSelectionId(rs.getInt("selection_id"));
			histRunnerPrice.setRecordTime(rs.getTimestamp("record_time"));
			histRunnerPrice.setInPlayDelay(rs.getInt("in_play_delay"));
			histRunnerPrice.setLastPriceMatched(rs.getDouble("last_price_matched"));
			histRunnerPrice.setPriceToBack(rs.getDouble("price_to_back"));
			histRunnerPrice.setPriceToLay(rs.getDouble("price_to_lay"));

			return histRunnerPrice;
		}

	}

}
