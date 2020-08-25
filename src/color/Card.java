package color;

enum Suit {
	Hearts, Spades, Diamond, Club

}

enum Value {
	Ace(14), King(13), Queen(12), Jack(11), Two(2), Three(3), Four(4), Five(5), Six(6), Seven(7), Eight(8), Nine(9),
	Ten(10);
	int value;

	Value(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

}

public class Card implements Comparable<Card> {
	private String name;
	private Suit suit;
	private Value value;

	public Card(String name, Suit suit, Value value) {
		this.name = name;
		this.suit = suit;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Suit getSuit() {
		return suit;
	}

	public void setSuit(Suit suit) {
		this.suit = suit;
	}

	public Value getValue() {
		return value;
	}

	public void setValue(Value value) {
		this.value = value;
	}

	@Override
	public int compareTo(Card o) {
		int cmp = this.suit.compareTo(o.getSuit());
		if (cmp != 0) {
			return cmp;
		}
		return this.value.compareTo(o.getValue());
	}

	@Override
	public String toString() {
		return "{" + name + ", " + suit + ", " + value + "}\n";
	}

}
