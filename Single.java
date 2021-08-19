/**
 * Hand Description: This hand consists of only one single card. The only card
 * in a single is referred to as the top card of this single. A single with a
 * higher rank beats a single with a lower rank. For singles with the same rank,
 * the one with a higher suit beats the one with a lower suit.
 * 
 * @author Thomas Ho
 * @version 1.0
 */
public class Single extends Hand {
	/**
	 * Pass arguments to superclass' constructor
	 * 
	 * @param player the player this hand belongs to
	 * @param cards  the cards in this hand
	 * @see Hand#Hand(CardGamePlayer player, CardList cards)
	 */
	public Single(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}

	/**
	 * a method for checking if this is a valid hand
	 * 
	 * @return true if this hand is Single, false otherwise
	 */
	public boolean isValid() {
		if (this.size() != 1)
			return false;
		else
			return true;
	}

	/**
	 * a method for returning a string specifying the type of this hand
	 * 
	 * @return a String object "Single"
	 */
	public String getType() {
		return new String("Single");
	}

}