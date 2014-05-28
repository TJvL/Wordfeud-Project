package domein;

public class PendingMatch {
	private int gameID;
	private String discription;
	private boolean ownGame;
	
	public PendingMatch(int gameID, String discription, boolean ownGame) {
		this.gameID = gameID;
		this.discription = discription;
		this.ownGame = ownGame;
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
	
	public boolean getOwnGame(){
		return ownGame;
	}
}
