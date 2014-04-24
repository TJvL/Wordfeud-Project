package domein;

import java.util.ArrayList;

public class Hand {
	private ArrayList<Tile> hand = new ArrayList<Tile>();

	public Hand() {

	}
	
	public void addTileToHand(Tile t){
		hand.add(t);
	}
	
	public void removeTileFromHand(Tile t){
		hand.remove(t);
	}
	
	public void setHand(ArrayList<Tile> tiles){
		hand.clear();
		this.hand = tiles;
	}
	
	public ArrayList<Tile> getHand(){
		return hand;
	}
	
	public int getHandSize(){
		return hand.size();
	}

}
