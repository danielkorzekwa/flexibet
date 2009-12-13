package dk.bot.webconsole.panels.marketdetails.stathelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math.util.MathUtils;

import dk.bot.bettingengine.regression.Prediction;
import dk.bot.marketobserver.model.Market;
import dk.bot.marketobserver.model.MarketRunner;

/**
 * Creates runners stat
 * 
 * @author daniel
 * 
 */
public class RunnerDataStatHelper {

	/**
	 * 
	 * @param completeMarket
	 * @param marketRunnersPrediction key - selectionId, value - runner prediction
	 * @return
	 */
	public static List<RunnerDataStat> createRunnerDataStat(Market completeMarket,Map<Integer,Prediction> marketRunnersPrediction) {

		List<RunnerDataStat> runnerDataStats = new ArrayList<RunnerDataStat>();

		for (MarketRunner marketRunner : completeMarket.getMarketRunners().getMarketRunners()) {
			RunnerDataStat runnerDataStat = new RunnerDataStat();
			runnerDataStat.setRunnerName(completeMarket.getMarketData().getSelectionName(
					marketRunner.getSelectionId()));
			runnerDataStat.setPriceToBack(marketRunner.getPriceToBack());
			runnerDataStat.setPriceToBackWeighted(MathUtils.round(completeMarket.getMarketRunners().getRunnerPriceToBackWeighted(marketRunner.getSelectionId()),2));
			runnerDataStat.setPriceToLay(marketRunner.getPriceToLay());
			runnerDataStat.setLastPriceToBack(marketRunner.getLastPriceToBack());
			runnerDataStat.setLastPriceToLay(marketRunner.getLastPriceToLay());
			runnerDataStat.setNearSP(marketRunner.getNearSP());
			runnerDataStat.setFarSP(marketRunner.getFarSP());
			runnerDataStat.setActualSP(marketRunner.getActualSP());
			runnerDataStat.setVirtWinner(false);
			
			if(marketRunnersPrediction==null) {
				marketRunnersPrediction = new HashMap<Integer, Prediction>();
			}
			Prediction prediction = marketRunnersPrediction.get(marketRunner.getSelectionId());
			if(prediction!=null) {
				runnerDataStat.setSlope(prediction.getSlope());
				runnerDataStat.setSlopeErr(prediction.getSlopeErr());
			}
			else {
				runnerDataStat.setSlope(Double.NaN);
				runnerDataStat.setSlopeErr(Double.NaN);
			}
			
			if(completeMarket.getMasseyPrices()!=null && completeMarket.getMasseyPrices().getMasseyRunnerPrice(marketRunner.getSelectionId())!=null) {
				Double masseyRunnerPrice = completeMarket.getMasseyPrices().getMasseyRunnerPrice(marketRunner.getSelectionId());
				runnerDataStat.setMasseyPrice(MathUtils.round(masseyRunnerPrice,2));
			}
			else {
				runnerDataStat.setMasseyPrice(Double.NaN);
			}
			
			if(completeMarket.getRacingPostPrices()!=null && completeMarket.getRacingPostPrices().getRacingPostRunnerPrice(marketRunner.getSelectionId())!=null) {
				Double racingPostPrice = completeMarket.getRacingPostPrices().getRacingPostRunnerPrice(marketRunner.getSelectionId());
				runnerDataStat.setRacingPostPrice(MathUtils.round(racingPostPrice,2));
			}
			else {
				runnerDataStat.setRacingPostPrice(Double.NaN);
			}
			
			runnerDataStats.add(runnerDataStat);
		}
		return runnerDataStats;
	}
}
