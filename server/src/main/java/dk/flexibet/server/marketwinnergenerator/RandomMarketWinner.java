package dk.flexibet.server.marketwinnergenerator;

import java.util.Random;


/**
 * 
 * @author daniel
 *
 */
public class RandomMarketWinner {

	/**Generate random winner on market based on runner winning probability.
	 * 
	 * @param runnerPrices runner prices(decimal odds) on market. Min number of runner prices is 2.
	 * @return index of winner in runnerPrices parameter
	 */
	public static int generateMarketWinner(Random random,double[] prices) {
		if(prices.length<2) {
			throw new IllegalArgumentException("Min num of runners is 2. Runners: " + prices.length);
		}
		
		int[] mass  = new int[prices.length];
		for(int i=0;i<prices.length;i++) {
			double runnerPrice = prices[i];
			mass[i] = (int)((1d/runnerPrice)*1000d);
		}
		
		int selectionIndex = new RandomSelection(mass,random).nextIndex();
		return selectionIndex;
	}
	
}
