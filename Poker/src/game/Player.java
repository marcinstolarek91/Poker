package game;

import java.util.List;

import cards.Card;
import cards.PlayerCards;
import cards.PlayersTurn;

public abstract class Player {
	public String name;
	public PlayerCards cards;
	public int place;
	protected int chips;
	protected boolean active;
	protected boolean activeAllIn; // active but cannot raise anymore (all-in)
	
	public Player(String name, int place) {
		active = true;
		activeAllIn = false;
		this.name = name;
		this.place = place;
		chips = 0;
		cards = new PlayerCards();
	}
	
	public Player(String name, int place, int chips) {
		this(name, place);
		this.chips = chips;
	}
	
	public int getChips() {
		return chips;
	}
	
	public void setChips(int chipsAmount) {
		chips = chipsAmount;
	}
	
	public void withdrawChips(int chipsAmount) {
		chips -= chipsAmount;
		if (chips < 0)
			chips = 0;
	}
	
	public void addChips(int chipsAmount) {
		chips += chipsAmount;
	}
	
	public void addOwnCard(Card card1, Card card2) {
		cards.addOwnCards(card1, card2);
	}
	
	public boolean isActive() {
		return active;
	}
	
	public boolean isActiveAllIn() {
		return activeAllIn;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public void setActiveAllIn(boolean active) {
		activeAllIn = active;
	}
	
	public void addFlop(Card card1, Card card2, Card card3) {
		cards.addFlop(card1, card2, card3);
	}
	
	public void addTurn(Card card) {
		cards.addTurn(card);
	}
	
	public void addRiver(Card card) {
		cards.addRiver(card);
	}
	
	public void resetCards() {
		cards.resetCards();
	}
	
	public List<Card> getOwnCards(){
		return cards.getOwnCards();
	}
	
	public List<Card> getHand(){
		return cards.getHand();
	}
	
	public List<Card> getCards(){
		return cards.getCards();
	}
	
	/**
	 * Generate new player turn (kind of turn and bid)
	 * @param cardsOnTable - number of known cards on table
	 * @param playerPosition - player position on table (from active players) 1-10 (1 - player with small blind)
	 * @param playersNumber - numbers of active players
	 * @param pot - chips in pot (include all raises)
	 * @param tableBet - actual bet that player should call to be in game
	 * @param smallBlind - actual small blind
	 * @param startBet - start player bet (example big blind or bet in last turn)
	 * @return new turn (bid NOT include last bet)
	 */
	public abstract PlayersTurn goThroughTurn(int cardsOnTable, int playerPosition, int playersNumber, int pot, int tableBet, int smallBlind, int startBet);

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + place;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (place != other.place)
			return false;
		return true;
	}
}
