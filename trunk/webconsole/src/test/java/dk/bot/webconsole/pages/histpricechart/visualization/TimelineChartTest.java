package dk.bot.webconsole.pages.histpricechart.visualization;

import java.util.Date;

import org.apache.velocity.app.VelocityEngine;
import org.joda.time.DateMidnight;
import org.junit.Test;

public class TimelineChartTest {

	private TimeLineChart lineChart = new TimeLineChart(new VelocityEngine());
	
	@Test
	public void testGenerate() {
		String[] series = new String[]{"BetFair","BetDaq"};
		TimeChartDataValue[] values = new TimeChartDataValue[1440];
		
		for(int i=0;i<1440;i++) {
			Date timestamp = new DateMidnight().toDateTime().plus((i+1)*1000*60).toDate();
			double betfairValue = i;
			double betdaqValue= i-100;
			values[i] = new TimeChartDataValue(timestamp,new Double[]{betfairValue,betdaqValue});
		}
	
		TimeChartData data = new TimeChartData(series,values);
		String report = lineChart.generate(data );
		System.out.println(report);
	}

}
