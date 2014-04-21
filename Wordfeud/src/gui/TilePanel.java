package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import domein.Tile;

public class TilePanel extends JPanel {
	private int size = 33;
	private static final int PANEL_SIZE = 45;
	private Tile tile;
	private boolean selected;

	// Every tiles has his own unique number
	// The color is gone be changed by a image
	// Use a paintcomponent for this

	// Moeten nog een tile object kunnen toevoegen hier voor het weergeven van
	// de letter
	public TilePanel(Tile tile) {
		this.tile = tile;
		this.setPreferredSize(new Dimension(PANEL_SIZE, PANEL_SIZE));
		this.setBackground(Color.white);
		// this.number = number;

		// this.setBackground(color);
		this.setSelected(false);
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
		if (selected) {
			size = 45;
		} else {
			size = 33;
		}
	}
	
	public void repaintPanel(){
		this.repaint();
	}

	// public int getNumber() {
	// return number;
	// }

	public void paintComponent(Graphics g) {
		g.clearRect(0, 0, 45, 45);
		g.drawImage(tile.getImage(), 0, 0, size, size, null);

	}

	public Tile getTile() {
		return tile;
	}

}
