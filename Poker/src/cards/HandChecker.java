package cards;

import java.util.ArrayList;
import java.util.List;

public abstract class HandChecker {
	
	/**
	 * 
	 * @param card
	 * @return card short name
	 */
	private static String getCardName(Card card) {
		if (card.faceCard == FaceCard.ACE)
			return "A";
		if (card.faceCard == FaceCard.KING)
			return "K";
		if (card.faceCard == FaceCard.QUEEN)
			return "Q";
		if (card.faceCard == FaceCard.JACK)
			return "J";
		if (card.faceCard == FaceCard.TEN)
			return "10";
		if (card.faceCard == FaceCard.NINE)
			return "9";
		if (card.faceCard == FaceCard.EIGHT)
			return "8";
		if (card.faceCard == FaceCard.SEVEN)
			return "7";
		if (card.faceCard == FaceCard.SIX)
			return "6";
		if (card.faceCard == FaceCard.FIVE)
			return "5";
		if (card.faceCard == FaceCard.FOUR)
			return "4";
		if (card.faceCard == FaceCard.THREE)
			return "3";
		return "2";
	}
	
	/**
	 * 
	 * @param hand - player hand
	 * @param type - figure type
	 * @return kicker name
	 */
	private static String getKickerName(List<Card> hand, PokerHandsType type) {
		int kickerLength;
		String name = "";
		if (getFigureType(hand) == PokerHandsType.POKER)
			return "";
		else if (getFigureType(hand) ==  PokerHandsType.FOUR)
			return getCardName(hand.get(0));
		else if (getFigureType(hand) ==  PokerHandsType.FULL_HOUSE)
			return "";
		else if (getFigureType(hand) ==  PokerHandsType.FLUSH)
			return "";
		else if (getFigureType(hand) ==  PokerHandsType.STRAIGHT)
			return "";
		else if (getFigureType(hand) ==  PokerHandsType.THREE)
			kickerLength = 2;
		else if (getFigureType(hand) ==  PokerHandsType.TWO_PAIRS)
			return getCardName(hand.get(0));
		else if (getFigureType(hand) ==  PokerHandsType.PAIR)
			kickerLength = 3;
		else // high card
			kickerLength = 4;
		for (int i = kickerLength - 1; i >= 0; i--) {
			name += getCardName(hand.get(i));
			if (i > 0)
				name += ", ";
		}
		return name;
	}
	
	/**
	 * Generate hand name
	 * @param cards - player cards
	 * @return hand name
	 */
	public static String getHandName(List<Card> cards) {
		List<Card> hand = new ArrayList<>();
		hand = getHand(cards);
		if (hand.size() < 5)
			return "";
		if (getFigureType(cards) == PokerHandsType.POKER)
			return "poker from " + getCardName(hand.get(0)) + " to " + getCardName(hand.get(4));
		if (getFigureType(cards) ==  PokerHandsType.FOUR)
			return "four + " + getKickerName(hand, PokerHandsType.FOUR) + " kicker";
		if (getFigureType(cards) ==  PokerHandsType.FULL_HOUSE)
			return "full house (" + getCardName(hand.get(4)) + ", " + getCardName(hand.get(1)) + ")";
		if (getFigureType(cards) ==  PokerHandsType.FLUSH) {
			String name = "flush with ";
			for (int i = hand.size() - 1; i >= hand.size() - 5; i--) {
				name += getCardName(hand.get(i));
				if (i != hand.size() - 5)
					name += ", ";
			}
			return name;
		}
		if (getFigureType(cards) ==  PokerHandsType.STRAIGHT)
			return "straight from " + getCardName(hand.get(0)) + " to " + getCardName(hand.get(4));
		if (getFigureType(cards) ==  PokerHandsType.THREE)
			return "three of " + getCardName(hand.get(4)) + " + " + getKickerName(hand, PokerHandsType.THREE) + " kicker";
		if (getFigureType(cards) ==  PokerHandsType.TWO_PAIRS)
			return "two pairs (" + getCardName(hand.get(4)) + ", " + getCardName(hand.get(2)) + ") + " + getKickerName(hand, PokerHandsType.TWO_PAIRS) + " kicker";
		if (getFigureType(cards) ==  PokerHandsType.PAIR)
			return " pair of  " + getCardName(hand.get(4)) + " + " + getKickerName(hand, PokerHandsType.PAIR) + " kicker";
		return "high card " + getCardName(hand.get(4)) + " + " + getKickerName(hand, PokerHandsType.HIGH_CARD) + " kicker"; // high card
	}
	
	/**
	 * 
	 * @param cardsA - cards from player A
	 * @param cardsB - cards from player B
	 * @return 1 if cardsA are better
	 * 0 if cards are equal
	 * 2 if cardsB are better
	 */
	public static int checkBetterHand(List<Card> cardsA, List<Card> cardsB) {
		List<Card> newCardsA = new ArrayList<>();
		List<Card> newCardsB = new ArrayList<>();
		newCardsA.addAll(cardsA);
		newCardsB.addAll(cardsB);
		PokerHandsType figureA = getFigureType(newCardsA);
		PokerHandsType figureB = getFigureType(newCardsB);
		if (figureA.ordinal() > figureB.ordinal())
			return 1;
		else if (figureA.ordinal() < figureB.ordinal())
			return 2;
		else {
			newCardsA = getHand(newCardsA);
			newCardsB = getHand(newCardsB);
			int size = (newCardsA.size() >= newCardsB.size() ? newCardsB.size() : newCardsA.size());
			if (newCardsA.equals(newCardsB))
				return 0;
			for (int i = 1; i <= size; i++) {
				if (newCardsA.get(newCardsA.size() - i).faceCard.ordinal() > newCardsB.get(newCardsB.size() - i).faceCard.ordinal())
					return 1;
				else if (newCardsA.get(newCardsA.size() - i).faceCard.ordinal() < newCardsB.get(newCardsB.size() - i).faceCard.ordinal())
					return 2;
			}
		}
		return 0;
	}
	
	/**
	 * 
	 * @param cards
	 * @return poker figure that player has
	 */
	public static PokerHandsType getFigureType(List<Card> cards) {
		if (hasPoker(cards))
			return PokerHandsType.POKER;
		if (hasFour(cards))
			return PokerHandsType.FOUR;
		if (hasFullHouse(cards))
			return PokerHandsType.FULL_HOUSE;
		if (hasFlush(cards))
			return PokerHandsType.FLUSH;
		if (hasStraight(cards))
			return PokerHandsType.STRAIGHT;
		if (hasThree(cards))
			return PokerHandsType.THREE;
		if (hasTwoPairs(cards))
			return PokerHandsType.TWO_PAIRS;
		if (hasPair(cards))
			return PokerHandsType.PAIR;
		return PokerHandsType.HIGH_CARD;
	}
	
	/**
	 * 
	 * @param cards - player cards
	 * @param type - known figure type
	 * @return only figure cards
	 */
	public static List<Card> getOnlyFigure(List<Card> cards, PokerHandsType type){
		switch(type) {
			case POKER: return getPoker(cards);
			case FOUR: return getFour(cards);
			case FULL_HOUSE: return getFullHouse(cards);
			case FLUSH: return getFlush(cards);
			case STRAIGHT: return getStraight(cards);
			case THREE: return getThree(cards);
			case TWO_PAIRS: return getTwoPairs(cards);
			case PAIR: return getPair(cards);
			case HIGH_CARD: {
				List<Card> highCard= new ArrayList<>();
				highCard.add(getHighCard(cards));
				return highCard;
			}
			default: return null;
		}
	}
	
	/**
	 * 
	 * @param cards - player cards
	 * @return only figure cards
	 */
	public static List<Card> getOnlyFigure(List<Card> cards){
		return getOnlyFigure(cards, getFigureType(cards));
	}
	
	private static List<Card> temp(List<Card> cards){
		return null;
	}
	
	/**
	 * 
	 * @param wholeCards - all cards (player cards and cards on the table) - maximum 7
	 * @param figureCards - pairs, three, flush, etc cards - maximum 5, all should be contained in wholeCards and sorted
	 * @return
	 */
	public static List<Card> getHand(List<Card> wholeCards, List<Card> figureCards) {
		List<Card> hand = new ArrayList<>();
		List<Card> newCards = new ArrayList<>();
		newCards.addAll(wholeCards);
		hand.addAll(figureCards);
		if (!newCards.containsAll(hand)) // wrong cards
			return null;
		if (hand.size() == 5)
			return hand;
		newCards.sort(null);
		for (int i = newCards.size() - 1; i >= 0 && hand.size() < 5; i--) {
			if (!hand.contains(newCards.get(i)))
				hand.add(0, newCards.get(i));
		}
		return hand;
	}
	
	/**
	 * 
	 * @param wholeCards - all cards (player cards and cards on the table) - maximum 7
	 * @return
	 */
	public static List<Card> getHand(List<Card> wholeCards) {
		return getHand(wholeCards, getOnlyFigure(wholeCards));
	}
	
	/**
	 * 
	 * @param cards
	 * @return the high card
	 * IMPORTANT: first should be detected if there is no better hand (example two of a kind)
	 */
	public static Card getHighCard(List<Card> cards) {
		if (cards == null)
			return null;
		Card highCard = cards.get(0);
		for (Card card : cards) {
			if (card.compareTo(highCard) > 0)
				highCard = card;
		}
		return highCard;
	}
	
	/**
	 * 
	 * @param cards
	 * @return sorted list with detected a pair
	 * IMPORTANT: first should be detected if there is no better hand (example three of a kind)
	 */
	public static List<Card> getPair(List<Card> cards) {
		List<Card> pair = new ArrayList<>();
		List<Card> newCards = new ArrayList<>();
		newCards.addAll(cards);
		int size = newCards.size();
		if (size < 2)
			return null;
		newCards.sort(null);
		for (int i = size - 1; i > 0; i--) {
			if (newCards.get(i).faceCard.equals(newCards.get(i - 1).faceCard)) {
				pair.add(newCards.get(i - 1));
				pair.add(newCards.get(i));
				return pair;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param cards
	 * @return sorted list with detected two pairs
	 * IMPORTANT: first should be detected if there is no better hand (example full house)
	 */
	public static List<Card> getTwoPairs(List<Card> cards) {
		List<Card> pairs = new ArrayList<>();
		List<Card> newCards = new ArrayList<>();
		newCards.addAll(cards);
		int size = newCards.size();
		Card cardFirstPair = null;
		if (size < 4)
			return null;
		newCards.sort(null);
		for (int i = size - 1; i > 0; i--) {
			if (newCards.get(i).faceCard.equals(newCards.get(i - 1).faceCard) && (cardFirstPair == null || !newCards.get(i).faceCard.equals(cardFirstPair.faceCard))) {
				pairs.add(newCards.get(i - 1));
				pairs.add(newCards.get(i));
				if (pairs.size() == 2)
					cardFirstPair = newCards.get(i);
				if (pairs.size() == 4) {
					pairs.sort(null);
					return pairs;
				}
				--i;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param cards
	 * @return sorted list with detected a three
	 * IMPORTANT: first should be detected if there is no better hand (example full house, a four of a kind)
	 */
	public static List<Card> getThree(List<Card> cards) {
		List<Card> three = new ArrayList<>();
		List<Card> newCards = new ArrayList<>();
		newCards.addAll(cards);
		int size = newCards.size();
		if (size < 3)
			return null;
		newCards.sort(null);
		for (int i = size - 1; i > 1; i--) {
			if (newCards.get(i).faceCard.equals(newCards.get(i - 1).faceCard) && newCards.get(i).faceCard.equals(newCards.get(i - 2).faceCard)) {
				three.add(newCards.get(i - 2));
				three.add(newCards.get(i - 1));
				three.add(newCards.get(i));
				return three;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param cards
	 * @return sorted list with detected straight (the least face card can be ACE - with 2, 3, 4, 5)
	 * IMPORTANT: first should be detected if there is no better hand (example flush, poker)
	 */
	public static List<Card> getStraight(List<Card> cards) {
		List<Card> straight = new ArrayList<>();
		List<Card> newCards = new ArrayList<>();
		newCards.addAll(cards);
		newCards.sort(null);
		for (int i = 0; i < newCards.size() - 1; i++) {
			while (i < newCards.size() - 1 && newCards.get(i).faceCard.equals(newCards.get(i + 1).faceCard))
				newCards.remove(i + 1);
		}
		if (newCards.size() < 5)
			return null;
		for (int i = newCards.size() - 1; i > 3; i--) {
			if (newCards.get(i).faceCard.ordinal() == newCards.get(i - 1).faceCard.ordinal() + 1) {
				if (newCards.get(i).faceCard.ordinal() == newCards.get(i - 2).faceCard.ordinal() + 2) {
					if (newCards.get(i).faceCard.ordinal() == newCards.get(i - 3).faceCard.ordinal() + 3) {
						if (newCards.get(i).faceCard.ordinal() == newCards.get(i - 4).faceCard.ordinal() + 4) {
							straight.add(newCards.get(i - 4));
							straight.add(newCards.get(i - 3));
							straight.add(newCards.get(i - 2));
							straight.add(newCards.get(i - 1));
							straight.add(newCards.get(i));
							return straight;
						}
					}
				}
			}
		}
		// Ace - 2 - 3 - 4 - 5
		if (newCards.get(0).faceCard.equals(FaceCard.TWO) && newCards.get(1).faceCard.equals(FaceCard.THREE) && newCards.get(2).faceCard.equals(FaceCard.FOUR) && newCards.get(3).faceCard.equals(FaceCard.FIVE) && newCards.get(newCards.size() - 1).faceCard.equals(FaceCard.ACE)) {
			straight.add(newCards.get(newCards.size() - 1));
			straight.add(newCards.get(0));
			straight.add(newCards.get(1));
			straight.add(newCards.get(2));
			straight.add(newCards.get(3));
			return straight;
		}
		return null;
	}
	
	/**
	 * 
	 * @param cards
	 * @return sorted list with detected flush
	 * IMPORTANT: first should be detected if there is no better hand (example four of a kind, poker)
	 */
	public static List<Card> getFlush(List<Card> cards) {
		List<Card> flush = new ArrayList<>();
		List<Card> newCards = new ArrayList<>();
		newCards.addAll(cards);
		int size = newCards.size();
		int[] color = {0, 0, 0, 0};
		if (size < 5)
			return null;
		for (Card card : newCards)
			++color[card.cardColor.ordinal()];
		for (int i = 0; i < 4; i++) {
			if (color[i] >= 5) {
				newCards.sort(null);
				for (int j = 0; j < size; j++) {
					if (newCards.get(j).cardColor.ordinal() == i)
						flush.add(newCards.get(j));
				}
				while (flush.size() > 5)
					flush.remove(0);
				flush.sort(null);
				return flush;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param cards
	 * @return sorted list with detected full house (three treated as greater than the two) 
	 * IMPORTANT: first should be detected if there is no better hand (example four of a kind)
	 */
	public static List<Card> getFullHouse(List<Card> cards) {
		List<Card> fullHouse = new ArrayList<>();
		int toDelete;
		if (cards.size() < 5 || getTwoPairs(cards) == null || getThree(cards) == null)
			return null;
		fullHouse.addAll(getTwoPairs(cards)); // should be added four cards
		fullHouse.addAll(getThree(cards)); // should be added three cards
		if (fullHouse.size() != 7) // should have seven cards
			return null;
		if (fullHouse.get(0).faceCard.equals(fullHouse.get(6).faceCard))
			toDelete = 0;
		else
			toDelete = 2;
		fullHouse.remove(toDelete); // remove two repeated cards
		fullHouse.remove(toDelete);
		return fullHouse;
	}
	
	/**
	 * 
	 * @param cards
	 * @return sorted list with detected a four
	 * IMPORNANT: if detected, it has to be the best hand 
	 */
	public static List <Card> getFour(List<Card> cards) {
		List<Card> four = new ArrayList<>();
		List<Card> newCards = new ArrayList<>();
		newCards.addAll(cards);
		int size = newCards.size();
		if (size < 4)
			return null;
		newCards.sort(null);
		for (int i = 3; i < size; i++) {
			if (newCards.get(i).faceCard.equals(newCards.get(i - 1).faceCard) && newCards.get(i).faceCard.equals(newCards.get(i - 2).faceCard) && newCards.get(i).faceCard.equals(newCards.get(i - 3).faceCard)) {
				four.add(newCards.get(i - 3));
				four.add(newCards.get(i - 2));
				four.add(newCards.get(i - 1));
				four.add(newCards.get(i));
				return four;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param cards
	 * @return sorted list with detected a poker (the least face card can be ACE - with 2, 3, 4, 5)
	 * IMPORNANT: if detected, it has to be the best hand
	 */
	public static List<Card> getPoker(List<Card> cards) {
		List<Card> poker = new ArrayList<>();
		List<Card> newCards = new ArrayList<>();
		newCards.addAll(cards);
		newCards.sort(null);
		for (int i = 0; i < newCards.size() - 1; i++) {
			while (i < newCards.size() - 1 && newCards.get(i).faceCard.equals(newCards.get(i + 1).faceCard))
				newCards.remove(i + 1);
		}
		if (newCards.size() < 5)
			return null;
		for (int i = newCards.size() - 1; i > 3; i--) {
			if (newCards.get(i).faceCard.ordinal() == newCards.get(i - 1).faceCard.ordinal() + 1 && newCards.get(i).cardColor.equals(newCards.get(i - 1).cardColor)) {
				if (newCards.get(i).faceCard.ordinal() == newCards.get(i - 2).faceCard.ordinal() + 2 && newCards.get(i).cardColor.equals(newCards.get(i - 2).cardColor)) {
					if (newCards.get(i).faceCard.ordinal() == newCards.get(i - 3).faceCard.ordinal() + 3 && newCards.get(i).cardColor.equals(newCards.get(i - 3).cardColor)) {
						if (newCards.get(i).faceCard.ordinal() == newCards.get(i - 4).faceCard.ordinal() + 4 && newCards.get(i).cardColor.equals(newCards.get(i - 4).cardColor)) {
							poker.add(newCards.get(i - 4));
							poker.add(newCards.get(i - 3));
							poker.add(newCards.get(i - 2));
							poker.add(newCards.get(i - 1));
							poker.add(newCards.get(i));
							return poker;
						}
					}
				}
			}
		}
		// Ace - 2 - 3 - 4 - 5
		if (newCards.get(0).faceCard.equals(FaceCard.TWO) && newCards.get(1).faceCard.equals(FaceCard.THREE) && newCards.get(2).faceCard.equals(FaceCard.FOUR) && newCards.get(3).faceCard.equals(FaceCard.FIVE) && newCards.get(newCards.size() - 1).faceCard.equals(FaceCard.ACE)) {
			if (newCards.get(0).cardColor.equals(newCards.get(1).cardColor) && newCards.get(0).cardColor.equals(newCards.get(2).cardColor) && newCards.get(0).cardColor.equals(newCards.get(3).cardColor) && newCards.get(0).cardColor.equals(newCards.get(newCards.size() - 1).cardColor)) {
				poker.add(newCards.get(newCards.size() - 1));
				poker.add(newCards.get(0));
				poker.add(newCards.get(1));
				poker.add(newCards.get(2));
				poker.add(newCards.get(3));
				return poker;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param cards - whole cards
	 * @return true if has any pair
	 * IMPORTANT: first should be detected if there is no better hand (example three of a kind)
	 */
	public static boolean hasPair(List<Card> cards) {
		if (getPair(cards) != null)
			return true;
		return false;
	}
	
	/**
	 * 
	 * @param cards - whole cards
	 * @param ownCards - player cards
	 * @return true if has own pair
	 */
	public static boolean hasOwnPair(List<Card> cards, List<Card> ownCards) {
		List<Card> pair = new ArrayList<>();
		do {
			pair = getPair(cards);
			if (pair == null)
				return false;
			if (pair.contains(ownCards.get(0)) || pair.contains(ownCards.get(1)))
				return true;
			else
				cards.removeAll(pair);
		}while(cards.size() >= 2);
		return false;
	}
	
	/**
	 * 
	 * @param cards - whole cards
	 * @return true if has two pairs
	 * IMPORTANT: first should be detected if there is no better hand (example full house)
	 */
	public static boolean hasTwoPairs(List<Card> cards) {
		if (getTwoPairs(cards) != null)
			return true;
		return false;
	}
	
	/**
	 * 
	 * @param cards - whole cards
	 * @param ownCards - player cards
	 * @return true if has own two pairs
	 */
	public static boolean hasOwnTwoPairs(List<Card> cards, List<Card> ownCards) {
		List<Card> twoPairs = new ArrayList<>();
		twoPairs = getTwoPairs(cards);
		if (twoPairs == null)
			return false;
		if (twoPairs.contains(ownCards.get(0)) || twoPairs.contains(ownCards.get(1)))
			return true;
		return false;
	}
	
	/**
	 * 
	 * @param cards - whole cards
	 * @param ownCards - player cards
	 * @return true if has own two pairs and each pair: one card in hand, second on the table
	 */
	public static boolean hasOwnTwoPairsEachPair(List<Card> cards, List<Card> ownCards) {
		List<Card> twoPairs = new ArrayList<>();
		if (!hasOwnTwoPairs(cards, ownCards))
			return false;
		if (getFigureType(ownCards) == PokerHandsType.PAIR)
			return false;
		twoPairs = getTwoPairs(cards);
		if (twoPairs == null)
			return false;
		twoPairs.removeAll(ownCards);
		if (twoPairs.size() == 2)
			return true;
		return false;
	}
	
	/**
	 * 
	 * @param cards - whole cards
	 * @return true if has a three
	 * IMPORTANT: first should be detected if there is no better hand (example full house, a four of a kind)
	 */
	public static boolean hasThree(List<Card> cards) {
		if (getThree(cards) != null)
			return true;
		return false;
	}
	
	/**
	 * 
	 * @param cards - whole cards
	 * @param ownCards - player cards
	 * @return true if has own three
	 */
	public static boolean hasOwnThree(List<Card> cards, List<Card> ownCards) {
		List<Card> three = new ArrayList<>();
		do {
			three = getThree(cards);
			if (three == null)
				return false;
			if (three.contains(ownCards.get(0)) || three.contains(ownCards.get(1)))
				return true;
			else
				cards.removeAll(three);
		}while(cards.size() >= 3);
		return false;
	}
	
	/**
	 * 
	 * @param cards - whole cards
	 * @return true if has a straight
	 * IMPORTANT: first should be detected if there is no better hand (example flush, poker)
	 */
	public static boolean hasStraight(List<Card> cards) {
		if (getStraight(cards) != null)
			return true;
		return false;
	}
	
	/**
	 * 
	 * @param cards - whole cards
	 * @param ownCards - player cards
	 * @return true if has own straight
	 */
	public static boolean hasOwnStraight(List<Card> cards, List<Card> ownCards) {
		List<Card> straight = new ArrayList<>();
		straight = getStraight(cards);
		if (straight == null)
			return false;
		if (straight.contains(ownCards.get(0)) || straight.contains(ownCards.get(1)))
			return true;
		else { // if player has own straight, but one of one card has the same face card than card on the table
			for (int i = 0; i < straight.size(); i++) {
				for (int j = 0; j < ownCards.size(); j++) {
					if (straight.get(i).faceCard.ordinal() == ownCards.get(j).faceCard.ordinal())
						return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param cards - whole cards
	 * @return true if has a flush
	 * IMPORTANT: first should be detected if there is no better hand (example four of a kind, poker)
	 */
	public static boolean hasFlush(List<Card> cards) {
		if (getFlush(cards) != null)
			return true;
		return false;
	}
	
	/**
	 * 
	 * @param cards - whole cards
	 * @param ownCards - player cards
	 * @return true if has own flush
	 */
	public static boolean hasOwnFlush(List<Card> cards, List<Card> ownCards) {
		List<Card> flush = new ArrayList<>();
		flush = getFlush(cards);
		if (flush == null)
			return false;
		if (flush.contains(ownCards.get(0)) || flush.contains(ownCards.get(1)))
			return true;
		return false;
	}
	
	/**
	 * 
	 * @param cards - whole cards
	 * @return true if has a full house
	 * IMPORTANT: first should be detected if there is no better hand (example four of a kind)
	 */
	public static boolean hasFullHouse(List<Card> cards) {
		if (getFullHouse(cards) != null)
			return true;
		return false;
	}
	
	/**
	 * 
	 * @param cards - whole cards
	 * @param ownCards - player cards
	 * @return true if has own fullHouse
	 */
	public static boolean hasOwnFullHouse(List<Card> cards, List<Card> ownCards) {
		List<Card> fullHouse = new ArrayList<>();
		fullHouse = getFullHouse(cards);
		if (fullHouse == null)
			return false;
		if (fullHouse.contains(ownCards.get(0)) || fullHouse.contains(ownCards.get(1)))
			return true;
		else { // if player has own full house, but one of one card has the same face card than card on the table (has two threes)
			for (int i = 0; i < fullHouse.size(); i++) {
				for (int j = 0; j < ownCards.size(); j++) {
					if (fullHouse.get(i).faceCard.ordinal() == ownCards.get(j).faceCard.ordinal())
						return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param cards - whole cards
	 * @return true if has a four
	 * IMPORNANT: if detected, it has to be the best hand
	 */
	public static boolean hasFour(List<Card> cards) {
		if (getFour(cards) != null)
			return true;
		return false;
	}
	
	/**
	 * 
	 * @param cards - whole cards
	 * @param ownCards - player cards
	 * @return true if has own four
	 */
	public static boolean hasOwnFour(List<Card> cards, List<Card> ownCards) {
		List<Card> four = new ArrayList<>();
		four = getFour(cards);
		if (four == null)
			return false;
		if (four.contains(ownCards.get(0)) || four.contains(ownCards.get(1)))
			return true;
		return false;
	}
	
	/**
	 * 
	 * @param cards - whole cards
	 * @return true if has a poker
	 * IMPORNANT: if detected, it has to be the best hand
	 */
	public static boolean hasPoker(List<Card> cards) {
		if (getPoker(cards) != null)
			return true;
		return false;
	}
	
	/**
	 * 
	 * @param cards - whole cards
	 * @param ownCards - player cards
	 * @return true if has own poker
	 */
	public static boolean hasOwnPoker(List<Card> cards, List<Card> ownCards) {
		List<Card> poker = new ArrayList<>();
		poker = getPoker(cards);
		if (poker == null)
			return false;
		if (poker.contains(ownCards.get(0)) || poker.contains(ownCards.get(1)))
			return true;
		return false;
	}
}
