package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cards.Card;
import cards.HandChecker;
import cards.PlayersTurn;
import cards.Turn;

public class TableInterface extends JFrame implements ActionListener {
	private List<Player> players = new ArrayList<>();
	private JPanel playerInterface = new JPanel();
	private JPanel communityCards = new JPanel();
	private int[] communityCardsNumbers = new int[5]; // -1 means empty slot
	private List<JPanel> playerPanel = new ArrayList<>();
	private JLabel humanPlayerBetPanel;
	private static final int X_HIDDEN = 5, Y_HIDDEN = 28;
	
	public TableInterface(int playersNumber, String[] names) {
		super("Poker Table");
		setVisible(true);
		setLocation(0,0);
		setSize(1000 + X_HIDDEN, 600 + Y_HIDDEN);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addPlayers(playersNumber, names);
		resetCommunityCards();
	}
	
	private void resetCommunityCards() {
		for (int i = 0; i < 5; i++)
			communityCardsNumbers[i] = -1;
	}
	
	private void addPlayers(int playersNumber, String[] names) {
		players.add(new PlayerHuman(names[0], 0));
		for (int i = 2; i <= playersNumber; i++)
			players.add(new PlayerAI(names[i - 1], i - 1));
	}
	
	private void waitForAccept() { // accept by player
		
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
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
		// TODO - show cards or no
		
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
			for (Player player : players) { // set all players active
				player.setActive(true);
				player.setActiveAllIn(false);
			}
			auction.add(new AuctionList(players, calculateStartPreFlopPosition(), 0));
			cardShuffle();
			getBlindsFromPlayers();
			playDeal();
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
			while (!players.get(dealerIndex).active || players.get(dealerIndex).activeAllIn) { // while dealer is not active
				--dealerIndex;
				if (dealerIndex < 0)
					dealerIndex = players.size() - 1;
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
			if (auction.get(0).activePlayers.get(smallBlindIndex).getChips() <= smallBlind) {
				// big blind player has less money than small blind player
				if (auction.get(0).activePlayers.get(bigBlindIndex).getChips() <= auction.get(0).activePlayers.get(smallBlindIndex).getChips()) {
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
					auction.get(0).addBet(auction.get(0).activePlayers.get(smallBlindIndex).getChips(), smallBlindIndex);
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
					auction.get(0).addBet(auction.get(0).activePlayers.get(smallBlindIndex).getChips(), smallBlindIndex);
					auction.get(0).addBet(auction.get(0).activePlayers.get(smallBlindIndex).getChips(), bigBlindIndex);
					auction.get(0).activePlayers.get(smallBlindIndex).setChips(0);
					auction.get(0).activePlayers.get(smallBlindIndex).setActiveAllIn(true);
					// new auction
					++numberOfAuctions;
					auction.add(new AuctionList(players, calculateStartPreFlopPosition(), 1));
					auction.get(1).addBet(bigBlind - auction.get(0).getBiggestBet(), auction.get(1).activePlayers.size() - 1);
					auction.get(0).activePlayers.get(bigBlindIndex).withdrawChips(bigBlind);
				}
			}
			else if (auction.get(0).activePlayers.get(bigBlindIndex).getChips() <= bigBlind) {
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
			}
			else {
				auction.get(0).addBet(smallBlind, smallBlindIndex);
				auction.get(0).activePlayers.get(smallBlindIndex).withdrawChips(smallBlind);
				auction.get(0).addBet(bigBlind, bigBlindIndex);
				auction.get(0).activePlayers.get(bigBlindIndex).withdrawChips(bigBlind);
			}
			
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
			for (int i = 0; i < auction.size(); i++) {
				auction.get(i).changeHasTurn(false, player);
			}
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
		}
		
		private void analyzeTurn(Player player, PlayersTurn turn) {
			if (turn.turn == Turn.FOLD)
				removePlayerFromAuction(player);
			else if (turn.turn == Turn.CHECK)
				setHasTurnAtAuction(player, false);
			else if (turn.turn == Turn.CALL || turn.turn == Turn.RAISE)
				addBetAtAuction(turn.bid, player);
			else { // ALL_IN
				addBetAtAuction(turn.bid, player);
				++numberOfAuctions;
				if (communityCardsNumber == 0)
					auction.add(new AuctionList(players, calculateStartPreFlopPosition(), numberOfAuctions - 1));
				else
					auction.add(new AuctionList(players, calculateFlopAndLaterPosition(), numberOfAuctions - 1));
				// all-in is too low to equals biggest bet - other active players bet go to the new auction
				int indexTemp = auction.get(numberOfAuctions - 2).activePlayers.indexOf(player);
				if (auction.get(numberOfAuctions - 2).playerBets.get(indexTemp).intValue() < auction.get(numberOfAuctions - 2).getBiggestBet()) {
					for (int i = 0; i < auction.get(numberOfAuctions - 2).playerBets.size(); i++) {
						int diff = auction.get(numberOfAuctions - 2).reduceBid(auction.get(numberOfAuctions - 2).playerBets.get(indexTemp).intValue(), i);
						Player playerTemp = auction.get(numberOfAuctions - 2).activePlayers.get(i);
						indexTemp = auction.get(numberOfAuctions - 1).activePlayers.indexOf(playerTemp);
						if (diff > 0 && indexTemp >= 0)
							auction.get(numberOfAuctions - 1).addBet(diff, playerTemp);
					}
				}
			}
		}
		
		private void goThroughAuction() {
			int iter = 0;
			int playerPosition;
			int playersNumber;
			Player player = null;
			PlayersTurn turn;
			if (auction.isEmpty()) // not preflop
				auction.add(new AuctionList(players, calculateFlopAndLaterPosition(), 0));
			do {
				if (auction.get(numberOfAuctions - 1).hasPlayerTurn(iter)) { // player has turn
					playerPosition = auction.get(numberOfAuctions - 1).activePlayers.size() - iter;
					playersNumber = auction.get(numberOfAuctions - 1).activePlayers.size();
					player = auction.get(numberOfAuctions - 1).activePlayers.get(iter);
					// player makes turn
					turn = auction.get(numberOfAuctions - 1).activePlayers.get(iter).goThroughTurn(communityCardsNumber, playerPosition, playersNumber, getSummaryPot(), getTableBet(), smallBlind, getPlayerBet(player));
					analyzeTurn(player, turn);
				}
				if (player.isActive() && !player.isActiveAllIn()) // no incrementation if player has been removed
					++iter;
				if (iter >= auction.get(numberOfAuctions - 1).activePlayers.size())
					iter = 0;
			}while(!auctionFinished() || auction.get(numberOfAuctions - 1).activePlayers.size() == 1);
		}
		
		private void rewardWinners() {
			for (int i = 0; i < auction.size(); i++) {
				if (i == auction.size() - 1 && auction.get(i).activePlayers.size() == 1) { // no cards shown
					auction.get(i).activePlayers.get(0).addChips(auction.get(i).getPot());
					Statement.printInfo("" + auction.get(i).activePlayers.get(0).name + " earns " + auction.get(i).getPot() + " chips.");
				}
				else { // cards shown
					List<Integer> winnerList = new ArrayList<>();
					winnerList.add(new Integer(0));
					int result;
					for (int j = 1; j < auction.get(i).activePlayers.size(); j++) {
						result = HandChecker.checkBetterHand(auction.get(i).activePlayers.get(j).getCards(), auction.get(i).activePlayers.get(winnerList.get(0).intValue()).getCards());
						if (result == 1) // new better hand
							winnerList.clear();
						if (result < 2) // equal or new better hand
							winnerList.add(new Integer(j));
					}
					for (int j = 0; j < winnerList.size(); j++) {
						auction.get(i).activePlayers.get(winnerList.get(j).intValue()).addChips(auction.get(i).getPot() / winnerList.size());
						Statement.printInfo("" + auction.get(i).activePlayers.get(winnerList.get(j).intValue()).name + " earns " + auction.get(i).getPot() + " chips with " + HandChecker.getHandName(auction.get(i).activePlayers.get(winnerList.get(j).intValue()).getCards()) + ".");
					}
				}
			}
			waitForAccept();
		}
		
		private void deleteBankrupts() {
			for (int i = 0; i < players.size(); i++) {
				if (players.get(i).getChips() <= 0) {
					players.remove(i);
					--i;
				}
			}
		}
		
		private void playDeal() {
			goThroughAuction(); // pre flop auction
			if (auction.get(numberOfAuctions - 1).activePlayers.size() == 1) { // there is a winner on pre flop
				rewardWinners();
				deleteBankrupts();
				return;
			}
			addCommunityCards(3);
			waitForAccept();
			goThroughAuction(); // after flop auction
			if (auction.get(numberOfAuctions - 1).activePlayers.size() == 1) { // there is a winner on flop
				rewardWinners();
				deleteBankrupts();
				return;
			}
			addCommunityCards(1);
			waitForAccept();
			goThroughAuction(); // after turn auction
			if (auction.get(numberOfAuctions - 1).activePlayers.size() == 1) { // there is a winner on turn
				rewardWinners();
				deleteBankrupts();
				return;
			}
			addCommunityCards(1);
			waitForAccept();
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
						newCard[j] = new Random().nextInt(52);
					}while(usedCards.contains(new Integer(newCard[j])));
					usedCards.add(new Integer(newCard[j]));
				}
				players.get(i).addOwnCard(new Card(newCard[0]), new Card(newCard[1]));
			}
		}
		
		private void addCommunityCards(int cardsNumber) {
			int[] newCard = new int[3];
			if (cardsNumber != 1 && cardsNumber != 3)
				return;
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
			}
			else if (cardsNumber == 1) {
				if (players.get(0).cards.getCards().size() == 5) { // turn
					for (Player player : players)
						player.addTurn(new Card(newCard[0]));
					communityCardsNumbers[3] = newCard[0];
				}
				if (players.get(0).cards.getCards().size() == 6) { // river
					for (Player player : players)
						player.addRiver(new Card(newCard[0]));
					communityCardsNumbers[4] = newCard[0];
				}
			}
		}
	} // end of class Deal
}
