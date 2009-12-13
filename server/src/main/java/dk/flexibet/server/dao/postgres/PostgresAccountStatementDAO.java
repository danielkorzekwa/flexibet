package dk.flexibet.server.dao.postgres;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import dk.flexibet.server.dao.AccountStatementDAO;
import dk.flexibet.server.model.DetailedAccountStatementItem;

public class PostgresAccountStatementDAO extends SimpleJdbcDaoSupport implements AccountStatementDAO {

	@Transactional
	public void add(List<DetailedAccountStatementItem> items) {

		String SQL_ADD_ITEM = "insert into account_statement(market_id,market_name,market_type,full_market_name,selection_id,selection_name,bet_id,event_type_id,bet_category_type,bet_type,bet_size,avg_price,placed_date,start_date,settled_date,amount, commission,state_machine_id,state_name) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		for (DetailedAccountStatementItem item : items) {
			getSimpleJdbcTemplate().update(SQL_ADD_ITEM, item.getMarketId(), item.getMarketName(),
					item.getMarketType(), item.getFullMarketName(), item.getSelectionId(), item.getSelectionName(),
					item.getBetId(), item.getEventTypeId(), item.getBetCategoryType(), item.getBetType(),
					item.getBetSize(), item.getAvgPrice(), item.getPlacedDate(), item.getStartDate(),
					item.getSettledDate(), item.getAmount(), item.isCommission(),item.getStateMachineId(),item.getStateName());
		}

	}

	public Date getLastestSettledDate() {
		String SQL_GET = "select max(settled_date) as latest_date from account_statement";

		ParameterizedRowMapper<Date> mapper = new ParameterizedRowMapper<Date>() {
			public Date mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getTimestamp("latest_date");
			}
		};

		List<Date> list = getSimpleJdbcTemplate().query(SQL_GET, mapper);

		Date latestSettledDate = null;
		if (list.size() > 0) {
			latestSettledDate = list.get(0);
		}
		if (latestSettledDate == null) {
			/**
			 * return default value (not account statement items in db (now - 1
			 * month)
			 */
			latestSettledDate = new Date(System.currentTimeMillis() - (long) 1000 * 3600 * 24 * 30);
		}

		return latestSettledDate;
	}

	public void deleteItems(Date from) {
		String SQL = "delete from account_statement where settled_date>=?";

		getSimpleJdbcTemplate().update(SQL, from);
	}

	public List<DetailedAccountStatementItem> getItems(Date from, Date to,boolean betItems) {
		String SQL_GET = "select * from account_statement where settled_date>=? and settled_date<=?";

		if(betItems) {
			SQL_GET = SQL_GET + " and bet_id>0";
		}
		else {
			SQL_GET = SQL_GET + " and bet_id=0";
		}
		
		List<DetailedAccountStatementItem> items = getSimpleJdbcTemplate().query(SQL_GET, new AccountStatementMapper(), from,
				to);

		return items;
	}

	private class AccountStatementMapper implements ParameterizedRowMapper<DetailedAccountStatementItem> {

		public DetailedAccountStatementItem mapRow(ResultSet rs, int rowNum) throws SQLException {
			DetailedAccountStatementItem item = new DetailedAccountStatementItem();
			
			item.setId(rs.getInt("id"));
			item.setMarketId(rs.getInt("market_id"));
			item.setMarketName(rs.getString("market_name"));
			item.setMarketType(rs.getString("market_type"));
			item.setFullMarketName(rs.getString("full_market_name"));

			item.setSelectionId(rs.getInt("selection_id"));
			item.setSelectionName(rs.getString("selection_name"));

			item.setBetId(rs.getLong("bet_id"));

			item.setEventTypeId(rs.getInt("event_type_id"));
			item.setBetCategoryType(rs.getString("bet_category_type"));
			item.setBetType(rs.getString("bet_type"));
			item.setBetSize(rs.getDouble("bet_size"));
			item.setAvgPrice(rs.getDouble("avg_price"));
			item.setPlacedDate(rs.getTimestamp("placed_date"));
			item.setStartDate(rs.getTimestamp("start_date"));
			item.setSettledDate(rs.getTimestamp("settled_date"));
			item.setAmount(rs.getDouble("amount"));
			item.setCommission(rs.getBoolean("commission"));
			
			item.setStateMachineId(rs.getString("state_machine_id"));
			item.setStateName(rs.getString("state_name"));

			return item;
		}
	}
}
