package datalaag;

public class DatabaseHandler {

	private static DatabaseHandler databaseHandler = new DatabaseHandler();
	
	public DatabaseHandler(){}
	
	public static DatabaseHandler getInstance(){
		return databaseHandler;
	}
	
}


/* aanmaken door:
 * DatabaseHandler dbh = DatabaseHandler.getInstance();
 * 
 * en aanroepen door:
 * dbh. --------
 */
