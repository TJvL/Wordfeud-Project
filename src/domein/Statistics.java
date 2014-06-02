package domein;

import datalaag.DatabaseHandler;

public class Statistics {
	
	
	private DatabaseHandler dbh;
	private String gamesWonString;
	private String playedGamesString;
	private String compsWonString;
	private String highScoreString;
	
	public Statistics()
	{
		
		dbh = DatabaseHandler.getInstance();

	}
	
	public void retrieveData(String userName)
	{
		
		
		String dataBefore = dbh.playerStatistics(userName);
		
		String[] dataAfter = dataBefore.split("-");

		compsWonString = dataAfter[0];
		gamesWonString = dataAfter[3];
		playedGamesString = dataAfter[9];
		
		highScoreString = dataAfter[12];
		
	}

	public String getGamesWonString()
	{
		return gamesWonString;
	}

	public String getPlayedGamesString()
	{
		return playedGamesString;
	}

	public String getCompsWonString()
	{
		return compsWonString;
	}

	public String getHighScoreString()
	{
		return highScoreString;
	}
	
	

}
