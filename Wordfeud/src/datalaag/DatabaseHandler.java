package datalaag;

public class DatabaseHandler {

	private static DatabaseHandler databaseHandler = new DatabaseHandler();

	private DatabaseHandler() {
	}

	public static DatabaseHandler getInstance() {
		return databaseHandler;
	}

	// ///////////////////////////
	public void changeTurn() {
		// zet de turn naar de tegenstander
	}

}

/*
 * aanmaken door: DatabaseHandler dbh = DatabaseHandler.getInstance();
 * 
 * en aanroepen door: dbh. --------
 */
