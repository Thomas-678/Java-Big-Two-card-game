/**
 * Hand Description: This hand consists of five cards with consecutive ranks.
 * For the sake of simplicity, 2 and A can only form a straight with K but not
 * with 3. The card with the highest rank in a straight is referred to as the
 * top card of this straight. A straight having a top card with a higher rank
 * beats a straight having a top card with a lower rank. For straights having
 * top cards with the same rank, the one having a top card with a higher suit
 * beats the one having a top card with a lower suit.
 * 
 * @author Thomas Ho
 * @version 1.0
 */
public class Straight extends Hand {
	/**
	 * Pass arguments to superclass' constructor
	 * 
	 * @param player the player this hand belongs to
	 * @param cards  the cards in this hand
	 * @see Hand#Hand(CardGamePlayer player, CardList cards)
	 */
	public Straight(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}

	/**
	 * a method for checking if this is a valid hand
	 * 
	 * @return true if this hand is Straight, false otherwise
	 */
	public boolean isValid() {
		if (this.size() != 5)
			return false;
		// If all cards have same suit, then the hand is not Straight (could be
		// StraightFlush)
		if (getCard(0).getSuit() == getCard(1).getSuit() && getCard(1).getSuit() == getCard(2).getSuit()
				&& getCard(2).getSuit() == getCard(3).getSuit() && getCard(3).getSuit() == getCard(4).getSuit())
			return false;

		// CardList#sort() will sort by Rank if Card obj (from A to K)
		// CardList#sort() will sort by Rank if BigTwoCard obj (from 3 to 2)
		CardList a = new CardList();
		for (int i = 0; i < 5; ++i)
			a.addCard(getCard(i));
		a = Hand.toCard(a);
		a.sort();
		// After sorting, if last card is "2", only 2 possible cases (according to
		// definition in this assignment)
		// J, Q, K, A, 2
		// 10, J, Q, K, A
		// If first card is 2, only {} is possible
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
	 * @return a String object "Straight"
	 */
	public String getType() {
		return new String("Straight");
	}

}