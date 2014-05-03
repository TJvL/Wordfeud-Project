package domein;

import java.util.ArrayList;

public class Player extends Role
	{
		private Hand hand;

		public Player(boolean hasPermissions)
			{
				super(hasPermissions);
				hand = new Hand();
			}

		public void addTileToHand(Tile t)
			{
				hand.addTileToHand(t);
			}

		public void removeTileFromHand(Tile t)
			{
				hand.removeTileFromHand(t);
			}

		public int getHandSize()
			{
				return hand.getHandSize();
			}

		public ArrayList<Tile> getHand()
			{
				return hand.getHand();
			}
	}
