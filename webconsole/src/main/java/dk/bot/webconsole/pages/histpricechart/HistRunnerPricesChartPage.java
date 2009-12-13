package dk.bot.webconsole.pages.histpricechart;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.velocity.app.VelocityEngine;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.spring.injection.annot.SpringBean;

import dk.bot.bettingengine.regression.Prediction;
import dk.bot.webconsole.pages.histpricechart.regressionslopecalc.RegressionSlopeCalculatorImpl;
import dk.bot.webconsole.pages.histpricechart.visualization.TimeChartData;
import dk.bot.webconsole.pages.histpricechart.visualization.TimeChartDataValue;
import dk.bot.webconsole.pages.histpricechart.visualization.TimeLineChart;
import dk.flexibet.server.mbean.BettingServerMBean;
import dk.flexibet.server.model.HistRunnerPrice;

/** Displays market details.
 * 
 * @author daniel
 *
 */
public class HistRunnerPricesChartPage extends WebPage{

	public static final String PAGE_PARAM_MARKETID="marketId";
	public static final String PAGE_PARAM_SELECTIONID="selectionId";
	private static final String PAGE_PARAM_SHOW_INPLAY = "inPlay";
	private static final String PAGE_PARAM_SHOW_PROBS = "probs";
	
	@SpringBean(name="bettingServerMBean")
	private BettingServerMBean bettingServerMBean;
	
	@SpringBean
	private VelocityEngine velocityEngine;
	
	public HistRunnerPricesChartPage(final PageParameters parameters) {
		super();
		
		int marketId = parameters.getInt(PAGE_PARAM_MARKETID, -1);
		int selectionId = parameters.getInt(PAGE_PARAM_SELECTIONID, -1);
		boolean showInPLay = parameters.getAsBoolean(PAGE_PARAM_SHOW_INPLAY,false);
		boolean showProbs = parameters.getAsBoolean(PAGE_PARAM_SHOW_PROBS,false);
		
		List<HistRunnerPrice> histRunnerPrices = bettingServerMBean.findRunnerPrices(marketId,selectionId);
		
		String chartData;
		if(showInPLay) {
		chartData = createHtmlChart(histRunnerPrices,showProbs);
		}
		else {
			List<HistRunnerPrice> notInPlayPrices = new ArrayList<HistRunnerPrice>();
			for(HistRunnerPrice histPrice: histRunnerPrices) {
				if(histPrice.getInPlayDelay()==0) {
					notInPlayPrices.add(histPrice);
				}
			}
			chartData = createHtmlChart(notInPlayPrices, showProbs);
		}
		
		Label chartDataLabel = new Label("histRunnerPricesChart",chartData);
		chartDataLabel.setEscapeModelStrings(false);
		
		add(chartDataLabel);
	}
	
	private String createHtmlChart(List<HistRunnerPrice> histRunnerPrices, boolean showProbability) {
		/** Create time chart html report for last matched prices for market runner. */
		Prediction[] regression = new RegressionSlopeCalculatorImpl().calculateRegressionForAvgPrice(histRunnerPrices, 1000*300, TimeUnit.SECONDS);
		
		String[] series = new String[] { "matchedPrice", "5MinSlope","5MinSlopeErr","5MinPredict" };
		TimeChartDataValue[] values = new TimeChartDataValue[histRunnerPrices.size()];
		for (int i = 0; i < histRunnerPrices.size(); i++) {
			HistRunnerPrice price = histRunnerPrices.get(i);
			double value = showProbability ? 1 / price.getLastPriceMatched() : price.getLastPriceMatched();
			TimeChartDataValue chartData = new TimeChartDataValue(price.getRecordTime(), new Double[] { value,
					regression[i].getSlope()*0.01,regression[i].getSlopeErr(),regression[i].getPredictedValue()});
			values[i] = chartData;
		}
		TimeChartData chartData = new TimeChartData(series, values);
		
		/** Generates time line chart. */
		TimeLineChart timeLineChart = new TimeLineChart(velocityEngine);
		String htmlReport = timeLineChart.generate(chartData);
		return htmlReport;
	}
}
