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
	
	public PlayerCards() {
		ownCard1 = null;
		ownCard2 = null;
	}
	
	public PlayerCards(Card card1, Card card2) {
		ownCard1 = card1;
		ownCard2 = card2;
	}
	
	public PlayerCards(Card card1, Card card2, Card flop1, Card flop2, Card flop3) {
		this(card1, card2);
		flopCard1 = flop1;
		flopCard2 = flop2;
		flopCard3 = flop3;
	}
	
	public PlayerCards(Card card1, Card card2, Card flop1, Card flop2, Card flop3, Card turn) {
		this(card1, card2, flop1, flop2, flop3);
		turnCard = turn;
	}
	
	public PlayerCards(Card card1, Card card2, Card flop1, Card flop2, Card flop3, Card turn, Card river) {
		this(card1, card2, flop1, flop2, flop3, turn);
		riverCard = river;
	}
	
	public void addOwnCards(Card card1, Card card2) {
		ownCard1 = card1;
		ownCard2 = card2;
	}
	
	public void addFlop(Card flop1, Card flop2, Card flop3) {
		flopCard1 = flop1;
		flopCard2 = flop2;
		flopCard3 = flop3;
	}
	
	public void addTurn(Card turn) {
		turnCard = turn;
	}
	
	public void addRiver(Card river) {
		riverCard = river;
	}
	
	public List<Card> getOwnCards(){
		List<Card> ownCards = new ArrayList<>();
		if (ownCard2 != null)
			ownCards.add(ownCard2);
		if (ownCard1 != null)
			ownCards.add(ownCard1);
		return ownCards;
	}
	
	public List<Card> getCards(){
		List<Card> cards = new ArrayList<>();
		if (riverCard != null)
			cards.add(riverCard);
		if (turnCard != null)
			cards.add(turnCard);
		if (flopCard3 != null)
			cards.add(flopCard3);
		if (flopCard2 != null)
			cards.add(flopCard2);
		if (flopCard1 != null)
			cards.add(flopCard1);
		if (ownCard2 != null)
			cards.add(ownCard2);
		if (ownCard1 != null)
			cards.add(ownCard1);
		return cards;
	}
	
	public List<Card> getHand(){
		return HandChecker.getHand(getCards());
	}
	
	public void resetCards() {
		ownCard1 = null;
		ownCard2 = null;
		flopCard1 = null;
		flopCard2 = null;
		flopCard3 = null;
		turnCard = null;
		riverCard = null;
	}
}
