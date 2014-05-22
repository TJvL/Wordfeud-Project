package domein;

public class PendingMatch {
	private int gameID;
	private String discription;
	
	public PendingMatch(int gameID, String discription) {
		this.gameID = gameID;
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

	public String getDiscription() {
		return discription;
	}
}
