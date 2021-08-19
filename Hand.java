import java.util.ArrayList;
/**
 * The Hand class is a subclass of the CardList class, and is used to model a
 * hand of cards. It has a private instance variable for storing the player who
 * plays this hand. It also has methods for getting the player of this hand,
 * checking if it is a valid hand, getting the type of this hand, getting the
 * top card of this hand, and checking if it beats a specified hand.
 * 
 * @author Thomas Ho
 * @version 1.0
 */
public abstract class Hand extends CardList {
	/**
	 * a constructor for building a hand with the specified player and list of cards
	 * 
	 * @param player the player this hand belongs to
	 * @param cards  the cards in this hand
	 */
	public Hand(CardGamePlayer player, CardList cards) {
		this.player = player;
		for (int i = 0; i < cards.size(); ++i)
			addCard(cards.getCard(i));
	}

	/**
	 * the player who plays this hand
	 */
	private CardGamePlayer player;

	/**
	 * a method for retrieving the player of this hand
	 * 
	 * @return a reference to the player object who plays this hand
	 */
	public CardGamePlayer getPlayer() {
		return this.player;
	}

	/**
	 * a method for retrieving the top card of this hand.<br>
	 * FullHouse and Quad are special cases. Their top card may not have the
	 * absolute highest value.<br>
	 * Top card in FullHouse is any one of triplet.<br>
	 * Top card in Quad is any one of the quadruplet.
	 * 
	 * @return the top card of this hand
	 */
	public Card getTopCard() {
		int x = 0; // represents any card with different rank than getCard(0)

		// Take care of cases of FullHose and Quad first
		int count = 0; // count how many cards have same rank (so far)
		String ii = getType();
		if (ii.contentEquals("FullHouse")) {
			for (int i = 1; i < 5; ++i) {
				if (getCard(i).getRank() == getCard(0).getRank())
					count++;
				else
					x = i;
			}
			if (count == 2)
				return getCard(x);
			else
				return getCard(0);
		}
		if (ii.contentEquals("Quad")) {
			for (int i = 1; i < 3; ++i) { // Searching first 3 cards is enough
				if (getCard(i).getRank() == getCard(0).getRank())
					count++;
				else
					x = i;
			}
			if (count == 2)
				return getCard(0);
			else
				return getCard(x);
		}

		// Back to default cases. Top card is the card with highest value
		// Now int x is the Top card position
		for (int i = 1; i < size(); ++i)
			if (getCard(i).compareTo(getCard(x)) > 0)
				x = i;
		return getCard(x);
	}

	/**
	 * a method for checking if this hand beats a specified hand.<br> <br>
	 * No need to override this method in subclasses, since the number of subclasses
	 * is fixed (BigTwo's rules have defined how many hands there are). So no future
	 * modification is required.
	 * 
	 * @param hand another hand to compare to
	 * @return true if this hand beats the specified hand, false otherwise
	 */
	public boolean beats(Hand hand) {
		// Assuming both hands were checked to be valid
		if (this.size() != hand.size())
			return false;

		if (this.size() <= 3) // cases where the hands have 3 or fewer cards
			if (this.getTopCard().compareTo(hand.getTopCard()) <= 0)
				return false;
			else
				return true;
		// cases where the 5-card hands are of same types
		String ii = getType();
		String jj = hand.getType();
		if (ii.contentEquals(jj)) {
			if (ii.contentEquals("Flush")) {
				if (this.getCard(0).getSuit() < hand.getCard(0).getSuit())
					return false;
				else if (this.getCard(0).getSuit() > hand.getCard(0).getSuit())
					return true;
				else if (this.getTopCard().compareTo(hand.getTopCard()) <= 0)
					return false;
				else
					return true;
			}
			if (ii.contentEquals("Straight") || ii.contentEquals("FullHouse") || ii.contentEquals("Quad")) {
				if (this.getTopCard().compareTo(hand.getTopCard()) <= 0)
					return false;
				else
					return true;
			}

			if (ii.contentEquals("StraightFlush")) {
				if (this.getTopCard().getRank() < hand.getTopCard().getRank())
					return false;
				else if (this.getTopCard().getRank() > hand.getTopCard().getRank())
					return true;
				else if (this.getTopCard().compareTo(hand.getTopCard()) <= 0)
					return false;
				else
					return true;
			}
		}

		// now cases where the 5-card hands are of different types
		// 5-card hands are ranked from index 0 to 4
		String hand_value[] = { "Straight", "Flush", "FullHouse", "Quad", "StraightFlush" };
		int i, j; // i is the hand value of this hand, j is the value of argument hand
					// reuse local String ii, jj defined above
		for (i = 0; i < 5; ++i)
			if (ii.contentEquals(hand_value[i]))
				break;
		for (j = 0; j < 5; ++j)
			if (jj.contentEquals(hand_value[j]))
				break;
		if (i < j)
			return false;
		else
			return true;
	}

	/**
	 * Lazy workaround method.<br>
	 * For each card object stored in inside this class, this method can convert
	 * BigTwoCard objects into Card objects, while maintaining the cards'
	 * positions.<br>
	 * Reason: Subclasses' isValid() were implemented using CardList#sort(). I
	 * originally designed isValid() based on Card's sorting rule, but the sorting
	 * rule is slightly different for Card object and BigTwoCard object, though I
	 * should have used boolean array to solve the problem.
	 * 
	 * @param c a CardList object containing Card or its subclass objects
	 * @return a CardList object containing Card objects
	 */
	public static CardList toCard(CardList c) {
		CardList a = new CardList();
		Card temp;
		for (int i = 0; i < c.size(); ++i) {
			temp = new Card(c.getCard(i).getSuit(), c.getCard(i).getRank());
			a.addCard(temp);
		}
		return a;
	}

	/**
	 * a method for checking if this is a valid hand
	 * 
	 * @return true if this hand is valid, false otherwise
	 */
	public abstract boolean isValid();

	/**
	 * a method for returning a string specifying the type of this hand
	 * 
	 * @return The string representation of the type of this hand
	 */
	public abstract String getType();

}