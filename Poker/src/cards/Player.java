package cards;

public class Player {
	public PlayerCards cards;
	private int chips;
	
	public Player() {
		cards = new PlayerCards();
		chips = 0;
	}
	
	public Player(int chips) {
		this.chips = chips;
	}
}
