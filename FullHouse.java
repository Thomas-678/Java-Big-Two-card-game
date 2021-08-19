/**
 * Hand Description: This hand consists of five cards, with two having the same
 * rank and three having another same rank. The card in the triplet with the
 * highest suit in a full house is referred to as the top card of this full
 * house. A full house always beats any straights and flushes. A full house
 * having a top card with a higher rank beats a full house having a top card
 * with a lower rank.
 * 
 * @author Thomas Ho
 * @version 1.0
 */
public class FullHouse extends Hand {
	/**
	 * Pass arguments to superclass' constructor
	 * 
	 * @param player the player this hand belongs to
	 * @param cards  the cards in this hand
	 * @see Hand#Hand(CardGamePlayer player, CardList cards)
	 */
	public FullHouse(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}

	/**
	 * a method for checking if this is a valid hand
	 * 
	 * @return true if this hand is FullHouse, false otherwise
	 */
	public boolean isValid() {
		if (this.size() != 5)
			return false;
		CardList a = new CardList();
		for (int i = 0; i < 5; ++i)
			a.addCard(getCard(i));
		a = Hand.toCard(a);
		a.sort();
		if (a.getCard(0).getRank() == a.getCard(1).getRank() && a.getCard(2).getRank() == a.getCard(3).getRank()
				&& a.getCard(3).getRank() == a.getCard(4).getRank() && a.getCard(1).getRank() != a.getCard(2).getRank())
			return true;
		if (a.getCard(0).getRank() == a.getCard(1).getRank() && a.getCard(1).getRank() == a.getCard(2).getRank()
				&& a.getCard(3).getRank() == a.getCard(4).getRank() && a.getCard(2).getRank() != a.getCard(3).getRank())
			return true;
		return false;
	}

	/**
	 * a method for returning a string specifying the type of this hand
	 * 
	 * @return a String object "FullHouse"
	 */
	public String getType() {
		return new String("FullHouse");
	}

}