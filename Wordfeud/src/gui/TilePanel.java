package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import domein.Tile;

public class TilePanel extends JPanel {
	private int size = 33;
	private static final int PANEL_SIZE = 45;
	private BufferedImage image;
	private Tile tile;

	// Creating a tilepanel
	public TilePanel(Tile tile) {
		this.tile = tile;
		this.image = tile.getImage();
		this.setPreferredSize(new Dimension(PANEL_SIZE, PANEL_SIZE));
		this.setBackground(Color.white);
		this.setSelected(false);
	}

	// A method to set the tile size
	public void setSelected(boolean selected) {
		if (selected) {
			size = 45;
		} else {
			size = 33;
		}
	}
	
	// Forces to repaint the panel
	public void repaintPanel(){
		this.repaint();
	}
	
	// Returns the stored tile from the panel
	public Tile getTile(){
		return tile;
	}

	// Paints the tile img
	public void paintComponent(Graphics g) {
		g.clearRect(0, 0, 45, 45);
		g.drawImage(image, 0, 0, size, size, null);

	}
}
