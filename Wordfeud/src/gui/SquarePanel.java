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
	private BufferedImage image;
	private int x;
	private int y;
	private Square square;
	private Tile tile;

	// Er moet een mogelijkheid komen om tiles toe te voegen aan de squarepanel
	// Er kan dan een check komen of er een tile in de square panel zit of niet
	// Dus er moet ook een square hier worden toegevoegd zodat als er geen tile
	// is
	// je de square value kunt laten zien.

	// public SquarePanel(int x, int y, Color color) {
	public SquarePanel(int x, int y, Square square) {
		this.square = square;
		this.image = square.getImage();
		this.x = x;
		this.y = y;
		this.setPreferredSize(new Dimension(SIZE, SIZE));
		// this.setBackground(Color.yellow);
	}

	// public void setBackgroundColor(Color color){
	// this.setBackground(color);
	// }

	// toevoegen en verwijderen van een tile
	public void addTile(Tile t) {
		this.tile = t;
	}

	public void removeTile() {
		this.tile = null;
	}

	public Tile getTile() {
		return tile;
	}

	// kijken of er een tile opligt of niet
	public boolean getOccupied() {
		if (tile != null) {
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
		if (tile == null) {
			g.drawImage(square.getImage(), 0, 0, null);
		} else {
			g.drawImage(tile.getImage(), 0, 0, null);
		}
	}

}