package game;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cards.PlayersTurn;
import cards.Turn;

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
		betPanel.Initialization(tableBet, smallBlind, startBet, tableBet);
		betPanel.setVisible(true);
		while (newTurn == null) // TODO - nie sprawdza na biezaco warunku
			Delay.sleep(5);
		chips -= newTurn.bid;
		betPanel.setVisible(false);
		betPanel.reset();
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
		private JLabel empty = new JLabel("");
		private int bet, smallBlind, startBet;
		private int playersRaise;
		private int minimumPlayersRaise;
		private boolean checkButtonVisibility, callButtonVisibility, raiseButtonVisibility, allInButtonVisibility;
		
		public BetPanel() {
			setVisible(false);
			addButtons();
		}
		
		public void reset() {
			bet = 0;
			smallBlind = 0;
			startBet = 0;
			playersRaise = 0;
		}
		
		public void Initialization(int bet, int smallBlind, int startBet, int tableBet) {
			this.bet = bet;
			this.smallBlind = smallBlind;
			this.startBet = startBet;
			playersRaise = tableBet;
			minimumPlayersRaise = tableBet;
			playersRaiseValue.setText("Bet: " + playersRaise);
			playersChipsAmount.setText("Chips: " + (chips - playersRaise + startBet));
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
			else
				add(new JLabel(""), 1);
			if (raiseButtonVisibility)
				add(raiseButton, 2);
			else
				add(new JLabel(""), 2);
			if (allInButtonVisibility)
				add(allInButton, 3);
			else
				add(new JLabel(""), 3);
			if (raiseButtonVisibility) {
				add(playersRaiseMinus, 4);
				add(playersRaisePlus, 5);
				add(playersRaiseValue, 6);
			}
			else {
				add(new JLabel(""), 4);
				add(new JLabel(""), 5);
				add(new JLabel(""), 6);
			}
			add(playersChipsAmount, 7);
			raiseButton.setEnabled(false);
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
			if (playersRaise <= minimumPlayersRaise) {
				playersRaise = minimumPlayersRaise;
				raiseButton.setEnabled(false);
			}
			playersRaiseValue.setText("Bet: " + playersRaise);
			playersChipsAmount.setText("Chips: " + (chips - playersRaise + startBet));
		}
		
		private void raisePlusButton() {
			raiseButton.setEnabled(true);
			playersRaise += smallBlind;
			if (playersRaise - startBet > chips)
				playersRaise = chips + startBet;
			playersRaiseValue.setText("Bet: " + playersRaise);
			playersChipsAmount.setText("Chips: " + (chips - playersRaise + startBet));
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (arg0.getSource() == foldButton) {
				newTurn = new PlayersTurn(Turn.FOLD, 0);
				active = false;
			}
			else if (arg0.getSource() == checkButton)
				newTurn = new PlayersTurn(Turn.CHECK, 0);
			else if (arg0.getSource() == callButton) {
				if ((playersRaise - startBet) == chips) {
					newTurn = new PlayersTurn(Turn.ALL_IN, chips);
					activeAllIn = true;
				}
				else
					newTurn = new PlayersTurn(Turn.CALL, bet - startBet);
			}
			else if (arg0.getSource() == raiseButton && playersRaise > minimumPlayersRaise) {
				if ((playersRaise - startBet) == chips) {
					newTurn = new PlayersTurn(Turn.ALL_IN, chips);
					activeAllIn = true;
				}
				else
					newTurn = new PlayersTurn(Turn.RAISE, playersRaise - startBet);
			}
			else if (arg0.getSource() == allInButton) {
				newTurn = new PlayersTurn(Turn.ALL_IN, chips);
				activeAllIn = true;
			}
			else if (arg0.getSource() == playersRaiseMinus)
				raiseMinusButton();
			else if (arg0.getSource() == playersRaisePlus)
				raisePlusButton();
		}
	} // end of BetPanel class
}
