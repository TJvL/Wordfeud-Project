package domein;

import java.awt.image.BufferedImage;

import datalaag.FileHandler;

public class Tile {
	private String letter;
	private int value;
	private boolean justPlayed;
	private String blancoLetterValue;
	private FileHandler fh = FileHandler.getInstance();
	private int x;
	private int y;
	private BufferedImage image;
	private int tileID;
	private int turn;

	// Constructor one
	public Tile(int tileID, String letter, int value, BufferedImage image) {
		this.letter = letter;
		this.value = value;
		this.justPlayed = false;
		this.image = image;
		this.tileID = tileID;
	}

	// Constructor two
	public Tile(String letter, int value, BufferedImage image) {
		this.letter = letter;
		this.value = value;
		this.justPlayed = false;
		this.image = image;
	}

	public Tile(int x, int y) {
		this.x = x;
		this.y = y;
	}

	// Constructor for spectating
	public Tile(BufferedImage image, int turn) {
		this.image = image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	// Sets the letter
	public void setLetter(String letter) {
		this.letter = letter;
	}

	// Returns the letter
	public String getLetter() {
		if (blancoLetterValue != null) {
			return blancoLetterValue;
		} else {
			return letter;
		}
	}

	// Returns the value
	public int getValue() {
		return value;
	}

	// Sets the blanco value - joker
	public void setBlancoLetterValue(String letter) {
		this.blancoLetterValue = letter;
	}

	// Returns the blanco value - joker
	public String getBlancoLetterValue() {
		return blancoLetterValue;
	}

	// Returns the image
	public BufferedImage getImage() {
		if (blancoLetterValue != null) {
			return fh.readImage("Plaatjes/" + blancoLetterValue + "J.png");
		} else {
			return image;
		}
	}

	// Sets the tile to justPlayed
	public void setJustPlayed(boolean justPlayed) {
		this.justPlayed = justPlayed;
	}

	// Returns justPlayed
	public boolean getJustPlayed() {
		return justPlayed;
	}

	// Sets the x value if it is a played tile
	public void setXValue(int x) {
		this.x = x;
	}

	// Sets the y value if it is a played tile
	public void setYValue(int y) {
		this.y = y;
	}

	// Returns the x value
	public int getXValue() {
		return x;
	}

	// Returns the value
	public int getYValue() {
		return y;
	}

	// Returns the tileID
	public int getTileID() {
		return tileID;
	}

	// Returns the turn played on
	public int getTurn() {
		return turn;
	}
}
