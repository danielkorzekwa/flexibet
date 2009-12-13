package dk.bot.webconsole.pages.histpricechart.regressionslopecalc;

import java.util.List;
import java.util.concurrent.TimeUnit;

import dk.bot.bettingengine.regression.Prediction;
import dk.flexibet.server.model.HistRunnerPrice;

/**Calculates regression for a period of runner prices.
 * 
 * @author korzekwad
 *
 */
public interface RegressionSlopeCalculator {

	/**Calculates the slope based on an avg price between best back/lay prices.
	 * 
	 * @param histRunnerPrices
	 * @param period recent period of time [ms] that the the regression is based on, e.g. 60 sec
	 * @param step what is the size of step on the 'x' axis, e.g. 1s or 1m
	 * @return
	 */
	Prediction[] calculateRegressionForAvgPrice(List<HistRunnerPrice> histRunnerPrices,long period,TimeUnit step);
}
