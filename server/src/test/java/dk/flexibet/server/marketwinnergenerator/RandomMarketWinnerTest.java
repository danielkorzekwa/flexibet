package dk.flexibet.server.marketwinnergenerator;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Random;

import org.junit.Test;
public class RandomMarketWinnerTest {

	private double runnerPrices[] = new double[]{1.01d,1000,2};

	/** It's a test based on a probability */
	@Test
	public void testGenerateMarketWinner() {
		
		Random random = new Random(System.currentTimeMillis());

		int selA = 0, selB = 0, selC = 0;

		for (int i = 0; i < 10000; i++) {
			int winnerSelectionId = RandomMarketWinner.generateMarketWinner(random, runnerPrices);
			if (winnerSelectionId == 0) {
				selA++;
			} else if (winnerSelectionId == 1) {
				selB++;
			} else if (winnerSelectionId == 2) {
				selC++;
			} else {
				fail("Runner not found for winnerSelectionId");
			}
		}
	
		System.out.println("Prob: " + runnerPrices[0] + ", won: " + selA);
		System.out.println("Prob: " + runnerPrices[1] + ", won: " + selB);
		System.out.println("Prob: " + runnerPrices[2] + ", won: " + selC);
		
		/**Check how many times runners won.*/
		assertTrue(selA + ">" + selB, selA>selB);
		assertTrue(selA + ">" + selC, selA>selC);
		assertTrue(selC + ">" + selB, selC>selB);
	
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testGenerateMarketWinnerLessThan2Runners() {
		runnerPrices = new double[]{1.01};
		RandomMarketWinner.generateMarketWinner(new Random(), runnerPrices);
	}
}
