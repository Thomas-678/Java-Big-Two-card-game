/**
 * Hand Description: This hand consists of three cards with the same rank. The
 * card with the highest suit in a triple is referred to as the top card of this
 * triple. A triple with a higher rank beats a triple with a lower rank.
 * 
 * @author Thomas Ho
 * @version 1.0
 */
public class Triple extends Hand {
	/**
	 * Pass arguments to superclass' constructor
	 * 
	 * @param player the player this hand belongs to
	 * @param cards  the cards in this hand
	 * @see Hand#Hand(CardGamePlayer player, CardList cards)
	 */
	public Triple(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}

	/**
	 * a method for checking if this is a valid hand
	 * 
	 * @return true if this hand is Triple, false otherwise
	 */
	public boolean isValid() {
		if (this.size() != 3)
			return false;
		else if (this.getCard(0).getRank() == this.getCard(1).getRank()
				&& this.getCard(1).getRank() == this.getCard(2).getRank())
			return true;
		else
			return false;
	}

	/**
	 * a method for returning a string specifying the type of this hand
	 * 
	 * @return a String object "Triple"
	 */
	public String getType() {
		return new String("Triple");
	}

}