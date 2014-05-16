package domein;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import datalaag.DatabaseHandler;
import datalaag.FileHandler;
import datalaag.ScoreCalculator;

public class Board {

	private Square[][] field;
	private ScoreCalculator calculator;
	private FileHandler fh;
	private DatabaseHandler dbh;
	private HashMap<String, BufferedImage> images;
	private int score;
	private String submittedWord;
	private ArrayList<String> checkedWords;

	// Hier moet gekeken of er een nieuwe bord wordt aangemaakt
	// Of het spel al bezig is het bord laden
	// Standaard bord laden of random bord laden

	public Board() {
		calculator = ScoreCalculator.getInstance();
		field = new Square[15][15];
		fh = FileHandler.getInstance();
		dbh = DatabaseHandler.getInstance();
		checkedWords = new ArrayList<String>();
		submittedWord = "*";
		images = new HashMap<String, BufferedImage>();
		images.put("DL", fh.readImage("Plaatjes/DL.png"));
		images.put("TL", fh.readImage("Plaatjes/TL.png"));
		images.put("DW", fh.readImage("Plaatjes/DW.png"));
		images.put("TW", fh.readImage("Plaatjes/TW.png"));
		images.put("*", fh.readImage("Plaatjes/star.png"));
		images.put("--", fh.readImage("Plaatjes/board.png"));
	}

	// Methode voor aanmaken van een new board
	public void addSquaresNewBoard(int x, int y, String value) {
		field[x][y] = new Square(x, y, value, images.get(value));
		// Zie addSquares();
	}

	// A method to remove all the tiles from the field
	public void clearField() {
		for (int y = 0; y < 15; y++) {
			for (int x = 0; x < 15; x++) {
				if (field[x][y] != null) {
					if (field[x][y].getTile() != null) {
						field[x][y].removeTile();
					}
				}
			}
		}
	}

	// A method to reset the ckeckedWords
	public void resetPlayedWords() {
		checkedWords.clear();
	}

	// A method to get the image from a square
	public BufferedImage getImage(int x, int y) {
		return field[x][y].getImage();
	}

	// A method to add tiles to the board
	public void addTileToSquare(Tile t, int x, int y) {
		field[x][y].addTile(t);
	}

	// A method to remove a tile from a square
	public void removeTileFromSquare(int x, int y) {
		field[x][y].removeTile();
	}

	// Start signal to add the squares to the calculator
	// and start it when it's done
	public boolean startCalculating() {

		// Gives the justPlayedTiles to the calculator
		calculator.addSquaresToBoard(field);
		boolean possible = false;

		// Starts the calculator and returns the score
		score = calculator.startCalculating();
		if (score > 0) {
			possible = true;
		} else {
		}
		return possible;
	}

	// Gets the score for the score panel
	public int getScore() {
		return score;
	}

	// Resets the score for the score panel
	public void setScore() {
		score = 0;
	}

	// A method to check if all words exist in the database
	public boolean checkWords() {
		boolean allWordsExist = true;
		ArrayList<Word> words = calculator.getjustPlayedWords();
		for (Word word : words) {
			// If the words exist it will say it does
			if (dbh.checkWord(word.getWord(), "EN")) {
				System.out.println("WOORD BESTAAT IN WOORDENBOEK");
				checkedWords.add(word.getWord() + " exixts");
				submittedWord = "*";
			}
			// Else it does not exist
			else {
				System.out.println("WOORD BESTAAT NIET IN HET WOORDENBOEK");
				allWordsExist = false;
				// submittedWord = "*";
				checkedWords.add(word.getWord() + " does NOT exixt");
				/*
				 * submittedWord = word.getWord(); while (!checkingWord){
				 * submittedWord = "*"; checkingWord = false;
				 * 
				 * if (dbh.checkWord(word.getWord())){
				 * System.out.println("WOORD BESTAAT IN WOORDENBOEK"); } else {
				 * System
				 * .out.println("HET WOORD IS AFGEKEURD OF TERUGGETROKKEN"); }
				 * 
				 * }
				 */

				// return false;
			}

		}
		return allWordsExist;
	}

	// A method that returns the words that were just made
	public ArrayList<String> getPlayedWords() {
		return checkedWords;
	}

	// Set all the tiles on the board to played
	public void setTilesPlayed() {
		for (int y = 0; y < 15; y++) {
			for (int x = 0; x < 15; x++) {
				if (field[x][y].getTile() != null) {
					field[x][y].getTile().setJustPlayed(false);
				}
			}
		}
	}

	// Adds the justPlayedTiles to the database
	public ArrayList<Tile> addtilesToDatabase() {
		ArrayList<Tile> justPlayedLetters = new ArrayList<Tile>();
		for (int y = 0; y < 15; y++) {
			for (int x = 0; x < 15; x++) {
				try {
					if (field[x][y].getTile().getJustPlayed()) {
						Tile t = field[x][y].getTile();
						t.setXValue(x);
						t.setYValue(y);
						justPlayedLetters.add(t);
					}
				} catch (NullPointerException e) {

				}
			}
		}
		return justPlayedLetters;
	}

	// A method to get a square
	public Square getSquare(int x, int y) {
		return field[x][y];
	}

}
