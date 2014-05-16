package domein;

import datalaag.DatabaseHandler;

public class MainClass {

	public static void main(String[] args) {
//		new WordFeud();
		
		DatabaseHandler dbh = DatabaseHandler.getInstance();
		
		String woord = dbh.requestWord("michael", "en");
		System.out.println(woord);
	}
}
