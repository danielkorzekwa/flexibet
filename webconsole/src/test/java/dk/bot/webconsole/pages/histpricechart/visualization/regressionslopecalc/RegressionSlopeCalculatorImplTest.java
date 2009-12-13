package dk.bot.webconsole.pages.histpricechart.visualization.regressionslopecalc;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

import dk.bot.bettingengine.regression.Prediction;
import dk.bot.webconsole.pages.histpricechart.regressionslopecalc.RegressionSlopeCalculator;
import dk.bot.webconsole.pages.histpricechart.regressionslopecalc.RegressionSlopeCalculatorImpl;
import dk.flexibet.server.model.HistRunnerPrice;

public class RegressionSlopeCalculatorImplTest {

	private RegressionSlopeCalculator calc = new RegressionSlopeCalculatorImpl();
	
	private List<HistRunnerPrice> histRunnerPrices = new ArrayList<HistRunnerPrice>();
	
	@Before
	public void setUp() {
		histRunnerPrices = new ArrayList<HistRunnerPrice>();
		histRunnerPrices.add(createHistPrice(1,2, 2.01));
		histRunnerPrices.add(createHistPrice(2,2, 2.01));
		histRunnerPrices.add(createHistPrice(3,2, 2.01));
		histRunnerPrices.add(createHistPrice(4,2.01, 2.02));
		histRunnerPrices.add(createHistPrice(5,2.01, 2.02));
		histRunnerPrices.add(createHistPrice(6,2.02, 2.03));
		histRunnerPrices.add(createHistPrice(7,2.02, 2.03));
		histRunnerPrices.add(createHistPrice(8,2.01, 2.02));
		histRunnerPrices.add(createHistPrice(9,2.02, 2.03));
		histRunnerPrices.add(createHistPrice(10,2.02, 2.03));
		histRunnerPrices.add(createHistPrice(11,2.03, 2.04));
		histRunnerPrices.add(createHistPrice(12,2.03, 2.04));
		histRunnerPrices.add(createHistPrice(13,2.03, 2.04));
		histRunnerPrices.add(createHistPrice(14,2.04, 2.05));
		histRunnerPrices.add(createHistPrice(15,2.04, 2.05));
		histRunnerPrices.add(createHistPrice(16,2.04, 2.05));
	}
	
	@Test
	public void testCalculateRegressionForAvgPrice() {
		
		Prediction[] predictions = calc.calculateRegressionForAvgPrice(histRunnerPrices,1000*60,TimeUnit.SECONDS);
		assertEquals(predictions.length, histRunnerPrices.size());
		
		for(Prediction prediction: predictions) {
			System.out.println("slope/err: " + prediction.getSlope() + "/" + prediction.getSlopeErr());
		}
		assertEquals(Double.NaN, predictions[0].getSlope(),0.001);
		assertEquals(0, predictions[1].getSlope(),0.001);
		assertEquals(0, predictions[2].getSlope(),0.001);
		assertEquals(4.246, predictions[3].getSlope(),0.001);
		assertEquals(4.246, predictions[4].getSlope(),0.001);
		assertEquals(5.634, predictions[5].getSlope(),0.001);
		assertEquals(5.529, predictions[6].getSlope(),0.001);
		assertEquals(3.863, predictions[7].getSlope(),0.001);
		assertEquals(3.760, predictions[8].getSlope(),0.001);
		assertEquals(3.502, predictions[9].getSlope(),0.001);
		assertEquals(3.832, predictions[10].getSlope(),0.001);
		assertEquals(3.876, predictions[11].getSlope(),0.001);
		assertEquals(3.775, predictions[12].getSlope(),0.001);
		assertEquals(3.995, predictions[13].getSlope(),0.001);
		assertEquals(4.039, predictions[14].getSlope(),0.001);
		assertEquals(3.979, predictions[15].getSlope(),0.001);
	}
	
	private HistRunnerPrice createHistPrice(long recordTimeInSec,double priceToBack,double priceToLay) {
		HistRunnerPrice histRunnerPrice = new HistRunnerPrice();
		histRunnerPrice.setPriceToBack(priceToBack);
		histRunnerPrice.setPriceToLay(priceToLay);
		histRunnerPrice.setRecordTime(new Date(recordTimeInSec*1000));
		
		return histRunnerPrice;
	}

}
