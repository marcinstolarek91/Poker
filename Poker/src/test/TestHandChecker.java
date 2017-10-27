package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cards.Card;
import cards.CardColor;
import cards.FaceCard;
import cards.HandChecker;
import cards.PokerHandsType;

public class TestHandChecker {
	List<Card> cards = new ArrayList<>();

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testCheckBetterHand1() {
		List<Card> cardsA = new ArrayList<>();
		List<Card> cardsB = new ArrayList<>();
		cardsA.add(new Card(FaceCard.QUEEN, CardColor.DIAMOND));
		cardsA.add(new Card(FaceCard.QUEEN, CardColor.CLUB));
		cardsA.add(new Card(FaceCard.QUEEN, CardColor.HEART));
		cardsA.add(new Card(FaceCard.SIX, CardColor.CLUB));
		cardsA.add(new Card(FaceCard.QUEEN, CardColor.SPADE));
		cardsB.addAll(cardsA); // flop, turn, river
		cardsA.add(new Card(FaceCard.THREE, CardColor.HEART));
		cardsA.add(new Card(FaceCard.THREE, CardColor.SPADE));
		cardsB.add(new Card(FaceCard.ACE, CardColor.DIAMOND));
		cardsB.add(new Card(FaceCard.TWO, CardColor.SPADE));
		assertEquals(2, HandChecker.checkBetterHand(cardsA, cardsB));
	}
	
	@Test
	public void testCheckBetterHand2() {
		List<Card> cardsA = new ArrayList<>();
		List<Card> cardsB = new ArrayList<>();
		cardsA.add(new Card(FaceCard.QUEEN, CardColor.DIAMOND));
		cardsA.add(new Card(FaceCard.QUEEN, CardColor.CLUB));
		cardsA.add(new Card(FaceCard.SIX, CardColor.HEART));
		cardsA.add(new Card(FaceCard.SIX, CardColor.CLUB));
		cardsA.add(new Card(FaceCard.QUEEN, CardColor.SPADE));
		cardsB.addAll(cardsA); // flop, turn, river
		cardsA.add(new Card(FaceCard.THREE, CardColor.HEART));
		cardsA.add(new Card(FaceCard.THREE, CardColor.SPADE));
		cardsB.add(new Card(FaceCard.ACE, CardColor.DIAMOND));
		cardsB.add(new Card(FaceCard.TWO, CardColor.SPADE));
		assertEquals(0, HandChecker.checkBetterHand(cardsA, cardsB));
	}
	
	@Test
	public void testCheckBetterHand3() {
		List<Card> cardsA = new ArrayList<>();
		List<Card> cardsB = new ArrayList<>();
		cardsA.add(new Card(FaceCard.QUEEN, CardColor.DIAMOND));
		cardsA.add(new Card(FaceCard.JACK, CardColor.CLUB));
		cardsA.add(new Card(FaceCard.TEN, CardColor.HEART));
		cardsA.add(new Card(FaceCard.SIX, CardColor.CLUB));
		cardsA.add(new Card(FaceCard.THREE, CardColor.SPADE));
		cardsB.addAll(cardsA); // flop, turn, river
		cardsA.add(new Card(FaceCard.ACE, CardColor.DIAMOND));
		cardsA.add(new Card(FaceCard.TWO, CardColor.SPADE));
		cardsB.add(new Card(FaceCard.TWO, CardColor.HEART));
		cardsB.add(new Card(FaceCard.KING, CardColor.SPADE));
		assertEquals(1, HandChecker.checkBetterHand(cardsA, cardsB));
	}
	
	@Test
	public void testGetOnlyFigure() {
		List<Card> figure = new ArrayList<>();
		cards.add(new Card(FaceCard.ACE, CardColor.CLUB));
		cards.add(new Card(FaceCard.TEN, CardColor.DIAMOND));
		cards.add(new Card(FaceCard.SIX, CardColor.SPADE));
		cards.add(new Card(FaceCard.ACE, CardColor.HEART));
		cards.add(new Card(FaceCard.KING, CardColor.SPADE));
		figure.add(new Card(FaceCard.ACE, CardColor.CLUB));
		figure.add(new Card(FaceCard.ACE, CardColor.HEART));
		assertEquals(figure, HandChecker.getOnlyFigure(cards));
	}
	
	@Test
	public void testGetHand() {
		List<Card> figure = new ArrayList<>();
		cards.add(new Card(FaceCard.FOUR, CardColor.CLUB));
		cards.add(new Card(FaceCard.TEN, CardColor.DIAMOND));
		cards.add(new Card(FaceCard.SIX, CardColor.SPADE));
		cards.add(new Card(FaceCard.FOUR, CardColor.HEART));
		cards.add(new Card(FaceCard.KING, CardColor.SPADE));
		figure.add(new Card(FaceCard.SIX, CardColor.SPADE));
		figure.add(new Card(FaceCard.TEN, CardColor.DIAMOND));
		figure.add(new Card(FaceCard.KING, CardColor.SPADE));
		figure.add(new Card(FaceCard.FOUR, CardColor.CLUB));
		figure.add(new Card(FaceCard.FOUR, CardColor.HEART));
		assertEquals(figure, HandChecker.getHand(cards));
	}
	
	@Test
	public void testGetPoker1() {
		cards.add(new Card(FaceCard.JACK, CardColor.CLUB));
		cards.add(new Card(FaceCard.NINE, CardColor.CLUB));
		cards.add(new Card(FaceCard.SEVEN, CardColor.CLUB));
		cards.add(new Card(FaceCard.QUEEN, CardColor.HEART));
		cards.add(new Card(FaceCard.EIGHT, CardColor.CLUB));
		cards.add(new Card(FaceCard.TEN, CardColor.CLUB));
		assertEquals(PokerHandsType.POKER, HandChecker.getFigureType(cards));
	}
	
	@Test
	public void testGetPoker2() {
		List<Card> hand = new ArrayList<>();
		cards.add(new Card(FaceCard.THREE, CardColor.SPADE));
		cards.add(new Card(FaceCard.ACE, CardColor.SPADE));
		cards.add(new Card(FaceCard.SIX, CardColor.SPADE));
		cards.add(new Card(FaceCard.SEVEN, CardColor.SPADE));
		cards.add(new Card(FaceCard.TWO, CardColor.SPADE));
		cards.add(new Card(FaceCard.FIVE, CardColor.SPADE));
		cards.add(new Card(FaceCard.FOUR, CardColor.SPADE));
		hand.addAll(cards);
		hand.remove(new Card(FaceCard.ACE, CardColor.SPADE));
		hand.remove(new Card(FaceCard.TWO, CardColor.SPADE));
		hand.sort(null);
		assertEquals(hand, HandChecker.getHand(cards));
	}
	
	@Test
	public void testGetPoker3() {
		cards.add(new Card(FaceCard.JACK, CardColor.CLUB));
		cards.add(new Card(FaceCard.NINE, CardColor.DIAMOND));
		cards.add(new Card(FaceCard.SEVEN, CardColor.CLUB));
		cards.add(new Card(FaceCard.QUEEN, CardColor.HEART));
		cards.add(new Card(FaceCard.EIGHT, CardColor.CLUB));
		cards.add(new Card(FaceCard.TEN, CardColor.CLUB));
		assertNotEquals(PokerHandsType.POKER, HandChecker.getFigureType(cards));
	}
	
	@Test
	public void testGetFour() {
		cards.add(new Card(FaceCard.THREE, CardColor.CLUB));
		cards.add(new Card(FaceCard.THREE, CardColor.HEART));
		cards.add(new Card(FaceCard.ACE, CardColor.SPADE));
		cards.add(new Card(FaceCard.THREE, CardColor.DIAMOND));
		cards.add(new Card(FaceCard.THREE, CardColor.SPADE));
		assertEquals(PokerHandsType.FOUR, HandChecker.getFigureType(cards));
	}
	
	@Test
	public void testGetFullHouse1() {
		cards.add(new Card(FaceCard.FIVE, CardColor.CLUB));
		cards.add(new Card(FaceCard.KING, CardColor.HEART));
		cards.add(new Card(FaceCard.FIVE, CardColor.HEART));
		cards.add(new Card(FaceCard.KING, CardColor.DIAMOND));
		cards.add(new Card(FaceCard.FIVE, CardColor.SPADE));
		cards.add(new Card(FaceCard.ACE, CardColor.HEART));
		assertEquals(PokerHandsType.FULL_HOUSE, HandChecker.getFigureType(cards));
	}
	
	@Test
	public void testGetFullHouse2() {
		List<Card> kings = new ArrayList<>();
		List<Card> hand = new ArrayList<>();
		cards.add(new Card(FaceCard.FIVE, CardColor.CLUB));
		cards.add(new Card(FaceCard.KING, CardColor.HEART));
		cards.add(new Card(FaceCard.FIVE, CardColor.HEART));
		cards.add(new Card(FaceCard.KING, CardColor.DIAMOND));
		cards.add(new Card(FaceCard.FIVE, CardColor.SPADE));
		cards.add(new Card(FaceCard.KING, CardColor.SPADE));
		kings.addAll(cards);
		kings.remove(new Card(FaceCard.FIVE, CardColor.CLUB));
		kings.remove(new Card(FaceCard.FIVE, CardColor.HEART));
		kings.remove(new Card(FaceCard.FIVE, CardColor.SPADE));
		hand = HandChecker.getOnlyFigure(cards);
		assertTrue(hand.containsAll(kings) && hand.size() == 5);
	}
	
	@Test
	public void testGetFlush() {
		cards.add(new Card(FaceCard.THREE, CardColor.HEART));
		cards.add(new Card(FaceCard.SIX, CardColor.HEART));
		cards.add(new Card(FaceCard.SEVEN, CardColor.SPADE));
		cards.add(new Card(FaceCard.NINE, CardColor.HEART));
		cards.add(new Card(FaceCard.ACE, CardColor.HEART));
		cards.add(new Card(FaceCard.QUEEN, CardColor.HEART));
		assertEquals(PokerHandsType.FLUSH, HandChecker.getFigureType(cards));
	}
	
	@Test
	public void testGetStraight1() {
		cards.add(new Card(FaceCard.JACK, CardColor.CLUB));
		cards.add(new Card(FaceCard.NINE, CardColor.SPADE));
		cards.add(new Card(FaceCard.SEVEN, CardColor.DIAMOND));
		cards.add(new Card(FaceCard.ACE, CardColor.HEART));
		cards.add(new Card(FaceCard.EIGHT, CardColor.SPADE));
		cards.add(new Card(FaceCard.TEN, CardColor.DIAMOND));
		assertEquals(PokerHandsType.STRAIGHT, HandChecker.getFigureType(cards));
	}
	
	@Test
	public void testGetStraight2() {
		List<Card> hand = new ArrayList<>();
		cards.add(new Card(FaceCard.THREE, CardColor.HEART));
		cards.add(new Card(FaceCard.ACE, CardColor.HEART));
		cards.add(new Card(FaceCard.SIX, CardColor.CLUB));
		cards.add(new Card(FaceCard.SEVEN, CardColor.DIAMOND));
		cards.add(new Card(FaceCard.TWO, CardColor.SPADE));
		cards.add(new Card(FaceCard.FIVE, CardColor.CLUB));
		cards.add(new Card(FaceCard.FOUR, CardColor.DIAMOND));
		hand.addAll(cards);
		hand.remove(new Card(FaceCard.ACE, CardColor.HEART));
		hand.remove(new Card(FaceCard.TWO, CardColor.SPADE));
		hand.sort(null);
		assertEquals(hand, HandChecker.getHand(cards));
	}
	
	@Test
	public void testGetThree() {
		cards.add(new Card(FaceCard.QUEEN, CardColor.CLUB));
		cards.add(new Card(FaceCard.TEN, CardColor.DIAMOND));
		cards.add(new Card(FaceCard.QUEEN, CardColor.DIAMOND));
		cards.add(new Card(FaceCard.QUEEN, CardColor.HEART));
		cards.add(new Card(FaceCard.ACE, CardColor.SPADE));
		assertEquals(PokerHandsType.THREE, HandChecker.getFigureType(cards));
	}
	
	@Test
	public void testGetTwoPairs() {
		cards.add(new Card(FaceCard.ACE, CardColor.CLUB));
		cards.add(new Card(FaceCard.TEN, CardColor.DIAMOND));
		cards.add(new Card(FaceCard.TEN, CardColor.SPADE));
		cards.add(new Card(FaceCard.ACE, CardColor.HEART));
		cards.add(new Card(FaceCard.KING, CardColor.SPADE));
		assertEquals(PokerHandsType.TWO_PAIRS, HandChecker.getFigureType(cards));
	}
	
	@Test
	public void testGetPair() {
		cards.add(new Card(FaceCard.ACE, CardColor.CLUB));
		cards.add(new Card(FaceCard.TEN, CardColor.DIAMOND));
		cards.add(new Card(FaceCard.SIX, CardColor.SPADE));
		cards.add(new Card(FaceCard.ACE, CardColor.HEART));
		cards.add(new Card(FaceCard.KING, CardColor.SPADE));
		assertEquals(PokerHandsType.PAIR, HandChecker.getFigureType(cards));
	}
	
	@Test
	public void testGetHighCard1() {
		cards.add(new Card(FaceCard.TWO, CardColor.CLUB));
		cards.add(new Card(FaceCard.TEN, CardColor.DIAMOND));
		cards.add(new Card(FaceCard.SIX, CardColor.SPADE));
		cards.add(new Card(FaceCard.EIGHT, CardColor.HEART));
		cards.add(new Card(FaceCard.KING, CardColor.SPADE));
		assertEquals(PokerHandsType.HIGH_CARD, HandChecker.getFigureType(cards));
	}
	
	@Test
	public void testGetHighCard2() {
		List<Card> highCardList = new ArrayList<>();
		cards.add(new Card(FaceCard.TWO, CardColor.CLUB));
		cards.add(new Card(FaceCard.TEN, CardColor.DIAMOND));
		cards.add(new Card(FaceCard.SIX, CardColor.SPADE));
		cards.add(new Card(FaceCard.EIGHT, CardColor.HEART));
		cards.add(new Card(FaceCard.KING, CardColor.SPADE));
		highCardList.add(new Card(FaceCard.KING, CardColor.SPADE));
		assertEquals(highCardList, HandChecker.getOnlyFigure(cards));
	}
	
	@Test
	public void testHasOwnPair() {
		List<Card> ownCards = new ArrayList<>();
		ownCards.add(new Card(FaceCard.SEVEN, CardColor.DIAMOND));
		ownCards.add(new Card(FaceCard.KING, CardColor.SPADE));
		cards.addAll(ownCards);
		cards.add(new Card(FaceCard.KING, CardColor.HEART));
		assertTrue(HandChecker.hasOwnPair(cards,  ownCards));
		cards.remove(new Card(FaceCard.KING, CardColor.HEART));
		cards.add(new Card(FaceCard.JACK, CardColor.DIAMOND));
		cards.add(new Card(FaceCard.JACK, CardColor.CLUB));
		assertFalse(HandChecker.hasOwnPair(cards,  ownCards));
	}
	
	@Test
	public void testHasOwnTwoPairs() {
		List<Card> ownCards = new ArrayList<>();
		ownCards.add(new Card(FaceCard.SEVEN, CardColor.DIAMOND));
		ownCards.add(new Card(FaceCard.KING, CardColor.SPADE));
		cards.addAll(ownCards);
		cards.add(new Card(FaceCard.KING, CardColor.HEART));
		cards.add(new Card(FaceCard.SEVEN, CardColor.HEART));
		assertTrue(HandChecker.hasOwnTwoPairs(cards,  ownCards));
		cards.remove(new Card(FaceCard.KING, CardColor.HEART));
		cards.remove(new Card(FaceCard.SEVEN, CardColor.HEART));
		cards.add(new Card(FaceCard.JACK, CardColor.DIAMOND));
		cards.add(new Card(FaceCard.JACK, CardColor.CLUB));
		cards.add(new Card(FaceCard.FOUR, CardColor.DIAMOND));
		cards.add(new Card(FaceCard.FOUR, CardColor.CLUB));
		assertFalse(HandChecker.hasOwnTwoPairs(cards,  ownCards));
	}
	
	@Test
	public void testHasOwnTwoPairsEachPair() {
		List<Card> ownCards = new ArrayList<>();
		ownCards.add(new Card(FaceCard.SEVEN, CardColor.DIAMOND));
		ownCards.add(new Card(FaceCard.KING, CardColor.SPADE));
		cards.addAll(ownCards);
		cards.add(new Card(FaceCard.KING, CardColor.HEART));
		cards.add(new Card(FaceCard.SEVEN, CardColor.HEART));
		assertTrue(HandChecker.hasOwnTwoPairsEachPair(cards,  ownCards));
		cards.remove(new Card(FaceCard.KING, CardColor.HEART));
		cards.add(new Card(FaceCard.JACK, CardColor.DIAMOND));
		cards.add(new Card(FaceCard.JACK, CardColor.CLUB));
		assertFalse(HandChecker.hasOwnTwoPairsEachPair(cards,  ownCards));
	}
	
	@Test
	public void testBetterHand() {
		List<Card> winnerCards = new ArrayList<>();
		List<Card> looserCards = new ArrayList<>();
		winnerCards.add(new Card(FaceCard.SIX, CardColor.SPADE));
		winnerCards.add(new Card(FaceCard.TEN, CardColor.DIAMOND));
		
		looserCards.add(new Card(FaceCard.KING, CardColor.CLUB));
		looserCards.add(new Card(FaceCard.KING, CardColor.DIAMOND));
		
		cards.add(new Card(FaceCard.SEVEN, CardColor.SPADE));
		cards.add(new Card(FaceCard.NINE, CardColor.SPADE));
		cards.add(new Card(FaceCard.SEVEN, CardColor.DIAMOND));
		cards.add(new Card(FaceCard.EIGHT, CardColor.CLUB));
		cards.add(new Card(FaceCard.QUEEN, CardColor.CLUB));
		winnerCards.addAll(cards);
		looserCards.addAll(cards);
		assertEquals(1, HandChecker.checkBetterHand(winnerCards, looserCards));
	}
	
	@Test
	public void testBetterHand2() {
		List<Card> winnerCards1 = new ArrayList<>();
		List<Card> winnerCards2 = new ArrayList<>();
		winnerCards1.add(new Card(FaceCard.SIX, CardColor.HEART));
		winnerCards1.add(new Card(FaceCard.KING, CardColor.SPADE));
		
		winnerCards2.add(new Card(FaceCard.EIGHT, CardColor.DIAMOND));
		winnerCards2.add(new Card(FaceCard.KING, CardColor.DIAMOND));
		
		cards.add(new Card(FaceCard.TEN, CardColor.CLUB));
		cards.add(new Card(FaceCard.TEN, CardColor.DIAMOND));
		cards.add(new Card(FaceCard.TWO, CardColor.SPADE));
		cards.add(new Card(FaceCard.TEN, CardColor.SPADE));
		cards.add(new Card(FaceCard.TWO, CardColor.DIAMOND));
		winnerCards1.addAll(cards);
		winnerCards2.addAll(cards);
		assertEquals(0, HandChecker.checkBetterHand(winnerCards1, winnerCards2));
	}
}
