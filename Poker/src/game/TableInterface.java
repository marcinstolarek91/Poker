package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cards.Player;
import cards.PlayerAI;
import cards.PlayerHuman;

public class TableInterface extends JFrame implements ActionListener {
	private List<Player> players = new ArrayList<>();
	private JPanel playerInterface = new JPanel();
	private JPanel communityCards = new JPanel();
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
	}
	
	private void addPlayers(int playersNumber, String[] names) {
		players.add(new PlayerHuman(names[0], 0));
		for (int i = 2; i <= playersNumber; i++)
			players.add(new PlayerAI(names[i - 1], i - 1));
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
	}

}
