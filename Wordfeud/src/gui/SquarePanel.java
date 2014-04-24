package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import domein.Square;
import domein.Tile;

public class SquarePanel extends JPanel {

	private static final int SIZE = 33;
	private BufferedImage squareImage;
	private BufferedImage tileImage;
	private int x;
	private int y;

	// Er moet een mogelijkheid komen om tiles toe te voegen aan de squarepanel
	// Er kan dan een check komen of er een tile in de square panel zit of niet
	// Dus er moet ook een square hier worden toegevoegd zodat als er geen tile
	// is
	// je de square value kunt laten zien.

	// public SquarePanel(int x, int y, Color color) {
	public SquarePanel(Square square) {
		this.squareImage = square.getImage();
		this.tileImage = null;
		this.x = square.getXPos();
		this.y = square.getYPos();
		this.setPreferredSize(new Dimension(SIZE, SIZE));
		//this.setBackground(Color.red);
	}

	// public void setBackgroundColor(Color color){
	// this.setBackground(color);
	// }

	// toevoegen en verwijderen van een tile
	public void addImage(BufferedImage tileImage) {
		this.tileImage = tileImage;
	}

	public void removeImage() {
		this.tileImage = null;
	}
	
	// kijken of er een tile opligt of niet
	public boolean getOccupied() {
		if (tileImage != null) {
			return true;
		} else {
			return false;
		}
	}

	public int getYValue() {
		return y;
	}

	public int getXValue() {
		return x;
	}

	public void paintComponent(Graphics g) {
		if (tileImage == null) {
			g.drawImage(squareImage, 0, 0, 33, 33, null);
		} else {
			g.drawImage(tileImage, 0, 0, 33, 33, null);
		}
	}

}