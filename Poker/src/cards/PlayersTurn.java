package cards;

public class PlayersTurn {
	public Turn turn;
	public int bid;
	
	public PlayersTurn(Turn turn) {
		this.turn = turn;
		checkCorrectness();
	}
	
	public PlayersTurn(Turn turn, int bid) {
		this(turn);
		this.bid = bid;
		checkCorrectness();
	}
	
	public void checkCorrectness() {
		if (turn == Turn.FOLD || turn == Turn.CHECK)
			bid = 0;
	}

}
