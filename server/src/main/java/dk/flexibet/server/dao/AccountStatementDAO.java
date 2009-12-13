package dk.flexibet.server.dao;

import java.util.Date;
import java.util.List;

import dk.flexibet.server.model.DetailedAccountStatementItem;

/**
 * DAO object for account statement
 * 
 * @author daniel
 * 
 */
public interface AccountStatementDAO {

	/**
	 * 
	 * @param items
	 */
	public void add(List<DetailedAccountStatementItem> items);
	
	/** Items started on 'from' date and ended on 'to' date
	 * 
	 * @param from
	 * @param to
	 * @param betItems If true then items related to bets are returned (betId>0). If false then other items are returned (betId=0).
	 * @return
	 */
	public List<DetailedAccountStatementItem> getItems(Date from, Date to,boolean betItems);
	
	/** Deletes items started on 'from' date.*/
	public void deleteItems(Date from);
	
	/** The latest date from all items in db
	 * 
	 * @return
	 */
	public Date getLastestSettledDate();
}
