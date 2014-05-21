package domein;

import java.util.ArrayList;

public class Hand {
	private ArrayList<Tile> hand = new ArrayList<Tile>();

	public Hand() {
	}

	// Adds a tiles to the hand
	public void addTileToHand(Tile t) {
		hand.add(t);
	}

	// Removes a tile form the hand
	public void removeTileFromHand(Tile t) {
		hand.remove(t);
	}

	// Fills the hand
	public void setHand(ArrayList<Tile> tiles) {
		hand.clear();
		this.hand = tiles;
	}

	// Returns the hand
	public ArrayList<Tile> getHand() {
		return hand;
	}

	// Returns the handsize
	public int getHandSize() {
		return hand.size();
	}

	// Clears the hand
	public void clearHand() {
		hand.clear();

	}

}
