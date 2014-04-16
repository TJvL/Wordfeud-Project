package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JPanel;

public class HandPanel extends JPanel{
	private ArrayList<TilePanel> tiles = new ArrayList<TilePanel>();
	
	public HandPanel(){
		this.setBackground(Color.white);
		this.setPreferredSize(new Dimension(500,66));
		addTiles();
	}
	
	// temp
	public void addTiles() {
		for (int p = 0; p < 7; p++) {
			TilePanel tile = new TilePanel(p);
			tiles.add(tile);
			this.add(tile);
		}
	}
	
	/*
	public void addTile(Tile t){
		TilePanel tile = new TilePanel(t.getNumber());
		tiles.add(tile);
		this.add(tile);
	}
	
	public void removeTile(int number){
		for (TilePanel t: tiles){
			if (t.getNumber() == number){
				tiles.remove(number);
			}
		}
	}
	*/
}
