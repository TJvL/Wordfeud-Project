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

	
	// NIEUW //
	public void clearField(){
		for (int y = 0; y < 15; y++) {
			for (int x = 0; x < 15; x++) {
				field[x][y].removeTile();		
			}
		}
	}
	
	public BufferedImage getImage(int x, int y){
		return field[x][y].getImage();
	}
	
	// NIEUW tot hier//
	
	
	public void createBoard() {

		// coordinaten en namen van de te plaatsen squares
		String[] typeSquares = { "dl", "tl", "dw", "tw" };
		int[] amounts = { 24, 20, 12, 8 };

		int[] xValuesDL = { 8, 2, 14, 7, 9, 7, 9, 3, 5, 11, 13, 1, 15, 3, 5,
				11, 13, 7, 9, 7, 9, 2, 14, 8 };
		int[] yValuesDL = { 1, 2, 2, 3, 3, 5, 5, 7, 7, 7, 7, 8, 8, 9, 9, 9, 9,
				11, 11, 13, 13, 14, 14, 15 };

		int[] xValuesTL = { 1, 15, 6, 10, 4, 12, 2, 6, 10, 14, 2, 6, 10, 14, 4,
				12, 6, 10, 1, 15 };
		int[] yValuesTL = { 1, 1, 2, 2, 4, 4, 6, 6, 6, 6, 10, 10, 10, 10, 12,
				12, 14, 14, 15, 15 };

		int[] xValuesDW = { 3, 13, 8, 5, 11, 4, 12, 5, 11, 8, 3, 13 };
		int[] yValuesDW = { 3, 3, 4, 5, 5, 8, 8, 11, 11, 12, 13, 13 };

		int[] xValuesTW = { 5, 11, 1, 15, 1, 15, 5, 11 };
		int[] yValuesTW = { 1, 1, 5, 5, 11, 11, 15, 15 };

		int[][] xValues = { xValuesDL, xValuesTL, xValuesDW, xValuesTW };
		int[][] yValues = { yValuesDL, yValuesTL, yValuesDW, yValuesTW };

		String[] paths = { "DL.png", "TL.png", "DW.png", "TW.png" };

		int x;
		int y;

		// plaatsen van de star
		field[7][7] = new Square(7, 7, "star", images.get("star"));

		// plaatsen van alle bonusSquares
		for (int i = 0; i < typeSquares.length; i++) {
			// voor alle typeSquares
			for (int number = 0; number < amounts[i]; number++) {
				// voor het aantal keer dat de typeSquare voorkomt op het bord
				x = xValues[i][number] - 1;
				y = yValues[i][number] - 1;
				field[x][y] = new Square(x, y, typeSquares[i],
						images.get("Plaatjes/" + paths[i]));
			}
		}
	}

	public void addTileToSquare(Tile t, int x, int y) {
		field[x][y].addTile(t);
	}

	public void removeTileFromSquare(int x, int y) {
		field[x][y].removeTile();
	}

	// Start signal to add the squares to the calculator
	// and start it when it's done
	public boolean startCalculating() {
		/*
		 * for (int y = 0; y < 15; y++) { for (int x = 0; x < 15; x++) {
		 * calculator.addSquaresToBoard(field[x][y]); } }
		 */

		calculator.addSquaresToBoard(field);

		boolean possible = false;
		score = calculator.startCalculating();
		if (score > 0) {
			// Hier moet de optie komen om de tiles uit justPlayedTile aan de
			// database toetevoegen
			// Hier ook gelijk de score update en beurt update
			// System.out.println("Woord was goed");
			possible = true;
		} else {
			// System.out.println("Woord was fout");
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

	public boolean checkWords() {
		boolean allWordsExist = true;
		ArrayList<Word> words = calculator.getjustPlayedWords();
		// System.out.println(words.size() + " DE SIZE VAN DE TEGEELS");
		for (Word word : words) {
			// woord geven aan de database
			// als woord niet bestaat allWordsExist = false
			// false returns en gamebutton
			// bij gamebutton aan match geven en match laten vragen of de speler
			// het wordt wil submitten of niet
			// niet - dan geen score
			if (dbh.checkWord(word.getWord(), "EN")) {
				System.out.println("WOORD BESTAAT IN WOORDENBOEK");
				checkedWords.add(word.getWord() + " exixts");
				submittedWord = "*";
			} else {
				System.out.println("WOORD BESTAAT NIET IN HET WOORDENBOEK");
				allWordsExist = false;
				//submittedWord = "*";
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

	public void checkWord(){
		// moet iets doen?
	}
	
	public String getWord() {
		return submittedWord;
	}

	public ArrayList<String> getPlayedWords(){
		return checkedWords;
	}
	
	public void setTilesPlayed() {
		for (int y = 0; y < 15; y++) {
			for (int x = 0; x < 15; x++) {
				if (field[x][y].getTile() != null) {
					field[x][y].getTile().setJustPlayed(false);
				}
			}
		}
	}

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

	// Methode voor laden van een spel wat al aan de gang is
	public void addSquares() {
		// Hier moeten de square worden opgevraagd aan de database
		/*
		 * for (int y = 0; y < 15; y++) { for (int x = 0; x < 15; x++) {
		 * dbh.squareCheck(x, y); } }
		 */

		// hier uit de database (heet tegel) x - y halen/ de borden staan
		// opgeslagen
		// in de database.
		// field[x][y] = new Square(x , y, value, path) gegevens van de database
		// Leeg veld heet nu empty - deze waarde kan niet worden opgeslagen in
		// de database
		// dus aanpassen.
		// }}
	}

	public Square getSquare(int x, int y) {
		return field[x][y];
	}

}
