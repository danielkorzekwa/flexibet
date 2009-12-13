package dk.flexibet.server.mbean.accountstatement;

import java.util.Date;
import java.util.List;

import dk.flexibet.server.mbean.accountstatement.model.AccountStatementStat;
import dk.flexibet.server.model.DetailedAccountStatementItem;

/**JMX interface for account statement.
 * 
 * @author daniel
 *
 */
public interface AccountStatementMBean {

	/**
	 * Gets account statement items from a given date.
	 * 
	 * @param from
	 *            Date that account statement items are returned from.
	 * @param stateMachineId
	 *            If null then all items are returned, if not null then items for specific state machine are returned.
	 * @param stateName If null then items for all state names are returned.
	 * @param betItems If true then items related to bets are returned (betId>0). If false then other items are returned (betId=0).
	 * @return
	 */
	public List<DetailedAccountStatementItem> getAccountStatement(Date from, String stateMachineId, String stateName,boolean betItems);

	/**
	 * Returns statistics for account statement. [0] - this week, [1] - last week
	 * 
	 */
	public AccountStatementStat[] getAccountStatementStat();

}
