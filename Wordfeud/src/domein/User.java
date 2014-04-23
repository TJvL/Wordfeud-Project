package domein;

public class User {
	private Player player;
	
	public User(){
		player = new Player("Klaas");
	}
	
	public Player getPlayer(){
		return player;
	}
}
