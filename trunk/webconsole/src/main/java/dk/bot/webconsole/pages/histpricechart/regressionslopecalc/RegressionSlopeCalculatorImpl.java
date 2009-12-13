package dk.bot.webconsole.pages.histpricechart.regressionslopecalc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.joda.time.DateTimeUtils;

import dk.bot.bettingengine.regression.MarketRunnerRegression;
import dk.bot.bettingengine.regression.Prediction;
import dk.bot.bettingengine.regression.RegressionCache;
import dk.bot.bettingengine.regression.RegressionCacheImpl;
import dk.bot.marketobserver.model.MarketRunner;
import dk.bot.marketobserver.model.MarketRunners;
import dk.bot.marketobserver.model.RunnerPrice;
import dk.flexibet.server.model.HistRunnerPrice;

/**Calculates regression for a period of runner prices.
 * 
 * @author korzekwad
 *
 */
public class RegressionSlopeCalculatorImpl implements RegressionSlopeCalculator{

	/**Regression for last 60 seconds with a step of second.
	 * 
	 */
	public Prediction[] calculateRegressionForAvgPrice(List<HistRunnerPrice> histRunnerPrices, long period,TimeUnit step ) {
		RegressionCache cache = new RegressionCacheImpl(300, 300);
		Prediction[] slopes = new Prediction[histRunnerPrices.size()];
		
		for(int i=0;i<histRunnerPrices.size();i++) {
			HistRunnerPrice histPrice = histRunnerPrices.get(i);
			//TODO setting this time causes a critical error in a betting engine. Wrong time is obtained. DateTimeUtils.setCurrentMillisFixed(histPrice.getRecordTime().getTime());
			
			MarketRunners marketRunners = createMarketRunners(histPrice);
			MarketRunner marketRunner = marketRunners.getMarketRunners().get(0);
			List<MarketRunners> marketHistory = cache.getMarketHistory(marketRunners.getMarketId());
			
			Prediction prediction = MarketRunnerRegression.calculateRegression(histPrice.getSelectionId(),
					marketHistory, new Date(histPrice.getRecordTime().getTime() - period), step, marketRunner,marketRunners.getTimestamp());
			
			slopes[i] = prediction;
			
			cache.putMarketRunners(marketRunners);
		}
		
		return slopes;
	}
	
	private MarketRunners createMarketRunners(HistRunnerPrice histRunnerPrice) {
		
			/** Some values are hard coded, because they are not not available in a historical data. */
			List<RunnerPrice> prices = new ArrayList<RunnerPrice>();
			prices.add(new RunnerPrice(histRunnerPrice.getPriceToBack(), 100, 0));
			prices.add(new RunnerPrice(histRunnerPrice.getPriceToLay(), 0, 100));
			MarketRunner marketRunner = new MarketRunner(histRunnerPrice.getSelectionId(), 10, histRunnerPrice
					.getLastPriceMatched(), 0, 0, 0, prices);
			
			List<MarketRunner> marketRunnersList = new ArrayList<MarketRunner>(1);
			marketRunnersList.add(marketRunner);
		
			MarketRunners marketRunners = new MarketRunners(1, marketRunnersList, histRunnerPrice.getInPlayDelay(),
				histRunnerPrice.getRecordTime());	
			
			return marketRunners;
	}

}
