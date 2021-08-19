/**
 * Hand Description: This hand consists of five cards with the same suit. The
 * card with the highest rank in a flush is referred to as the top card of this
 * flush. A flush always beats any straights. A flush with a higher suit beats a
 * flush with a lower suit. For flushes with the same suit, the one having a top
 * card with a higher rank beats the one having a top card with a lower rank.
 * 
 * @author Thomas Ho
 * @version 1.0
 */
public class Flush extends Hand {
	/**
	 * Pass arguments to superclass' constructor
	 * 
	 * @param player the player this hand belongs to
	 * @param cards  the cards in this hand
	 * @see Hand#Hand(CardGamePlayer player, CardList cards)
	 */
	public Flush(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}

	/**
	 * a method for checking if this is a valid hand
	 * 
	 * @return true if this hand is Flush, false otherwise
	 */
	public boolean isValid() {
		for (int i = 1; i < 5; ++i)
			if (getCard(i).getSuit() != getCard(i - 1).getSuit())
				return false;
		// now verify this hand is not StraightFlush
		CardList a = new CardList();
		for (int i = 0; i < 5; ++i)
			a.addCard(getCard(i));
		return !(new StraightFlush(getPlayer(), a)).isValid();
	}

	/**
	 * a method for returning a string specifying the type of this hand
	 * 
	 * @return a String object "Flush"
	 */
	public String getType() {
		return new String("Flush");
	}

}