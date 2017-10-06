package game;

import javax.swing.JPanel;

import cards.Card;

public class PlayerPanel extends JPanel {
	public boolean cardsToShow;
	public int chips;
	public int bid;
	private String playerName;
	private Card card1, card2;
	private int cardsPosition; // like in numeric keyboard - around 5 (7, 8, 9 - up, etc)
	
	public PlayerPanel(String playerName, int cardsPosition) {
		super();
		this.playerName = playerName;
		this.cardsPosition = cardsPosition;
	}
	
	public void shuffleNewCard(Card newCard1, Card newCard2) {
		if (card1 == null)
			card1 = new Card(newCard1.faceCard, newCard1.cardColor);
		else
			card1.changeCard(newCard1);
		if (card2 == null)
			card2 = new Card(newCard2.faceCard, newCard2.cardColor);
		else
			card2.changeCard(newCard2);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + cardsPosition;
		result = prime * result + ((playerName == null) ? 0 : playerName.hashCode());
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
		PlayerPanel other = (PlayerPanel) obj;
		if (cardsPosition != other.cardsPosition)
			return false;
		if (playerName == null) {
			if (other.playerName != null)
				return false;
		} else if (!playerName.equals(other.playerName))
			return false;
		return true;
	}
	
	
}
