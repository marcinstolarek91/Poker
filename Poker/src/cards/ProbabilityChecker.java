package cards;

import java.util.ArrayList;
import java.util.List;

public abstract class ProbabilityChecker {
	private static final int CARD_SUM = 52;
	
	/**
	 * Check if cards are OK to use them in another methods
	 * @param cards - all known cards - it has to be a list with at least ownCards (has to contains them); cards size have to be 2, 5 or 6
	 * @param ownCards - only player cards - it has to be a list of two cards
	 * @return true if parameters are as in description
	 * false otherwise
	 */
	private static boolean cardsAreOK(List<Card> cards, List<Card> ownCards) {
		if ((cards.size() != 2 && cards.size() != 5 && cards.size() != 6) || ownCards.size() != 2 || !cards.containsAll(ownCards))
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
		if (HandChecker.getFigureType(figure).ordinal() >= type.ordinal())
			return 2;
		return 0;
	}
	
	/**
	 * Check if all goodNumbers are positive
	 * @param number1 - first goodNumber
	 * @param numbers - others goodNumbers
	 * @return true if all goodNumbers are positive
	 */
	private static boolean goodNumbersPositive(int number1, int ... numbers) {
		if (number1 <= 0)
			return false;
		for (int i = 0; i < numbers.length; i++) {
			if (numbers[i] <= 0)
				return false;
		}
		return true;
	}
	
	private static float calculateProbability(List<Float> positive, List<Float> negative) {
		float probability = 0.0F;
		for (int i = 0; i < positive.size(); i++) {
			if (i == 0)
				probability = (1.0F - positive.get(i).floatValue());
			else
				probability *= (1.0F - positive.get(i).floatValue());
		}
		if (probability > 0.0F)
			probability = 1.0F - probability;
		for (Float f : negative)
				probability *= (1.0F - f.floatValue());
		return probability;
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
		if (!goodNumbersPositive(goodNumbers))
			return 0.0F;
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
		if (!goodNumbersPositive(goodNumbers1, goodNumbers2))
			return 0.0F;
		if (goodNumbers2 >= goodNumbers1)
			return 0.0F;
		if (goodNumbers1 >= allNumbers)
			return 1.0F;
		if (steps <= 1 )
			return 0.0F;
		else if (steps == 2) {
			return ((float) goodNumbers1 / (float) allNumbers) * ((float) goodNumbers2 / (float) (allNumbers - 1));
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
		if (!goodNumbersPositive(goodNumbers1, goodNumbers2, goodNumbers3))
			return 0.0F;
		if (goodNumbers2 >= goodNumbers1)
			return 0.0F;
		if (goodNumbers3 >= goodNumbers2)
			return 0.0F;
		if (goodNumbers1 >= allNumbers)
			return 1.0F;
		if (steps <= 2 )
			return 0.0F;
		else if (steps == 3) {
			return ((float) goodNumbers1 / (float) allNumbers) * ((float) goodNumbers2 / (float) (allNumbers - 1)) * ((float) goodNumbers3 / (float) (allNumbers - 2));
		}
		else { //steps > 3
			temp1 = 1.0F - ((float) goodNumbers1 / (float) allNumbers) * getProbabilityTwoCards(steps - 1, knownNumbers + 1, goodNumbers2, goodNumbers3); // chance to get first card in this step and next cards in next steps (1 - chance)
			temp2 = 1.0F - getProbabilityThreeCards(steps - 1, knownNumbers + 1, goodNumbers1, goodNumbers2, goodNumbers3); // chance to get first card in next steps and then next cards in next steps (1 - chance)
			return (1.0F - temp1 * temp2);
		}
	}
	
	/**
	 * Get probability when to get figure four cards are needed
	 * @param steps - how many steps to end
	 * @param knownNumbers - how many cards cards have been known already
	 * @param goodNumbers1 - how many cards allow to get figure as first card
	 * @param goodNumbers2 - how many cards allow to get figure as second card (have to be less than goodNumbers1)
	 * @param goodNumbers3 - how many cards allow to get figure as third card (have to be less than goodNumbers2)
	 * @param goodNumbers4 - how many cards allow to get figure as fourth card (have to be less than goodNumbers3)
	 * @return probability (0.00 - 1.00) to get exact four cards that are needed
	 */
	private static float getProbabilityFourCards(int steps, int knownNumbers, int goodNumbers1, int goodNumbers2, int goodNumbers3, int goodNumbers4) {
		float temp1 = 0, temp2 = 0;
		int allNumbers = CARD_SUM - knownNumbers;
		if (!goodNumbersPositive(goodNumbers1, goodNumbers2, goodNumbers3, goodNumbers4))
			return 0.0F;
		if (goodNumbers2 >= goodNumbers1)
			return 0.0F;
		if (goodNumbers3 >= goodNumbers2)
			return 0.0F;
		if (goodNumbers4 >= goodNumbers3)
			return 0.0F;
		if (goodNumbers1 >= allNumbers)
			return 1.0F;
		if (steps <= 3 )
			return 0.0F;
		else if (steps == 4) {
			return ((float) goodNumbers1 / (float) allNumbers) * ((float) goodNumbers2 / (float) (allNumbers - 1)) * ((float) goodNumbers3 / (float) (allNumbers - 2)) * ((float) goodNumbers4 / (float) (allNumbers - 3));
		}
		else { //steps > 4
			temp1 = 1.0F - ((float) goodNumbers1 / (float) allNumbers) * getProbabilityThreeCards(steps - 1, knownNumbers + 1, goodNumbers2, goodNumbers3, goodNumbers4); // chance to get first card in this step and next cards in next steps (1 - chance)
			temp2 = 1.0F - getProbabilityFourCards(steps - 1, knownNumbers + 1, goodNumbers1, goodNumbers2, goodNumbers3, goodNumbers4); // chance to get first card in next steps and then next cards in next steps (1 - chance)
			return (1.0F - temp1 * temp2);
		}
	}
	
	/**
	 * Get probability when to get figure five cards are needed
	 * @param steps - how many steps to end
	 * @param knownNumbers - how many cards cards have been known already
	 * @param goodNumbers1 - how many cards allow to get figure as first card
	 * @param goodNumbers2 - how many cards allow to get figure as second card (have to be less than goodNumbers1)
	 * @param goodNumbers3 - how many cards allow to get figure as third card (have to be less than goodNumbers2)
	 * @param goodNumbers4 - how many cards allow to get figure as fourth card (have to be less than goodNumbers3)
	 * @param goodNumbers5 - how many cards allow to get figure as fifth card (have to be less than goodNumbers4)
	 * @return probability (0.00 - 1.00) to get exact five cards that are needed
	 */
	private static float getProbabilityFiveCards(int steps, int knownNumbers, int goodNumbers1, int goodNumbers2, int goodNumbers3, int goodNumbers4, int goodNumbers5) {
		float temp1 = 0, temp2 = 0;
		int allNumbers = CARD_SUM - knownNumbers;
		if (!goodNumbersPositive(goodNumbers1, goodNumbers2, goodNumbers3, goodNumbers4, goodNumbers5))
			return 0.0F;
		if (goodNumbers2 >= goodNumbers1)
			return 0.0F;
		if (goodNumbers3 >= goodNumbers2)
			return 0.0F;
		if (goodNumbers4 >= goodNumbers3)
			return 0.0F;
		if (goodNumbers5 >= goodNumbers4)
			return 0.0F;
		if (goodNumbers1 >= allNumbers)
			return 1.0F;
		if (steps <= 4 )
			return 0.0F;
		else if (steps == 5) {
			return ((float) goodNumbers1 / (float) allNumbers) * ((float) goodNumbers2 / (float) (allNumbers - 1)) * ((float) goodNumbers3 / (float) (allNumbers - 2)) * ((float) goodNumbers4 / (float) (allNumbers - 3)) * ((float) goodNumbers5 / (float) (allNumbers - 4));
		}
		else { //steps > 5
			temp1 = 1.0F - ((float) goodNumbers1 / (float) allNumbers) * getProbabilityFourCards(steps - 1, knownNumbers + 1, goodNumbers2, goodNumbers3, goodNumbers4, goodNumbers5); // chance to get first card in this step and next cards in next steps (1 - chance)
			temp2 = 1.0F - getProbabilityFiveCards(steps - 1, knownNumbers + 1, goodNumbers1, goodNumbers2, goodNumbers3, goodNumbers4, goodNumbers5); // chance to get first card in next steps and then next cards in next steps (1 - chance)
			return (1.0F - temp1 * temp2);
		}
	}
	
	/**
	 * Check probability to get a pair with at least one own card
	 * @param cards - all known cards - it has to be a list with at least ownCards (has to contains them)
	 * @param ownCards - only player cards - it has to be a list of two cards
	 * @return probability (0.00 - 1.00) to have a pair with own card
	 * 100F if already has own pair
	 * 0F if there isn't two ownCards, cards don't contain them, there are more than seven cards (impossible)
	 * 0F if player has better hand (including the table only)
	 */
	public static float checkChanceToOwnPair(List<Card> cards, List<Card> ownCards) {
		int cardsToSeeNumber = 7 - cards.size();
		if (!cardsAreOK(cards, ownCards))
			return 0.0F;
		switch (alreadyHasFigure(cards, ownCards, PokerHandsType.PAIR)) {
			case 1: return 1.0F; // already has an own pair
			case 2: return 0.0F;
			default: break;
		}
		return getProbabilityOneCard(cardsToSeeNumber, cards.size(), 6); // exact six cards can give the player a pair
	}
	
	//TODO - dokonczyc
	public static float checkChanceToOwnTwoPairs(List<Card> cards, List<Card> ownCards) {
		List<Float> probabilityListPositive = new ArrayList<>();
		List<Float> probabilityListNegative = new ArrayList<>();
		int cardsToSeeNumber = 7 - cards.size();
		int cardsOnTable = cards.size() - 2;
		int higherThanOwnOnTable = 0;
		int ownPairFigure;
		int usedCards = 0; // cards on table in pair with own card
		float temp1, temp2, temp3, temp4;
		if (!cardsAreOK(cards, ownCards))
			return 0.0F;
		switch (alreadyHasFigure(cards, ownCards, PokerHandsType.TWO_PAIRS)) {
			case 1: return 1.0F; // already has an own two pairs
			case 2: return 0.0F;
			default: break;
		}
		if (HandChecker.getFigureType(ownCards) == PokerHandsType.PAIR) {// has own pair in hand
			for (Card card : cards) {
				if (card.faceCard.ordinal() > ownCards.get(0).faceCard.ordinal())
					++higherThanOwnOnTable;
			}
		}
		else { // hasn't pair in hand
			usedCards = 1;
			if (HandChecker.hasOwnPair(cards, ownCards)) { // has pair from one own card - looking for new pair
				if (HandChecker.getPair(cards).contains(ownCards.get(0)))
					ownPairFigure = ownCards.get(0).faceCard.ordinal();
				else
					ownPairFigure = ownCards.get(1).faceCard.ordinal();
				for (Card card : cards) {
					if (card.faceCard.ordinal() > ownPairFigure)
						++higherThanOwnOnTable;
				}
			}
			else { // hasn't any pair
				for (Card card : cards) {
					if (card.faceCard.ordinal() > (ownCards.get(0).faceCard.ordinal() + ownCards.get(1).faceCard.ordinal()) / 2) // averange card
						++higherThanOwnOnTable;
				}
			}
		}
		if (HandChecker.getFigureType(ownCards) == PokerHandsType.PAIR || HandChecker.hasOwnPair(cards, ownCards)) {
			if (cardsOnTable - usedCards > 0) { // pair from one card on table and one new
				temp1 = getProbabilityOneCard(cardsToSeeNumber, cards.size(), 3);
				temp2 = (float) (higherThanOwnOnTable / (cardsOnTable - usedCards)); // second par (one card to pair is now on the table) is greater than own - chance in %
				temp3 = temp2;
				temp2 *= getProbabilityOneCard(cardsToSeeNumber - 1, cards.size() + 1, (higherThanOwnOnTable - 1) * 3); // probability to get third (greater than own) pair - from cards on table
				temp3 *= (float) ((CARD_SUM / 4) - (ownCards.get(0).faceCard.ordinal() + 1) - higherThanOwnOnTable) * getProbabilityTwoCards(cardsToSeeNumber - 1, cards.size() + 1, 4, 3); // probability to get third (greater than own) pair - from new cards
				probabilityListPositive.add(new Float((float)(cardsOnTable - usedCards) * (temp1 * (1.0F - temp2) * (1.0F - temp3)))); // pair from two new cards
			}
			temp1 = getProbabilityTwoCards(cardsToSeeNumber, cards.size(), 4, 3);
			temp2 = (float) ((CARD_SUM / 4) - (ownCards.get(0).faceCard.ordinal() + 1) - higherThanOwnOnTable) / ((CARD_SUM / 4) - 1); // second par (both cards to pair is now on the table) is greater than own - chance in %
			temp3 = temp2;
			temp2 *= getProbabilityOneCard(cardsToSeeNumber - 2, cards.size() + 2, higherThanOwnOnTable * 3); // probability to get third (greater than own) pair - from cards on table
			temp3 *= (float) ((CARD_SUM / 4) - (ownCards.get(0).faceCard.ordinal() + 1) - higherThanOwnOnTable) * getProbabilityTwoCards(cardsToSeeNumber - 2, cards.size() + 2, 4, 3); // probability to get third (greater than own) pair - from new cards
			probabilityListPositive.add(new Float((float)((CARD_SUM / 4) - 1 - (cardsOnTable - usedCards) - 1) * (temp1 * (1.0F - temp2) * (1.0F - temp3))));
		}
		if (HandChecker.getFigureType(ownCards) != PokerHandsType.PAIR && HandChecker.hasOwnPair(cards, ownCards)) // pair with second own card
			probabilityListPositive.add(new Float(getProbabilityOneCard(cardsToSeeNumber, cards.size(), 3)));
		else if (cardsToSeeNumber >= 2 && HandChecker.getFigureType(ownCards) != PokerHandsType.PAIR && !HandChecker.hasOwnPair(cards, ownCards)) { // has no pair yet
			temp1 = getProbabilityOneCard(cardsToSeeNumber, cards.size(), 6); // get pair with any own card
			temp2 = getProbabilityOneCard(cardsToSeeNumber - 1, cards.size() + 1, 3); // get pair with second card
			probabilityListPositive.add(new Float(temp1 * temp2));
			temp1 = temp1 * (1.0F - temp2); // probability to get only one pair with own cards
			if (cardsOnTable - usedCards > 0) { // pair from one card on table and one new
				temp2 = getProbabilityOneCard(cardsToSeeNumber - 1, cards.size() + 1, 3);
				temp3 = (float) (higherThanOwnOnTable / (cardsOnTable - usedCards)); // second par (one card to pair is now on the table) is greater than own - chance in %
				temp4 = temp2;
				temp3 *= getProbabilityOneCard(cardsToSeeNumber - 2, cards.size() + 2, (higherThanOwnOnTable - 1) * 3); // probability to get third (greater than own) pair - from cards on table
				temp4 *= (float) ((CARD_SUM / 4) - (ownCards.get(0).faceCard.ordinal() + 1) - higherThanOwnOnTable) * getProbabilityTwoCards(cardsToSeeNumber - 2, cards.size() + 2, 4, 3); // probability to get third (greater than own) pair - from new cards
				probabilityListPositive.add(new Float((float)(cardsOnTable - usedCards) * (temp1 * temp2 * (1.0F - temp3) * (1.0F - temp4)))); // pair from two new cards
			}
			temp2 = getProbabilityTwoCards(cardsToSeeNumber - 1, cards.size() + 1, 4, 3);
			temp3 = (float) ((CARD_SUM / 4) - (ownCards.get(0).faceCard.ordinal() + 1) - higherThanOwnOnTable) / ((CARD_SUM / 4) - 1); // second par (both cards to pair is now on the table) is greater than own - chance in %
			temp4 = temp2;
			temp3 *= getProbabilityOneCard(cardsToSeeNumber - 3, cards.size() + 3, higherThanOwnOnTable * 3); // probability to get third (greater than own) pair - from cards on table
			temp4 *= (float) ((CARD_SUM / 4) - (ownCards.get(0).faceCard.ordinal() + 1) - higherThanOwnOnTable) * getProbabilityTwoCards(cardsToSeeNumber - 3, cards.size() + 3, 4, 3); // probability to get third (greater than own) pair - from new cards
			probabilityListPositive.add(new Float((float)((CARD_SUM / 4) - 1 - (cardsOnTable - usedCards) - 1) * (temp1 * temp2 * (1.0F - temp3) * (1.0F - temp4))));
		}
		return calculateProbability(probabilityListPositive, probabilityListNegative);
	}
	
	/**
	 * Check probability to get a three with at least one own card
	 * @param cards - all known cards - it has to be a list with at least ownCards (has to contains them)
	 * @param ownCards - only player cards - it has to be a list of two cards
	 * @return probability (0.00 - 1.00) to have a three with own card
	 * 100F if already has own three
	 * 0F if there isn't two ownCards, cards don't contain them, there are more than seven cards (impossible)
	 * 0F if player has better hand (including the table only)
	 */
	public static float checkChanceToOwnThree(List<Card> cards, List<Card> ownCards) {
		int cardsToSeeNumber = 7 - cards.size();
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
	 * Check probability to get a straight with at least one own card
	 * @param cards - all known cards - it has to be a list with at least ownCards (has to contains them)
	 * @param ownCards - only player cards - it has to be a list of two cards
	 * @return probability (0.00 - 1.00) to have a straight with own card
	 * 100F if already has own straight
	 * 0F if there isn't two ownCards, cards don't contain them, there are more than seven cards (impossible)
	 * 0F if player has better hand (including the table only)
	 */
	public static float checkChanceToOwnStraight(List<Card> cards, List<Card> ownCards) {
		List<Float> probabilityListPositive = new ArrayList<>();
		List<Float> probabilityListNegative = new ArrayList<>();
		List<List<Integer>> combinationChecked = new ArrayList<>(); // the same combination has been already checked
		List<Integer> actualCombination = new ArrayList<>();
		int cardsToSeeNumber = 7 - cards.size();
		int temp;
		int highestOwnCard;
		boolean isOwnCardInRange;
		boolean[] figurePresent = new boolean[13]; // is that figure in cards
		boolean[] ownFigurePresent = new boolean[13]; // is that figure in ownCards
		if (!cardsAreOK(cards, ownCards))
			return 0.0F;
		switch (alreadyHasFigure(cards, ownCards, PokerHandsType.STRAIGHT)) {
			case 1: return 1.0F; // already has an own flush
			case 2: return 0.0F;
			default: break;
		}
		for (int i = 0; i < 13; i++) {
			if (cards.contains(new Card(i * 4)) || cards.contains(new Card(i * 4 + 1)) || cards.contains(new Card(i * 4 + 2)) || cards.contains(new Card(i * 4 + 3)))
				figurePresent[i] = true;
			else
				figurePresent[i] = false;
			if (ownCards.contains(new Card(i * 4)) || ownCards.contains(new Card(i * 4 + 1)) || ownCards.contains(new Card(i * 4 + 2)) || ownCards.contains(new Card(i * 4 + 3)))
				ownFigurePresent[i] = true;
			else
				ownFigurePresent[i] = false;
		}
		for (int i = 0; i < 9; i++) {
			temp = 5;
			isOwnCardInRange = false;
			for (int j = i; j < i + 5; j++) {
				if (ownFigurePresent[j])
					isOwnCardInRange = true;
				if (figurePresent[j])
					--temp;
				else
					actualCombination.add(new Integer(j));
			}
			if (isOwnCardInRange && !combinationChecked.contains(actualCombination)) {
				combinationChecked.add(actualCombination);
				switch (temp) {
					case 4: probabilityListPositive.add(new Float(getProbabilityFourCards(cardsToSeeNumber, cards.size(), 16, 12, 8, 4))); break;
					case 3: probabilityListPositive.add(new Float(getProbabilityThreeCards(cardsToSeeNumber, cards.size(), 12, 8, 4))); break;
					case 2: probabilityListPositive.add(new Float(getProbabilityTwoCards(cardsToSeeNumber, cards.size(), 8, 4))); break;
					case 1: probabilityListPositive.add(new Float(getProbabilityOneCard(cardsToSeeNumber, cards.size(), 4))); break;
					default: break;
				}
			}
			actualCombination = new ArrayList<>();
		}
		// ACE - TWO - THREE - FOUR - FIVE straight
		temp = 5;
		isOwnCardInRange = false;
		for (int j = 0; j < 4; j++) {
			if (ownFigurePresent[j])
				isOwnCardInRange = true;
			if (figurePresent[j])
				--temp;
			else
				actualCombination.add(new Integer(j));
		}
		if (ownFigurePresent[12])
			isOwnCardInRange = true;
		if (figurePresent[12])
			--temp;
		else
			actualCombination.add(new Integer(12));
		if (isOwnCardInRange && !combinationChecked.contains(actualCombination)) {
			combinationChecked.add(actualCombination);
			switch (temp) {
				case 4: probabilityListPositive.add(new Float(getProbabilityFourCards(cardsToSeeNumber, cards.size(), 16, 12, 8, 4))); break;
				case 3: probabilityListPositive.add(new Float(getProbabilityThreeCards(cardsToSeeNumber, cards.size(), 12, 8, 4))); break;
				case 2: probabilityListPositive.add(new Float(getProbabilityTwoCards(cardsToSeeNumber, cards.size(), 8, 4))); break;
				case 1: probabilityListPositive.add(new Float(getProbabilityOneCard(cardsToSeeNumber, cards.size(), 4))); break;
				default: break;
			}
		}
		actualCombination = new ArrayList<>();
		combinationChecked.clear();
		highestOwnCard = (ownCards.get(0).faceCard.ordinal() >= ownCards.get(1).faceCard.ordinal()) ? ownCards.get(0).faceCard.ordinal() : ownCards.get(1).faceCard.ordinal();
		if (highestOwnCard <= 7) { // NINE or lower card - case when there are five higher card on table
			for (int i = highestOwnCard + 1; i < 9; i++) {
				temp = 5;
				for (int j = i; j < i + 5; j++) {
					if (figurePresent[j])
						--temp;
					else
						actualCombination.add(new Integer(j));
				}
				if (!combinationChecked.contains(actualCombination)) {
					combinationChecked.add(actualCombination);
					switch (temp) {
						case 4: probabilityListNegative.add(new Float(getProbabilityFourCards(cardsToSeeNumber, cards.size(), 16, 12, 8, 4))); break;
						case 3: probabilityListNegative.add(new Float(getProbabilityThreeCards(cardsToSeeNumber, cards.size(), 12, 8, 4))); break;
						case 2: probabilityListNegative.add(new Float(getProbabilityTwoCards(cardsToSeeNumber, cards.size(), 8, 4))); break;
						case 1: probabilityListNegative.add(new Float(getProbabilityOneCard(cardsToSeeNumber, cards.size(), 4))); break;
						default: break;
					}
				}
				actualCombination = new ArrayList<>();
			}
		}
		return calculateProbability(probabilityListPositive, probabilityListNegative);
	}
	
	/**
	 * Check probability to get a flush with at least one own card
	 * @param cards - all known cards - it has to be a list with at least ownCards (has to contains them)
	 * @param ownCards - only player cards - it has to be a list of two cards
	 * @return probability (0.00 - 1.00) to have a flush with own card
	 * 100F if already has own flush
	 * 0F if there isn't two ownCards, cards don't contain them, there are more than seven cards (impossible)
	 * 0F if player has better hand (including the table only)
	 */
	public static float checkChanceToOwnFlush(List<Card> cards, List<Card> ownCards) {
		int cardsToSeeNumber = 7 - cards.size();
		int[] color = {0, 0, 0, 0}; // number of cards in specified color
		int highestOwnCard;
		int higherCardNumber;
		int highestPossibleCard = (CARD_SUM / 4) - 1;
		List<Float> probabilityListPositive = new ArrayList<>();
		List<Float> probabilityListNegative = new ArrayList<>();
		if (!cardsAreOK(cards, ownCards))
			return 0.0F;
		switch (alreadyHasFigure(cards, ownCards, PokerHandsType.FLUSH)) {
			case 1: return 1.0F; // already has an own flush
			case 2: return 0.0F;
			default: break;
		}
		for (Card card : cards)
			++color[card.cardColor.ordinal()];
		if (cardsToSeeNumber < (5 - color[ownCards.get(0).cardColor.ordinal()]) && cardsToSeeNumber < (5 - color[ownCards.get(1).cardColor.ordinal()]))
			return 0.0F; // cannot get flush
		for (int i = 0; i < 2; i++) {
			higherCardNumber = 0;
			if (ownCards.get(0).cardColor.equals(ownCards.get(1).cardColor)) {
				if (ownCards.get(0).faceCard.ordinal() >= ownCards.get(1).faceCard.ordinal())
					highestOwnCard = ownCards.get(0).faceCard.ordinal();
				else
					highestOwnCard = ownCards.get(1).faceCard.ordinal();
			}
			else
				highestOwnCard = ownCards.get(i).faceCard.ordinal();
			for (Card card : cards) {
				if (!ownCards.contains(card) && card.faceCard.ordinal() > highestOwnCard)
					++higherCardNumber;
			}
			if (color[ownCards.get(i).cardColor.ordinal()] == 1)
				probabilityListPositive.add(new Float(getProbabilityFourCards(cardsToSeeNumber, cards.size(), 12, 11, 10, 9)));
			else if (color[ownCards.get(i).cardColor.ordinal()] == 2)
				probabilityListPositive.add(new Float(getProbabilityThreeCards(cardsToSeeNumber, cards.size(), 11, 10, 9)));
			else if (color[ownCards.get(i).cardColor.ordinal()] == 3)
				probabilityListPositive.add(new Float(getProbabilityTwoCards(cardsToSeeNumber, cards.size(), 10, 9)));
			else if (color[ownCards.get(i).cardColor.ordinal()] == 4)
				probabilityListPositive.add(new Float(getProbabilityOneCard(cardsToSeeNumber, cards.size(), 9)));
			if (highestOwnCard <= 7) { // NINE or lower card - case when there are five higher card on table
				if (higherCardNumber == 0)
					probabilityListNegative.add(new Float(getProbabilityFiveCards(cardsToSeeNumber, cards.size(), highestPossibleCard - highestOwnCard, highestPossibleCard - highestOwnCard - 1, highestPossibleCard - highestOwnCard - 2, highestPossibleCard - highestOwnCard - 3, highestPossibleCard - highestOwnCard - 4)));
				else if (higherCardNumber == 1)
					probabilityListNegative.add(new Float(getProbabilityFourCards(cardsToSeeNumber, cards.size(), highestPossibleCard - highestOwnCard - 1, highestPossibleCard - highestOwnCard - 2, highestPossibleCard - highestOwnCard - 3, highestPossibleCard - highestOwnCard - 4)));
				else if (higherCardNumber == 2)
					probabilityListNegative.add(new Float(getProbabilityThreeCards(cardsToSeeNumber, cards.size(), highestPossibleCard - highestOwnCard - 2, highestPossibleCard - highestOwnCard - 3, highestPossibleCard - highestOwnCard - 4)));
				else if (higherCardNumber == 3)
					probabilityListNegative.add(new Float(getProbabilityTwoCards(cardsToSeeNumber, cards.size(), highestPossibleCard - highestOwnCard - 3, highestPossibleCard - highestOwnCard - 4)));
				else if (higherCardNumber == 4)
					probabilityListNegative.add(new Float(getProbabilityOneCard(cardsToSeeNumber, cards.size(), highestPossibleCard - highestOwnCard - 4)));
			}
			if (ownCards.get(0).cardColor.equals(ownCards.get(1).cardColor)) // cards with the same color
				return calculateProbability(probabilityListPositive, probabilityListNegative);
		}
		return calculateProbability(probabilityListPositive, probabilityListNegative);
	}
	
	/**
	 * Check probability to get a four with at least one own card
	 * @param cards - all known cards - it has to be a list with at least ownCards (has to contains them)
	 * @param ownCards - only player cards - it has to be a list of two cards
	 * @return probability (0.00 - 1.00) to have a four with own card
	 */
	public static float checkChanceToOwnFour(List<Card> cards, List<Card> ownCards) {
		int cardsToSeeNumber = 7 - cards.size();
		if (!cardsAreOK(cards, ownCards))
			return 0.0F;
		switch (alreadyHasFigure(cards, ownCards, PokerHandsType.FOUR)) {
			case 1: return 1.0F; // already has an own three
			case 2: return 0.0F;
			default: break;
		}
		if (HandChecker.getFigureType(ownCards) == PokerHandsType.PAIR) { // has own pair
			if (HandChecker.hasOwnThree(cards, ownCards)) // one card to four is on the table
				return getProbabilityOneCard(cardsToSeeNumber, cards.size(), 1);
			return getProbabilityTwoCards(cardsToSeeNumber, cards.size(), 2, 1); // exact two cards can give the player a three (no matter in which step)
		}
		if (HandChecker.hasOwnFullHouse(cards, ownCards)) // two card to four is on the table, but there is possibility to get four with second card in players hand (one card on the table)
			return getProbabilityOneCard(cardsToSeeNumber, cards.size(), 1) + getProbabilityTwoCards(cardsToSeeNumber, cards.size(), 2, 1);
		if (HandChecker.hasOwnThree(cards, ownCards)) // two card to four is on the table, but there is possibility to get four with second card in players hand
			return getProbabilityOneCard(cardsToSeeNumber, cards.size(), 1) + getProbabilityThreeCards(cardsToSeeNumber, cards.size(), 3, 2, 1);
		if (HandChecker.hasOwnPair(cards, ownCards)) {// a pair with at least one card from players hand
			if (HandChecker.hasOwnTwoPairs(cards, ownCards)) // player has two pairs
				return 2.0F * getProbabilityTwoCards(cardsToSeeNumber, cards.size(), 2, 1);
			return getProbabilityTwoCards(cardsToSeeNumber, cards.size(), 2, 1) + getProbabilityThreeCards(cardsToSeeNumber, cards.size(), 3, 2, 1);
		}
		return 3.0F * getProbabilityThreeCards(cardsToSeeNumber, cards.size(), 3, 2, 1);
	}

	/**
	 * Check probability to get a poker with at least one own card
	 * @param cards - all known cards - it has to be a list with at least ownCards (has to contains them)
	 * @param ownCards - only player cards - it has to be a list of two cards
	 * @return probability (0.00 - 1.00) to have a poker with own card
	 * 100F if already has own poker
	 * 0F if there isn't two ownCards, cards don't contain them, there are more than seven cards (impossible)
	 * 0F if player has better hand (including the table only)
	 */
	public static float checkChanceToOwnPoker(List<Card> cards, List<Card> ownCards) {
		List<Float> probabilityListPositive = new ArrayList<>();
		List<Float> probabilityListNegative = new ArrayList<>();
		List<List<Integer>> combinationChecked = new ArrayList<>(); // the same combination has been already checked
		List<Integer> actualCombination = new ArrayList<>();
		int cardsToSeeNumber = 7 - cards.size();
		int[] color = {0, 0, 0, 0}; // number of cards in specified color
		int temp;
		int highestOwnCardColor;
		boolean isOwnCardInRange;
		boolean[] figurePresent = new boolean[13]; // is that figure in cards
		boolean[] ownFigurePresent = new boolean[13]; // is that figure in ownCards
		if (!cardsAreOK(cards, ownCards))
			return 0.0F;
		switch (alreadyHasFigure(cards, ownCards, PokerHandsType.POKER)) {
			case 1: return 1.0F; // already has an own flush
			case 2: return 0.0F;
			default: break;
		}
		for (Card card : cards)
			++color[card.cardColor.ordinal()];
		if (cardsToSeeNumber < (5 - color[ownCards.get(0).cardColor.ordinal()]) && cardsToSeeNumber < (5 - color[ownCards.get(1).cardColor.ordinal()]))
			return 0.0F; // cannot get poker
		for (int c = 0; c < 2; c++) {
			if (ownCards.get(0).cardColor.equals(ownCards.get(1).cardColor)) {// only one color
				++c;
				highestOwnCardColor = (ownCards.get(0).faceCard.ordinal() >= ownCards.get(1).faceCard.ordinal()) ? ownCards.get(0).faceCard.ordinal() : ownCards.get(1).faceCard.ordinal();
			}
			else
				highestOwnCardColor = ownCards.get(c).faceCard.ordinal();
			for (int i = 0; i < 13; i++) {
				if (cards.contains(new Card(i * 4 + ownCards.get(c).cardColor.ordinal())))
					figurePresent[i] = true;
				else
					figurePresent[i] = false;
				if (ownCards.contains(new Card(i * 4 + ownCards.get(c).cardColor.ordinal())))
					ownFigurePresent[i] = true;
				else
					ownFigurePresent[i] = false;
			}
			for (int i = 0; i < 9; i++) {
				temp = 5;
				isOwnCardInRange = false;
				for (int j = i; j < i + 5; j++) {
					if (ownFigurePresent[j])
						isOwnCardInRange = true;
					if (figurePresent[j])
						--temp;
					else
						actualCombination.add(new Integer(j));
				}
				if (isOwnCardInRange && !combinationChecked.contains(actualCombination)) {
					combinationChecked.add(actualCombination);
					switch (temp) {
						case 4: probabilityListPositive.add(new Float(getProbabilityFourCards(cardsToSeeNumber, cards.size(), 4, 3, 2, 1))); break;
						case 3: probabilityListPositive.add(new Float(getProbabilityThreeCards(cardsToSeeNumber, cards.size(), 3, 2, 1))); break;
						case 2: probabilityListPositive.add(new Float(getProbabilityTwoCards(cardsToSeeNumber, cards.size(), 2, 1))); break;
						case 1: probabilityListPositive.add(new Float(getProbabilityOneCard(cardsToSeeNumber, cards.size(), 1))); break;
						default: break;
					}
				}
				actualCombination = new ArrayList<>();
			}
			// ACE - TWO - THREE - FOUR - FIVE straight
			temp = 5;
			isOwnCardInRange = false;
			for (int j = 0; j < 4; j++) {
				if (ownFigurePresent[j])
					isOwnCardInRange = true;
				if (figurePresent[j])
					--temp;
				else
					actualCombination.add(new Integer(j));
			}
			if (ownFigurePresent[12])
				isOwnCardInRange = true;
			if (figurePresent[12])
				--temp;
			else
				actualCombination.add(new Integer(12));
			if (isOwnCardInRange && !combinationChecked.contains(actualCombination)) {
				combinationChecked.add(actualCombination);
				switch (temp) {
					case 4: probabilityListPositive.add(new Float(getProbabilityFourCards(cardsToSeeNumber, cards.size(), 16, 12, 8, 4))); break;
					case 3: probabilityListPositive.add(new Float(getProbabilityThreeCards(cardsToSeeNumber, cards.size(), 12, 8, 4))); break;
					case 2: probabilityListPositive.add(new Float(getProbabilityTwoCards(cardsToSeeNumber, cards.size(), 8, 4))); break;
					case 1: probabilityListPositive.add(new Float(getProbabilityOneCard(cardsToSeeNumber, cards.size(), 4))); break;
					default: break;
				}
			}
			actualCombination = new ArrayList<>();
			combinationChecked.clear();
			if (highestOwnCardColor <= 7) { // NINE or lower card - case when there are five higher card on table in that color
				for (int i = highestOwnCardColor + 1; i < 9; i++) {
					temp = 5;
					for (int j = i; j < i + 5; j++) {
						if (figurePresent[j])
							--temp;
						else
							actualCombination.add(new Integer(j));
					}
					if (!combinationChecked.contains(actualCombination)) {
						combinationChecked.add(actualCombination);
						switch (temp) {
							case 4: probabilityListNegative.add(new Float(getProbabilityFourCards(cardsToSeeNumber, cards.size(), 4, 3, 2, 1))); break;
							case 3: probabilityListNegative.add(new Float(getProbabilityThreeCards(cardsToSeeNumber, cards.size(), 3, 2, 1))); break;
							case 2: probabilityListNegative.add(new Float(getProbabilityTwoCards(cardsToSeeNumber, cards.size(), 2, 1))); break;
							case 1: probabilityListNegative.add(new Float(getProbabilityOneCard(cardsToSeeNumber, cards.size(), 1))); break;
							default: break;
						}
					}
					actualCombination = new ArrayList<>();
				}
			}
		}
		return calculateProbability(probabilityListPositive, probabilityListNegative);
	}
	
} // end of class
