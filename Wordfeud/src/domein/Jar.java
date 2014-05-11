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
	
	// NIEUW //
	public BufferedImage getImage(String letter){
		return images.get(letter);
	}
	// NIEUW TOT HIER //
	
	public void resetJar(){
		tiles.clear();
	}
	
	// Get the size of the Jar
	public int getJarSize(){
		return tiles.size();
	}

	// methode vul Jar aan de hand van stenen in de pot (database)
	// Als nieuwe game is - database vullen

	public Tile createTile(int id, String letter, int value) {
		tile = new Tile(id, letter, value, images.get(letter));
		return tile;
	}
	
	public Tile createTile(String letter, int value) {
		tile = new Tile(letter, value, images.get(letter));
		return tile;
	}
	

	public Tile getNewTile() {
		int randomTile = (int) (0 + (Math.random()) * ((tiles.size() - 0)));
		Tile tile = tiles.get(randomTile);
		// als er een tile getrokken wordt en in de hand zit
		// zit het niet meer in de pot dus wordt verwijderd
		tiles.remove(tile);
		// haal tile uit de arraylist
		// vraag tile op aan database en voeg toe aan hand
		return tile;
	}

	/*
	public void fillJar() {
		String[] alphabet = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
				"K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
				"W", "X", "Y", "Z", "?" };
		int[] numberOfTiles = { 10, 2, 2, 5, 12, 2, 3, 3, 9, 1, 1, 4, 2, 6, 7,
				2, 1, 6, 5, 7, 4, 2, 2, 1, 2, 1, 2 };
		// Laatste waard moet 2 zijn - nu zijn er 100 jokers
		int[] tileValue = { 1, 4, 4, 2, 1, 4, 3, 4, 1, 10, 5, 1, 3, 1, 1, 4,
				10, 1, 1, 1, 2, 4, 4, 8, 4, 10, 0 };
		// hard coded contence of Jar

		for (int i = 0; i < alphabet.length; i++) {
			for (int k = 0; k < numberOfTiles[i]; k++) {
				tiles.add(tile = new Tile(alphabet[i], tileValue[i], images.get(alphabet[i])));
			}
		}
	}*/

	public void addNewTile(Tile t) {
		tiles.add(t);
		// vul aan database
	}

	public void getJar() {
		// tiles.clear
		// vult de jar aan de hand van de database
	}
}
