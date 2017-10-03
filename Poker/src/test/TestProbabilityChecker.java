package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cards.Card;
import cards.CardColor;
import cards.FaceCard;
import cards.HandChecker;
import cards.ProbabilityChecker;

public class TestProbabilityChecker {
	private List<Card> cards = new ArrayList<>();
	private List<Card> ownCards = new ArrayList<>();

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testCheckChanceToOwnPair1() {
		ownCards.add(new Card(FaceCard.JACK, CardColor.DIAMOND));
		ownCards.add(new Card(FaceCard.TWO, CardColor.HEART));
		cards.addAll(ownCards);
		assertEquals(0.487F, ProbabilityChecker.checkChanceToOwnPair(cards, ownCards), 0.001F);
		cards.remove(0);
		assertEquals(0F, ProbabilityChecker.checkChanceToOwnPair(cards, ownCards), 0.0005F);
		cards.add(new Card(FaceCard.ACE, CardColor.SPADE));
		assertEquals(0F, ProbabilityChecker.checkChanceToOwnPair(cards, ownCards), 0.0005F);
	}
	
	@Test
	public void testCheckChanceToOwnPair2() {
		ownCards.add(new Card(FaceCard.JACK, CardColor.DIAMOND));
		ownCards.add(new Card(FaceCard.TWO, CardColor.HEART));
		cards.addAll(ownCards);
		cards.add(new Card(FaceCard.TWO, CardColor.DIAMOND));
		cards.add(new Card(FaceCard.ACE, CardColor.CLUB));
		cards.add(new Card(FaceCard.SIX, CardColor.CLUB));
		assertEquals(1.0F, ProbabilityChecker.checkChanceToOwnPair(cards, ownCards), 0.0005F);
		cards.add(new Card(FaceCard.TWO, CardColor.SPADE));
		assertEquals(0.0F, ProbabilityChecker.checkChanceToOwnPair(cards, ownCards), 0.0005F);
	}
	
	@Test
	public void testCheckChanceToOwnThree1() {
		ownCards.add(new Card(FaceCard.JACK, CardColor.DIAMOND));
		ownCards.add(new Card(FaceCard.TWO, CardColor.HEART));
		cards.addAll(ownCards);
		cards.add(new Card(FaceCard.SIX, CardColor.DIAMOND));
		cards.add(new Card(FaceCard.ACE, CardColor.CLUB));
		cards.add(new Card(FaceCard.KING, CardColor.CLUB));
		assertEquals(0.00555F, ProbabilityChecker.checkChanceToOwnThree(cards, ownCards), 0.0005F);
		cards.add(new Card(FaceCard.JACK, CardColor.HEART));
		assertEquals(0.0435F, ProbabilityChecker.checkChanceToOwnThree(cards, ownCards), 0.0005F);
	}
	
	@Test
	public void testCheckChanceToOwnThree2() {
		ownCards.add(new Card(FaceCard.JACK, CardColor.DIAMOND));
		ownCards.add(new Card(FaceCard.TWO, CardColor.HEART));
		cards.addAll(ownCards);
		cards.add(new Card(FaceCard.SIX, CardColor.DIAMOND));
		cards.add(new Card(FaceCard.ACE, CardColor.CLUB));
		cards.add(new Card(FaceCard.KING, CardColor.CLUB));
		assertEquals(0.0056F, ProbabilityChecker.checkChanceToOwnThree(cards, ownCards), 0.0005F);
	}
	
	@Test
	public void testCheckChanceToOwnThree3() {
		ownCards.add(new Card(FaceCard.JACK, CardColor.DIAMOND));
		ownCards.add(new Card(FaceCard.JACK, CardColor.HEART));
		cards.addAll(ownCards);
		cards.add(new Card(FaceCard.SIX, CardColor.DIAMOND));
		cards.add(new Card(FaceCard.ACE, CardColor.CLUB));
		cards.add(new Card(FaceCard.KING, CardColor.CLUB));
		assertEquals(0.0842F, ProbabilityChecker.checkChanceToOwnThree(cards, ownCards), 0.0005F);
	}
	
	@Test
	public void testCheckChanceToOwnTwoPairs1() {
		Card newCard;
		int twoPairsCounter = 0;
		int maxTries = 100000;
		float result;
		ownCards.add(new Card(FaceCard.ACE, CardColor.DIAMOND));
		ownCards.add(new Card(FaceCard.ACE, CardColor.HEART));
		cards.addAll(ownCards);
		for (int i = 0; i < maxTries; i++) {
			cards.clear();
			cards.addAll(ownCards);
			/*cards.add(new Card(FaceCard.THREE, CardColor.HEART));
			cards.add(new Card(FaceCard.JACK, CardColor.SPADE));
			cards.add(new Card(FaceCard.KING, CardColor.CLUB));*/
			while (cards.size() != 7) {
				do {
					newCard = null;
					newCard = new Card((new Random()).nextInt(52));
				} while(cards.contains(newCard));
				cards.add(newCard);
			}
			if (HandChecker.hasOwnTwoPairs(cards, ownCards))
				++twoPairsCounter;
		}
		cards.clear();
		cards.addAll(ownCards);
		/*cards.add(new Card(FaceCard.THREE, CardColor.HEART));
		cards.add(new Card(FaceCard.JACK, CardColor.SPADE));
		cards.add(new Card(FaceCard.KING, CardColor.CLUB));*/
		result = (float) twoPairsCounter / (float) maxTries;
		assertEquals(result, ProbabilityChecker.checkChanceToOwnTwoPairs(cards, ownCards), 0.005F);
	}
	
	@Test
	public void testCheckChanceToOwnFlush1() {
		Card newCard;
		int flushCounter = 0;
		int maxTries = 100000;
		float result;
		ownCards.add(new Card(FaceCard.ACE, CardColor.SPADE));
		ownCards.add(new Card(FaceCard.TEN, CardColor.SPADE));
		cards.addAll(ownCards);
		for (int i = 0; i < maxTries; i++) {
			cards.clear();
			cards.addAll(ownCards);
			cards.add(new Card(FaceCard.THREE, CardColor.SPADE));
			cards.add(new Card(FaceCard.JACK, CardColor.DIAMOND));
			cards.add(new Card(FaceCard.KING, CardColor.CLUB));
			while (cards.size() != 7) {
				do {
					newCard = null;
					newCard = new Card((new Random()).nextInt(52));
				} while(cards.contains(newCard));
				cards.add(newCard);
			}
			if (HandChecker.hasOwnFlush(cards, ownCards))
				++flushCounter;
		}
		cards.clear();
		cards.addAll(ownCards);
		cards.add(new Card(FaceCard.THREE, CardColor.SPADE));
		cards.add(new Card(FaceCard.JACK, CardColor.DIAMOND));
		cards.add(new Card(FaceCard.KING, CardColor.CLUB));
		result = (float) flushCounter / (float) maxTries;
		assertEquals(result, ProbabilityChecker.checkChanceToOwnFlush(cards, ownCards), 0.015F);
	}
	
	@Test
	public void testCheckChanceToOwnStraight1() {
		Card newCard;
		int straightCounter = 0;
		int maxTries = 100000;
		float result;
		ownCards.add(new Card(FaceCard.SIX, CardColor.SPADE));
		ownCards.add(new Card(FaceCard.QUEEN, CardColor.HEART));
		cards.addAll(ownCards);
		for (int i = 0; i < maxTries; i++) {
			cards.clear();
			cards.addAll(ownCards);
			cards.add(new Card(FaceCard.THREE, CardColor.SPADE));
			cards.add(new Card(FaceCard.FOUR, CardColor.DIAMOND));
			cards.add(new Card(FaceCard.SEVEN, CardColor.CLUB));
			//cards.add(new Card(FaceCard.TWO, CardColor.CLUB));
			while (cards.size() != 7) {
				do {
					newCard = null;
					newCard = new Card((new Random()).nextInt(52));
				} while(cards.contains(newCard));
				cards.add(newCard);
			}
			if (HandChecker.hasOwnStraight(cards, ownCards))
				++straightCounter;
		}
		cards.clear();
		cards.addAll(ownCards);
		cards.add(new Card(FaceCard.THREE, CardColor.SPADE));
		cards.add(new Card(FaceCard.FOUR, CardColor.DIAMOND));
		cards.add(new Card(FaceCard.SEVEN, CardColor.CLUB));
		//cards.add(new Card(FaceCard.TWO, CardColor.CLUB));
		result = (float) straightCounter / (float) maxTries;
		assertEquals(result, ProbabilityChecker.checkChanceToOwnStraight(cards, ownCards), 0.005F);
	}
	
	@Test
	public void testCheckChanceToOwnPoker1() {
		Card newCard;
		int pokerCounter = 0;
		int maxTries = 100000;
		float result;
		ownCards.add(new Card(FaceCard.SIX, CardColor.HEART));
		ownCards.add(new Card(FaceCard.QUEEN, CardColor.HEART));
		cards.addAll(ownCards);
		for (int i = 0; i < maxTries; i++) {
			cards.clear();
			cards.addAll(ownCards);
			cards.add(new Card(FaceCard.THREE, CardColor.HEART));
			cards.add(new Card(FaceCard.FOUR, CardColor.HEART));
			cards.add(new Card(FaceCard.SEVEN, CardColor.HEART));
			//cards.add(new Card(FaceCard.TWO, CardColor.HEART));
			while (cards.size() != 7) {
				do {
					newCard = null;
					newCard = new Card((new Random()).nextInt(52));
				} while(cards.contains(newCard));
				cards.add(newCard);
			}
			if (HandChecker.hasOwnPoker(cards, ownCards))
				++pokerCounter;
		}
		cards.clear();
		cards.addAll(ownCards);
		cards.add(new Card(FaceCard.THREE, CardColor.HEART));
		cards.add(new Card(FaceCard.FOUR, CardColor.HEART));
		cards.add(new Card(FaceCard.SEVEN, CardColor.HEART));
		//cards.add(new Card(FaceCard.TWO, CardColor.HEART));
		result = (float) pokerCounter / (float) maxTries;
		assertEquals(result, ProbabilityChecker.checkChanceToOwnPoker(cards, ownCards), 0.005F);
	}
}
