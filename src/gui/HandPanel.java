package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JPanel;

// A panel to show the tiles
public class HandPanel extends JPanel{
	private ArrayList<TilePanel> tiles = new ArrayList<TilePanel>();
	
	public HandPanel(){
		this.setBackground(Color.white);
		this.setPreferredSize(new Dimension(500,50));
		this.setBackground(Color.blue);
	}
	
	// Adds the tiles to the hand
	public void addTile(TilePanel t){
		this.add(t);
	}
	
	// Removes the tiles from the hand
	public void disposeTiles(){
		this.removeAll();
	}
}
