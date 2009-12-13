package dk.flexibet.server.marketwinnergenerator;

import java.util.Random;

/*******************************************************************************
 * Generates random number from set of weighted numbers, e.g. horse race winner
 * basing on runner probabilities.
 *  
 * /** usage: int[] e = {19, 27, -1, 4}; int m = {1, 2, 3 ,4}; RandomSelection r =
 * new RandomSelection(m); for(int i = 0; i<10; i++){ System.out.println(
 * e[r.nextIndex()])};
 */
public class RandomSelection {
	private int c[]; // binary choice tree - kept heap ordered in array
	private int n;
	private final Random random;

	public RandomSelection(int mass[],Random random) {
		if(mass.length<2) {
			throw new IllegalArgumentException("Min size of mass is 2. Mass size: " + mass.length);
		}
		
		this.random = random;
		n = mass.length;
		c = new int[n];
		for (int i = n - 1; i > 0; i--) { // first calculate total mass from
											// leaves to top
			int k = 2 * i; // k is left child, k+1 is right
			c[i] = get(k, mass) + get(k + 1, mass); // c is sum of mass of two
													// children.
		}

		// since we aren't using c[0] for anything, we will keep total mass
		// there
		c[0] = c[1]; // c[1] was the total mass of entire tree.

		// Now we fixup internal nodes to be only hold mass of the right child
		for (int i = 1; i < n; i++) {
			c[i] -= get(2 * i + 1, mass);
		}
	}

	// helper routine - lets us treat c and mass as one large array.
	private int get(int i, int[] mass) {
		return (i < n) ? c[i] : mass[i - n];
	}

	public int nextIndex() {
		int r = random.nextInt(c[0]); // r is uniformly distributed across total
									// mass
		return reduce(r); // r reduces to a single index, this function
							// simplifies testing
	}

	private int reduce(int r) {
		int k = 1;
		while (k < n) {
			int m = c[k];
			k *= 2;
			if (r >= m) {
				r -= m;
				k++;
			}
		}
		return k - n;
	}
}