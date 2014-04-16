package datalaag;

public class ScoreCalculator {

	private static ScoreCalculator scoreCalculator = new ScoreCalculator();
	
	private ScoreCalculator(){}
	
	public static ScoreCalculator getInstance(){
		return scoreCalculator;
	}
	
}


/* aanmaken door:
 * DatabaseHandler dbh = DatabaseHandler.getInstance();
 * 
 * en aanroepen door:
 * dbh. --------
 */