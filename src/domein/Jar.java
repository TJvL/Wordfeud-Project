package domein;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import datalaag.FileHandler;

public class Jar {
	private ArrayList<Tile> tiles;
	private HashMap<String, BufferedImage> images;
	private Tile tile;
	private FileHandler fh;

	// Loads all the images
	public Jar() {
		fh = FileHandler.getInstance();
		tiles = new ArrayList<Tile>();
		images = new HashMap<String, BufferedImage>();
		String[] alphabet = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
				"K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
				"W", "X", "Y", "Z", "?" };
		for (String letters : alphabet) {
			if (letters.equals("?")) {
				images.put(letters, fh.readImage("Plaatjes/Joker.png"));
			} else {
				images.put(letters,
						fh.readImage("Plaatjes/" + letters + ".png"));
			}
		}
	}
	
	// Returns a image
	public BufferedImage getImage(String letter){
		return images.get(letter);
	}
	
	// Resets the jar
	public void resetJar(){
		tiles.clear();
	}
	
	// Get the size of the Jar
	public int getJarSize(){
		return tiles.size();
	}

	// Creating a new Tile
	public Tile createTile(int id, String letter, int value) {
		tile = new Tile(id, letter, value, images.get(letter));
		return tile;
	}
	
	// Creating a new Tile
	public Tile createTile(String letter, int value) {
		tile = new Tile(letter, value, images.get(letter));
		return tile;
	}
	
	// Returns a new tile from the jar
	public Tile getNewTile() {
		int randomTile = (int) (0 + (Math.random()) * ((tiles.size() - 0)));
		Tile tile = tiles.get(randomTile);
		tiles.remove(tile);
		return tile;
	}

	// Adds a tile to the jar
	public void addNewTile(Tile t) {
		tiles.add(t);
	}
}
