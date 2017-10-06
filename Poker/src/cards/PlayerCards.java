package cards;

import java.util.ArrayList;
import java.util.List;

public class PlayerCards {
	public Card ownCard1;
	public Card ownCard2;
	private Card flopCard1 = null;
	private Card flopCard2 = null;
	private Card flopCard3 = null;
	private Card turnCard = null;
	private Card riverCard = null;
	private List<Card> cards = new ArrayList();
	private List<Card> hand = new ArrayList();
	
	public PlayerCards() {
		ownCard1 = null;
		ownCard2 = null;
	}
	
	public PlayerCards(Card card1, Card card2) {
		ownCard1 = card1;
		ownCard2 = card2;
		cards.add(card1);
		cards.add(card2);
	}
	
	public PlayerCards(Card card1, Card card2, Card flop1, Card flop2, Card flop3) {
		this(card1, card2);
		flopCard1 = flop1;
		flopCard2 = flop2;
		flopCard3 = flop3;
		cards.add(flop1);
		cards.add(flop2);
		cards.add(flop3);
	}
	
	public PlayerCards(Card card1, Card card2, Card flop1, Card flop2, Card flop3, Card turn) {
		this(card1, card2, flop1, flop2, flop3);
		turnCard = turn;
		cards.add(turn);
	}
	
	public PlayerCards(Card card1, Card card2, Card flop1, Card flop2, Card flop3, Card turn, Card river) {
		this(card1, card2, flop1, flop2, flop3, turn);
		riverCard = river;
		cards.add(river);
	}
	
	public void addOwnCards(Card card1, Card card2) {
		if (ownCard1 != null)
			cards.remove(ownCard1);
		if (ownCard2 != null)
			cards.remove(ownCard2);
		cards.add(card1);
		cards.add(card2);
		ownCard1 = card1;
		ownCard2 = card2;
	}
	
	public void addFlop(Card flop1, Card flop2, Card flop3) {
		if (flopCard1 != null)
			cards.remove(flopCard1);
		if (flopCard2 != null)
			cards.remove(flopCard2);
		if (flopCard3 != null)
			cards.remove(flopCard3);
		cards.add(flop1);
		cards.add(flop2);
		cards.add(flop3);
		flopCard1 = flop1;
		flopCard2 = flop2;
		flopCard3 = flop3;
	}
	
	public void addTurn(Card turn) {
		if (turnCard != null)
			cards.remove(turnCard);
		cards.add(turn);
		turnCard = turn;
	}
	
	public void addRiver(Card river) {
		if (riverCard != null)
			cards.remove(riverCard);
		cards.add(river);
		riverCard = river;
	}
	
	public void resetCards() {
		cards.clear();
		hand.clear();
		ownCard1 = null;
		ownCard2 = null;
		flopCard1 = null;
		flopCard2 = null;
		flopCard3 = null;
		turnCard = null;
		riverCard = null;
	}
	
	public void generateHand() {
		hand = HandChecker.getHand(cards);
	}
}
