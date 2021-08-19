/**
 * Hand Description: This hand consists of two cards with the same rank. The
 * card with a higher suit in a pair is referred to as the top card of this
 * pair. A pair with a higher rank beats a pair with a lower rank. For pairs
 * with the same rank, the one containing the highest suit beats the other.
 * 
 * @author Thomas Ho
 * @version 1.0
 */
public class Pair extends Hand {
	/**
	 * Pass arguments to superclass' constructor
	 * 
	 * @param player the player this hand belongs to
	 * @param cards  the cards in this hand
	 * @see Hand#Hand(CardGamePlayer player, CardList cards)
	 */
	public Pair(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}

	/**
	 * a method for checking if this is a valid hand
	 * 
	 * @return true if this hand is Pair, false otherwise
	 */
	@Override
	public boolean isValid() {
		if (this.size() != 2)
			return false;
		else if (this.getCard(0).getRank() != this.getCard(1).getRank())
			return false;
		else
			return true;
	}

	/**
	 * a method for returning a string specifying the type of this hand
	 * 
	 * @return a String object "Pair"
	 */
	@Override
	public String getType() {
		return new String("Pair");
	}

}