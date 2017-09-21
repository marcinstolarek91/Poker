package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cards.Card;
import cards.FaceCard;
import cards.CardColor;
import cards.ProbabilityChecker;

public class TestProbabilityChecker {
	List<Card> cards = new ArrayList<>();
	List<Card> ownCards = new ArrayList<>();

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testGetProbabilityOneCard() {
		assertEquals(0.487F, ProbabilityChecker.checkChanceToOwnPair(5, 2), 0.1F);
	}
	
	@Test
	public void testCheckChanceToOwnPair1() {
		ownCards.add(new Card(FaceCard.JACK, CardColor.DIAMOND));
		ownCards.add(new Card(FaceCard.TWO, CardColor.HEART));
		cards.addAll(ownCards);
		assertEquals(0.487F, ProbabilityChecker.checkChanceToOwnPair(cards, ownCards, 5), 0.001F);
		cards.remove(0);
		assertEquals(0F, ProbabilityChecker.checkChanceToOwnPair(cards, ownCards, 5), 0.0005F);
		cards.add(new Card(FaceCard.ACE, CardColor.SPADE));
		assertEquals(0F, ProbabilityChecker.checkChanceToOwnPair(cards, ownCards, 5), 0.0005F);
	}
	
	@Test
	public void testCheckChanceToOwnPair2() {
		ownCards.add(new Card(FaceCard.JACK, CardColor.DIAMOND));
		ownCards.add(new Card(FaceCard.TWO, CardColor.HEART));
		cards.addAll(ownCards);
		cards.add(new Card(FaceCard.TWO, CardColor.DIAMOND));
		cards.add(new Card(FaceCard.ACE, CardColor.CLUB));
		cards.add(new Card(FaceCard.SIX, CardColor.CLUB));
		assertEquals(1.0F, ProbabilityChecker.checkChanceToOwnPair(cards, ownCards, 2), 0.0005F);
		cards.add(new Card(FaceCard.TWO, CardColor.SPADE));
		assertEquals(0.0F, ProbabilityChecker.checkChanceToOwnPair(cards, ownCards, 2), 0.0005F);
	}
	
	@Test
	public void testCheckChanceToOwnThree1() {
		ownCards.add(new Card(FaceCard.JACK, CardColor.DIAMOND));
		ownCards.add(new Card(FaceCard.TWO, CardColor.HEART));
		cards.addAll(ownCards);
		cards.add(new Card(FaceCard.SIX, CardColor.DIAMOND));
		cards.add(new Card(FaceCard.ACE, CardColor.CLUB));
		cards.add(new Card(FaceCard.KING, CardColor.CLUB));
		assertEquals(0.00555F, ProbabilityChecker.checkChanceToOwnThree(cards, ownCards, 2), 0.0005F);
		cards.add(new Card(FaceCard.JACK, CardColor.HEART));
		assertEquals(0.0435F, ProbabilityChecker.checkChanceToOwnThree(cards, ownCards, 1), 0.0005F);
	}
	
	@Test
	public void testCheckChanceToOwnThree2() {
		ownCards.add(new Card(FaceCard.JACK, CardColor.DIAMOND));
		ownCards.add(new Card(FaceCard.TWO, CardColor.HEART));
		cards.addAll(ownCards);
		cards.add(new Card(FaceCard.SIX, CardColor.DIAMOND));
		cards.add(new Card(FaceCard.ACE, CardColor.CLUB));
		cards.add(new Card(FaceCard.KING, CardColor.CLUB));
		assertEquals(0.0056F, ProbabilityChecker.checkChanceToOwnThree(cards, ownCards, 2), 0.0005F);
	}
	
	@Test
	public void testCheckChanceToOwnThree3() {
		ownCards.add(new Card(FaceCard.JACK, CardColor.DIAMOND));
		ownCards.add(new Card(FaceCard.JACK, CardColor.HEART));
		cards.addAll(ownCards);
		cards.add(new Card(FaceCard.SIX, CardColor.DIAMOND));
		cards.add(new Card(FaceCard.ACE, CardColor.CLUB));
		cards.add(new Card(FaceCard.KING, CardColor.CLUB));
		assertEquals(0.0842F, ProbabilityChecker.checkChanceToOwnThree(cards, ownCards, 2), 0.0005F);
	}
}
