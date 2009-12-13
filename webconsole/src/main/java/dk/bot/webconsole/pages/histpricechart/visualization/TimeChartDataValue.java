package dk.bot.webconsole.pages.histpricechart.visualization;

import java.util.Date;

/**
 * Represents values for all time series at a given time to be displayed at a timeline chart./
 * 
 * @author korzekwad
 * 
 */
public class TimeChartDataValue {

	/**GMT time of chart values.*/
	private final Date timestamp;

	/**
	 * Values for all series - size of this array must equal to number of series. First element is a value for first
	 * time series. Values can be null.
	 */
	private final Double[] values;

	/**
	 * 
	 * @param timestamp
	 *            GMT time of chart values
	 * @param values
	 */
	public TimeChartDataValue(Date timestamp, Double[] values) {
		this.timestamp = timestamp;
		this.values = values;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public Double[] getValues() {
		return values;
	}

}
