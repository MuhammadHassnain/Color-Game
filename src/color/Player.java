package color;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Player implements Runnable {
	private final Object replaceLock = new Object();
	private boolean myturn = false;
	private String name;
	private List<Card> hand;
	private Game game; /// game player is playing
	private int score;

	public int getScore() {
		return score;
	}

	public void calculateScore() {
		for (int i = 0; i < hand.size(); ++i) {
			if (hand.get(i).getSuit() == Game.colorOfGame) {
				score += hand.get(i).getValue().value;
			}
			score += hand.get(i).getValue().value;
		}
	}

	public Player(String name, Game game) {
		setName(name);
		hand = new ArrayList<>();
		this.game = game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public Game getGame() {
		return game;
	}

	public String getName() {
		return name;

	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @param card recive a card and puts it in hand
	 * @return if card is jack the return true otherwise false
	 */

	public boolean receiveCard(Card card) {
		if (hand == null)
			hand = new ArrayList<>();
		hand.add(card);
		if (card.getValue() == Value.Jack) {
			return true;
		}
		return false;
	}

	/**
	 * this funtion will make the hand empty
	 * 
	 */
	public void makeHandEmpty() {
		hand = null;
	}

	public void setColorOfGame() {
		Suit[] suit = Suit.values();
		int rand = Util.getRandomInRange(0, suit.length);
		Game.colorOfGame = suit[rand];
		System.out.println(name + " Setting color of game to:" + Game.colorOfGame);
	}

	public void showHand() {
		try {
			Game.logBufWriter.write(this.name+ " Showing Hand---\n\n");

			for(Card c:hand) {
				Game.logBufWriter.write(c.toString());
			}
			Game.logBufWriter.write("\n\n");
			Game.logBufWriter.flush();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			Game.logBufWriter.write("\n\n\n" + this.name + " Turn---\n\n\n");
			Set<Integer> indexOfCardTodump = new HashSet<>();
			List<Card> cardtodump = new ArrayList<>();
			while (indexOfCardTodump.size() < 5) {
				indexOfCardTodump.add(Util.getRandomInRange(0, hand.size()));
			}

			Game.logBufWriter.write(this.name + " Dump Following Card(s) in open pile----\n");

			for (Integer i : indexOfCardTodump) {
				Util.dumpCard(hand.get(i));
				Game.logBufWriter.write(hand.get(i).toString());
				cardtodump.add(hand.get(i));
			}
			Game.logBufWriter.flush();
			hand.removeAll(cardtodump);
			game.NotifyGame(); // not

		} catch (Exception e) {
			e.printStackTrace();
		}

		synchronized (replaceLock) {
			try {
				while (!myturn)
					replaceLock.wait();
				/**
				 * code for replacing card in pile
				 */
				Game.logBufWriter.write("\n\n---"+this.getName()+" replacing card in open pile---\n\n");
				for (int i = 0; i < 2; ++i) {
					Integer rand = Util.getRandomInRange(0, hand.size());
					Card ret = hand.get(rand);
					hand.remove(ret);
					Card rep = Util.replaceCard(ret);
					hand.add(rep);
					Game.logBufWriter.write(ret.toString()+ " replaced with "+ rep.toString());
					
				}
				Game.logBufWriter.flush();
				game.notifyPlayerHasReplaced();

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void notifyToReplace() {
		synchronized (replaceLock) {
			myturn = true;
			replaceLock.notify();
			System.out.println(name + " has been notified");
		}
	}
}
