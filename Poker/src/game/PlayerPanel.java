package game;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cards.Card;

public class PlayerPanel extends JPanel {
			boolean cardsToShow;
			int chips;
			int bet;
	private String playerName;
	private Card card1, card2;
	private int nameAndChipsPosition; // 0 - up, 1 - center, 2 - down
	private int statusAndBetPosition; // 0 - up, 1 - center, 2 - down
	private int cardsPosition; // 0 - up, 1 - center, 2 - down
	private JLabel card1Label, card2Label, nameLabel, chipsLabel, statusLabel, betLabel;
	private final Color ORIGINAL_FOREGROUND_COLOR = Color.ORANGE;
	private final Color PASSED_FOREGROUND_COLOR = Color.RED;
	private final Color ALL_IN_FOREGROUND_COLOR = Color.BLUE;
	private final Color PLAYER_MOVE_FOREGROUND_COLOR = Color.MAGENTA;
	private final Color PLAYER_NOT_MOVE_FOREGROUND_COLOR = Color.BLACK;
	
	public PlayerPanel(String playerName, int playerPosition, boolean showCards) {
		super();
		setLayout(null);
		this.playerName = playerName;
		cardsToShow = showCards;
		calculateElementsPosition(playerPosition);
		placeElements();
	}
	
	public String getPlayerName() {
		return playerName;
	}
	
	/**
	 * Reset view, bet and cardsToShow status
	 * @param showCards
	 * @param newDeal - if newDeal then reset bet and view
	 */
	public void reset(boolean showCards, boolean newDeal) {
		if (newDeal) {
			bet = 0;
			resetStatusView();
		}
		cardsToShow = showCards;
		refresh();
	}
	
	public void setChips(int chipsAmount) {
		chips = chipsAmount;
		refresh();
	}
	
	public void addBet(int betAmount) {
		bet += betAmount;
		chips -= betAmount;
		refresh();
	}
	
	public void resetBet() {
		bet = 0;
		refresh();
	}
	
	public void setStatus(String status) {
		statusLabel.setText(status);
		if (status.equals("Passed"))
			setStatusPassedView();
		else if (status.equals("All-in"))
			setStatusAllInView();
		else
			resetStatusView();
	}
	
	public void setCardsVisible() {
		cardsToShow = true;
		refresh();
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
		refresh();
	}
	
	public void setPlayerMoveView() {
		nameLabel.setForeground(PLAYER_MOVE_FOREGROUND_COLOR);
	}
	
	public void resetPlayerMoveView() {
		nameLabel.setForeground(PLAYER_NOT_MOVE_FOREGROUND_COLOR);
	}
	
	private void refresh() {
		if (cardsToShow) {
			card1Label.setIcon(new ImageIcon(Card.getCardPictureName(card1)));
			card2Label.setIcon(new ImageIcon(Card.getCardPictureName(card2)));
		}
		else {
			card1Label.setIcon(new ImageIcon(Card.getCardPictureName(-1)));
			card2Label.setIcon(new ImageIcon(Card.getCardPictureName(-1)));
		}
		chipsLabel.setText(chipsString());
		betLabel.setText(betString());
	}
	
	private void setStatusPassedView() {
		statusLabel.setForeground(PASSED_FOREGROUND_COLOR);
	}
	
	private void setStatusAllInView() {
		statusLabel.setForeground(ALL_IN_FOREGROUND_COLOR);
	}
	
	private void resetStatusView() {
		statusLabel.setForeground(ORIGINAL_FOREGROUND_COLOR);
	}
	
	private void placeElements() {
		card1Label = new JLabel(new ImageIcon(Card.getCardPictureName(-1)));
		card1Label.setSize(44, 56);
		card1Label.setLocation(10, 2 + 30 * cardsPosition);
		card2Label = new JLabel(new ImageIcon(Card.getCardPictureName(-1)));
		card2Label.setSize(44, 56);
		card2Label.setLocation(66, 2 + 30 * cardsPosition);
		nameLabel = new JLabel(playerName);
		nameLabel.setSize(60, 30);
		nameLabel.setLocation(0, 5 + 60 * nameAndChipsPosition);
		resetPlayerMoveView();
		chipsLabel = new JLabel(chipsString());
		chipsLabel.setSize(60, 30);
		chipsLabel.setLocation(60, 5 + 60 * nameAndChipsPosition);
		statusLabel = new JLabel("Active");
		statusLabel.setSize(60, 30);
		statusLabel.setLocation(0, 5 + 90 - 60 * (2 - statusAndBetPosition));
		resetStatusView();
		betLabel = new JLabel(betString());
		betLabel.setSize(60, 30);
		betLabel.setLocation(60, 5 + 90 - 60 * (2 - statusAndBetPosition));
		add(card1Label);
		add(card2Label);
		add(nameLabel);
		add(chipsLabel);
		add(statusLabel);
		add(betLabel);
	}
	
	private String chipsString() {
		return "Sum:" + chips;
	}
	
	private String betString() {
		return "Bet:" + bet;
	}
	
	private void calculateElementsPosition(int playerPosition) {
		if ((playerPosition >= 0 && playerPosition <= 2) || playerPosition == 9) {
			cardsPosition = 0;
			nameAndChipsPosition = 1;
			statusAndBetPosition = 2;
		}
		else if (playerPosition == 3 || playerPosition == 8) {
			cardsPosition = 1;
			nameAndChipsPosition = 0;
			statusAndBetPosition = 2;
		}
		else {
			cardsPosition = 2;
			nameAndChipsPosition = 0;
			statusAndBetPosition = 1;
		}
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
