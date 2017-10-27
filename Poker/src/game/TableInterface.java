package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import cards.Card;
import cards.HandChecker;
import cards.PlayersTurn;
import cards.Turn;
// 44x56 - cards png
public class TableInterface extends JFrame implements ActionListener {
	private List<Player> players = new ArrayList<>();
	private JPanel playerInterface = new JPanel();
	private JPanel communityCardsPanel = new JPanel();
	private JLabel[] communityCard = new JLabel[5];
	private JLabel dealerSign = new JLabel("D");
	private JPanel infoPanel = new JPanel();
	private JLabel[] infoLabel = new JLabel[10];
	private int[] communityCardsNumbers = new int[5]; // -1 means empty slot
	private List<PlayerPanel> playerPanel = new ArrayList<>();
	private PlayerHuman human;
	private boolean showAICards = false;
	private boolean goAhead = false;
	private JButton continueButton = new JButton("Continue");
	private JLabel potLabel, betLabel;
	private Timer turnTimer = new Timer();
	private Timer infoTimer = new Timer();
	private static final int X_HIDDEN = 5, Y_HIDDEN = 28;
	private Player playerDealer;
	
	public TableInterface(int playersNumber, String[] names, int startChips, int startBid) {
		super("Poker Table");
		setVisible(true);
		setLayout(null);
		setLocation(0,0);
		setSize(1200 + X_HIDDEN, 850 + Y_HIDDEN);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addPlayers(playersNumber, names, startChips);
		addDealerSign();
		addPotAndBet();
		resetCommunityCards();
		generateCommunityCardsPanel();
		addContinueButton();
		addInfoPanel();
		playerDealer = players.get(0);
		play(startBid, 0);
		infoTimer.schedule(new InfoTask(), 0, 500);
	}
	
	private void addInfoPanel() {
		GridLayout grid = new GridLayout(10, 1);
		infoPanel.setSize(600, 200);
		infoPanel.setLocation(X_HIDDEN, 650 + Y_HIDDEN);
		infoPanel.setVisible(true);
		infoPanel.setLayout(grid);
		infoPanel.setOpaque(true);
		infoPanel.setBackground(Color.WHITE);
		for (int i = 0; i < 10; i++) {
			infoLabel[i] = new JLabel("");
			infoLabel[i].setForeground(Color.RED);
			infoPanel.add(infoLabel[i]);
		}
		add(infoPanel);
	}
	
	private void updateInfoPanel() {
		for (int i = 0; i < 10; i++) {
			if (Statement.infoList.size() > i) {
				infoLabel[i].setText(Statement.infoList.get(i));
				infoLabel[i].repaint();
			}
		}
		infoPanel.repaint();
	}
	
	private void resetCommunityCards() {
		for (int i = 0; i < 5; i++)
			communityCardsNumbers[i] = -1;
	}
	
	private void addContinueButton() {
		continueButton.setSize(110, 30);
		continueButton.setLocation(720 + X_HIDDEN, 650 + Y_HIDDEN);
		continueButton.setVisible(false);
		add(continueButton);
		continueButton.addActionListener(this);
	}
	
	private void addPlayers(int playersNumber, String[] names, int startChips) {
		int place = 0;
		human = new PlayerHuman(names[0], place);
		players.add(human);
		players.get(0).setChips(startChips);
		addPlayerPanel(names[0], place, true);
		for (int i = 2; i <= playersNumber; i++) {
			place = (int)((10.0F / (float)playersNumber) * (float)(i - 1));
			players.add(new PlayerAI(names[i - 1], place));
			players.get(i - 1).setChips(startChips);
			addPlayerPanel(names[i - 1], place, showAICards);
		}
		playerInterface = human.betPanel;
		playerInterface.setSize(450, 80);
		playerInterface.setLocation(720 + X_HIDDEN, 700 + Y_HIDDEN);
		add(playerInterface);
	}
	
	private void addDealerSign() {
		dealerSign.setFont(new Font("Arial", Font.BOLD, 14));
		dealerSign.setForeground(Color.RED);
		dealerSign.setBackground(Color.ORANGE);
		dealerSign.setHorizontalAlignment(SwingConstants.CENTER);
		dealerSign.setVerticalAlignment(SwingConstants.CENTER);
		dealerSign.setOpaque(true);
		dealerSign.setSize(30, 30);
		dealerSign.setLocation(playerPanel.get(0).getLocation().x + 45, playerPanel.get(0).getLocation().y - 30);
		add(dealerSign);
	}
	
	private void addPlayerPanel(String name, int place, boolean showCards) {
		PlayerPanel newPP = new PlayerPanel(name, place, showCards);
		newPP.setSize(120, 120);
		switch (place) {
			case 0: newPP.setLocation(360 + X_HIDDEN, 510 + Y_HIDDEN); break;
			case 1: newPP.setLocation(240 + X_HIDDEN, 510 + Y_HIDDEN); break;
			case 2: newPP.setLocation(120 + X_HIDDEN, 450 + Y_HIDDEN); break;
			case 3: newPP.setLocation(0 + X_HIDDEN, 270 + Y_HIDDEN); break;
			case 4: newPP.setLocation(120 + X_HIDDEN, 90 + Y_HIDDEN); break;
			case 5: newPP.setLocation(240 + X_HIDDEN, 30 + Y_HIDDEN); break;
			case 6: newPP.setLocation(360 + X_HIDDEN, 30 + Y_HIDDEN); break;
			case 7: newPP.setLocation(480 + X_HIDDEN, 90 + Y_HIDDEN); break;
			case 8: newPP.setLocation(600 + X_HIDDEN, 270 + Y_HIDDEN); break;
			case 9: newPP.setLocation(480 + X_HIDDEN, 450 + Y_HIDDEN); break;
			default: break;
		}
		playerPanel.add(newPP);
		add(newPP);
	}
	
	private void repaintPlayerPanels() {
		for (PlayerPanel pp : playerPanel)
			pp.repaint();
	}
	
	private void moveDealerSign(int place) {
		switch (place) {
			case 0: dealerSign.setLocation(360 + X_HIDDEN + 45, 510 + Y_HIDDEN - 30); break;
			case 1: dealerSign.setLocation(240 + X_HIDDEN + 45, 510 + Y_HIDDEN - 30); break;
			case 2: dealerSign.setLocation(120 + X_HIDDEN + 45, 450 + Y_HIDDEN - 30); break;
			case 3: dealerSign.setLocation(0 + X_HIDDEN + 120, 270 + Y_HIDDEN - 45); break;
			case 4: dealerSign.setLocation(120 + X_HIDDEN + 45, 90 + Y_HIDDEN + 120); break;
			case 5: dealerSign.setLocation(240 + X_HIDDEN + 45, 30 + Y_HIDDEN + 120); break;
			case 6: dealerSign.setLocation(360 + X_HIDDEN + 45, 30 + Y_HIDDEN + 120); break;
			case 7: dealerSign.setLocation(480 + X_HIDDEN + 45, 90 + Y_HIDDEN + 120); break;
			case 8: dealerSign.setLocation(600 + X_HIDDEN - 30, 270 + Y_HIDDEN - 45); break;
			case 9: dealerSign.setLocation(480 + X_HIDDEN + 45, 450 + Y_HIDDEN - 30); break;
			default: break;
		}
		dealerSign.repaint();
	}
	
	private void addPotAndBet() {
		potLabel = new JLabel("Pot: 0");
		betLabel = new JLabel("Bet: 0");
		potLabel.setFont(new Font("Arial", Font.BOLD, 14));
		potLabel.setSize(100, 30);
		potLabel.setLocation(260 + X_HIDDEN, 272 + Y_HIDDEN);
		potLabel.setHorizontalAlignment(SwingConstants.CENTER);
		betLabel.setFont(new Font("Arial", Font.BOLD, 14));
		betLabel.setSize(100, 30);
		betLabel.setLocation(360 + X_HIDDEN, 272 + Y_HIDDEN);
		betLabel.setHorizontalAlignment(SwingConstants.CENTER);
		add(potLabel);
		add(betLabel);
	}
	
	private void updatePotAndBet(int pot, int bet) {
		potLabel.setText("Pot: " + pot);
		betLabel.setText("Bet: " + bet);
	}
	
	private void generateCommunityCardsPanel() {
		GridLayout grid = new GridLayout(1, 5, 3, 0);
		communityCardsPanel.setLayout(grid);
		communityCardsPanel.setSize(250, 56);
		for (int i = 0; i < 5; i++) {
			communityCard[i] = new JLabel(new ImageIcon(Card.getCardPictureName(communityCardsNumbers[i])));
			communityCardsPanel.add(communityCard[i], i);
		}
		communityCardsPanel.setLocation(235 + X_HIDDEN, 302 + Y_HIDDEN);
		add(communityCardsPanel);
	}
	
	private void updateCommunityCards() {
		for (int i = 0; i < 5; i++) {
			communityCard[i].setIcon(new ImageIcon(Card.getCardPictureName(communityCardsNumbers[i])));
			communityCard[i].repaint();
			Statement.printProgrammerInfo("updateCommunityCards: " + communityCardsNumbers[i]);
		}
		communityCardsPanel.repaint();
		repaint();
	}
	
	private void showCards(int...playerIndex) {
		for (int i = 0; i < playerPanel.size(); i++) {
			for (int j = 0; j < playerIndex.length; j++) {
				if (i == playerIndex[j])
					playerPanel.get(i).reset(true, false);
			}
		}
	}
	
	private void hideCards() {
		for (int i = 0; i < playerPanel.size(); i++) {
			if (players.get(i) instanceof PlayerHuman)
				playerPanel.get(i).reset(true, false);
			else
				playerPanel.get(i).reset(showAICards, false);
		}
	}
	
	private void waitForAccept() { // accept by player
		Statement.printProgrammerInfo("Wait for accept");
		goAhead = false;
		continueButton.setVisible(true);
		while(!goAhead)
			Delay.sleep(5);
		goAhead = false;
		continueButton.setVisible(false);
	}
	
	private void generateNewDealer() {
		if (!players.contains(playerDealer)) { // playerDealer lost all chips
			int place = playerDealer.place + 1;
			playerDealer = null;
			for (;playerDealer == null; place++) {
				if (place > 10)
					place = 0;
				for (int i = 0; i < players.size(); i++) {
					if (players.get(i).place == place) {
						playerDealer = players.get(i);
					}
				}				
			}
		}
		else {
			if (players.indexOf(playerDealer) == players.size() - 1)
				playerDealer = players.get(0);
			else
				playerDealer = players.get(players.indexOf(playerDealer) + 1);
		}
	}
	
	private void play(int startSmallBlind, int deals) {
		turnTimer.cancel();
		turnTimer.purge();
		turnTimer = new Timer();
		turnTimer.schedule(new TurnTask(startSmallBlind, deals), 0);
	}
		
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == continueButton) {
			goAhead = true;
		}
	}
	
	private class InfoTask extends TimerTask{
		@Override
		public void run() {
			updateInfoPanel();
		}
	}
	
	private class TurnTask extends TimerTask {
		int smallBlind;
		int deals;
		
		public TurnTask(int smallBlind, int deals) {
			this.smallBlind = smallBlind;
			this.deals = deals;
		}
		
		@Override
		public void run() {
			++deals;
			Statement.printInfo("New deal: " + deals);
			if (deals % 10 == 0 && deals > 0) {
				smallBlind *= 3;
				smallBlind /= 2;
				smallBlind -= smallBlind % 5;
				Statement.printInfo("Blinds increased - small: " + smallBlind + ", big: " + 2 * smallBlind + ".");
			}
			moveDealerSign(playerDealer.place);
			Deal deal = new Deal(smallBlind, playerDealer.place);
			while (!deal.isEndOfDeal())
				Delay.sleep(5);
			generateNewDealer();
			play(smallBlind, deals);
		}
	}

	public class Deal {
		private List<AuctionList> auction = new ArrayList<>();
		private int numberOfAuctions;
		private List<Integer> usedCards = new ArrayList<>(); // used cards (players cards, community cards)
		private int smallBlind;
		private int bigBlind;
		private int dealerPosition;
		private int communityCardsNumber;
		private boolean endOfDeal;
		private Timer playTimer = new Timer();
		
		/**
		 * Create a new deal (new cards, new auction)
		 * @param smallBlind - chips needed for small blind
		 * @param dealerPosition - dealer (player) place
		 */
		public Deal(int smallBlind, int dealerPosition) {
			this.smallBlind = smallBlind;
			this.dealerPosition = dealerPosition;
			bigBlind = 2 * smallBlind;
			numberOfAuctions = 1;
			communityCardsNumber = 0;
			endOfDeal = false;
			resetCommunityCards();
			updateCommunityCards();
			cardShuffle();
			for (Player player : players) { // set all players active
				player.setActive(true);
				player.setActiveAllIn(false);
				playerPanel.get(players.indexOf(player)).setChips(player.getChips());
			}
			auction.add(new AuctionList(players, calculateStartPreFlopPosition(), 0));
			getBlindsFromPlayers();
			repaintPlayerPanels();
			repaint();
			playTimer.schedule(new DealTask(), 0);
		}
		
		/**
		 * Check if deal is over
		 * @return true if deal is over
		 */
		public boolean isEndOfDeal() {
			return endOfDeal;
		}
		
		/**
		 * Find index of dealer
		 * @return dealer index
		 */
		private int findDealerIndex() {
			int dealerIndex = -1;
			for (int i = 0; i < players.size(); i++) {
				if (players.get(i).place == dealerPosition) {
					dealerIndex = i;
					break;
				}
			}
			return dealerIndex;
		}
		
		/**
		 * Calculate start index before starting the deal (next player to big blind)
		 * @return start index
		 */
		private int calculateStartPreFlopPosition() {
			int dealerIndex = findDealerIndex();
			int startIndex;
			if (players.size() == 2)
				startIndex = dealerIndex;
			else
				startIndex = dealerIndex + 3;
			if (startIndex >= players.size()) // start index is bigger than last index
				startIndex -= players.size();
			return startIndex;
		}
		
		private int calculateFlopAndLaterPosition() {
			int dealerIndex = findDealerIndex();
			int startIndex = dealerIndex + 1;
			if (startIndex >= players.size()) // start index is bigger than last index
				startIndex = 0;
			return startIndex;
		}
		
		private void getBlindsFromPlayers() {
			int smallBlindIndex = auction.get(0).activePlayers.size() - 2;
			int bigBlindIndex = auction.get(0).activePlayers.size() - 1;
			int playerSmallBlindIndex = players.indexOf(auction.get(0).activePlayers.get(smallBlindIndex));
			int playerBigBlindIndex = players.indexOf(auction.get(0).activePlayers.get(bigBlindIndex));
			if (auction.get(0).activePlayers.get(smallBlindIndex).getChips() <= smallBlind) {
				// big blind player has less money than small blind player
				if (auction.get(0).activePlayers.get(bigBlindIndex).getChips() <= auction.get(0).activePlayers.get(smallBlindIndex).getChips()) {
					playerPanel.get(playerBigBlindIndex).addBet(auction.get(0).activePlayers.get(bigBlindIndex).getChips());
					playerPanel.get(playerSmallBlindIndex).addBet(auction.get(0).activePlayers.get(smallBlindIndex).getChips());
					auction.get(0).addBet(auction.get(0).activePlayers.get(bigBlindIndex).getChips(), smallBlindIndex);
					auction.get(0).addBet(auction.get(0).activePlayers.get(bigBlindIndex).getChips(), bigBlindIndex);
					auction.get(0).activePlayers.get(bigBlindIndex).setChips(0);
					auction.get(0).activePlayers.get(bigBlindIndex).setActiveAllIn(true);
										// new auction
					++numberOfAuctions;
					auction.add(new AuctionList(players, calculateStartPreFlopPosition(), 1));
					// chips from small blind and big blind players are equal
					if (auction.get(0).activePlayers.get(bigBlindIndex).getChips() == auction.get(0).activePlayers.get(smallBlindIndex).getChips())
						auction.get(1).removePlayer(auction.get(1).activePlayers.size() - 1);
					else
						auction.get(1).addBet(auction.get(0).activePlayers.get(smallBlindIndex).getChips() - auction.get(0).getBiggestBet(), auction.get(1).activePlayers.size() - 1);
					auction.get(0).activePlayers.get(smallBlindIndex).setChips(0);
					auction.get(0).activePlayers.get(smallBlindIndex).setActiveAllIn(true);
				}
				else if (auction.get(0).activePlayers.get(bigBlindIndex).getChips() <= bigBlind) { // more than small blind player but not enough
					playerPanel.get(playerBigBlindIndex).addBet(auction.get(0).activePlayers.get(bigBlindIndex).getChips());
					playerPanel.get(playerSmallBlindIndex).addBet(auction.get(0).activePlayers.get(smallBlindIndex).getChips());
					auction.get(0).addBet(auction.get(0).activePlayers.get(smallBlindIndex).getChips(), bigBlindIndex);
					auction.get(0).activePlayers.get(smallBlindIndex).setChips(0);
					auction.get(0).activePlayers.get(smallBlindIndex).setActiveAllIn(true);
					// new auction
					++numberOfAuctions;
					auction.add(new AuctionList(players, calculateStartPreFlopPosition(), 1));
					auction.get(1).addBet(auction.get(0).activePlayers.get(bigBlindIndex).getChips() - auction.get(0).getBiggestBet(), auction.get(1).activePlayers.size() - 1);
					auction.get(0).activePlayers.get(bigBlindIndex).setChips(0);
					auction.get(0).activePlayers.get(bigBlindIndex).setActiveAllIn(true);
				}
				else { // big blind player has enough money
					playerPanel.get(playerBigBlindIndex).addBet(bigBlind);
					playerPanel.get(playerSmallBlindIndex).addBet(auction.get(0).activePlayers.get(smallBlindIndex).getChips());
					auction.get(0).addBet(auction.get(0).activePlayers.get(smallBlindIndex).getChips(), smallBlindIndex);
					auction.get(0).addBet(auction.get(0).activePlayers.get(smallBlindIndex).getChips(), bigBlindIndex);
					auction.get(0).activePlayers.get(smallBlindIndex).setChips(0);
					auction.get(0).activePlayers.get(smallBlindIndex).setActiveAllIn(true);
					auction.get(0).changeHasTurn(true, bigBlindIndex);
					// new auction
					++numberOfAuctions;
					auction.add(new AuctionList(players, calculateStartPreFlopPosition(), 1));
					auction.get(1).addBet(bigBlind - auction.get(0).getBiggestBet(), auction.get(1).activePlayers.size() - 1);
					auction.get(0).activePlayers.get(bigBlindIndex).withdrawChips(bigBlind);
					auction.get(1).changeHasTurn(true, auction.get(1).activePlayers.size() - 1);
				}
			}
			else if (auction.get(0).activePlayers.get(bigBlindIndex).getChips() <= bigBlind) {
				playerPanel.get(playerBigBlindIndex).addBet(auction.get(0).activePlayers.get(bigBlindIndex).getChips());
				playerPanel.get(playerSmallBlindIndex).addBet(smallBlind);
				// big blind player has less money than small blind
				if (auction.get(0).activePlayers.get(bigBlindIndex).getChips() <= auction.get(0).activePlayers.get(smallBlindIndex).getChips()) {
					auction.get(0).addBet(auction.get(0).activePlayers.get(bigBlindIndex).getChips(), smallBlindIndex);
					auction.get(0).addBet(auction.get(0).activePlayers.get(bigBlindIndex).getChips(), bigBlindIndex);
					auction.get(0).activePlayers.get(bigBlindIndex).setChips(0);
					auction.get(0).activePlayers.get(bigBlindIndex).setActiveAllIn(true);
					// new auction
					++numberOfAuctions;
					auction.add(new AuctionList(players, calculateStartPreFlopPosition(), 1));
					auction.get(1).addBet(smallBlind - auction.get(0).getBiggestBet(), auction.get(1).activePlayers.size() - 1);
					auction.get(0).activePlayers.get(smallBlindIndex).withdrawChips(smallBlind);
					auction.get(1).changeHasTurn(true, auction.get(1).activePlayers.size() - 1);
				}
				else { // more than small blind
					auction.get(0).addBet(smallBlind, smallBlindIndex);
					auction.get(0).activePlayers.get(smallBlindIndex).withdrawChips(smallBlind);
					auction.get(0).addBet(auction.get(0).activePlayers.get(bigBlindIndex).getChips(), bigBlindIndex);
					auction.get(0).activePlayers.get(bigBlindIndex).setChips(0);
					auction.get(0).activePlayers.get(bigBlindIndex).setActiveAllIn(true);
					// new auction
					++numberOfAuctions;
					auction.add(new AuctionList(players, calculateStartPreFlopPosition(), 1));
				}
				auction.get(0).changeHasTurn(true, smallBlindIndex);
			}
			else {
				playerPanel.get(playerBigBlindIndex).addBet(bigBlind);
				playerPanel.get(playerSmallBlindIndex).addBet(smallBlind);
				auction.get(0).addBet(smallBlind, smallBlindIndex);
				auction.get(0).activePlayers.get(smallBlindIndex).withdrawChips(smallBlind);
				auction.get(0).addBet(bigBlind, bigBlindIndex);
				auction.get(0).activePlayers.get(bigBlindIndex).withdrawChips(bigBlind);
				auction.get(0).changeHasTurn(true, smallBlindIndex);
				auction.get(0).changeHasTurn(true, bigBlindIndex);
			}
			updatePotAndBet(getSummaryPot(), getTableBet());
		}
		
		private boolean auctionFinished() {
			int finished = 0;
			for (int i = 0; i < auction.size(); i++) {
				if (auction.get(i).auctionFinished())
					++finished;
			}
			return (finished == auction.size()) ? true : false;
		}
		
		private int getSummaryPot() {
			int summaryPot = 0;
			for (int i = 0; i < auction.size(); i++) {
				summaryPot += auction.get(i).getPot();
			}
			return summaryPot;
		}
		
		private int getTableBet() {
			int tableBet = 0;
			for (int i = 0; i < auction.size(); i++) {
				tableBet += auction.get(i).getBiggestBet();
			}
			return tableBet;
		}
		
		private int getPlayerBet(Player	player) {
			int playerBet = 0;
			int index;
			for (int i = 0; i < auction.size(); i++) {
				index = auction.get(i).activePlayers.indexOf(player);
				playerBet += auction.get(i).playerBets.get(index).intValue();
			}
			return playerBet;
		}
		
		private void setHasTurnAtAuction(Player player, boolean status) {
			if (!player.active || player.activeAllIn)
				status = false;
			for (int i = 0; i < auction.size(); i++) {
				auction.get(i).changeHasTurn(status, player);
			}
		}
		
		private void setTurnToOthersPlayer(Player player) {
			for (int i = 0; i < auction.size(); i++) {
				for (int j = 0; j < auction.get(i).activePlayers.size(); j++) {
					if (!auction.get(i).activePlayers.get(j).equals(player) && auction.get(i).activePlayers.get(j).active && !auction.get(i).activePlayers.get(j).activeAllIn)
						setHasTurnAtAuction(auction.get(i).activePlayers.get(j), true);
				}
			}
		}
		
		private int calculateActivePlayers() {
			int active = 0;
			for (Player p : players) {
				if (p.active)
					++active;
			}
			return active;
		}
		
		private void removePlayerFromAuction(Player player) {
			for (int i = 0; i < auction.size(); i++)
				auction.get(i).removePlayer(player);
		}
		
		/**
		 * Add bet at all auctions
		 * @param additionalBet - new bet, there can be old non-zero value bet
		 * @param player
		 */
		private void addBetAtAuction(int additionalBet, Player player) {
			int index;
			int temp;
			for (int i = 0; i < auction.size(); i++) {
				index = auction.get(i).activePlayers.indexOf(player);
				if (index >= 0 && auction.get(i).playerBets.get(index).intValue() < auction.get(i).getBiggestBet()) {
					temp = auction.get(i).getBiggestBet() - auction.get(i).playerBets.get(index).intValue();
					if (temp > additionalBet)
						Statement.printError("TableInterface->Deal->addBetAtAuction: Too low bet!");
					auction.get(i).addBet(temp, index);
					additionalBet -= temp;
				}
			}
			auction.get(numberOfAuctions - 1).addBet(additionalBet, auction.get(numberOfAuctions - 1).activePlayers.indexOf(player));
		}
		
		private void analyzeTurn(Player player, PlayersTurn turn) {
			if (turn.turn == Turn.FOLD) {
				removePlayerFromAuction(player);
				playerPanel.get(players.indexOf(player)).setStatus("Passed");
			}
			else if (turn.turn == Turn.CHECK)
				setHasTurnAtAuction(player, false);
			else if (turn.turn == Turn.CALL || turn.turn == Turn.RAISE) {
				addBetAtAuction(turn.bid, player);
				if (turn.turn == Turn.RAISE)
					setTurnToOthersPlayer(player);
				setHasTurnAtAuction(player, false);
				playerPanel.get(players.indexOf(player)).addBet(turn.bid);
				updatePotAndBet(getSummaryPot(), getTableBet());
			}
			else { // ALL_IN
				addBetAtAuction(turn.bid, player);
				setHasTurnAtAuction(player, false);
				playerPanel.get(players.indexOf(player)).addBet(turn.bid);
				playerPanel.get(players.indexOf(player)).setStatus("All-in");
				++numberOfAuctions;
				if (communityCardsNumber == 0)
					auction.add(new AuctionList(players, calculateStartPreFlopPosition(), numberOfAuctions - 1));
				else
					auction.add(new AuctionList(players, calculateFlopAndLaterPosition(), numberOfAuctions - 1));
				// all-in is too low to equals biggest bet - other active players bet go to the new auction
				int indexTemp = auction.get(numberOfAuctions - 2).activePlayers.indexOf(player);
				if (indexTemp == -1)
					Statement.printError("TableInterface->Deal->analyzeTurn indexTemp = -1!");
				if (auction.get(numberOfAuctions - 2).playerBets.get(indexTemp).intValue() < auction.get(numberOfAuctions - 2).getBiggestBet()) {
					for (int i = 0; i < auction.get(numberOfAuctions - 2).playerBets.size(); i++) {
						int diff = auction.get(numberOfAuctions - 2).reduceBid(auction.get(numberOfAuctions - 2).playerBets.get(indexTemp).intValue(), i);
						Player playerTemp = auction.get(numberOfAuctions - 2).activePlayers.get(i);
						indexTemp = auction.get(numberOfAuctions - 1).activePlayers.indexOf(playerTemp);
						if (diff > 0 && indexTemp >= 0)
							auction.get(numberOfAuctions - 1).addBet(diff, playerTemp);
					}
				}
				else { // bet is equal as biggest bet
					int indexLastAuction;
					for (int i = 0; i < auction.get(numberOfAuctions - 1).activePlayers.size(); i++) {
						indexLastAuction = auction.get(numberOfAuctions - 2).activePlayers.indexOf(auction.get(numberOfAuctions - 1).activePlayers.get(i));
						if (indexLastAuction >= 0) // set has turn to old status
							auction.get(numberOfAuctions - 1).changeHasTurn(auction.get(numberOfAuctions - 2).hasTurn.get(indexLastAuction), i);
					}
				}
				setTurnToOthersPlayer(player);
				setHasTurnAtAuction(player, false);
				updatePotAndBet(getSummaryPot(), getTableBet());
			}
			repaintPlayerPanels();
		}
		
		private void goThroughAuction() {
			int iter = 0;
			int playerPosition;
			int playersNumber;
			Player player = null;
			PlayersTurn turn = null;
			if (auction.isEmpty()) // not preflop
				auction.add(new AuctionList(players, calculateFlopAndLaterPosition(), 0));
			while(!auctionFinished()) {
				player = auction.get(numberOfAuctions - 1).activePlayers.get(iter);
				Statement.printProgrammerInfo(auction.get(numberOfAuctions - 1).activePlayers.get(iter).name + " have a turn");
				if (auction.get(numberOfAuctions - 1).hasPlayerTurn(iter)) { // player has turn
					playerPosition = auction.get(numberOfAuctions - 1).activePlayers.indexOf(player) + 1;
					if (communityCardsNumber == 0)
						playerPosition += 2;
					if (playerPosition > auction.get(numberOfAuctions - 1).activePlayers.size())
						playerPosition -= auction.get(numberOfAuctions - 1).activePlayers.size();
					playersNumber = auction.get(numberOfAuctions - 1).activePlayers.size();
					playerPanel.get(players.indexOf(player)).setPlayerMoveView();
					// player makes turn
					turn = auction.get(numberOfAuctions - 1).activePlayers.get(iter).goThroughTurn(communityCardsNumber, playerPosition, playersNumber, getSummaryPot(), getTableBet(), smallBlind, getPlayerBet(player));
					analyzeTurn(player, turn);
					Delay.sleep(1500);
					playerPanel.get(players.indexOf(player)).resetPlayerMoveView();
				}
				if (player.isActive() && !player.isActiveAllIn()) // no incrementation if player has been removed
					++iter;
				if (iter >= auction.get(numberOfAuctions - 1).activePlayers.size())
					iter = 0;
			}
			Statement.printProgrammerInfo("Auction finished");
		}
		
		private void rewardWinners() {
			for (int i = 0; i < auction.size(); i++) {
				if (auction.get(i).getPot() > 0 && !auction.get(i).activePlayers.isEmpty()) {
					if (/*i == auction.size() - 1 && */auction.get(i).activePlayers.size() == 1) { // no cards shown
						auction.get(i).activePlayers.get(0).addChips(auction.get(i).getPot());
						Statement.printInfo(auction.get(i).activePlayers.get(0).name + " earns " + auction.get(i).getPot() + " chips.");
					}
					else { // cards shown
						List<Integer> winnerList = new ArrayList<>();
						int[] playerIndexToShow = new int[auction.get(i).activePlayers.size()];
						for (int j = 0; j < playerIndexToShow.length; j++)
							playerIndexToShow[j] = players.indexOf(auction.get(i).activePlayers.get(j));
						showCards(playerIndexToShow);
						winnerList.add(new Integer(0));
						int result;
						for (int j = 1; j < auction.get(i).activePlayers.size(); j++) {
							result = HandChecker.checkBetterHand(auction.get(i).activePlayers.get(j).getCards(), auction.get(i).activePlayers.get(winnerList.get(0).intValue()).getCards());
							if (result == 1) // new better hand
								winnerList.clear();
							if (result < 2) // equal or new better hand
								winnerList.add(new Integer(j));
							else if (result == 2) // display looser hand
								Statement.printProgrammerInfo("" + auction.get(i).activePlayers.get(j).name + " has lost with " + HandChecker.getHandName(auction.get(i).activePlayers.get(j).getCards()) + ".");
							if (j == 1 && result == 1)
								Statement.printProgrammerInfo("" + auction.get(i).activePlayers.get(0).name + " has lost with " + HandChecker.getHandName(auction.get(i).activePlayers.get(0).getCards()) + ".");
						}
						for (int j = 0; j < winnerList.size(); j++) {
							auction.get(i).activePlayers.get(winnerList.get(j).intValue()).addChips(auction.get(i).getPot() / winnerList.size());
							Statement.printInfo("" + auction.get(i).activePlayers.get(winnerList.get(j).intValue()).name + " earns " + auction.get(i).getPot() / winnerList.size() + " chips with " + HandChecker.getHandName(auction.get(i).activePlayers.get(winnerList.get(j).intValue()).getCards()) + ".");
						}
					}
				}
			}
			repaintPlayerPanels();
			waitForAccept();
			hideCards();
		}
		
		private void deleteBankrupts() {
			for (int i = 0; i < players.size(); i++) {
				if (players.get(i).getChips() <= 0) {
					if (players.get(i).equals(playerDealer))
						generateNewDealer();
					Statement.printInfo(players.get(i).name + " lost the game.");
					players.remove(i);
					remove(playerPanel.get(i));
					playerPanel.remove(i);
					--i;
				}
			}
			if (playerPanel.size() > players.size()) { // cleaning view
				for (int i = playerPanel.size() - 1; i >= 0; i++) {
					for (int j = 0; j < players.size(); j++) {
						if (playerPanel.get(i).getName().equals(players.get(j).name))
							break;
						if (j == players.size() - 1) { // delete bankrupts from view
							remove(playerPanel.get(i));
							playerPanel.remove(i);
						}
					}
				}
			}
		}
		
		private void cleanAuctions() {
			for (int i = auction.size() - 1; i >= 0 ; i--) {
				if (auction.get(i).getPot() == 0) {
					auction.remove(i);
					--numberOfAuctions;
					Statement.printProgrammerInfo("Removed auction with no pot (index " + i + "). Remains " + auction.size() + " auctions.");
				}
				else if (auction.get(i).activePlayers.size() == 1) { // only one player - auction to delete
					Player player = auction.get(i).activePlayers.get(0);
					int index = players.indexOf(player);
					playerPanel.get(index).setStatus("Active");
					player.addChips(auction.get(i).getPot());
					playerPanel.get(index).setChips(player.getChips());
					auction.remove(i);
					--numberOfAuctions;
					Statement.printProgrammerInfo("Removed auction with only one player (index " + i + "). Remains " + auction.size() + " auctions.");
				}
			}
		}
		
		private void prepareNewAuction() {
			boolean isAllIn = false;
			for (int i = 0; i < auction.get(numberOfAuctions - 1).activePlayers.size(); i++) {
				if (auction.get(numberOfAuctions - 1).activePlayers.get(i).activeAllIn) {
					isAllIn = true;
					break;
				}
			}
			++numberOfAuctions;
			auction.add(new AuctionList(players, calculateFlopAndLaterPosition(), 0));
			if (!isAllIn) {
				int pot = auction.get(numberOfAuctions - 2).getPot();
				auction.get(numberOfAuctions - 1).setStartPot(pot);
				auction.remove(auction.size() - 2);
				--numberOfAuctions;
			}
		}
		
		private void prepareToNewAuction() {
			if (auction.isEmpty())
				return;
			cleanAuctions();
			prepareNewAuction();
			for (AuctionList a : auction) {
				for (Player p : a.activePlayers) {
					a.clearBet(p);
				}
			}
			updatePotAndBet(getSummaryPot(), getTableBet());
			for (PlayerPanel pp : playerPanel)
				pp.resetBet();
			repaintPlayerPanels();
		}
		
		/**
		 * All-in players and one player with no all-in, but he cannot bet anymore
		 * @return
		 */
		private boolean onlyAllInPlayersSurvived() {
			int allInCount = 0;
			int surviversCount = 0;
			for (Player p : players) {
				if (p.active)
					++surviversCount;
				if (p.activeAllIn)
					++allInCount;
			}
			return (allInCount + 1 >= surviversCount) ? true : false;
		}
		
		private void playDeal() {
			waitForAccept();
			goThroughAuction(); // pre flop auction
			if (calculateActivePlayers() == 1) { // there is a winner on pre flop
				rewardWinners();
				deleteBankrupts();
				return;
			}
			waitForAccept();
			prepareToNewAuction();
			addCommunityCards(3);
			if (!onlyAllInPlayersSurvived()) // poczatek (byla para na stole)
				goThroughAuction(); // after flop auction
			if (calculateActivePlayers() == 1) { // there is a winner on flop
				rewardWinners();
				deleteBankrupts();
				return;
			}
			waitForAccept();
			prepareToNewAuction();
			addCommunityCards(1);
			if (!onlyAllInPlayersSurvived()) // koniec
				goThroughAuction(); // after turn auction
			if (calculateActivePlayers() == 1) { // there is a winner on turn
				rewardWinners();
				deleteBankrupts();
				return;
			}
			waitForAccept();
			prepareToNewAuction();
			addCommunityCards(1);
			if (!onlyAllInPlayersSurvived())
				goThroughAuction(); // after river auction
			rewardWinners(); // reward winners
			deleteBankrupts();
		}
		
		private void cardShuffle() {
			int[] newCard = new int[2];
			resetCommunityCards();
			for (int i = 0; i < players.size(); i++) {
				for (int j = 0; j < 2; j++) {
					do {
						newCard[j] = (new Random()).nextInt(52);
					}while(usedCards.contains(new Integer(newCard[j])));
					usedCards.add(new Integer(newCard[j]));
				}
				players.get(i).cards.resetCards();
				players.get(i).addOwnCard(new Card(newCard[0]), new Card(newCard[1]));
				playerPanel.get(i).shuffleNewCard(new Card(newCard[0]), new Card(newCard[1]));
				if (players.get(i) instanceof PlayerHuman)
					playerPanel.get(i).reset(true, true);
				else
					playerPanel.get(i).reset(showAICards, true);
				playerPanel.get(i).setStatus("Active");
			}
		}
		
		private void addCommunityCards(int cardsNumber) {
			int[] newCard = new int[3];
			Statement.printProgrammerInfo("addCommunityCards: " + cardsNumber);
			for (int i = 0; i < cardsNumber; i++) {
				do {
					newCard[i] = new Random().nextInt(52);
				}while(usedCards.contains(new Integer(newCard[i])));
				usedCards.add(new Integer(newCard[i]));
			}
			if (cardsNumber == 3) {
				for (Player player : players)
					player.addFlop(new Card(newCard[0]), new Card(newCard[1]), new Card(newCard[2]));
				communityCardsNumbers[0] = newCard[0];
				communityCardsNumbers[1] = newCard[1];
				communityCardsNumbers[2] = newCard[2];
				Statement.printProgrammerInfo("Flop added");
			}
			else if (cardsNumber == 1) {
				if (players.get(0).cards.getCards().size() == 5) { // turn
					for (Player player : players)
						player.addTurn(new Card(newCard[0]));
					communityCardsNumbers[3] = newCard[0];
					Statement.printProgrammerInfo("Turn added");
				}
				else if (players.get(0).cards.getCards().size() == 6) { // river
					for (Player player : players)
						player.addRiver(new Card(newCard[0]));
					communityCardsNumbers[4] = newCard[0];
					Statement.printProgrammerInfo("River added");
				}
			}
			communityCardsNumber += cardsNumber;
			updateCommunityCards();
		}
		
		private class DealTask extends TimerTask {
			@Override
			public void run() {
				Statement.printProgrammerInfo("Play deal");
				playDeal();
				endOfDeal = true;
			}
			
		}
	} // end of class Deal
}
