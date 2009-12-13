package dk.bot.webconsole.pages.histpricechart.visualization;


/**Represents all values for all time series to be displayed on the timeline chart.
 *  
 * @author korzekwad
 *
 */
public class TimeChartData {

	/**Labels for chart series, e.g. BetFair, BetDaq.*/
	final private String[] series;
	
	final private TimeChartDataValue[] values;
	
	public TimeChartData(String[] series,TimeChartDataValue[] values) {
		this.series = series;
		this.values = values;
	}

	public String[] getSeries() {
		return series;
	}

	public TimeChartDataValue[] getValues() {
		return values;
	}
	
}
