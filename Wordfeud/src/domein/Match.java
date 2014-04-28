package domein;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import gui.GameFieldPanel;
import gui.TilePanel;

public class Match {
	private Tile tile;
	private Jar jar;
	private Board board;
	private GameFieldPanel gameField;
	private Player player;
	@SuppressWarnings("unused")
	private Player enemy;

	public Match(Player player) {
		this.player = player;

		board = new Board();
		jar = new Jar();
		// Load the enemy from the database and add is as a new player
		// this.enemy = new enemy(stuff to make a new enemy)
	}

	public void startNewGame(GameFieldPanel gameFieldPanel) {
		this.gameField = gameFieldPanel;
		// vullen van de jar - een nieuwe jar aanmaken
		fillJar();
		gameField.addSquares();
		gameField.repaintBoard();
		fillHand();
	}

	// Return the player names
	public String getOwnName(){
		if (player.getName() != null){
		return player.getName();
		} else {
			return "No-Name";
		}
	}
	
	public String getEnemyName(){
		if (enemy.getName() != null){
		return enemy.getName();
		} else {
			return "No-Name";
		}
	}
	
	// Jar gedeelte
	public void fillJar() {
		// checke of er al een pot is
		// dus checke of het een nieuwe game is of een waar je verder in speelt
		// database bestaat nog niet

		jar.fillJar();
	}

	// Takes a random tile from the jar
	// Add it to the player and and to the gui
	public void getTileFromJar() {
		Tile t = jar.getNewTile();
		player.addTileToHand(t);
		gameField.addTileToHand(t);
		gameField.repaintBoard();
	}

	// Add a tile to the jar
	// remove it from the hand en field
	public void addTileToJar(Tile t) {
		player.removeTileFromHand(t);
		gameField.removeTileFromHand(t);
		jar.addNewTile(t);
	}

	// Takes the tile selected and move it to the board
	// Sets the tile on just played
	// Add the tile to the board
	public void moveTileFromHandToBoard(int x, int y) {
		tile.setJustPlayed(true);
		if (tile.getValue() == 0) {

			String[] choices = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
					"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
					"V", "W", "X", "Y", "Z" };
			String input = (String) JOptionPane.showInputDialog(null,
					"Select your letter below...", "Choose you letter",
					JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
			System.out.println(input);
			tile.setBlancoLetterValue(input);
			tile.setLetter(input);

			/*
			 * String input = JOptionPane.showInputDialog(
			 * "Enter the letter you want the Joker to be:"); if (input !=
			 * null){ System.out.println(input);
			 * tile.setBlancoLetterValue(input); }
			 */
			// Hier moet de methode komen voor input vanuit de joker
		}
		player.removeTileFromHand(tile);
		board.addTileToSquare(tile, x, y);
		startCalculating();
		gameField.repaintBoard();
	}

	// Takes a tile from the board and puts it back in the hand
	// X and Y are the cords for the squares on the board
	// Needed to get the tile back
	public void moveTileFromBoardToHand(int x, int y) {
		Tile t = board.getSquare(x, y).getTile();
		t.setJustPlayed(false);
		if (t.getValue() == 0){
			t.setBlancoLetterValue(null);
			t.setLetter("?");
		}
		player.addTileToHand(t);
		board.removeTileFromSquare(x, y);
		gameField.addTileToHand(t);
		startCalculating();
		gameField.repaintBoard();
	}

	// Clears all the tiles from the board
	public void clearTilesFromBoard() {
		for (int y = 0; y < 15; y++) {
			for (int x = 0; x < 15; x++) {
				if (gameField.getOccupied(x, y)
						&& board.getSquare(x, y).getTile().getJustPlayed()) {
					board.getSquare(x, y).getTile().setJustPlayed(false);
					gameField.removeImageSquare(x, y);
					moveTileFromBoardToHand(x, y);
					gameField.repaintBoard();
				}
			}
		}
		startCalculating();
		gameField.repaintBoard();
	}

	// Fills the hand back to 7
	public void fillHand() {
		while (player.getHandSize() < 7) {
			getTileFromJar();
			gameField.repaintBoard();
		}
	}

	// Swapping tiles from the hand back to the jar
	public void swapTiles(ArrayList<TilePanel> tilesToSwap) {
		for (TilePanel tile : tilesToSwap) {
			addTileToJar(tile.getTile());
		}
		tilesToSwap.clear();
		fillHand();
	}

	// Gets a square from the board on x,y
	public Square getSquare(int x, int y) {
		return board.getSquare(x, y);
	}

	// Stores the selected tile
	public void selectedTile(Tile t) {
		this.tile = t;
	}

	// Gets the selected tile back
	public Tile getSelectedTile() {
		return tile;
	}

	// Beurt wisselingen
	public void changeTurn() {
		// verander van beurt
	}

	// A method to start calculating
	public boolean startCalculating() {
		return board.startCalculating();
	}

	public void setTilesPlayed(){
		board.setTilesPlayed();
	}
	
	public boolean checkWords(){
		return board.checkWords();
	}
	
	// Get the score of the just placed tiles
	public int getScore(){
		return board.getScore();
	}
}
