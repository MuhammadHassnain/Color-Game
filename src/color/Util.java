package color;

import java.util.Random;
import java.util.StringTokenizer;

public class Util {
	/**
	 * 
	 * @param min lower limit and it is included
	 * @param max upper limit and it is excluded
	 * @return random number between min and max
	 */
	public static int getRandomInRange(int min,int max) {
		Random random = new Random();
		int randno = min + random.nextInt(max-min);
		return randno;
	}
	
	public synchronized static void dumpCard(Card c) {
		synchronized (Game.pileLock) {
			Game.openPile.add(c);
		}
	}
	public synchronized static Card replaceCard(Card c) {
		Integer i = getRandomInRange(0, Game.openPile.size());
		Card ret = Game.openPile.get(i);
		Game.openPile.remove(ret);
		Game.openPile.add(c);
		return ret;
	}

}
