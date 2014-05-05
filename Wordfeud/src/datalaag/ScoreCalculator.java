package datalaag;

import domein.Square;
import domein.Word;

import java.util.ArrayList;

import domein.Square;

public class ScoreCalculator {

	private static ScoreCalculator scoreCalculator = new ScoreCalculator();
	private Square[][] field;
	private boolean wordHorizontal;
	private Word word;
	private ArrayList<Square> justPlayedTiles;
	private ArrayList<Word> playedWords;
	private ArrayList<Word> wordsToCheck;

	private ScoreCalculator() {
		field = new Square[15][15];
		justPlayedTiles = new ArrayList<Square>();
		wordsToCheck = new ArrayList<Word>();
		wordHorizontal = true;
		playedWords = new ArrayList<Word>();
	}

	// Returns de calculator
	public static ScoreCalculator getInstance() {
		return scoreCalculator;
	}

	// Adds the sqaures from the bord with justplayed true
	// to a seprate arraylist
	public void addSquaresToBoard(Square[][] field) {
		// field[square.getXPos()][square.getYPos()] = square;
		this.field = field;
	}

	// Method with a few checks and leads the calculating
	public int startCalculating() {
		wordsToCheck.clear();
		
		int score = 0;
		this.setJustPlayedTiles();
		this.checkFirstWordOnStart();
		boolean runScoreCalculator = true;

		// Checks if a joker is on the board with no value
		for (Square tiles: justPlayedTiles){
			if (tiles.getTile().getValue() == 0){
				runScoreCalculator = false;
				System.err.println("ER LIGT EEN LEGE JOKER OP HET VELD!");
				System.out.println("ER LIGT EEN LEGE JOKER OP HET VELD!");
			}
		}
		
		
		// Checks if the board is empty, the first word is placed on the star
		if (!checkIfBoardNotEmpty()) {
			if (checkFirstWordOnStart()) {
				runScoreCalculator = true;
			} else {
				runScoreCalculator = false;
				System.err.println("First word is not bin placed on the star");
			}
		}
		// Method to check if the played word is connected to atleast one other
		// tile
		else {
			boolean oneConected = false;
			for (Square square : justPlayedTiles) {
				if (field[square.getXPos() - 1][square.getYPos()].getTile() != null
						&& !field[square.getXPos() - 1][square.getYPos()]
								.getTile().getJustPlayed()) {
					oneConected = true;
				}
				if (field[square.getXPos() + 1][square.getYPos()].getTile() != null
						&& !field[square.getXPos() + 1][square.getYPos()]
								.getTile().getJustPlayed()) {
					oneConected = true;
				}
				if (field[square.getXPos()][square.getYPos() - 1].getTile() != null
						&& !field[square.getXPos()][square.getYPos() - 1]
								.getTile().getJustPlayed()) {
					oneConected = true;
				}
				if (field[square.getXPos()][square.getYPos() + 1].getTile() != null
						&& !field[square.getXPos()][square.getYPos() + 1]
								.getTile().getJustPlayed()) {
					oneConected = true;
				}

			}
			if (!oneConected) {
				runScoreCalculator = false;
				System.err.println("THE WORDS ARE NOT CONNECTED!!!");
			}
		}

		// Check to be sure the first placed word is on the star
		if (runScoreCalculator) {
			// Check if the played word is longer then 1
			if (justPlayedTiles.size() > 1) {
	//			System.out.println(justPlayedTiles.size() + " grote van");
				if (tilesInOneLine()) {
					// alle gelegde tiles liggen op dezelfde lijn

					if (justPlayedTiles.size() > 1) {

						// er is meer dan 1 tile gelegd

						if (playedTilesConnected()) {
							// alle gelegde tiles liggen aan elkaar
	//						System.out.println("alle checks zijn geslaagd");
							score = calculateScore();
						}
					}
				}
			}
			// If the played word is 1 long
			else if (justPlayedTiles.size() == 1) {
	//			System.out.println("TEST");
				score = calculateScoreOneTile();
			}
		}

		// method om te kijken of alle woorden bestaan in de database
		// worden opgeslagen in playedWords
		for (Word word : playedWords) {
			System.out.println("Gespeeld: " + word.getWord());
			wordsToCheck.add(word);
		}

		// clear all the list and prints the score
		justPlayedTiles.clear();
		playedWords.clear();
		System.err.println("/////////////// De score: " + score
				+ "\\\\\\\\\\\\\\\\\\");
		System.out.println("/////////////// De score: " + score
				+ "\\\\\\\\\\\\\\\\\\");
		return score;
	}

	// Returns the words that were just played on the field
	public ArrayList<Word> getJustPlayedTiles(){
		return wordsToCheck;
	}
	
	// Method to check if the board is empty except the just played wordes
	public boolean checkIfBoardNotEmpty() {
		boolean boardEmpty = true;
		int countOfTilesOnBoard = 0;
		for (int y = 0; y < 15; y++) {
			for (int x = 0; x < 15; x++) {
				if (field[y][x].getTile() != null) {
					countOfTilesOnBoard += 1;
				}
			}
		}
		if (countOfTilesOnBoard == justPlayedTiles.size()) {
			boardEmpty = false;
		}
		return boardEmpty;
	}

	// Method to check if the first word placed is on the star
	public boolean checkFirstWordOnStart() {
		boolean firstWordOnStar = false;		
		for (Square sq : justPlayedTiles) {
			if (sq.getValue().equals("*")) {
				firstWordOnStar = true;
			}
		}
		System.out.println("DIT IS HET EERSTE WOORD: " + firstWordOnStar);
		return firstWordOnStar;
	}

	public boolean tilesInOneLine() {
		int tempY = 0;
		boolean result = true;
		boolean firstY = true;
		boolean stopLoop = false;
		// Iets apart maken als er maar een letter is gespeeld

		// Onlt counts if atleast 2 tiles are placed
		// Woord is horizontaal
		for (int y = 0; y < 15 && !stopLoop; y++) {
			for (int x = 0; x < 15 && !stopLoop; x++) {
				if (field[x][y].getTile() != null) {
					if (field[x][y].getTile().getJustPlayed()) {
						if (firstY) {
							tempY = field[x][y].getYPos();
							firstY = false;
						} else {
							if (tempY != field[x][y].getYPos()) {
								stopLoop = true;
								wordHorizontal = false;
	//							System.out.println("Woord is niet horizontaal");
								result = false;
							} else {
								wordHorizontal = true;
	//							System.out.println("Woord is horizontaal");
								result = true;
							}
						}
					}
				}
			}
		}

		stopLoop = false;
		int tempX = 0;
		boolean firstX = true;
		// Woord is verticaal
		if (!wordHorizontal) {
			for (int y = 0; y < 15 && !stopLoop; y++) {
				for (int x = 0; x < 15 && !stopLoop; x++) {
					if (field[x][y].getTile() != null) {
						if (field[x][y].getTile().getJustPlayed()) {
							if (firstX) {
								tempX = field[x][y].getXPos();
								firstX = false;
							} else {
								if (tempX != field[x][y].getXPos()) {
									stopLoop = true;
	//								System.out
	//										.println("Woord is niet verticaal");
									result = false;
								} else {
									wordHorizontal = false;
	//								System.out.println("Woord is verticaal");
									result = true;
								}
							}
						}
					}
				}
			}
		}
		return result;
	}

	// Calculate the score if only one tile is played
	// returns the score
	public int calculateScoreOneTile() {
		int score = 0;
		Square square = justPlayedTiles.get(0);

		// Horizontal direction
		word = new Word();
		for (int x = 0; x < 15; x++) {
			if (field[x][square.getYPos()].getTile() != null) {
				if (isConnected(field[x][square.getYPos()],
						field[square.getXPos()][square.getYPos()], "horizontal")) {
					word.addLetter(field[x][square.getYPos()].getTile()
							.getLetter());
					word.increaseScore(checkBonusOffSquare(
							field[x][square.getYPos()], word,
							field[x][square.getYPos()].getTile().getValue()));

				}
			}
		}
		if (word.getLengthWord() > 1 && word != null) {
			word.addWordBonusToScore();
			playedWords.add(word);
		}

		// Veritcal direction
		word = new Word();
		for (int y = 0; y < 15; y++) {
			if (field[square.getXPos()][y].getTile() != null) {

				if (isConnected(field[square.getXPos()][y],
						field[square.getXPos()][square.getYPos()], "vertical")) {
	//				System.out.println("Letter: "
	//						+ field[square.getXPos()][y].getTile().getLetter()
	//						+ " waarde: "
	//						+ field[square.getXPos()][y].getTile().getValue());
					word.addLetter(field[square.getXPos()][y].getTile()
							.getLetter());
					word.increaseScore(checkBonusOffSquare(
							field[square.getXPos()][y], word,
							field[square.getXPos()][y].getTile().getValue()));
				}
			}
		}
		if (word.getLengthWord() > 1 && word != null) {
			word.addWordBonusToScore();
			playedWords.add(word);
		}
		for (Word word : playedWords) {
			score = score + word.getScore();
		}
		return score;
	}

	// Takes all the just played tiles form the board and adds then to a list
	public void setJustPlayedTiles() {
		for (Square[] s : field) {
			for (Square sq : s) {
				if (sq.getTile() != null) {
					if (sq.getTile().getJustPlayed()) {
						justPlayedTiles.add(sq);
					}
				}
			}
		}
	}

	// Calculates the score
	// Checks if they are connected to a just played tile
	// Adds the words to a new String
	public int calculateScore() {
		if (wordHorizontal) {
			// woord is horizontaal
	//		System.out.println("---- woord is horizontaal");
			int testingXValue;
			int testingYValue1 = 0;

			for (Square sq : justPlayedTiles) {
				testingXValue = sq.getXPos();
				testingYValue1 = sq.getYPos();
				word = new Word();

				for (int x = 0; x < 15; x++) {
					for (int y = 0; y < 15; y++) {

						if (field[x][y].getTile() != null) {
							// er ligt een tile op de square
							if (field[x][y].getXPos() == testingXValue) {
								// de tile heeft dezelfde x waarde als de net
								// geplaatste tile
								if (isConnected(field[x][y], sq, "vertical")) {
									word.increaseScore(checkBonusOffSquare(
											field[x][y], word, field[x][y]
													.getTile().getValue()));
									word.addLetter(field[x][y].getTile()
											.getLetter());
	//								System.out
	//										.println("Letter: "
	//												+ field[x][y].getTile()
	//														.getLetter());
								}
							}
						}
					}
				}
				if (word.getLengthWord() > 1 && word != null) {
					word.addWordBonusToScore();
					playedWords.add(word);
				}
			}
			// waarde berekende horizontaal
			word = new Word();
			for (int x = 0; x < 15; x++) {
				if (field[x][testingYValue1].getTile() != null) {
					// er ligt een tile op de square
					if (isConnected(
							field[x][testingYValue1],
							field[justPlayedTiles.get(0).getXPos()][testingYValue1],
							"horizontal")) {
	//					System.out
	//							.println("Letter: "
	//									+ field[x][testingYValue1].getTile()
	//											.getLetter()
	//									+ " waarde: "
	//									+ field[x][testingYValue1].getTile()
	//											.getValue());
						word.addLetter(field[x][testingYValue1].getTile()
								.getLetter());
						word.increaseScore(checkBonusOffSquare(
								field[x][testingYValue1], word,
								field[x][testingYValue1].getTile().getValue()));

					}
				}
			}
			word.addWordBonusToScore();
			playedWords.add(word);
		} else {
			// woord is verticaal -------------------------
			int testingYValue;
			int testingXValue1 = 0;

			for (Square sq : justPlayedTiles) {
				testingYValue = sq.getYPos();
				testingXValue1 = sq.getXPos();
				word = new Word();

				for (int x = 0; x < 15; x++) {
					for (int y = 0; y < 15; y++) {
						if (field[x][y].getTile() != null) {
							// er ligt een tile op de square

							if (field[x][y].getYPos() == testingYValue) {
								// de tile heeft dezelfde y waarde als de net
								// geplaatste tile
								// if (!field[x][y].getTile().getJustPlayed()) {
								if (isConnected(field[x][y], sq, "horizontal")) {
									// de tile is verbonden met de net
									// geplaatste tile
									word.increaseScore(checkBonusOffSquare(
											field[x][y], word, field[x][y]
													.getTile().getValue()));
									word.addLetter(field[x][y].getTile()
											.getLetter());
	//								System.out
	//										.println("Letter: "
	//												+ field[x][y].getTile()
	//														.getLetter());
								}
							}
						}
					}
				}
				if (word.getLengthWord() > 1 && word != null) {
					word.addWordBonusToScore();
					playedWords.add(word);
				}
			}
			// waarde berekenen verticaal
			word = new Word();
			for (int y = 0; y < 15; y++) {
				if (field[testingXValue1][y].getTile() != null) {
					// er ligt een tile op de square

					if (isConnected(field[testingXValue1][y],
							field[testingXValue1][justPlayedTiles.get(0)
									.getYPos()], "vertical")) {
	//					System.out
	//							.println("Letter: "
	//									+ field[testingXValue1][y].getTile()
	//											.getLetter()
	//									+ " waarde: "
	//									+ field[testingXValue1][y].getTile()
	//											.getValue());
						word.addLetter(field[testingXValue1][y].getTile()
								.getLetter());
						// checking for bonussen
						word.increaseScore(checkBonusOffSquare(
								field[testingXValue1][y], word,
								field[testingXValue1][y].getTile().getValue()));
					}
				}
			}
			word.addWordBonusToScore();
			playedWords.add(word);

		}

		int score = 0;
		for (Word word : playedWords) {
			score = score + word.getScore();
		}

		return score;
	}

	// Checks to bonnus of a square
	// Uses the square, the word object and a score input
	// returns the bonnus with the new score
	public int checkBonusOffSquare(Square square, Word word, int score1) {
		int score = score1;
		int x = square.getXPos();
		int y = square.getYPos();
		if (field[x][y].getTile().getJustPlayed()) {
			if (field[x][y].getValue() != null) {
				if (field[x][y].getValue().equals("dl")) {
					score = score * 2;
					System.out.println("***DL bonnus***");
				} else if (field[x][y].getValue().equals("tl")) {
					score = score * 3;
					System.out.println("***TL bonnus***");
				} else if (field[x][y].getValue().equals("tw")) {
					word.addWordBonus(3);
					System.out.println("***TW bonnus***");
				} else if (field[x][y].getValue().equals("dw")) {
					word.addWordBonus(2);
					System.out.println("***DW bonnus***");
				}
			}
		}
		return score;
	}

	// Check to see if a tile it connected to a tile that is just place on the
	// board
	public boolean isConnected(Square testSq, Square playedSq, String direction) {
		boolean connected = true;
		if (testSq != playedSq) {
			if (direction.equals("horizontal")) {
				int i = testSq.getXPos() - playedSq.getXPos();
				if (i > 0) {
					// testSq ligt rechts van de playedSq
	//				System.out.println(testSq.getTile().getLetter()
	//						+ " ligt rechts van de "
	//						+ playedSq.getTile().getLetter());
					for (i = testSq.getXPos() - 1; i > playedSq.getXPos(); i--) {

						if (field[i][testSq.getYPos()].getTile() == null) {
							connected = false;
	//						System.out.println("Connected if " + connected);
						}
					}

				} else if (i < 0) {
					// testSq ligt links van de playedSq
	//				System.out.println(testSq.getTile().getLetter()
	//						+ " ligt links van de "
	//						+ playedSq.getTile().getLetter());
					for (i = testSq.getXPos() + 1; i < playedSq.getXPos(); i++) {

						if (field[i][testSq.getYPos()].getTile() == null) {
							connected = false;
	//						System.out.println("Connected if " + connected);
						}
					}

				}
			} else if (direction.equals("vertical")) {
				int i = testSq.getYPos() - playedSq.getYPos();
				if (i > 0) {
					// testSq onder de playedSq
	//				System.out.println(testSq.getTile().getLetter()
	//						+ " ligt onder de "
	//						+ playedSq.getTile().getLetter());
					for (i = playedSq.getYPos() - 1; i > playedSq.getYPos(); i--) {
						if (field[testSq.getXPos()][i].getTile() == null) {
							connected = false;
	//						System.out.println("Connected if " + connected);
						}
					}
				} else if (i < 0) {
					// testSq boven de playedSq
	//				System.out.println(testSq.getTile().getLetter()
	//						+ " ligt boven de "
	//						+ playedSq.getTile().getLetter());
					for (i = testSq.getYPos() + 1; i < playedSq.getYPos(); i++) {
						if (field[testSq.getXPos()][i].getTile() == null) {
							connected = false;
	//						System.out.println("Connected if " + connected);
						}
					}
				}
			}
		}
	//	System.out.println("De tegels zijn: " + connected + " test " + testSq.getTile().getLetter() + " " + playedSq.getTile().getLetter());
		return connected;
	}

	// This method checks if the played tiles are connected somehow
	// Returns true or false
	public boolean playedTilesConnected() {
		boolean result = true;
		boolean running = true;
		if (wordHorizontal == true) {
			int testingYValue = justPlayedTiles.get(0).getYPos();
			// de y variabelen zijn opvolgend
			for (int i = 1; i < justPlayedTiles.size(); i++) {
				// System.out.println(justPlayedTiles.get(i).getXPos() + " ! = "
				// + (justPlayedTiles.get(i - 1).getXPos() + 1));
				if (justPlayedTiles.get(i).getXPos() != (justPlayedTiles.get(
						i - 1).getXPos() + 1)) {
					for (int p = 1; p < 15 && running; p++) {
						int x = justPlayedTiles.get(i).getXPos();
						if (field[x - p][testingYValue].getTile() == null) {
							result = false;
							running = false;
							System.out
									.println("Niet alle just played tiles are connected!");
						} else if (field[x - p][testingYValue].getTile()
								.getJustPlayed()) {
							running = false;
						}
					}
				}
			}
		}

		else {
			int testingXValue = justPlayedTiles.get(0).getXPos();
			// de x variabelen zijn opvolgend
			for (int i = 1; i < justPlayedTiles.size(); i++) {
				if (justPlayedTiles.get(i).getYPos() != (justPlayedTiles.get(
						i - 1).getYPos() + 1)) {
					int y = justPlayedTiles.get(i).getYPos();
					for (int p = 1; p < 15 && running; p++) {
						if (field[testingXValue][y - p].getTile() == null) {
							result = false;
							running = false;
							System.out
									.println("Niet alle just played tiles are connected!");
						} else if (field[testingXValue][y - p].getTile()
								.getJustPlayed()) {
							running = false;
						}
					}
				}
			}

		}
		return result;
	}
}

/*
 * aanmaken door: DatabaseHandler dbh = DatabaseHandler.getInstance();
 * 
 * en aanroepen door: dbh. --------
 */