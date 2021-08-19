/**
 * The BigTwoCard class is a subclass of the Card class, and is used to model a
 * card used in a Big Two card game. It should override the compareTo() method
 * it inherited from the Card class to reflect the ordering of cards used in a
 * Big Two card game.
 * 
 * @author Thomas Ho
 * @version 1.0
 */
public class BigTwoCard extends Card {
	/**
	 * a constructor for building a card with the specified suit and rank.
	 * <strong>suit</strong> is n integer between 0 and 3, and <strong>rank</strong>
	 * is an integer between 0 and 12.
	 * 
	 * @param suit an int value between 0 and 3 representing the suit of a card:
	 *             <p>
	 *             0 = Diamond, 1 = Club, 2 = Heart, 3 = Spade
	 * @param rank an int value between 0 and 12 representing the rank of a card:
	 *             <p>
	 *             0 = 'A', 1 = '2', 2 = '3', ..., 8 = '9', 9 = '0', 10 = 'J', 11 =
	 *             'Q', 12 = 'K'
	 */
	public BigTwoCard(int suit, int rank) {
		super(suit, rank);
	}

	/**
	 * a method for comparing the order of this card with the specified card.
	 * Returns a negative integer, zero, or a positive integer as this card is less
	 * than, equal to, or greater than the specified card.
	 * 
	 * @param card a card object to compare against
	 * @return 1 if this card object has a higher order; <br>
	 *         -1 if this card object has a lower order; <br>
	 *         0 if two cards are equal
	 */
	@Override
	public int compareTo(Card card) {
		// Consider cases with same rank first
		// Then cases with "2" and "A" suit
		// Otherwise, other cards can be
		// compared using their integral suit values

		if (this.getRank() == card.getRank()) { // Both cards same rank
			if (card.getSuit() < this.getSuit())
				return 1;
			else if ((card.getSuit() > this.getSuit()))
				return -1;
			else
				return 0;
		}

		if (this.getRank() == 1)  // this card has rank "2"
				return 1;
		if (card.getRank() == 1)  // Argument card has rank "2"
				return -1;
		if (this.getRank() == 0)  // this card has rank "A"
				return 1;
		if (card.getRank() == 0)  // Argument card has rank "A"
				return -1;

		if (card.getRank() < this.getRank()) // when both cards have ranks "3" to "K"
			return 1;
		else
			return -1;

	}
}