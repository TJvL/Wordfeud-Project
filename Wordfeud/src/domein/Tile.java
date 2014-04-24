package domein;

import java.awt.image.BufferedImage;

import datalaag.FileHandler;

public class Tile {
	private String letter;
	private int value;
	private boolean justPlayed;
	private String blancoLetterValue;
	private FileHandler fh = FileHandler.getInstance();
	private int x = 1111;
	private int y = 1111;
	private BufferedImage image;

	public Tile(String letter, int value, BufferedImage image) {
		this.letter = letter;
		this.value = value;
		this.justPlayed = false;
		this.image = image;
	}

	// Als een letter wordt gelegd en bevestigt dam slaat de gelegde letter ook
	// zijn positie op

	public void setLetter(String letter){
		this.letter = letter;
	}
	
	public String getLetter() {
		return letter;
	}

	public int getValue() {
		return value;
	}

	public void setBlancoLetterValue(String letter) {
		this.blancoLetterValue = letter;
	}

	public String getBlancoLetterValue() {
		return blancoLetterValue;
	}

	public BufferedImage getImage() {
		// System.out.println("test " + path);
		// BufferedImage image = fh.readImage(path);
		// BufferedImage image = fh.readImage("Plaatjes/leeg_plaatje.png");
		if (blancoLetterValue != null) {
			return fh.readImage("Plaatjes/" + blancoLetterValue + "J.png");
		} else {
			return image;
		}
	}

	public void setJustPlayed(boolean justPlayed) {
		this.justPlayed = justPlayed;
	}

	public boolean getJustPlayed() {
		return justPlayed;
	}

	public void setXValue(int x) {
		this.x = x;
	}

	public void setYValue(int y) {
		this.y = y;
	}

	public int getXValue() {
		return x;
	}

	public int getYValue() {
		return y;
	}
}
