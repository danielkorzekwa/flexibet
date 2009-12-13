package dk.flexibet.server.dao.postgres;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import dk.bot.bettingengine.statemachine.customaction.PlaceBet;
import dk.bot.marketobserver.model.MarketData;
import dk.bot.marketobserver.model.MarketDetailsRunner;
import dk.flexibet.server.dao.MarketDAO;

public class PostgresMarketDAO extends SimpleJdbcDaoSupport implements MarketDAO {

	private final Log log = LogFactory.getLog(PostgresMarketDAO.class.getSimpleName());
	
	public MarketData findMarket(int marketId) {
		String SQL_FIND_MARKET = "select * from market where market_id=?";
		String SQL_FIND_RUNNER = "select * from runner where market_id=?";

		ParameterizedRowMapper<MarketData> marketDataMapper = getMarketDataMapper();
		List<MarketData> MarketDataList = getSimpleJdbcTemplate().query(SQL_FIND_MARKET, marketDataMapper,
				marketId);

		if (MarketDataList.size() == 0) {
			return null;
		} else if (MarketDataList.size() == 1) {

			MarketData marketData = MarketDataList.get(0);
			ParameterizedRowMapper<MarketDetailsRunner> runnerMapper = getRunnerMapper();
			List<MarketDetailsRunner> runners = getSimpleJdbcTemplate().query(SQL_FIND_RUNNER,
					runnerMapper, marketId);
			marketData.setRunners(runners);

			return marketData;
		} else {
			throw new RuntimeException("More than one MarketData exists for market_id: " + marketId);
		}

	}

	public boolean marketExists(int marketId) {
		String SQL = "select market_id from market where market_id=?";

		try {
			getSimpleJdbcTemplate().queryForObject(SQL, Integer.class, marketId);
			return true;
		} catch (EmptyResultDataAccessException e) {
			return false;
		}

	}

	@Transactional
	public void saveMarket(MarketData marketData) {
		String SQL_SAVE_MARKET = "insert into market(market_id,market_name,market_type,market_status,market_time,suspend_time,sport_id,menu_path,event_hierarchy,country_code,number_of_runners,number_of_winners,bsb_market,turning_in_play) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		String SQL_SAVE_RUNNER = "insert into runner(market_id,selection_id,selection_name) values(?,?,?)";

		
		int sportId=-1;
		if(marketData.getEventHierarchy().length()>0) {
			String[] split = marketData.getEventHierarchy().split("/");
			try {
			sportId = Integer.parseInt(split[1]);
			}
			catch(NumberFormatException e) {
				log.warn("Can't parse sportId:" + marketData.getEventHierarchy());
			}
		}
		
		getSimpleJdbcTemplate().update(SQL_SAVE_MARKET, marketData.getMarketId(), marketData.getMarketName(),
				marketData.getMarketType(), marketData.getMarketStatus(), marketData.getEventDate(),
				marketData.getSuspendTime(), sportId, marketData.getMenuPath(), marketData.getEventHierarchy(),
				marketData.getCountryCode(), marketData.getNumberOfRunners(), marketData.getNumberOfWinners(),
				marketData.isBsbMarket(), marketData.isTurningInPlay());

		for (MarketDetailsRunner runner : marketData.getRunners()) {
			getSimpleJdbcTemplate().update(SQL_SAVE_RUNNER, marketData.getMarketId(), runner.getSelectionId(),
					runner.getSelectionName());
		}
	}
	
	public List<MarketData> findMarkets(String sql) {
		
		String SQL_FIND_RUNNER = "select * from runner where market_id=?";
		
		List<MarketData> markets = getSimpleJdbcTemplate().query(sql, getMarketDataMapper());
		
		/**Set market runners*/
		for(MarketData marketData: markets) {
			ParameterizedRowMapper<MarketDetailsRunner> runnerMapper = getRunnerMapper();
			List<MarketDetailsRunner> runners = getSimpleJdbcTemplate().query(SQL_FIND_RUNNER,
					runnerMapper, marketData.getMarketId());
			marketData.setRunners(runners);
		}
		
		return markets;
	}

	private ParameterizedRowMapper<MarketData> getMarketDataMapper() {
		ParameterizedRowMapper<MarketData> MarketDataMapper = new ParameterizedRowMapper<MarketData>() {

			public MarketData mapRow(ResultSet rs, int rowNum) throws SQLException {

				MarketData marketData = new MarketData();
				marketData.setMarketId(rs.getInt("market_id"));
				marketData.setMarketName(rs.getString("market_name"));
				marketData.setMarketType(rs.getString("market_type"));
				marketData.setMarketStatus(rs.getString("market_status"));
				marketData.setEventDate(rs.getTimestamp("market_time"));
				marketData.setSuspendTime(rs.getTimestamp("suspend_time"));
				marketData.setMenuPath(rs.getString("menu_path"));
				marketData.setEventHierarchy(rs.getString("event_hierarchy"));
				marketData.setCountryCode(rs.getString("country_code"));
				marketData.setNumberOfRunners(rs.getInt("number_of_runners"));
				marketData.setNumberOfWinners(rs.getInt("number_of_winners"));
				marketData.setBsbMarket(rs.getBoolean("bsb_market"));
				marketData.setTurningInPlay(rs.getBoolean("turning_in_play"));

				return marketData;
			}

		};
		return MarketDataMapper;
	}

	private ParameterizedRowMapper<MarketDetailsRunner> getRunnerMapper() {
		ParameterizedRowMapper<MarketDetailsRunner> runnerMapper = new ParameterizedRowMapper<MarketDetailsRunner>() {

			public MarketDetailsRunner mapRow(ResultSet rs, int rowNum) throws SQLException {

				int selectionId = rs.getInt("selection_id");
				String selectionName = rs.getString("selection_name");

				MarketDetailsRunner MarketDataRunner = new MarketDetailsRunner(selectionId, selectionName);

				return MarketDataRunner;
			}

		};

		return runnerMapper;
	}

}
