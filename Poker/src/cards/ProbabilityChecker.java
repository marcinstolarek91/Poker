package cards;

import java.util.ArrayList;
import java.util.List;

public abstract class ProbabilityChecker {
	private static final int CARD_SUM = 52;
	
	/**
	 * Check if cards are OK to use them in another methods
	 * @param cards - all known cards - it has to be a list with at least ownCards (has to contains them)
	 * @param ownCards - only player cards - it has to be a list of two cards
	 * @return true if parameters are as in description
	 * false otherwise
	 */
	private static boolean cardsAreOK(List<Card> cards, List<Card> ownCards) {
		if (cards.size() < 2 || cards.size() > 7 || ownCards.size() != 2 || !cards.containsAll(ownCards))
			return false;
		return true;
	}
	
	/**
	 * Check if player already has figure, or there is better figure on the table
	 * @param cards - all known cards - it has to be a list with at least ownCards (has to contains them)
	 * @param ownCards - only player cards - it has to be a list of two cards
	 * @param type - figure to check (example FLUSH, POKER)
	 * @return 0 if doesn't have that figure with own cards (and better on table)
	 * 1 if have exactly that figure
	 * 2 if has at least that figure (including the table only)
	 */
	private static int alreadyHasFigure(List<Card> cards, List<Card> ownCards, PokerHandsType type) {
		List<Card> figure = new ArrayList<>();
		figure = HandChecker.getOnlyFigure(cards);
		if (HandChecker.getFigureType(figure).ordinal() == type.ordinal()) {
			if (figure.contains(ownCards.get(0)) || figure.contains(ownCards.get(1)))
				return 1; // already has that figure
		}
		else if (HandChecker.getFigureType(figure).ordinal() >= type.ordinal())
			return 2;
		return 0;
	}
	
	/**
	 * Get probability when to get figure only one card is needed
	 * @param steps - how many steps to end
	 * @param knownNumbers - how many cards cards have been known already
	 * @param goodNumbers - how many cards allow to get figure (they are good cards)
	 * @return probability (0.00 - 1.00) to get one good card that is needed
	 */
	private static float getProbabilityOneCard(int steps, int knownNumbers, int goodNumbers) {
		float temp1 = 0, temp2 = 0;
		int allNumbers = CARD_SUM - knownNumbers;
		if (goodNumbers >= allNumbers)
			return 1.0F;
		if (steps <= 0 )
			return 0.0F;
		else if (steps == 1) {
			return (float) goodNumbers / (float) allNumbers;
		}
		else { //steps > 1
			temp1 = 1.0F - ((float) goodNumbers / (float) allNumbers); // chance to get card in this step (1 - chance)
			temp2 = 1.0F - getProbabilityOneCard(steps - 1, knownNumbers + 1, goodNumbers); // chance to get card in next steps (1 - chance)
			return (1.0F - temp1 * temp2);
		}
	}
	
	/**
	 * Get probability when to get figure two cards are needed
	 * @param steps - how many steps to end
	 * @param knownNumbers - how many cards cards have been known already
	 * @param goodNumbers1 - how many cards allow to get figure as first card
	 * @param goodNumbers2 - how many cards allow to get figure as second card (have to be less than goodNumbers1)
	 * @return probability (0.00 - 1.00) to get exact two cards that are needed
	 */
	private static float getProbabilityTwoCards(int steps, int knownNumbers, int goodNumbers1, int goodNumbers2) {
		float temp1 = 0, temp2 = 0;
		int allNumbers = CARD_SUM - knownNumbers;
		if (goodNumbers2 >= goodNumbers1)
			return 0.0F;
		if (goodNumbers1 >= allNumbers)
			return 1.0F;
		if (steps <= 1 )
			return 0.0F;
		else if (steps == 2) {
			return ((float) goodNumbers1 / (float) allNumbers) * ((float) goodNumbers2 / (float) allNumbers);
		}
		else { //steps > 2
			temp1 = 1.0F - ((float) goodNumbers1 / (float) allNumbers) * getProbabilityOneCard(steps - 1, knownNumbers + 1, goodNumbers2); // chance to get first card in this step and next card in one of next steps (1 - chance)
			temp2 = 1.0F - getProbabilityTwoCards(steps - 1, knownNumbers + 1, goodNumbers1, goodNumbers2); // chance to get first card in next steps and then next card in one of next steps (1 - chance)
			return (1.0F - temp1 * temp2);
		}
	}
	
	/**
	 * Get probability when to get figure three cards are needed
	 * @param steps - how many steps to end
	 * @param knownNumbers - how many cards cards have been known already
	 * @param goodNumbers1 - how many cards allow to get figure as first card
	 * @param goodNumbers2 - how many cards allow to get figure as second card (have to be less than goodNumbers1)
	 * @param goodNumbers3 - how many cards allow to get figure as third card (have to be less than goodNumbers2)
	 * @return probability (0.00 - 1.00) to get exact three cards that are needed
	 */
	private static float getProbabilityThreeCards(int steps, int knownNumbers, int goodNumbers1, int goodNumbers2, int goodNumbers3) {
		float temp1 = 0, temp2 = 0;
		int allNumbers = CARD_SUM - knownNumbers;
		if (goodNumbers2 >= goodNumbers1)
			return 0.0F;
		if (goodNumbers3 >= goodNumbers2)
			return 0.0F;
		if (goodNumbers1 >= allNumbers)
			return 1.0F;
		if (steps <= 2 )
			return 0.0F;
		else if (steps == 3) {
			return ((float) goodNumbers1 / (float) allNumbers) * ((float) goodNumbers2 / (float) allNumbers) * ((float) goodNumbers3 / (float) allNumbers);
		}
		else { //steps > 3
			temp1 = 1.0F - ((float) goodNumbers1 / (float) allNumbers) * getProbabilityTwoCards(steps - 1, knownNumbers + 1, goodNumbers2, goodNumbers3); // chance to get first card in this step and next cards in two of next steps (1 - chance)
			temp2 = 1.0F - getProbabilityThreeCards(steps - 1, knownNumbers + 1, goodNumbers1, goodNumbers2, goodNumbers3); // chance to get first card in next steps and then next cards in two of next steps (1 - chance)
			return (1.0F - temp1 * temp2);
		}
	}
	
	/**
	 * Check probability to get a pair with at least one own card
	 * @param cards - all known cards - it has to be a list with at least ownCards (has to contains them)
	 * @param ownCards - only player cards - it has to be a list of two cards
	 * @param cardsToSeeNumber - how many cards will appear on the table
	 * @return probability (0.00 - 1.00) to have a pair with own card
	 * 100F if already has own pair
	 * 0F if there isn't two ownCards, cards don't contain them, there are more than seven cards (impossible)
	 * 0F if player has better hand (including the table only)
	 */
	public static float checkChanceToOwnPair(List<Card> cards, List<Card> ownCards, int cardsToSeeNumber) {
		if (!cardsAreOK(cards, ownCards))
			return 0.0F;
		switch (alreadyHasFigure(cards, ownCards, PokerHandsType.PAIR)) {
			case 1: return 1.0F; // already has an own pair
			case 2: return 0.0F;
			default: break;
		}
		return checkChanceToOwnPair(cardsToSeeNumber, cards.size());
	}
	
	/**
	 * Check probability to get a pair with at least one own card
	 * @param cardsToSeeNumber - how many cards will appear on the table
	 * @param knownNumbers - how many cards have been known yet
	 * @return probability (0.00 - 1.00) to have a pair with own card
	 * there is assumption that player doesn't have a pair with own card yet (has two others face cards)
	 */
	public static float checkChanceToOwnPair(int cardsToSeeNumber, int knownNumbers) {
		return getProbabilityOneCard(cardsToSeeNumber, knownNumbers, 6); // exact six cards can give the player a pair
	}
	
	/**
	 * Check probability to get a three with at least one own card
	 * @param cards - all known cards - it has to be a list with at least ownCards (has to contains them)
	 * @param ownCards - only player cards - it has to be a list of two cards
	 * @param cardsToSeeNumber - how many cards will appear on the table
	 * @return probability (0.00 - 1.00) to have a three with own card
	 */
	public static float checkChanceToOwnThree(List<Card> cards, List<Card> ownCards, int cardsToSeeNumber) {
		if (!cardsAreOK(cards, ownCards))
			return 0.0F;
		switch (alreadyHasFigure(cards, ownCards, PokerHandsType.THREE)) {
			case 1: return 1.0F; // already has an own three
			case 2: return 0.0F;
			default: break;
		}
		if (HandChecker.getFigureType(ownCards) == PokerHandsType.PAIR) // has own pair
			return getProbabilityOneCard(cardsToSeeNumber, cards.size(), 2); // exact two cards can give the player a three (no matter in which step)
		if (HandChecker.hasOwnTwoPairsEachPair(cards, ownCards)) // has two pair - each pair: one card on the table, one in hand
			return getProbabilityOneCard(cardsToSeeNumber, cards.size(), 2);
		if (HandChecker.hasOwnPair(cards, ownCards)) // has pair - one card on the table
			return getProbabilityOneCard(cardsToSeeNumber, cards.size(), 2) + getProbabilityTwoCards(cardsToSeeNumber, cards.size(), 3, 2);
		return 2.0F * getProbabilityTwoCards(cardsToSeeNumber, cards.size(), 3, 2);
	}
	
	/**
	 * Check probability to get a four with at least one own card
	 * @param cards - all known cards - it has to be a list with at least ownCards (has to contains them)
	 * @param ownCards - only player cards - it has to be a list of two cards
	 * @param cardsToSeeNumber - how many cards will appear on the table
	 * @return probability (0.00 - 1.00) to have a four with own card
	 */
	public static float checkChanceToOwnFour(List<Card> cards, List<Card> ownCards, int cardsToSeeNumber) {
		if (!cardsAreOK(cards, ownCards))
			return 0.0F;
		switch (alreadyHasFigure(cards, ownCards, PokerHandsType.FOUR)) {
			case 1: return 1.0F; // already has an own three
			case 2: return 0.0F;
			default: break;
		}
		if (HandChecker.getFigureType(ownCards) == PokerHandsType.PAIR) { // has own pair
			if (alreadyHasFigure(cards, ownCards, PokerHandsType.THREE) >= 1 && HandChecker.hasThree(cards)) // one card to four is on the table
				return getProbabilityOneCard(cardsToSeeNumber, cards.size(), 1);
			return getProbabilityTwoCards(cardsToSeeNumber, cards.size(), 2, 1); // exact two cards can give the player a three (no matter in which step)
		}
		if (alreadyHasFigure(cards, ownCards, PokerHandsType.FULL_HOUSE) >= 1 && HandChecker.hasFullHouse(cards)) // two card to four is on the table, but there is possibility to get four with second card in players hand (one card on the table)
			return getProbabilityOneCard(cardsToSeeNumber, cards.size(), 1) + getProbabilityTwoCards(cardsToSeeNumber, cards.size(), 2, 1);
		if (alreadyHasFigure(cards, ownCards, PokerHandsType.THREE) >= 1 && HandChecker.hasThree(cards)) // two card to four is on the table, but there is possibility to get four with second card in players hand
			return getProbabilityOneCard(cardsToSeeNumber, cards.size(), 1) + getProbabilityThreeCards(cardsToSeeNumber, cards.size(), 3, 2, 1);
		if (alreadyHasFigure(cards, ownCards, PokerHandsType.PAIR) >= 1 && HandChecker.hasPair(cards)) {// a pair with at least one card from players hand
			if (alreadyHasFigure(cards, ownCards, PokerHandsType.TWO_PAIRS) >= 1 && HandChecker.hasTwoPairs(cards)) // player has two pairs
				return 2.0F * getProbabilityTwoCards(cardsToSeeNumber, cards.size(), 2, 1);
			return getProbabilityTwoCards(cardsToSeeNumber, cards.size(), 2, 1) + getProbabilityThreeCards(cardsToSeeNumber, cards.size(), 3, 2, 1);
		}
		return 3.0F * getProbabilityThreeCards(cardsToSeeNumber, cards.size(), 3, 2, 1);
	}
}
