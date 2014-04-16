package datalaag;

public class FileHandler {

	private static FileHandler fileHandler = new FileHandler();
	
	private FileHandler(){}
	
	public static FileHandler getInstance(){
		return fileHandler;
	}
	
}


/* aanmaken door:
 * DatabaseHandler dbh = DatabaseHandler.getInstance();
 * 
 * en aanroepen door:
 * dbh. --------
 */