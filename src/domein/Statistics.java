//de functonaliteit is compleet behalve het stuk dat de juiste username pakt
//misschien gettext doen in loginscreen? 
package domein;

import datalaag.DatabaseHandler;

public class Statistics
{

	private String highestGameScore;
	private String numGamesPlayed;
	private String mostValuableWord;
	private String gamesWon;
	private String competitionsWon;
	private String statistics;
	private String userName;
	private DatabaseHandler dbh = DatabaseHandler.getInstance();

	
	public void getUserName(String username)
	{
		this.userName = username;
		System.out.println(userName);
	}
	public String getHighestGameScore()
	{
		statistics = dbh.playerStatistics(userName);

		
			highestGameScore = statistics
					.substring(statistics.lastIndexOf('h') + 1);
			System.out.println(highestGameScore);
			return highestGameScore;

		

	}

	public String getNumGamesPlayed()
	{
		statistics = dbh.playerStatistics(userName);
		numGamesPlayed = statistics.substring(statistics.lastIndexOf('f') + 1,
				statistics.lastIndexOf('g'));
		System.out.println(numGamesPlayed);
		return numGamesPlayed;
	}

	public String mostValuableWord()
	{
		statistics = dbh.playerStatistics(userName);
		mostValuableWord = statistics.substring(
				statistics.lastIndexOf('d') + 1, statistics.lastIndexOf('e'));
		System.out.println(mostValuableWord);
		return mostValuableWord;
	}

	public String getGamesWon()
	{
		statistics = dbh.playerStatistics(userName);
		gamesWon = statistics.substring(statistics.lastIndexOf('b') + 1,
				statistics.lastIndexOf('c'));
		System.out.println(gamesWon);
		return gamesWon;
	}

	public String getCompetitionsWon()
	{
		statistics = dbh.playerStatistics(userName);
		competitionsWon = statistics.substring(0, statistics.lastIndexOf('a'));
		System.out.println(competitionsWon);
		return competitionsWon;
	}

}
