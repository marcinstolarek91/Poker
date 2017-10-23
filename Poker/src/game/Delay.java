package game;

public class Delay {
	public static void sleep(int ms) {
		try {
			Thread.sleep((long) ms);
		}
		catch (InterruptedException e) {
			Statement.printError("Delay->sleep - InterruptedException");
		}
	}
}
