package domein;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import datalaag.DatabaseHandler;
import gui.GameFieldPanel;
import gui.GameSpecScreen;
import gui.TilePanel;

public class Match implements Observer {
	private Tile tempTile;
	private Jar jar;
	private Board board;
	private GameFieldPanel gameField;
	private GameSpecScreen gameSpec;
	private DatabaseHandler dbh;
	private Player player;
	private String myName;
	private String opponentName;
	private int gameID;
	private ArrayList<String> tilesForJar;
	private boolean myTurn;
	private int maxTurn;
	private boolean surrenderd;
	private boolean swapAllowed;
	private ArrayList<TilePanel> tilesToSwap;

	// Constructor for starting a game where you are playing in
	public Match(int gameID, Player player, GameFieldPanel gameField,
			String myName) {
		this.gameField = gameField;
		this.gameID = gameID;
		board = new Board();
		jar = new Jar();
		this.player = player;
		this.surrenderd = false;
		this.swapAllowed = true;
		// Dit is tijdelijk todat je mensen kunt uitdagen
		if (myName.equals("Spectator")) {
			myName = "testSubject";
		}

		this.myName = myName;
		dbh = DatabaseHandler.getInstance();
		String inputName = dbh.opponentName(gameID);
		String[] splitName = inputName.split("---");
		if (splitName[0].equals(myName)) {
			this.opponentName = splitName[1];
		} else {
			this.opponentName = splitName[0];
		}
		myTurn = true;
	}

	// Constructor for starting a game where you are playing in
	public Match(int gameID, String myName) {
		this.gameID = gameID;
		// Dit is tijdelijk todat je mensen kunt uitdagen
		if (myName.equals("Spectator")) {
			myName = "testSubject";
		}

		this.myName = myName;
		dbh = DatabaseHandler.getInstance();
		String inputName = dbh.opponentName(gameID);
		String[] splitName = inputName.split("---");
		if (splitName[0].equals(myName)) {
			this.opponentName = splitName[1];
		} else {
			this.opponentName = splitName[0];
		}
		myTurn = true;
		swapAllowed = true;
	}

	// Constructor for a spectator game - does not need a player
	public Match(int gameID) {
		this.gameID = gameID;
		board = new Board();
		jar = new Jar();
		dbh = DatabaseHandler.getInstance();
	}

	// Returns the gameID
	public synchronized int getGameID() {
		return gameID;
	}

	// Return the player names
	public synchronized String getOwnName() {
		// Dit is tijdelijk todat je mensen kunt uitdagen
		if (myName.equals("Spectator")) {
			myName = "mike";
		}
		// System.out.println("MIJN NAAM IS: " + myName);
		return myName;
	}

	// Returns the names of the opponent
	public synchronized String getEnemyName() {
		if (opponentName != null) {
			return opponentName;
		} else {
			return "Opponent";
		}
	}

	// Method to check who's turn it is and what turn it is
	// From the database
	public synchronized void getMaxTurnID() {
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
	}

	// Method to get maxTurn
	public synchronized int getMaxTurn() {
		return maxTurn;
	}

	// Method for the thread to see if it is my turn
	public synchronized boolean getMyTurn() {
		return myTurn;
	}

	// The obserable the detects buttonactions from the game
	// This method calls the right method to handle the event
	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof Tile) {
			moveTile((Tile) arg);
		} else {
			String requestedAction = (String) arg;
			if (requestedAction.equals("play")) {
				this.playWord();
			} else if (requestedAction.equals("pass")) {
				this.skipTurn();
			} else if (requestedAction.equals("clear")) {
				this.clearTilesFromBoard();
			} else if (requestedAction.equals("surrender")) {
				this.surrenderGame();
			} else if (requestedAction.equals("swap")) {
				this.swapTiles(gameField.getTilesToSwap());
			} else if (requestedAction.equals("forward")) {
				updateSpecTurn(true);
			} else if (requestedAction.equals("backward")) {
				updateSpecTurn(false);
			}
		}
	}

	// Spectator only uses match to board to load up the squares
	// And uses the hands
	// Uses a separate panel
	public void loadSpecateGame(GameSpecScreen gameSpecScreen) {
		this.gameSpec = gameSpecScreen;
		gameSpec.resetGame();
		gameSpec.resetHands();
		// board.clearField();

		// Filling the field
		ArrayList<String> squares = dbh.squareCheck();
		for (int i = 0; i < squares.size(); i++) {
			String[] splits = squares.get(i).split("---");
			board.addSquaresNewBoard(Integer.parseInt(splits[0]) - 1,
					Integer.parseInt(splits[1]) - 1, splits[2]);
		}
		// Spectator needs the Match to function
		gameSpec.createField(this);

		// Loading the played tiles
		ArrayList<String> playedWords = dbh.playedWords(gameID, 0, true);
		for (String played : playedWords) {
			String[] split = played.split("---");
			String letters = split[5];
			String xPos = split[1];
			String yPos = split[2];
			if (split[5].equals("?")) {
				letters += split[4];
			}
			board.addTileToSquare(new Tile(jar.getImage(letters)),
					Integer.parseInt(xPos) - 1, Integer.parseInt(yPos) - 1);
			gameSpec.addImageToBoard(jar.getImage(letters),
					Integer.parseInt(xPos) - 1, Integer.parseInt(yPos) - 1);
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
			myTurn = true;
			handTilesP1 = dbh.handContent(gameID, maxTurn - 1);
			handTilesP2 = dbh.handContent(gameID, maxTurn - 2);
			gameSpec.setTurnScoreP1(dbh.turnScore(gameID, maxTurn));
			gameSpec.setTurnScoreP2(dbh.turnScore(gameID, maxTurn - 1));
			gameSpec.setTurn(false);
		} else {
			handTilesP1 = dbh.handContent(gameID, maxTurn - 2);
			handTilesP2 = dbh.handContent(gameID, maxTurn - 1);
			gameSpec.setTurnScoreP1(dbh.turnScore(gameID, maxTurn - 1));
			gameSpec.setTurnScoreP2(dbh.turnScore(gameID, maxTurn));
			myTurn = false;
			gameSpec.setTurn(true);
		}

		// Adding the tiles to both hands
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
		// Repaint the board
		gameSpec.repaintBoard();
	}

	// Updating a spectator turn - depends on forward ot backwards parameter
	// If forward it will increase maxTurn by and same for backwards but -1
	public void updateSpecTurn(boolean forward) {
		// Checks what turn it is
		String names = dbh.opponentName(gameID);
		String[] playerNames = names.split("---");
		String[] splits = dbh.checkTurn(playerNames[0], gameID).split("---");

		// If forward it will increase maxTurn
		if (forward) {
			if (maxTurn < Integer.parseInt(splits[1])) {
				maxTurn += 1;
				myTurn = !myTurn;
			}
		}
		// Els it will decrease it by 1
		else {
			if (maxTurn - 1 > 1) {
				maxTurn -= 1;
				myTurn = !myTurn;
			}
		}

		// Adding the tiles to the hands
		// And also reset the handpanels
		gameSpec.resetHands();
		ArrayList<String> handTilesP1;
		ArrayList<String> handTilesP2;
		if (myTurn) {
			handTilesP1 = dbh.handContent(gameID, maxTurn - 1);
			handTilesP2 = dbh.handContent(gameID, maxTurn - 2);
			gameSpec.setTurnScoreP1(dbh.turnScore(gameID, maxTurn));
			gameSpec.setTurnScoreP2(dbh.turnScore(gameID, maxTurn - 1));
			gameSpec.setTurn(false);
		} else {
			handTilesP1 = dbh.handContent(gameID, maxTurn - 2);
			handTilesP2 = dbh.handContent(gameID, maxTurn - 1);
			gameSpec.setTurnScoreP1(dbh.turnScore(gameID, maxTurn - 1));
			gameSpec.setTurnScoreP2(dbh.turnScore(gameID, maxTurn));
			gameSpec.setTurn(true);
		}

		// Adds the tiles to the hands
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
		gameSpec.resestField(this);

		// Loads the played words from the database
		// Only if they were played before maxTurn
		ArrayList<String> playedWords = dbh.playedWords(gameID, maxTurn, false);
		for (String played : playedWords) {
			String[] split = played.split("---");
			String letters = split[5];
			String xPos = split[1];
			String yPos = split[2];
			if (split[5].equals("?")) {
				letters += split[4];
			}
			int turn = Integer.parseInt(split[0]);
			if (turn <= maxTurn) {
				board.addTileToSquare(new Tile(jar.getImage(letters)),
						Integer.parseInt(xPos) - 1, Integer.parseInt(yPos) - 1);
				gameSpec.addImageToBoard(jar.getImage(letters),
						Integer.parseInt(xPos) - 1, Integer.parseInt(yPos) - 1);
			}
		}
		gameSpec.repaintBoard();
	}

	// A method to start a new game by me
	public void startNewGame() {
		dbh.createJar(gameID, "EN");
		ArrayList<String> tilesToLoad = dbh.jarContent(gameID);
		Jar newJar = new Jar();
		// Creating the jar - This loads the jar from the database
		for (String tiles : tilesToLoad) {
			String[] splits = tiles.split("---");
			Tile t = newJar.createTile(Integer.parseInt(splits[0]), splits[1],
					Integer.parseInt(splits[2]));
			newJar.addNewTile(t);
		}

		swapAllowed = true;
		// Fills the player hands
		fillHand(newJar);

		dbh.gameStatusUpdate(gameID, "Playing");
	}

	// Loads a game from the database
	public void loadGame() {
		// gameField.clearField();
		if (board != null) {
			board.clearField();
		}
		tilesForJar = dbh.jarContent(gameID);
		getMaxTurnID();

		swapAllowed = true;

		// Creating the board
		ArrayList<String> squares = dbh.squareCheck();
		for (int i = 0; i < squares.size(); i++) {
			String[] splits = squares.get(i).split("---");
			board.addSquaresNewBoard(Integer.parseInt(splits[0]) - 1,
					Integer.parseInt(splits[1]) - 1, splits[2]);
		}

		// Creating the jar - This loads the jar from the database
		for (String tiles : tilesForJar) {
			String[] splits = tiles.split("---");
			Tile t = jar.createTile(Integer.parseInt(splits[0]), splits[1],
					Integer.parseInt(splits[2]));
			jar.addNewTile(t);
		}

		// SwingUtilities.invokeLater(new Runnable() {
		// @Override
		// public void run() {
		// gameField.addSquares(this);
		// }
		// });
		// Calls the method to make the field and gives it this Match as param
		// gameField.addSquares(this);

		// Checks if the game being loaded has not started and the player was
		// invited
		// The enemy then started so there are no words to load or hand to load
		// Loads all the played words
		ArrayList<String> playedWords = dbh.playedWords(gameID, 0, true);
		for (String played : playedWords) {
			String[] splits = played.split("---");
			String letters = splits[5];
			String xPos = splits[1];
			String yPos = splits[2];
			if (splits[5].equals("?")) {
				letters += splits[4];
			}
			int value = dbh.letterValue("EN", letters);
			Tile t = jar.createTile(letters, value);
			board.addTileToSquare(t, Integer.parseInt(xPos) - 1,
					Integer.parseInt(yPos) - 1);
			// gameField.addSquarePanel(Integer.parseInt(xPos) - 1,
			// Integer.parseInt(yPos) - 1, t.getImage());
		}

		// Loads the player hand
		if (!dbh.getGameStatusValue(gameID).equals("Finished")
				|| !dbh.getGameStatusValue(gameID).equals("Resigned")) {
			System.out.println("RAW DATA, laden van de match " + myTurn
					+ " beurt: " + maxTurn);
			ArrayList<String> handTiles;
			if (myTurn) {
				handTiles = dbh.handContent(gameID, maxTurn - 2);
			} else {
				handTiles = dbh.handContent(gameID, maxTurn);
			}
			player.clearHand();
			for (int z = 0; z < handTiles.size(); z++) {
				String[] tiles = handTiles.get(z).split("---");
				Tile t = jar.createTile(Integer.parseInt(tiles[0]), tiles[1],
						Integer.parseInt(tiles[2]));
				addTileToHand(t);
			}
		} else {
			System.out.println(gameID + " De game is al over");
		}

		if (jar.getJarSize() < 7) {
			swapAllowed = false;
		}

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				gameField.clearField();
				for (int y = 0; y < 15; y++) {
					for (int x = 0; x < 15; x++) {
						gameField.addSquares(board.getSquare(x, y));
					}
				}
				ArrayList<Tile> tiles = player.getHand();
				System.err.println(tiles.size() + " DE GROTE");
				for (Tile t : tiles) {
					gameField.addTileToHand(t);
				}
				gameField.repaintBoard();
				gameField.revalidate();
			}
		});
	}

	// A method to update the playfield
	public synchronized void updateField() {
		// clears the board when turn is swapped
		clearTilesFromBoard();

		// Updating the field
		ArrayList<String> playedWords = dbh.playedWords(gameID, maxTurn - 2,
				true);
		for (String played : playedWords) {
			String[] splits = played.split("---");
			String letters = splits[5];
			String xPos = splits[1];
			String yPos = splits[2];
			if (splits[5].equals("?")) {
				letters += splits[4];
			}
			int value = dbh.letterValue("EN", letters);
			Tile t = jar.createTile(letters, value);
			board.addTileToSquare(t, Integer.parseInt(xPos) - 1,
					Integer.parseInt(yPos) - 1);
			System.out.println("tile " + t.getLetter() + " placed at x "
					+ (Integer.parseInt(xPos) - 1) + " and at y "
					+ (Integer.parseInt(yPos) - 1));
			gameField.addSquarePanel(Integer.parseInt(xPos) - 1,
					Integer.parseInt(yPos) - 1, t.getImage());
		}

		// Repainting and reseting jar
		gameField.repaintBoard();
		jar.resetJar();

		// Making and filling the update jar
		tilesForJar = dbh.jarContent(gameID);
		// Creating the jar - This loads the jar from the database
		for (String tiles : tilesForJar) {
			String[] splits = tiles.split("---");
			Tile t = jar.createTile(Integer.parseInt(splits[0]), splits[1],
					Integer.parseInt(splits[2]));
			jar.addNewTile(t);
		}

		swapAllowed = true;
		if (jar.getJarSize() < 7) {
			swapAllowed = false;
		}
	}

	public void moveTile(Tile t) {
		int x = t.getXValue();
		int y = t.getYValue();
		if (board.getSquare(x, y).getTile() == null) {
			moveTileFromHandToBoard(t, x, y);
		} else if (board.getSquare(x, y).getTile().getJustPlayed()) {
			moveTileFromBoardToHand(x, y);
		}
	}

	// Method for loading games to add tiles to hand
	public void addTileToHand(Tile t) {
		player.addTileToHand(t);
		// gameField.addTileToHand(t, this);
		// gameField.repaintBoard();
	}

	// Takes a random tile from the jar
	// Add it to the player and and to the gui
	public int getTileFromJar() {
		tempTile = jar.getNewTile();
		// addTileToHand(t);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				gameField.addTileToHand(tempTile);
				gameField.repaintBoard();
			}
		});
		player.addTileToHand(tempTile);
		return tempTile.getTileID();
	}

	// Takes the tile selected and move it to the board
	// Sets the tile on just played
	// Add the tile to the board
	public void moveTileFromHandToBoard(Tile t, int x, int y) {
		if (t.getValue() == 0) {

			// This allows you to pick your Joker value
			String[] choices = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
					"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
					"V", "W", "X", "Y", "Z" };
			String input = (String) JOptionPane.showInputDialog(null,
					"Select your letter below...", "Choose you letter",
					JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
			System.out.println(input);
			if (!input.equals("")) {
				t.setBlancoLetterValue(input);
				t.setLetter(input);
				t.setJustPlayed(true);
				player.removeTileFromHand(t);
				board.addTileToSquare(t, x, y);
				board.startCalculating();
				// gameField.repaintBoard();
			}
		} else {
			t.setJustPlayed(true);
			player.removeTileFromHand(t);
			board.addTileToSquare(t, x, y);
			System.out.println("IETS VAN DE TILE " + t.getLetter()
					+ t.getXValue() + t.getYValue());
			board.startCalculating();
			// gameField.repaintBoard();
		}
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				gameField.repaintBoard();

			}
		});
	}

	// Takes a tile from the board and puts it back in the hand
	// X and Y are the cords for the squares on the board
	// Needed to get the tile back
	public void moveTileFromBoardToHand(int x, int y) {
		Tile t = board.getSquare(x, y).getTile();
		tempTile = t;
		t.setJustPlayed(false);
		if (t.getValue() == 0) {
			t.setBlancoLetterValue(null);
			t.setLetter("?");
			jar.getImage("?");
			t.setImage(jar.getImage("?"));
		}
		player.addTileToHand(t);
		board.removeTileFromSquare(x, y);
		board.startCalculating();

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				gameField.addTileToHand(tempTile);
				gameField.repaintBoard();
			}
		});
	}

	// Clears all the tiles from the board
	public void clearTilesFromBoard() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				for (int y = 0; y < 15; y++) {
					for (int x = 0; x < 15; x++) {
						if (gameField.getOccupied(x, y)) {
							if (removeTile(x, y)) {
								gameField.removeImageSquare(x, y);
								gameField.repaintBoard();
							}
						}
					}
				}
			}
		});
		board.startCalculating();
	}

	public boolean removeTile(int x, int y) {
		boolean possible = false;
		if (board.getSquare(x, y).getTile().getJustPlayed()) {
			board.getSquare(x, y).getTile().setJustPlayed(false);
			moveTileFromBoardToHand(x, y);
			possible = true;
		}
		return possible;
	}

	// Fills the hand back to 7
	public synchronized void fillHand(Jar newJar) {
		getMaxTurnID();

		// Method to fill the hands for a new game
		if (newJar != null) {
			ArrayList<Integer> ownHand = new ArrayList<Integer>();
			for (int i = 0; i < 7; i++) {
				int id = (newJar.getNewTile()).getTileID();
				ownHand.add(id);
			}
			dbh.addTileToHand(gameID, ownHand, 1);

			ArrayList<Integer> enemyHand = new ArrayList<Integer>();
			for (int i = 0; i < 7; i++) {
				int id = (newJar.getNewTile()).getTileID();
				enemyHand.add(id);
			}
			dbh.addTileToHand(gameID, enemyHand, 2);
		} else {
			if (player.getHandSize() == 0 && jar.getJarSize() == 0) {
				this.winGame();
			} else {
				// ArrayList<Integer> tileID = new ArrayList<Integer>();
				while (player.getHandSize() < 7 && jar.getJarSize() > 0) {
					int id = getTileFromJar();
					if (id != -1) {
						// tileID.add(id);
					} else {
						System.out
								.println("ER IS IETS FOUT GEGAAN BY FILLHAND");
					}
				}

				ArrayList<Tile> tilesInHand = player.getHand();
				ArrayList<Integer> tilesNumber = new ArrayList<Integer>();
				for (Tile tile : tilesInHand) {
					tilesNumber.add(tile.getTileID());
				}

				dbh.addTileToHand(gameID, tilesNumber, maxTurn);
				// dbh.addTileToHand(gameID, tileID, maxTurn);
				board.setScore();
			}
		}
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				gameField.repaintBoard();
			}
		});
	}

	// Swapping tiles from the hand back to the jar
	public void swapTiles(ArrayList<TilePanel> tilesToSwap) {
		this.tilesToSwap = tilesToSwap;
		for (TilePanel tile : tilesToSwap) {
			player.removeTileFromHand(tile.getTile());
			jar.addNewTile(tile.getTile());
		}
		addTileToJar();
		if (tilesToSwap.size() == 0) {
			JOptionPane.showMessageDialog(null, "No tiles selected to swap",
					"Notice", JOptionPane.OK_OPTION);
		} else {
			dbh.updateTurn(maxTurn, gameID, getOwnName(), 0, "Swap");
			tilesToSwap.clear();
			gameField.swapTiles();
			fillHand(null);

		}
		// ***** dbh.updateTurn(maxTurn + 1, gameID, getEnemyName(), 25,
		// "Pass");
	}

	// remove it from the hand en field
	public void addTileToJar() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				for (TilePanel tile : tilesToSwap) {
					gameField.removeTileFromHand(tile.getTile());
				}
				gameField.repaintBoard();
			}
		});
	}

	public synchronized boolean swapAllowed() {
		return swapAllowed;
	}

	// Gets a square from the board on x,y
	public Square getSquare(int x, int y) {
		return board.getSquare(x, y);
	}

	// Gets the image form the board
	public BufferedImage getImage(int x, int y) {
		return board.getImage(x, y);
	}

	// Get the score of the just placed tiles
	public synchronized int getScore() {
		return board.getScore();
	}

	// When the play button is pressed
	// This method does the word
	public void playWord() {
		int answer;

		if (board.startCalculating()) {
			// if this is true, a score will be calculated
			System.err.println("De score is berekend");
			if (board.checkWords()) {

				JOptionPane.showMessageDialog(
						null,
						"Your word(s) are correct \n WordValue: "
								+ board.getScore(), " Words checked ",
						JOptionPane.YES_NO_CANCEL_OPTION);

				dbh.updateTurn(maxTurn, gameID, getOwnName(), getScore(),
						"Word");
				board.setScore();
				ArrayList<Tile> justPlayedTiles = board.addtilesToDatabase();
				for (Tile tiles : justPlayedTiles) {
					dbh.tileToBoard(gameID, maxTurn, tiles.getTileID(),
							tiles.getBlancoLetterValue(),
							tiles.getXValue() + 1, tiles.getYValue() + 1);
				}

				board.setTilesPlayed();

				fillHand(null);

				// Tijdelijke reactie van de tegenstander
				// *****
				// \ dbh.updateTurn(maxTurn + 1, gameID, getEnemyName(), 25,
				// "Pass");

			} else {
				System.err.println("WOORDEN ZIJN FOUT");
				// hier moet de optie komen om ze te laten keuren
				String printString = "";
				ArrayList<String> playedWords = board.getPlayedWords();
				for (String playedWord : playedWords) {
					printString += playedWord + "\n";
				}

				answer = JOptionPane.showConfirmDialog(null,
						"Your word(s) are incorrect \n" + printString
								+ "Would you like to request a word?",
						"Words checked", JOptionPane.YES_NO_OPTION);

				if (answer == 0) {
					System.out.println("answer given is Yes");
					requestWord();
				}
			}
		} else {
			JOptionPane.showMessageDialog(null, "Can't play this word!",
					"Play error", JOptionPane.INFORMATION_MESSAGE);
		}
		board.resetPlayedWords();
	}

	public void requestWord() {
		ArrayList<String> playedWords = board.getRequestableWords();
		String[] choices = new String[playedWords.size()];
		int i = 0;
		for (String playedWord : playedWords) {
			choices[i] = playedWord;
			i++;
		}
		System.out.println("joptionpane");
		String input = (String) JOptionPane.showInputDialog(null,
				"Select your letter below...", "Choose you word",
				JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
		System.out.println("given word request: " + input);

		if (input != null) {
			dbh.requestWord(input, "EN");
			JOptionPane.showMessageDialog(null, input + " has been requested.",
					"Request confirmation", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	// Method to surrender the game
	public void surrenderGame() {
		if (myTurn) {
			dbh.surrender(gameID, maxTurn, getOwnName(), getEnemyName());
		} else {
			dbh.surrender(gameID, maxTurn + 1, getOwnName(), getEnemyName());
		}
		System.out.println("JE HEBT GESURRENDERD!");
		surrenderd = true;
		dbh.gameStatusUpdate(gameID, "Resigned");
	}

	public boolean getSurrender() {
		return surrenderd;
	}

	// Method to pass
	public void skipTurn() {
		dbh.updateTurn(maxTurn, gameID, getOwnName(), 0, "Pass");
		if (dbh.triplePass(gameID, getOwnName())) {
			winGame();
		}
		ArrayList<Integer> tiles = new ArrayList<Integer>();
		ArrayList<Tile> tile = player.getHand();
		for (Tile t : tile) {
			tiles.add(t.getTileID());
		}
		dbh.addTileToHand(gameID, tiles, maxTurn);
	}

	public void winGame() {
		int handTileFromTurn = 0;
		if (dbh.score(gameID, getOwnName()) > dbh.score(gameID, getEnemyName())) {
			handTileFromTurn = maxTurn - 1;
			JOptionPane.showMessageDialog(null, "YOU WON THE GAME!",
					"Game ended", JOptionPane.INFORMATION_MESSAGE);
		} else {
			handTileFromTurn = maxTurn;
			JOptionPane.showMessageDialog(null, "YOU LOST THE GAME!",
					"Game ended", JOptionPane.INFORMATION_MESSAGE);
		}
		int handScore = 0;
		System.out.println("POT IS LEEG, GEEN NIEUWE LETTERS MEER!");
		ArrayList<String> handContent = dbh.handContent(gameID,
				handTileFromTurn);
		for (String content : handContent) {
			String[] split = content.split("---");
			handScore += Integer.parseInt(split[2]);
		}
		dbh.updateTurn(maxTurn + 1, gameID, getOwnName(), handScore, "End");
		dbh.updateTurn(maxTurn + 2, gameID, getEnemyName(), -handScore, "End");
		dbh.gameStatusUpdate(gameID, "Finished");
	}

	public synchronized int getJarSize() {
		if (jar != null) {
			return jar.getJarSize();
		} else {
			return 10;
		}
	}
}
