package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import domein.Square;

@SuppressWarnings("serial")
public class SquarePanel extends JPanel {

	private static final int SIZE = 33;
	private BufferedImage squareImage;
	private BufferedImage tileImage;
	private int x;
	private int y;

	// Contructor from the panel
	// As parameter is a square to get the img from it
	public SquarePanel(Square square) {
		this.squareImage = square.getImage();
		this.tileImage = null;
		this.x = square.getXPos();
		this.y = square.getYPos();
		this.setPreferredSize(new Dimension(SIZE, SIZE));
	}

	// Adding a image to the square
	public void addImage(BufferedImage tileImage) {
		this.tileImage = tileImage;
	}

	// Removes the tile img
	public void removeImage() {
		this.tileImage = null;
	}
	
	// Checks if a square is occupied or not
	public boolean getOccupied() {
		if (tileImage != null) {
			return true;
		} else {
			return false;
		}
	}

	// Gets the y value
	public int getYValue() {
		return y;
	}

	// Gets the X value
	public int getXValue() {
		return x;
	}

	// Paints the TileImg if not empty, else it will paint the square img
	public void paintComponent(Graphics g) {
		g.setColor(Color.blue);
		g.fillRect(0, 0, 50, 50);
		if (tileImage == null) {
			g.drawImage(squareImage, 0, 0, 33, 33, null);
		} else {
			g.drawImage(tileImage, 0, 0, 33, 33, null);
		}
	}

}