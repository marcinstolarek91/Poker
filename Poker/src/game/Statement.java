package game;

import java.util.ArrayList;
import java.util.List;

public class Statement {
	public static List<String> infoList = new ArrayList<>();
	
	public static void printInfo(String info) {
		System.out.println(info);
		flush();
		infoList.add(0, info);
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
