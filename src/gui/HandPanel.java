package gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

import datalaag.WordFeudConstants;

// A panel to show the tiles
@SuppressWarnings("serial")
public class HandPanel extends JPanel{
	
	public HandPanel(){

		this.setPreferredSize(new Dimension(500,50));
		this.setBackground(WordFeudConstants.DARKBLUE);
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
