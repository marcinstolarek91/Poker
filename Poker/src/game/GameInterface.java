package game;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GameInterface extends JFrame implements ActionListener {
	private JPanel newTable, statistics;
	private static final int X_HIDDEN = 5, Y_HIDDEN = 28;
	private JTextField playerName = new JTextField("Player");
	private JComboBox<Integer> opponentsNumber = new JComboBox<>();
	private JTextField startChips = new JTextField("1000");
	private JTextField startBid = new JTextField("10");
	private JButton startButton = new JButton("New table");
	
	public GameInterface() {
		super("Poker");
		setVisible(true);
		setLocation(0,0);
		setSize(1000 + X_HIDDEN, 600 + Y_HIDDEN);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		panelInit();
		setLayout(new GridLayout(1, 2, 15, 15));
		add(newTable, 0);
		add(statistics, 1);
	}
	
	private void panelInit() {
		GridLayout grid = new GridLayout(5, 2, 2, 2);
		for (int i = 1; i <= 9; i++)
			opponentsNumber.addItem(new Integer(i));
		newTable = new JPanel(grid);
		newTable.add(new JLabel("Player name"), 0);
		newTable.add(playerName, 1);
		newTable.add(new JLabel("Opponents number"), 2);
		newTable.add(opponentsNumber, 3);
		newTable.add(new JLabel("Start chips ammount"), 4);
		newTable.add(startChips, 5);
		newTable.add(new JLabel("Start bid ammount"), 6);
		newTable.add(startBid, 7);
		newTable.add(new JLabel("Create the new table"), 8);
		newTable.add(startButton, 9);
		newTable.setBackground(Color.GRAY);
		startButton.addActionListener(this);
		grid = new GridLayout(10, 2, 2, 2);
		statistics = new JPanel(grid);
		statistics.add(new JLabel("Statistics - to do in future"), 0);
		statistics.setBackground(Color.WHITE);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == startButton) {
			//TODO - open new table
		}
	}
	
	
}
