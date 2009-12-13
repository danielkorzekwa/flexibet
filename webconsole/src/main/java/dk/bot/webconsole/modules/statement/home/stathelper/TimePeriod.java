package dk.bot.webconsole.modules.statement.home.stathelper;

import java.io.Serializable;
import java.util.Date;

/** Defines time periods for which the account statement is displayed
 * 
 * @author daniel
 *
 */
public class TimePeriod implements Serializable{

	private final String periodName;	
	private final Date dateFrom;

	public TimePeriod(String periodName,Date dateFrom) {
		this.periodName = periodName;
		this.dateFrom = dateFrom;
	}
	
	public Date getDateFrom() {
		return dateFrom;
	}
	public String getPeriodName() {
		return periodName;
	}
	
	@Override
	public String toString() {
		return periodName;
	}
}
