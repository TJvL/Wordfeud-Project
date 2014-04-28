package domein;


import java.util.ArrayList;


// Played deed user extend
// was een oneindige loop
public class Player{

	private String name;
	private Hand hand;
	
	public Player(String name){
		this.name = name;
		hand = new Hand();
	}

	public String getName(){
		return name;
	}
	
	public void addTileToHand(Tile t){
		hand.addTileToHand(t);
	}
	
	public void removeTileFromHand(Tile t){
		hand.removeTileFromHand(t);
	}
	
	public int getHandSize(){
		return hand.getHandSize();
	}
	
	public ArrayList<Tile> getHand(){
		return hand.getHand();
	}
}
