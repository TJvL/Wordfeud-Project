package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JPanel;

public class HandPanel extends JPanel{
	private ArrayList<TilePanel> tiles = new ArrayList<TilePanel>();
	
	public HandPanel(){
		this.setBackground(Color.LIGHT_GRAY);
		this.setPreferredSize(new Dimension(500,50));
	}
	

	public void addTile(TilePanel t){
		this.add(t);
	}
	
	public void disposeTiles(){
		this.removeAll();
	}
	
	//public void getSquareValue(){
		
	//}
}
