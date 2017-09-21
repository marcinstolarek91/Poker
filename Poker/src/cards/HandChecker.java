package cards;

import java.util.ArrayList;
import java.util.List;

public abstract class HandChecker {
	
	/**
	 * 
	 * @param cardsA - cards from player A
	 * @param cardsB - cards from player B
	 * @return 1 if cardsA are better
	 * 0 if cards are equal
	 * 2 if cardsB are better
	 */
	public static int checkBetterHand(List<Card> cardsA, List<Card> cardsB) {
		PokerHandsType figureA = getFigureType(cardsA);
		PokerHandsType figureB = getFigureType(cardsB);
		int size = (cardsA.size() >= cardsB.size() ? cardsB.size() : cardsA.size());
		if (figureA.ordinal() > figureB.ordinal())
			return 1;
		else if (figureA.ordinal() < figureB.ordinal())
			return 2;
		else {
			cardsA = getHand(cardsA);
			cardsB = getHand(cardsB);
			if (cardsA.equals(cardsB))
				return 0;
			for (int i = 1; i <= size; i++) {
				if (cardsA.get(cardsA.size() - i).faceCard.ordinal() > cardsB.get(cardsB.size() - i).faceCard.ordinal())
					return 1;
				else if (cardsA.get(cardsA.size() - i).faceCard.ordinal() < cardsB.get(cardsB.size() - i).faceCard.ordinal())
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
	
	/**
	 * 
	 * @param wholeCards - all cards (player cards and cards on the table) - maximum 7
	 * @param figureCards - pairs, three, flush, etc cards - maximum 5, all should be contained in wholeCards and sorted
	 * @return
	 */
	public static List<Card> getHand(List<Card> wholeCards, List<Card> figureCards) {
		if (!wholeCards.containsAll(figureCards)) // wrong cards
			return null;
		if (figureCards.size() == 5)
			return figureCards;
		wholeCards.sort(null);
		for (int i = wholeCards.size() - 1; i >= 0 && figureCards.size() < 5; i--) {
			if (!figureCards.contains(wholeCards.get(i)))
				figureCards.add(0, wholeCards.get(i));
		}
		return figureCards;
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
		int size = cards.size();
		if (size < 2)
			return null;
		cards.sort(null);
		for (int i = size - 1; i > 0; i--) {
			if (cards.get(i).faceCard.equals(cards.get(i - 1).faceCard)) {
				pair.add(cards.get(i - 1));
				pair.add(cards.get(i));
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
		int size = cards.size();
		Card cardFirstPair = null;
		if (size < 4)
			return null;
		cards.sort(null);
		for (int i = size - 1; i > 0; i--) {
			if (cards.get(i).faceCard.equals(cards.get(i - 1).faceCard) && (cardFirstPair == null || !cards.get(i).faceCard.equals(cardFirstPair.faceCard))) {
				pairs.add(cards.get(i - 1));
				pairs.add(cards.get(i));
				if (pairs.size() == 2)
					cardFirstPair = cards.get(i);
				if (pairs.size() == 4) {
					cards.sort(null);
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
		int size = cards.size();
		if (size < 3)
			return null;
		cards.sort(null);
		for (int i = size - 1; i > 1; i--) {
			if (cards.get(i).faceCard.equals(cards.get(i - 1).faceCard) && cards.get(i).faceCard.equals(cards.get(i - 2).faceCard)) {
				three.add(cards.get(i - 2));
				three.add(cards.get(i - 1));
				three.add(cards.get(i));
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
		int size = cards.size();
		if (size < 5)
			return null;
		cards.sort(null);
		for (int i = size - 1; i > 3; i--) {
			if (cards.get(i).faceCard.ordinal() == cards.get(i - 1).faceCard.ordinal() + 1) {
				if (cards.get(i).faceCard.ordinal() == cards.get(i - 2).faceCard.ordinal() + 2) {
					if (cards.get(i).faceCard.ordinal() == cards.get(i - 3).faceCard.ordinal() + 3) {
						if (cards.get(i).faceCard.ordinal() == cards.get(i - 4).faceCard.ordinal() + 4) {
							straight.add(cards.get(i - 4));
							straight.add(cards.get(i - 3));
							straight.add(cards.get(i - 2));
							straight.add(cards.get(i - 1));
							straight.add(cards.get(i));
							return straight;
						}
					}
				}
			}
		}
		// Ace - 2 - 3 - 4 - 5
		if (cards.get(0).faceCard.equals(FaceCard.TWO) && cards.get(1).faceCard.equals(FaceCard.THREE) && cards.get(2).faceCard.equals(FaceCard.FOUR) && cards.get(3).faceCard.equals(FaceCard.FIVE) && cards.get(size - 1).faceCard.equals(FaceCard.ACE)) {
			straight.add(cards.get(size - 1));
			straight.add(cards.get(0));
			straight.add(cards.get(1));
			straight.add(cards.get(2));
			straight.add(cards.get(3));
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
		int size = cards.size();
		int[] color = {0, 0, 0, 0};
		if (size < 5)
			return null;
		for (Card card : cards)
			++color[card.cardColor.ordinal()];
		for (int i = 0; i < 4; i++) {
			if (color[i] >= 5) {
				cards.sort(null);
				for (int j = 0; j < size; j++) {
					if (cards.get(i).cardColor.ordinal() == i)
						flush.add(cards.get(i));
				}
				while (flush.size() > 5)
					flush.remove(0);
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
		int size = cards.size();
		if (size < 4)
			return null;
		cards.sort(null);
		for (int i = 3; i < size; i++) {
			if (cards.get(i).faceCard.equals(cards.get(i - 1).faceCard) && cards.get(i).faceCard.equals(cards.get(i - 2).faceCard) && cards.get(i).faceCard.equals(cards.get(i - 3).faceCard)) {
				four.add(cards.get(i - 3));
				four.add(cards.get(i - 2));
				four.add(cards.get(i - 1));
				four.add(cards.get(i));
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
		int size = cards.size();
		if (size < 5)
			return null;
		cards.sort(null);
		for (int i = size - 1; i > 3; i--) {
			if (cards.get(i).faceCard.ordinal() == cards.get(i - 1).faceCard.ordinal() + 1 && cards.get(i).cardColor.equals(cards.get(i - 1).cardColor)) {
				if (cards.get(i).faceCard.ordinal() == cards.get(i - 2).faceCard.ordinal() + 2 && cards.get(i).cardColor.equals(cards.get(i - 2).cardColor)) {
					if (cards.get(i).faceCard.ordinal() == cards.get(i - 3).faceCard.ordinal() + 3 && cards.get(i).cardColor.equals(cards.get(i - 3).cardColor)) {
						if (cards.get(i).faceCard.ordinal() == cards.get(i - 4).faceCard.ordinal() + 4 && cards.get(i).cardColor.equals(cards.get(i - 4).cardColor)) {
							poker.add(cards.get(i - 4));
							poker.add(cards.get(i - 3));
							poker.add(cards.get(i - 2));
							poker.add(cards.get(i - 1));
							poker.add(cards.get(i));
							return poker;
						}
					}
				}
			}
		}
		// Ace - 2 - 3 - 4 - 5
		if (cards.get(0).faceCard.equals(FaceCard.TWO) && cards.get(1).faceCard.equals(FaceCard.THREE) && cards.get(2).faceCard.equals(FaceCard.FOUR) && cards.get(3).faceCard.equals(FaceCard.FIVE) && cards.get(size - 1).faceCard.equals(FaceCard.ACE)) {
			if (cards.get(0).cardColor.equals(cards.get(1).cardColor) && cards.get(0).cardColor.equals(cards.get(2).cardColor) && cards.get(0).cardColor.equals(cards.get(3).cardColor) && cards.get(0).cardColor.equals(cards.get(size - 1).cardColor)) {
				poker.add(cards.get(size - 1));
				poker.add(cards.get(0));
				poker.add(cards.get(1));
				poker.add(cards.get(2));
				poker.add(cards.get(3));
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
