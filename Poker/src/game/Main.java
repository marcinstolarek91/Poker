package game;

public class Main {
	public static final String version = "1.01.00";

	public static void main(String[] args) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				new GameInterface();
			}
		});
	}

}