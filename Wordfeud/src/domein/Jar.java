package domein;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import datalaag.FileHandler;

public class Jar {
	private ArrayList<Tile> tiles = new ArrayList<Tile>();
	private Tile tile;
	private FileHandler fh;

	public Jar() {
		fh = FileHandler.getInstance();
	}

	// methode vul Jar aan de hand van stenen in de pot (database)
	// Als nieuwe game is - database vullen

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

	public void fillJar() {
		String[] alphabet = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
				"K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
				"W", "X", "Y", "Z", "empty" };
		int[] numberOfTiles = { 10, 2, 2, 5, 12, 2, 3, 3, 9, 1, 1, 4, 2, 6, 7,
				2, 1, 6, 5, 7, 4, 2, 2, 1, 2, 1, 2 };
		int[] tileValue = { 1, 4, 4, 2, 1, 4, 3, 4, 1, 10, 5, 1, 3, 1, 1, 4,
				10, 1, 1, 1, 2, 4, 4, 8, 4, 10, 0 };
		// hard coded contence of Jar

		for (int i = 0; i < alphabet.length; i++) {
			BufferedImage image = fh.readImage("Plaatjes/" + alphabet[i]
					+ ".png");
			for (int k = 0; k < numberOfTiles[i]; k++) {
				tiles.add(tile = new Tile(alphabet[i], tileValue[i], image));
			}
		}
	}

	public void addNewTile(Tile t) {
		tiles.add(t);
		// vul aan database
	}

	public void getJar() {
		// tiles.clear
		// vult de jar aan de hand van de database
	}
}
