package dk.flexibet.server.marketwinnergenerator;

import java.util.Random;

import org.junit.Test;

public class RandomSelectionTest {

	private Random rnd = new Random(System.currentTimeMillis());
	
	/**Mass size must be > 1*/
	@Test(expected=IllegalArgumentException.class)
	public void testNextIndex() {
		int[] mass = new int[1];
		mass[0] = 3;
		new RandomSelection(mass ,rnd);
	}

}
