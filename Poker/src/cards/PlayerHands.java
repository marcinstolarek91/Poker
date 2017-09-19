package cards;

import java.util.ArrayList;
import java.util.List;

public class PlayerHands {
	private Card playerCard1;
	private Card playerCard2;
	private Card flopCard1 = null;
	private Card flopCard2 = null;
	private Card flopCard3 = null;
	private Card turnCard = null;
	private Card riverCard = null;
	private List<Card> playerCards = new ArrayList<>();
	private List<Card> tableCards = new ArrayList<>();
	private List<Card> wholeCards = new ArrayList<>();
	private List<Card> hand = new ArrayList();
	
	public PlayerHands(Card card1, Card card2) {
		playerCard1 = card1;
		playerCard2 = card2;
		playerCards.add(card1);
		playerCards.add(card2);
		generateWholeCards();
	}
	
	public PlayerHands(Card card1, Card card2, Card flop1, Card flop2, Card flop3) {
		this(card1, card2);
		flopCard1 = flop1;
		flopCard2 = flop2;
		flopCard3 = flop3;
		tableCards.add(flop1);
		tableCards.add(flop2);
		tableCards.add(flop3);
		generateWholeCards();
	}
	
	public PlayerHands(Card card1, Card card2, Card flop1, Card flop2, Card flop3, Card turn) {
		this(card1, card2, flop1, flop2, flop3);
		turnCard = turn;
		tableCards.add(turn);
		generateWholeCards();
	}
	
	public PlayerHands(Card card1, Card card2, Card flop1, Card flop2, Card flop3, Card turn, Card river) {
		this(card1, card2, flop1, flop2, flop3, turn);
		riverCard = river;
		tableCards.add(river);
		generateWholeCards();
	}
	
	public void addFlop(Card flop1, Card flop2, Card flop3) {
		if (flopCard1 != null)
			tableCards.remove(flopCard1);
		if (flopCard2 != null)
			tableCards.remove(flopCard2);
		if (flopCard3 != null)
			tableCards.remove(flopCard3);
		tableCards.add(flop1);
		tableCards.add(flop2);
		tableCards.add(flop3);
		flopCard1 = flop1;
		flopCard2 = flop2;
		flopCard3 = flop3;
		generateWholeCards();
	}
	
	public void addTurn(Card turn) {
		if (turnCard != null)
			tableCards.remove(turnCard);
		tableCards.add(turn);
		turnCard = turn;
		generateWholeCards();
	}
	
	public void addRiver(Card river) {
		if (riverCard != null)
			tableCards.remove(riverCard);
		tableCards.add(river);
		riverCard = river;
		generateWholeCards();
	}
	
	private void generateWholeCards() {
		wholeCards.clear();
		wholeCards.addAll(playerCards);
		wholeCards.addAll(tableCards);
	}
	
	public float chanceToPair() {
		
		return 0;
	}
}
