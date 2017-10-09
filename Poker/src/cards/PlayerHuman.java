package cards;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public final class PlayerHuman extends Player {
	public BetPanel betPanel;
	private PlayersTurn newTurn;

	public PlayerHuman(String name, int place) {
		super(name, place);
		newTurn = null;
		betPanel = new BetPanel();
		betPanel.setVisible(false);
	}

	@Override
	public PlayersTurn goThroughTurn(int cardsOnTable, int playerPosition, int playersNumber, int pot, int tableBet, int smallBlind, int startBet) {
		newTurn = null;
		betPanel.Initialization(tableBet, smallBlind, startBet);
		betPanel.setVisible(true);
		while (newTurn == null);
		betPanel.setVisible(false);
		return newTurn;
	}

	public class BetPanel extends JPanel implements ActionListener {
		private JButton foldButton = new JButton("Fold");
		private JButton checkButton = new JButton("Check");
		private JButton callButton = new JButton("Call");
		private JButton raiseButton = new JButton("Raise");
		private JButton allInButton = new JButton("All In");
		private JButton playersRaiseMinus = new JButton("-");
		private JButton playersRaisePlus = new JButton("+");
		private JLabel playersRaiseValue = new JLabel("0");
		private JLabel playersChipsAmount = new JLabel("Chips: " + chips);
		private int bet, smallBlind, startBet;
		private int playersRaise;
		private boolean checkButtonVisibility, callButtonVisibility, raiseButtonVisibility, allInButtonVisibility;
		
		public BetPanel() {
			setVisible(false);
			addButtons();
		}
		
		public void Initialization(int bet, int smallBlind, int startBet) {
			this.bet = bet;
			this.smallBlind = smallBlind;
			this.startBet = startBet;
			playersRaise = startBet;
			playersRaiseValue.setText("" + playersRaise);
			buttonVisibility(bet, startBet);
			arrangeElements();
		}
		
		private void arrangeElements() {
			GridLayout grid = new GridLayout(2, 4, 5, 5);
			setLayout(grid);
			removeAll();
			add(foldButton, 0);
			if (checkButtonVisibility)
				add(checkButton, 1);
			else if (callButtonVisibility)
				add(callButton, 1);
			if (raiseButtonVisibility) {
				add(raiseButton, 2);
				add(playersRaiseMinus, 4);
				add(playersRaisePlus, 5);
				add(playersRaiseValue, 6);
			}
			if (allInButtonVisibility)
				add(allInButton, 3);
			add(playersChipsAmount, 7);
		}
		
		private void addButtons() {
			foldButton.addActionListener(this);
			checkButton.addActionListener(this);
			callButton.addActionListener(this);
			raiseButton.addActionListener(this);
			allInButton.addActionListener(this);
			playersRaiseMinus.addActionListener(this);
			playersRaisePlus.addActionListener(this);
		}
		
		private void buttonVisibility(int bet, int startBet) {
			int valueToBet = bet - startBet;
			if (valueToBet < 0)
				valueToBet = 0;
			if (chips == 0) {
				checkButtonVisibility = true;
				callButtonVisibility = false;
				raiseButtonVisibility = false;
				allInButtonVisibility = false;
			}
			else if (valueToBet == 0) {
				checkButtonVisibility = true;
				callButtonVisibility = false;
				raiseButtonVisibility = true;
				allInButtonVisibility = true;
			}
			else if (chips < valueToBet) {
				checkButtonVisibility = false;
				callButtonVisibility = false;
				raiseButtonVisibility = false;
				allInButtonVisibility = true;
			}
			else if (chips == valueToBet) {
				checkButtonVisibility = false;
				callButtonVisibility = true;
				raiseButtonVisibility = false;
				allInButtonVisibility = true;
			}
			else if (chips > valueToBet) {
				checkButtonVisibility = false;
				callButtonVisibility = true;
				raiseButtonVisibility = true;
				allInButtonVisibility = true;
			}
		}
		
		private void raiseMinusButton() {
			playersRaise -= smallBlind;
			if (playersRaise < startBet)
				playersRaise = startBet;
			playersRaiseValue.setText("" + playersRaise);
			playersChipsAmount.setText("Chips: " + (chips - playersRaise + startBet));
		}
		
		private void raisePlusButton() {
			playersRaise += smallBlind;
			if (playersRaise - startBet > chips)
				playersRaise = chips + startBet;
			playersRaiseValue.setText("" + playersRaise);
			playersChipsAmount.setText("Chips: " + (chips - playersRaise + startBet));
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (arg0.getSource() == foldButton)
				newTurn = new PlayersTurn(Turn.FOLD, 0);
			else if (arg0.getSource() == checkButton)
				newTurn = new PlayersTurn(Turn.CHECK, 0);
			else if (arg0.getSource() == callButton)
				newTurn = new PlayersTurn(Turn.CALL, bet - startBet);
			else if (arg0.getSource() == raiseButton)
				newTurn = new PlayersTurn(Turn.RAISE, playersRaise - startBet);
			else if (arg0.getSource() == allInButton)
				newTurn = new PlayersTurn(Turn.ALL_IN, chips);
			else if (arg0.getSource() == playersRaiseMinus)
				raiseMinusButton();
			else if (arg0.getSource() == playersRaiseMinus)
				raisePlusButton();
		}
	} // end of BetPanel class
}
