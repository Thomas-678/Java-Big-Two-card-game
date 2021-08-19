/**
 * Hand Description: This hand consists of five cards with consecutive ranks and
 * the same suit. For the sake of simplicity, 2 and A can only form a straight
 * flush with K but not with 3. The card with the highest rank in a straight
 * flush is referred to as the top card of this straight flush. A straight flush
 * always beats any straights, flushes, full houses and quads. A straight flush
 * having a top card with a higher rank beats a straight flush having a top card
 * with a lower rank. For straight flushes having top cards with the same rank,
 * the one having a top card with a higher suit beats one having a top card with
 * a lower suit.
 * 
 * @author Thomas Ho
 * @version 1.0
 */
public class StraightFlush extends Hand {
	/**
	 * Pass arguments to superclass' constructor
	 * 
	 * @param player the player this hand belongs to
	 * @param cards  the cards in this hand
	 * @see Hand#Hand(CardGamePlayer player, CardList cards)
	 */
	public StraightFlush(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}

	/**
	 * a method for checking if this is a valid hand
	 * 
	 * @return true if this hand is StraightFlush, false otherwise
	 */
	public boolean isValid() {
		if (this.size() != 5)
			return false;
		// check all have same suit first
		for (int i = 1; i < 5; ++i)
			if (getCard(i).getSuit() != getCard(i - 1).getSuit())
				return false;

		// CardList#sort() will sort by Rank
		CardList a = new CardList();
		for (int i = 0; i < 5; ++i)
			a.addCard(getCard(i));
		a = Hand.toCard(a);
		a.sort();
		// After sorting, if first card is ace, only 2 possible cases (according to
		// definition in this assignment)
		// A, 2, J, Q, K
		// A, 10, J, Q, K
		// If first card is 2, only {2, 3, 4, 5, 6} is possible
		if (a.getCard(0).getRank() == 0) {
			if ((a.getCard(1).getRank() == 1 || a.getCard(1).getRank() == 9) && a.getCard(2).getRank() == 10
					&& a.getCard(3).getRank() == 11 && a.getCard(4).getRank() == 12)
				return true;
			return false;
		}

		for (int i = 1; i < 5; ++i) // default cases
			if (a.getCard(i).getRank() != a.getCard(i - 1).getRank() + 1)
				return false;
		return true;
	}

	/**
	 * a method for returning a string specifying the type of this hand
	 * 
	 * @return a String object "StraightFlush"
	 */
	public String getType() {
		return new String("StraightFlush");
	}

}