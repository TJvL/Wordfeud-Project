package domein;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JOptionPane;

import datalaag.DatabaseHandler;
import gui.GameFieldPanel;
import gui.GameSpecScreen;
import gui.SpecScreen;
import gui.TilePanel;

public class Match implements Observer{
	private Tile selectedTile;
	private Jar jar;
	private Board board;
	private GameFieldPanel gameField;
	private GameSpecScreen gameSpec;
	private DatabaseHandler dbh;
	private Player player;
	@SuppressWarnings("unused")
	private Player enemy;
	private int gameID;
	// private Map<Integer, String> jarTiles;
	// private Map<Integer, String> tileIndex;
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

	// NIEUW - voor spectaten ***************************************//
	public Match(int gameID) {
		this.gameID = gameID;
		board = new Board();
		jar = new Jar();
		dbh = DatabaseHandler.getInstance();
	}

	// Dit stond na loadmatch

	// Spectator only uses match to board to load up the squares
	// And uses the hands
	// Uses a separate panel
	public void loadSpecateGame(GameSpecScreen gameSpecScreen) {
		this.gameSpec = gameSpecScreen;

		dbh.connection();
		// vullen van de jar - een nieuwe jar aanmaken
		// opvragen van het bord
		for (int y = 1; y < 16; y++) {
			for (int x = 1; x < 16; x++) {
				board.addSquaresNewBoard(x - 1, y - 1, dbh.squareCheck(x, y));
			}
		}
		dbh.closeConnection();
		gameSpec.createField();

		ArrayList<String> playedWords = dbh.playedWords(gameID);
		for (String played : playedWords) {
			String[] splits = played.split("---");
			String[] letters = splits[0].split(",");
			String[] xPos = splits[1].split(",");
			String[] yPos = splits[2].split(",");

			for (int p = 0; p < letters.length; p++) {
				board.addTileToSquare(new Tile(jar.getImage(letters[p])),
						Integer.parseInt(xPos[p]) - 1,
						Integer.parseInt(yPos[p]) - 1);
				gameSpec.addImageToBoard(jar.getImage(letters[p]),
						Integer.parseInt(xPos[p]) - 1,
						Integer.parseInt(yPos[p]) - 1);
			}
		}

		// Creating the score panels and setting the values
		String names = dbh.opponentName(gameID);
		String[] playerNames = names.split("---");
		gameSpec.setScoreP1(dbh.score(gameID, playerNames[0]));
		gameSpec.setNameP1(playerNames[0]);
		gameSpec.setScoreP2(dbh.score(gameID, playerNames[1]));
		gameSpec.setNameP2(playerNames[1]);

		// Adding the tiles to the hands
		ArrayList<String> handTilesP1;
		ArrayList<String> handTilesP2;
		String[] splits = dbh.checkTurn(playerNames[0], gameID).split("---");
		maxTurn = Integer.parseInt(splits[1]);
		if (splits[0].equals("true")) {
			handTilesP1 = dbh.handContent(gameID, maxTurn - 1);
			handTilesP2 = dbh.handContent(gameID, maxTurn);
			myTurn = true;
			gameSpec.setTurn(true);
		} else {
			handTilesP1 = dbh.handContent(gameID, maxTurn);
			handTilesP2 = dbh.handContent(gameID, maxTurn - 1);
			myTurn = false;
			gameSpec.setTurn(false);
		}

		for (int z = 0; z < handTilesP1.size(); z++) {
			String[] tiles = handTilesP1.get(z).split("---");
			Tile t = jar.createTile(Integer.parseInt(tiles[0]), tiles[1],
					Integer.parseInt(tiles[2]));
			gameSpec.addTilesP1(t);
		}

		for (int z = 0; z < handTilesP2.size(); z++) {
			String[] tiles = handTilesP2.get(z).split("---");
			Tile t = jar.createTile(Integer.parseInt(tiles[0]), tiles[1],
					Integer.parseInt(tiles[2]));
			gameSpec.addTilesP2(t);
		}

		gameSpec.repaintBoard();
	}
	
	@Override
	public void update(Observable o, Object arg) {
		String direction = (String) arg;
		System.out.println("OBSERVER TEST: " + direction);
		if (direction.equals("forward")){
			updateSpecTurn(true);
		}else {
			updateSpecTurn(false);
		}
		//updateSpecTurn()
	}
	
	public void updateSpecTurn(boolean forward) {
		String names = dbh.opponentName(gameID);
		String[] playerNames = names.split("---");
		String[] splits = dbh.checkTurn(playerNames[0], gameID).split("---");
		if (forward) {
			if (maxTurn < Integer.parseInt(splits[1])) {
				maxTurn += 1;
				myTurn = !myTurn;
			}
		} else {
			if (maxTurn - 1 > 0) {
				maxTurn -= 1;
				myTurn = !myTurn;
			}
		}
		// Adding the tiles to the hands
		gameSpec.resetHands();
		ArrayList<String> handTilesP1;
		ArrayList<String> handTilesP2;
		if (myTurn) {
			handTilesP1 = dbh.handContent(gameID, maxTurn - 1);
			handTilesP2 = dbh.handContent(gameID, maxTurn);
			gameSpec.setTurnScoreP1(dbh.turnScore(gameID, maxTurn - 1));
			gameSpec.setTurnScoreP2(dbh.turnScore(gameID, maxTurn));
			gameSpec.setTurn(true);
		} else {
			handTilesP1 = dbh.handContent(gameID, maxTurn);
			handTilesP2 = dbh.handContent(gameID, maxTurn - 1);
			gameSpec.setTurnScoreP1(dbh.turnScore(gameID, maxTurn));
			gameSpec.setTurnScoreP2(dbh.turnScore(gameID, maxTurn - 1));
			gameSpec.setTurn(false);
		}

		for (int z = 0; z < handTilesP1.size(); z++) {
			String[] tiles = handTilesP1.get(z).split("---");
			Tile t = jar.createTile(Integer.parseInt(tiles[0]), tiles[1],
					Integer.parseInt(tiles[2]));
			gameSpec.addTilesP1(t);
		}

		for (int z = 0; z < handTilesP2.size(); z++) {
			String[] tiles = handTilesP2.get(z).split("---");
			Tile t = jar.createTile(Integer.parseInt(tiles[0]), tiles[1],
					Integer.parseInt(tiles[2]));
			gameSpec.addTilesP2(t);
		}

		board.clearField();
		gameSpec.resestField();
		
		ArrayList<String> playedWords = dbh.playedWords(gameID);
		for (String played : playedWords) {
			String[] split = played.split("---");
			String[] letters = split[0].split(",");
			String[] xPos = split[1].split(",");
			String[] yPos = split[2].split(",");
			int turn = Integer.parseInt(split[3]);
			
			for (int p = 0; p < letters.length; p++) {
				System.out.println(turn + " MAXTURN: " + maxTurn);
				if (turn <= maxTurn) {
					board.addTileToSquare(new Tile(jar.getImage(letters[p])),
							Integer.parseInt(xPos[p]) - 1,
							Integer.parseInt(yPos[p]) - 1);
					gameSpec.addImageToBoard(jar.getImage(letters[p]),
							Integer.parseInt(xPos[p]) - 1,
							Integer.parseInt(yPos[p]) - 1);
				}
			}
		}	
		gameSpec.repaintBoard();
	}

	public BufferedImage getImage(int x, int y) {
		return board.getImage(x, y);
	}
	// NIEUW TOT HIER **************************************//
	
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

	public synchronized void updateField() {

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
				System.out.println("tile " + t.getLetter() + " placed at x "
						+ (Integer.parseInt(xPos[p]) - 1) + " and at y "
						+ (Integer.parseInt(yPos[p]) - 1));
				gameField.addSquarePanel(Integer.parseInt(xPos[p]) - 1,
						Integer.parseInt(yPos[p]) - 1, t.getImage());
			}
		}

		gameField.repaintBoard();
		jar.resetJar();
		tilesForJar = dbh.jarContent(gameID);
		// Creating the jar - This loads the jar from the database
		for (String tiles : tilesForJar) {
			String[] splits = tiles.split("---");
			Tile t = jar.createTile(Integer.parseInt(splits[0]), splits[1],
					Integer.parseInt(splits[2]));
			jar.addNewTile(t);
		}
	}

	public void startNewGame(GameFieldPanel gameFieldPanel) {
		this.gameField = gameFieldPanel;

		getMaxTurnID();
		dbh.connection();
		// vullen van de jar - een nieuwe jar aanmaken
		// opvragen van het bord
		for (int y = 1; y < 16; y++) {
			for (int x = 1; x < 16; x++) {
				board.addSquaresNewBoard(x - 1, y - 1, dbh.squareCheck(x, y));
			}
		}
		dbh.closeConnection();

		// Method to add the tiles to the jar
		System.out.println(gameID + " DE GAME ID");
		dbh.createJar(gameID, "EN");
		tilesForJar = dbh.jarContent(gameID);
		// Creating the jar - This loads the jar from the database
		for (String tiles : tilesForJar) {
			String[] splits = tiles.split("---");
			Tile t = jar.createTile(Integer.parseInt(splits[0]), splits[1],
					Integer.parseInt(splits[2]));
			jar.addNewTile(t);
			// System.out.println("IETS" + splits[0] + splits[1] + splits[2]);
		}
		/*
		 * jarTiles = dbh.jarContent(gameID); for (Entry<Integer, String> entry
		 * : jarTiles.entrySet()) { Integer letterID = entry.getKey(); String
		 * letter = entry.getValue(); int value = dbh.letterValue("EN", letter);
		 * Tile t = jar.createTile(letter, value); jar.addNewTile(t);
		 * 
		 * // ... }
		 */

		tilesForJar = dbh.jarContent(gameID);
		fillHand();

		// tijdelijk voor het zetten van een beurt van de tegenstander
		// *****
		dbh.updateTurn(maxTurn + 1, gameID, getEnemyName(), 0, "Begin");
		gameField.addSquares();
		gameField.repaintBoard();
	}

	// Load a game from the database
	public void loadGame(GameFieldPanel gameFieldPanel) {
		this.gameField = gameFieldPanel;
		tilesForJar = dbh.jarContent(gameID);
		getMaxTurnID();

		dbh.connection();
		// Creating the board
		for (int y = 1; y < 16; y++) {
			for (int x = 1; x < 16; x++) {
				board.addSquaresNewBoard(x - 1, y - 1, dbh.squareCheck(x, y));
			}
		}
		dbh.closeConnection();
		// Creating the jar - This loads the jar from the database
		for (String tiles : tilesForJar) {
			String[] splits = tiles.split("---");
			Tile t = jar.createTile(Integer.parseInt(splits[0]), splits[1],
					Integer.parseInt(splits[2]));
			jar.addNewTile(t);
		}

		gameField.addSquares();
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
					gameField.addSquarePanel(Integer.parseInt(xPos[p]) - 1,
							Integer.parseInt(yPos[p]) - 1, t.getImage());
				}
			}

			// Loads the player hand
			if (!dbh.getGameStatusValue(gameID).equals("Finished")
					|| !dbh.getGameStatusValue(gameID).equals("Resigned")) {
				ArrayList<String> handTiles;
				if (myTurn) {
					handTiles = dbh.handContent(gameID, maxTurn - 1);
				} else {
					handTiles = dbh.handContent(gameID, maxTurn);
				}

				for (int z = 0; z < handTiles.size(); z++) {
					String[] tiles = handTiles.get(z).split("---");
					Tile t = jar.createTile(Integer.parseInt(tiles[0]),
							tiles[1], Integer.parseInt(tiles[2]));
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

		gameField.repaintBoard();

	}

	// Return the player names
	public synchronized String getOwnName() {
		/*
		 * if (player.getName() != null) { return player.getName(); } else {
		 * return "No-Name"; }
		 */

		// De usernaam moet nog ergens worden opgevraagd
		return "marijntje42";

	}

	public synchronized String getEnemyName() {
		// if (enemy.getName() != null) {
		return "jager684";
		// return dbh.opponentName(gameID);
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
		player.addTileToHand(t);
		gameField.addTileToHand(t);
		gameField.repaintBoard();
		return t.getTileID();
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
			t.setBlancoLetterValue("?");
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

		if (maxTurn == 1 && myTurn) {
			dbh.updateTurn(maxTurn, gameID, getOwnName(), 0, "Begin");
		} else if (maxTurn == 2 && myTurn) {
			dbh.updateTurn(maxTurn, gameID, getOwnName(), 0, "Begin");
		}

		// ArrayList<Integer> tileID = new ArrayList<Integer>();
		while (player.getHandSize() < 7 && jar.getJarSize() > 0) {
			int id = getTileFromJar();
			if (id != -1) {
				// tileID.add(id);
			} else {
				System.out.println("ER IS IETS FOUT GEGAAN BY FILLHAND");
			}
			gameField.repaintBoard();
		}
		if (player.getHandSize() < 7 && jar.getJarSize() == 0) {
			System.out.println("POT IS LEEG, GEEN NIEUWE LETTERS MEER!");
		}
		ArrayList<Tile> tilesInHand = player.getHand();
		ArrayList<Integer> tilesNumber = new ArrayList<Integer>();
		for (Tile tile : tilesInHand) {
			tilesNumber.add(tile.getTileID());
		}
		getMaxTurnID();
		System.out.println(gameID + " " + tilesNumber.size() + " " + maxTurn);
		dbh.addTileToHand(gameID, tilesNumber, maxTurn);
		// Dit kan dus nog niet - we hebben de tile id nog niet opgeslagen in
		// tiles

		// dbh.addTileToHand(gameID, tileID, maxTurn);

		board.setScore();
	}

	// Swapping tiles from the hand back to the jar
	public void swapTiles(ArrayList<TilePanel> tilesToSwap) {
		for (TilePanel tile : tilesToSwap) {
			addTileToJar(tile.getTile());
		}
		dbh.updateTurn(maxTurn, gameID, getOwnName(), 0, "Swap");
		tilesToSwap.clear();
		fillHand();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// ***** dbh.updateTurn(maxTurn + 1, gameID, getEnemyName(), 25,
		// "Pass");
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

				JOptionPane.showMessageDialog(
						null,
						"Your word(s) are correct \n WordValue: "
								+ board.getScore(), " Words checked ",
						JOptionPane.OK_OPTION);
				board.setScore();
				dbh.updateTurn(maxTurn, gameID, getOwnName(), getScore(),
						"Word");

				ArrayList<Tile> justPlayedTiles = board.addtilesToDatabase();
				for (Tile tiles : justPlayedTiles) {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					dbh.tileToBoard(gameID, maxTurn, tiles.getTileID(),
							tiles.getBlancoLetterValue(),
							tiles.getXValue() + 1, tiles.getYValue() + 1);
				}

				board.setTilesPlayed();

				fillHand();

				// Tijdelijke reactie van de tegenstander
				// *****
				// dbh.updateTurn(maxTurn + 1, gameID, getEnemyName(), 25,
				// "Pass");

			} else {
				System.err.println("WOORDEN ZIJN FOUT");
				// hier moet de optie komen om ze te laten keuren
				String printString = "";
				ArrayList<String> playedWords = board.getPlayedWords();
				for (String playedWord : playedWords) {
					printString += playedWord + "\n";
				}

				JOptionPane.showMessageDialog(null,
						" Your word(s) are incorrect \n" + printString,
						" Words checked", JOptionPane.OK_OPTION);

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
			// dbh.updateTurn(maxTurn, gameID, getOwnName(), 0, "Resign");
			dbh.surrender(gameID, maxTurn, getOwnName(), getEnemyName());
		} else {
			// dbh.updateTurn(maxTurn + 1, gameID, getOwnName(), 0, "Resign");
			dbh.surrender(gameID, maxTurn + 1, getOwnName(), getEnemyName());
		}
		System.out.println("JE HEBT GESURRENDERD!");
		dbh.gameStatusUpdate(gameID, "Resigned");

		// System.exit(0);
	}

	public void skipTurn() {
		// Naam nog toevoegen

		dbh.updateTurn(maxTurn, gameID, getOwnName(), 0, "Pass");
		// dbh.updateTurn(getMaxTurnID(), gameID, "Klaas", dbh.score(gameID,
		// "Klaas), "Pass");
	}
}
