package domein;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JOptionPane;

import datalaag.DatabaseHandler;
import gui.GameFieldPanel;
import gui.TilePanel;

public class Match {
	private Tile selectedTile;
	private Jar jar;
	private Board board;
	private GameFieldPanel gameField;
	private DatabaseHandler dbh;
	private Player player;
	@SuppressWarnings("unused")
	private Player enemy;
	private int gameID;
	// private Map<Integer, String> jarTiles;
	private Map<Integer, String> tileIndex;
	private ArrayList<String> tilesForJar;
	private boolean myTurn;
	private int maxTurn;

	// /////////////////////////////////////////////////////////////////
	// Spelers nog toevoegen aan een spel ////////////////////////////
	// /////////////////////////////////////////////////////////////////

	public Match(int gameID, Player player) {
		this.gameID = gameID;
		board = new Board();
		jar = new Jar();
		this.player = player;
		dbh = DatabaseHandler.getInstance();
		myTurn = true;
		// Load the enemy from the database and add is as a new player
		// this.enemy = new enemy(stuff to make a new enemy)
	}

	public int getGameID() {
		return gameID;
	}

	public synchronized boolean getMyTurn() {
		return myTurn;
	}

	public synchronized void getMaxTurnID() {

		// TIJDELIJKE NAAMGEVING
		// Player - user gedeelte is nog niet af - kan nog geen naam opvragen
		try {
			String[] splits = dbh.checkTurn(getOwnName(), gameID).split("---");
			maxTurn = Integer.parseInt(splits[1]);
			if (splits[0].equals("true")) {
				myTurn = true;
			} else {
				myTurn = false;
			}
		} catch (NullPointerException e) {
			System.out.println("NULLPOINTER");
		}
		// System.out.println("HET IS BEURT " + myTurn + ". DUS TURN " +
		// maxTurn);
	}

	public void startNewGame(GameFieldPanel gameFieldPanel) {
		this.gameField = gameFieldPanel;

		getMaxTurnID();
		// vullen van de jar - een nieuwe jar aanmaken
		// opvragen van het bord
		for (int y = 1; y < 16; y++) {
			for (int x = 1; x < 16; x++) {
				board.addSquaresNewBoard(x - 1, y - 1, dbh.squareCheck(x, y));
			}
		}

		// Method to add the tiles to the jar
		System.out.println(gameID + " DE GAME ID");
		tilesForJar = dbh.createJar(gameID, "EN");
		for (String tiles : tilesForJar) {
			int value = dbh.letterValue("EN", tiles);
			Tile t = jar.createTile(tiles, value);
			jar.addNewTile(t);
		}
		/*
		 * jarTiles = dbh.jarContent(gameID); for (Entry<Integer, String> entry
		 * : jarTiles.entrySet()) { Integer letterID = entry.getKey(); String
		 * letter = entry.getValue(); int value = dbh.letterValue("EN", letter);
		 * Tile t = jar.createTile(letter, value); jar.addNewTile(t);
		 * 
		 * // ... }
		 */

		tileIndex = dbh.gameTiles(gameID);
		fillHand();

		// tijdelijk voor het zetten van een beurt van de tegenstander
		dbh.updateTurn(maxTurn + 1, gameID, getEnemyName(), 0, "Begin");

		// Ik heb nog een letter id nodig hiervoor
		/*
		 * for (Integer tiles: jarTiles){ int value = dbh.letterValue("EN",
		 * tiles); Tile t = jar.createTile(tiles, value); jar.addNewTile(t);
		 * 
		 * }
		 */
		jar.setGameID(gameID);
		board.setGameID(gameID);

		// hier moet nog handvullen komen ------ fillHand();

		gameField.addSquares();
		gameField.repaintBoard();

	}

	// Load a game from the database
	public void loadGame(GameFieldPanel gameFieldPanel) {
		this.gameField = gameFieldPanel;
		tileIndex = dbh.gameTiles(gameID);
		getMaxTurnID();
		jar.setGameID(gameID);
		board.setGameID(gameID);

		// Creating the board
		for (int y = 1; y < 16; y++) {
			for (int x = 1; x < 16; x++) {
				board.addSquaresNewBoard(x - 1, y - 1, dbh.squareCheck(x, y));
			}
		}
		// Creating the jar - This loads the jar from the database
		for (Entry<Integer, String> entry : tileIndex.entrySet()) {
			Integer letterID = entry.getKey();
			String letter = entry.getValue();
			int value = dbh.letterValue("EN", letter);
			Tile t = jar.createTile(letter, value);
			jar.addNewTile(t);

			// ...
		}

		// Checks if the game being loaded has not started and the player was
		// invited
		// The enemy then started so there are no words to load or hand to load
		if (maxTurn != 2 && !myTurn) {
			// Loads all the played words
			ArrayList<String> playedWords = dbh.playedWords(gameID);
			for (String played : playedWords) {
				String[] splits = played.split("---");
				String[] letters = splits[0].split(",");
				String[] xPos = splits[1].split(",");
				String[] yPos = splits[2].split(",");

				for (int p = 0; p < letters.length; p++) {
					int value = dbh.letterValue("EN", letters[p]);
					Tile t = jar.createTile(letters[p], value);
					board.addTileToSquare(t, Integer.parseInt(xPos[p]) - 1,
							Integer.parseInt(yPos[p]) - 1);
				}
			}

			// Loads the player hand
			if (!dbh.gameStatusValue(gameID).equals("Finished")
					|| !dbh.gameStatusValue(gameID).equals("Resigned")) {
				String handTiles;
				if (myTurn) {
					handTiles = dbh.handContent(gameID, maxTurn);
				} else {
					handTiles = dbh.handContent(gameID, maxTurn - 1);
				}
				String[] tiles = handTiles.split(",");
				for (int p = 0; p < tiles.length; p++) {
					int value = dbh.letterValue("EN", tiles[p]);
					Tile t = jar.createTile(tiles[p], value);
					addTileToHand(t);
				}
			} else {
				System.out.println(gameID + " De game is al over");
			}
		}

		// Method of fill the hand at the beginning of a game
		else {
			fillHand();
		}
		// Method when the game has not really started

		// Moet er ook nog tussenkomen
		gameField.addSquares();
		gameField.repaintBoard();

	}

	// Spectator only uses match to board to load up the squares
	// And uses the hands
	// Uses a seprate panel
	public void loadSpecateGame(int gameID) {

	}

	// Return the player names
	public synchronized String getOwnName() {
		/*
		 * if (player.getName() != null) { return player.getName(); } else {
		 * return "No-Name"; }
		 */

		// De usernaam moet nog ergens worden opgevraagd
		return "jager684";
	}

	public synchronized String getEnemyName() {
		// if (enemy.getName() != null) {
		return dbh.opponentName(gameID);
		// } else {
		// return "No-Name";
		// }
	}

	// Jar gedeelte
	public void fillJar() {
		// checke of er al een pot is
		// dus checke of het een nieuwe game is of een waar je verder in speelt
		// database bestaat nog niet

		// jar.fillJar();
	}

	// Method for loading games to add tiles to hand
	public void addTileToHand(Tile t) {
		player.addTileToHand(t);
		gameField.addTileToHand(t);
		gameField.repaintBoard();
	}

	// Takes a random tile from the jar
	// Add it to the player and and to the gui
	public int getTileFromJar() {
		Tile t = jar.getNewTile();
		// addTileToHand(t);
		for (Entry<Integer, String> entry : tileIndex.entrySet()) {
			Integer letterID = entry.getKey();
			String letter = entry.getValue();
			if (t.getLetter().equals(letter)) {
				player.addTileToHand(t);
				gameField.addTileToHand(t);
				gameField.repaintBoard();
				tileIndex.remove(letterID);
				return letterID;
			}
		}
		return -1;
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
		selectedTile.setJustPlayed(true);
		if (selectedTile.getValue() == 0) {

			String[] choices = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
					"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
					"V", "W", "X", "Y", "Z" };
			String input = (String) JOptionPane.showInputDialog(null,
					"Select your letter below...", "Choose you letter",
					JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
			System.out.println(input);
			selectedTile.setBlancoLetterValue(input);
			selectedTile.setLetter(input);

			/*
			 * String input = JOptionPane.showInputDialog(
			 * "Enter the letter you want the Joker to be:"); if (input !=
			 * null){ System.out.println(input);
			 * tile.setBlancoLetterValue(input); }
			 */
			// Hier moet de methode komen voor input vanuit de joker
		}
		player.removeTileFromHand(selectedTile);
		board.addTileToSquare(selectedTile, x, y);
		board.startCalculating();
		gameField.repaintBoard();
	}

	// Takes a tile from the board and puts it back in the hand
	// X and Y are the cords for the squares on the board
	// Needed to get the tile back
	public void moveTileFromBoardToHand(int x, int y) {
		Tile t = board.getSquare(x, y).getTile();
		t.setJustPlayed(false);
		if (t.getValue() == 0) {
			t.setBlancoLetterValue(null);
			t.setLetter("?");
		}
		player.addTileToHand(t);
		board.removeTileFromSquare(x, y);
		gameField.addTileToHand(t);
		board.startCalculating();
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
		board.startCalculating();
		gameField.repaintBoard();
	}

	// Fills the hand back to 7
	public void fillHand() {
		//ArrayList<Integer> tileID = new ArrayList<Integer>();
		while (player.getHandSize() < 7 && jar.getJarSize() > 0) {
			int id = getTileFromJar();
			if (id != -1) {
		//		tileID.add(id);
			} else {
				System.out.println("ER IS IETS FOUT GEGAAN BY FILLHAND");
			}
			gameField.repaintBoard();
		}
		ArrayList<Tile> tilesInHand = player.getHand();

		// Dit kan dus nog niet - we hebben de tile id nog niet opgeslagen in tiles
		
		//dbh.addTileToHand(gameID, tileID, maxTurn);
		if (maxTurn == 1 && myTurn) {
			dbh.updateTurn(maxTurn, gameID, getOwnName(), 0, "Begin");
		} else if (maxTurn == 2 && myTurn) {
			dbh.updateTurn(maxTurn, gameID, getOwnName(), 0, "Begin");
		}
		board.setScore();
	}

	// Swapping tiles from the hand back to the jar
	public void swapTiles(ArrayList<TilePanel> tilesToSwap) {
		for (TilePanel tile : tilesToSwap) {
			addTileToJar(tile.getTile());
		}
		tilesToSwap.clear();
		fillHand();

		dbh.updateTurn(maxTurn, gameID, getOwnName(), 0, "Swap");
	}

	// Gets a square from the board on x,y
	public Square getSquare(int x, int y) {
		return board.getSquare(x, y);
	}

	// Stores the selected tile
	public void selectedTile(Tile t) {
		this.selectedTile = t;
	}

	// Gets the selected tile back
	public Tile getSelectedTile() {
		return selectedTile;
	}

	// Get the score of the just placed tiles
	public int getScore() {
		return board.getScore();
	}

	// When the play button is pressed
	// This method does the word
	public void playWord() {

		if (board.startCalculating()) {
			// if this is true, a score will be calculated
			System.err.println("De score is berekend");
			if (board.checkWords()) {
				// ArrayList<Tile> justPlayedTiles = board.addtilesToDatabase();
				/*
				 * for (Tile tiles: justPlayedTiles){
				 * tileIndex.get(tiles.getLetter()); dbh.tileToBoard(gameID,
				 * maxTurn, tiles.getTileID(), tiles.getBlancoLetterValue(),
				 * tiles.getXValue(), tiles.getYValue()); }
				 */

				board.setTilesPlayed();

				dbh.updateTurn(maxTurn, gameID, getOwnName(), getScore(),
						"Word");

				fillHand();

				// Tijdelijke reactie van de tegenstander
				dbh.updateTurn(maxTurn + 1, gameID, getEnemyName(), 0, "Pass");

			} else {
				System.err.println("WOORDEN ZIJN FOUT");
				// hier moet de optie komen om ze te laten keuren
			}
		}
	}

	// A method for the secondThread to see if a word is submitted
	public synchronized String getSubmittedWord() {
		return board.getWord();
	}

	public synchronized void checkWord() {
		board.checkWord();
	}

	// Method to surrender the game
	public void surrenderGame() {
		// Eige naam nog toevoegen - die classe zijn nog niet helemaal goed
		// dbh.surrender(gameID, getMaxTurnID(), "Klaas");

		if (myTurn) {
			dbh.updateTurn(maxTurn, gameID, getOwnName(), 0, "Resign");
		} else {
			dbh.updateTurn(maxTurn + 1, gameID, getOwnName(), 0, "Resign");
		}
		System.out.println("JE HEBT GESURRENDERD!");

		// System.exit(0);
	}

	public void skipTurn() {
		// Naam nog toevoegen

		dbh.updateTurn(maxTurn, gameID, getOwnName(), 0, "Pass");
		// dbh.updateTurn(getMaxTurnID(), gameID, "Klaas", dbh.score(gameID,
		// "Klaas), "Pass");
	}
}
