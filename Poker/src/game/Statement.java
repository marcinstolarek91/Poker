package game;

public class Statement {
	
	public static void printInfo(String info) {
		System.out.println(info);
		flush();
	}
	
	public static void printError(String error) {
		System.out.println(error);
		flush();
	}
	
	public static void printProgrammerInfo(String info) {
		System.out.println(info);
		flush();
	}
	
	private static void flush() {
		System.out.flush();
	}
}
