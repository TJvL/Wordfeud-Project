package domein;

public class ActiveMatch {
	private int gameID;
	private String enemyName;
	private String discription;
	
	public ActiveMatch(int gameID, String discription) {
		this.gameID = gameID;
	//	this.enemyName = enemyName;
		this.discription = discription;
	}

	@Override
	public String toString() {
		return discription;
	}
	
	public int getGameID() {
		return gameID;
	}

	public String getEnemyName() {
		return enemyName;
	}

	public String getDiscription() {
		return discription;
	}	
}
