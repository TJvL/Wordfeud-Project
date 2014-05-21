package domein;

public class PendingMatch {
	private int gameID;
	private int competionID;
	private String discription;
	
	public PendingMatch(int gameID, int competionID, String discription) {
		this.gameID = gameID;
		this.competionID = competionID;
		this.discription = discription;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return discription;
	}
	
	public int getGameID() {
		return gameID;
	}

	public int getCompetionID() {
		return competionID;
	}

	public String getDiscription() {
		return discription;
	}
}
