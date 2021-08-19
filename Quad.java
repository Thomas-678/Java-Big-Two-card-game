/**
 * Hand Description: This hand consists of five cards, with four having the same
 * rank. The card in the quadruplet with the highest suit in a quad is referred
 * to as the top card of this quad. A quad always beats any straights, flushes
 * and full houses. A quad having a top card with a higher rank beats a quad
 * having a top card with a lower rank.
 * 
 * @author Thomas Ho
 * @version 1.0
 */
public class Quad extends Hand {
	/**
	 * Pass arguments to superclass' constructor
	 * 
	 * @param player the player this hand belongs to
	 * @param cards  the cards in this hand
	 * @see Hand#Hand(CardGamePlayer player, CardList cards)
	 */
	public Quad(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}

	/**
	 * a method for checking if this is a valid hand
	 * 
	 * @return true if this hand is Quad, false otherwise
	 */
	public boolean isValid() {
		if (this.size() != 5)
			return false;
		CardList a = new CardList();
		for (int i = 0; i < 5; ++i)
			a.addCard(getCard(i));
		a = Hand.toCard(a);
		a.sort();
		if (a.getCard(0).getRank() != a.getCard(1).getRank() && a.getCard(1).getRank() == a.getCard(2).getRank()
				&& a.getCard(2).getRank() == a.getCard(3).getRank() && a.getCard(3).getRank() == a.getCard(4).getRank())
			return true;
		if (a.getCard(0).getRank() == a.getCard(1).getRank() && a.getCard(1).getRank() == a.getCard(2).getRank()
				&& a.getCard(2).getRank() == a.getCard(3).getRank() && a.getCard(3).getRank() != a.getCard(4).getRank())
			return true;
		return false;
	}

	/**
	 * a method for returning a string specifying the type of this hand
	 * 
	 * @return a String object "Quad"
	 */
	public String getType() {
		return new String("Quad");
	}

}