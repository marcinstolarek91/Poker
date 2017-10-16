package game;

import java.util.ArrayList;
import java.util.List;

public class AuctionList {
	public List<Player> activePlayers = new ArrayList<>();
	public List<Boolean> hasTurn = new ArrayList<>();
	public List<Integer> playerBets = new ArrayList<>();
	private int pot;
	private int biggestBet;
	private int level; // level of auction list; it can be some of auction list when players make all-in turn
	
	/**
	 * Method create auction list - tool to get players bets and control auction
	 * @param players - players list
	 * @param actualTurn - position at list - player which is beginning turn
	 * @param level - auction list level
	 */
	public AuctionList(List<Player> players, int actualTurn, int level) {
		if (actualTurn < 0 || actualTurn >= activePlayers.size())
			actualTurn = 0;
		for (int i = actualTurn; i < players.size(); i++) {
			if (players.get(i).active && !players.get(i).activeAllIn) {
				activePlayers.add(players.get(i));
				hasTurn.add(new Boolean(true));
				playerBets.add(new Integer(0));
			}
		}
		for (int i = 0; i < actualTurn; i++) {
			if (players.get(i).active && !players.get(i).activeAllIn) {
				activePlayers.add(players.get(i));
				hasTurn.add(new Boolean(true));
				playerBets.add(new Integer(0));
			}
		}
		pot = 0;
		biggestBet = 0;
		this.level = level;
	}
	
	public int getPot() {
		return pot;
	}
	
	public int getBiggestBet() {
		return biggestBet;
	}
	
	public int getLevel() {
		return level;
	}
	
	/**
	 * Check if player has turn in that auction
	 * @param player - player to check
	 * @return true if player has turn, false otherwise
	 */
	public boolean hasPlayerTurn(Player player) {
		for (int i = 0; i < activePlayers.size(); i++){
			if (activePlayers.get(i).equals(player)) {
				return hasPlayerTurn(i);
			}
		}
		return false;
	}
	
	/**
	 * Check if player has turn in that auction
	 * @param index - index of player to check
	 * @return true if player has turn, false otherwise
	 */
	public boolean hasPlayerTurn(int index) {
		if (hasTurn.get(index) == new Boolean(true))
			return true;
		else
			return false;
	}
	
	/**
	 * Check if auction is finished (no one has turn)
	 * @return true if auction is finished
	 */
	public boolean auctionFinished() {
		return !hasTurn.contains(new Boolean(true));
	}
	
	/**
	 * Add new player bet and update hasTurn list
	 * @param bet - player bet (only new chips)
	 * @param player
	 */
	public void addBet(int bet, Player player) {
		for (int i = 0; i < activePlayers.size(); i++){
			if (activePlayers.get(i).equals(player))
				addBet(bet, i);
		}
	}
	
	/**
	 * Add new player bet and update hasTurn list
	 * @param bet - player bet (only new chips)
	 * @param index - player index
	 */
	public void addBet(int bet, int index) {
		if (index < 0 || index >= activePlayers.size())
			return;
		changeBet(bet + playerBets.get(index).intValue(), index);
		pot += bet;
		if (playerBets.get(index) > biggestBet) {
			biggestBet = playerBets.get(index);
			for (int i = 0; i < activePlayers.size(); i++) { // player raised - others have to turn again
				if (activePlayers.get(i).active && !activePlayers.get(i).activeAllIn && i != index)
					changeHasTurn(true, index);
			}
		}
		changeHasTurn(false, index);
	}
	
	/**
	 * Reduce player bet at this auction
	 * @param limitBet - maximal bet, bigger bet are reduced
	 * @param index - player index
	 * @return reduced value
	 */
	public int reduceBid(int limitBet, int index) {
		int reducedValue = 0;
		if (playerBets.get(index).intValue() > limitBet) {
			reducedValue = playerBets.get(index).intValue() - limitBet;
			changeBet(limitBet, index);
		}
		return reducedValue;
	}
	
	/**
	 * Change player bet
	 * @param newBet - new bet value
	 * @param index - player index
	 */
	private void changeBet(int newBet, int index) {
		playerBets.add(index, new Integer(newBet));
		playerBets.remove(index + 1);
	}
	
	/**
	 * Remove player from auction list, but not change active and activeAllIn status
	 * @param player
	 */
	public void removePlayer(Player player) {
		for (int i = 0; i < activePlayers.size(); i++) {
			if (activePlayers.get(i).equals(player))
				removePlayer(i);
		}
	}
	
	/**
	 * Remove player from auction list, but not change active and activeAllIn status
	 * @param index - player index
	 */
	public void removePlayer(int index) {
		activePlayers.remove(index);
		playerBets.remove(index);
		hasTurn.remove(index);
	}
	
	/**
	 * Change player turn
	 * @param status - new status of hasTurn (true/false)
	 * @param player
	 */
	public void changeHasTurn(boolean status, Player player) {
		int index = activePlayers.indexOf(player);
		if (index >= 0)
			changeHasTurn(status, index);
	}
	
	/**
	 * Change player turn
	 * @param status - new status of hasTurn (true/false)
	 * @param index - player index
	 */
	public void changeHasTurn(boolean status, int index) {
		hasTurn.add(index, new Boolean(status));
		hasTurn.remove(index + 1);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((activePlayers == null) ? 0 : activePlayers.hashCode());
		result = prime * result + level;
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
		AuctionList other = (AuctionList) obj;
		if (activePlayers == null) {
			if (other.activePlayers != null)
				return false;
		} else if (!activePlayers.equals(other.activePlayers))
			return false;
		if (level != other.level)
			return false;
		return true;
	}
	
	
}
