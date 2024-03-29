package datalaag;

import domein.Square;
import domein.Word;

import java.util.ArrayList;

public class ScoreCalculator {

	private static ScoreCalculator scoreCalculator = new ScoreCalculator();
	private Square[][] field;
	private boolean wordHorizontal;
	private Word word;
	private ArrayList<Square> justFilledSquares;
	private ArrayList<Word> playedWords;
	private ArrayList<Word> wordsToCheck;

	private ScoreCalculator() {
		field = new Square[15][15];
		justFilledSquares = new ArrayList<Square>();
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
		this.setjustFilledSquares();
		this.checkFirstWordOnStart();
		boolean runScoreCalculator = true;

		// Checks if a joker is on the board with no value
		for (Square tiles : justFilledSquares) {
			if (tiles.getTile().getBlancoLetterValue() != null) {
				if (tiles.getTile().getBlancoLetterValue().equals("?")) {
					runScoreCalculator = false;
				}
			}
		}

		// Checks if the board is empty, the first word is placed on the star
		if (!checkIfBoardNotEmpty()) {
			if (checkFirstWordOnStart()) {
				runScoreCalculator = true;
			} else {
				runScoreCalculator = false;
			}
		}
		// Method to check if the played word is connected to atleast one other
		// tile
		else {
			boolean oneConected = false;
			for (Square square : justFilledSquares) {
				if (square.getXPos() - 1 >= 0) {
					if (field[square.getXPos() - 1][square.getYPos()].getTile() != null
							&& !field[square.getXPos() - 1][square.getYPos()]
									.getTile().getJustPlayed()) {
						oneConected = true;
					}
				}
				if (square.getXPos() + 1 <= 14) {
					if (field[square.getXPos() + 1][square.getYPos()].getTile() != null
							&& !field[square.getXPos() + 1][square.getYPos()]
									.getTile().getJustPlayed()) {
						oneConected = true;
					}
				}
				if (square.getYPos() - 1 >= 0) {
					if (field[square.getXPos()][square.getYPos() - 1].getTile() != null
							&& !field[square.getXPos()][square.getYPos() - 1]
									.getTile().getJustPlayed()) {
						oneConected = true;
					}
				}
				if (square.getYPos() + 1 <= 14) {
					if (field[square.getXPos()][square.getYPos() + 1].getTile() != null
							&& !field[square.getXPos()][square.getYPos() + 1]
									.getTile().getJustPlayed()) {
						oneConected = true;
					}
				}

			}
			if (!oneConected) {
				runScoreCalculator = false;
			}
		}

		// Check to be sure the first placed word is on the star
		if (runScoreCalculator) {
			// Check if the played word is longer then 1
			if (justFilledSquares.size() > 1) {
				// System.out.println(justFilledSquares.size() + " grote van");
				if (tilesInOneLine()) {
					// All tiles in one line
					if (justFilledSquares.size() > 1) {
						// More then 1 tile
						if (playedTilesConnected()) {
							// All tiles connected
							score = calculateScore();
							if (justFilledSquares.size() == 7) {
								score += 40;
							}
						}
					}
				}
			}
			// If the played word is 1 long
			else if (justFilledSquares.size() == 1) {
				score = calculateScoreOneTile();
			}		
		}

		// Adds all the formed words to a list and prints it
		for (Word word : playedWords) {
			System.out.println("Gespeeld: " + word.getWord() + ".");
			wordsToCheck.add(word);
		}

		// clear all the list and prints the score
		justFilledSquares.clear();
		playedWords.clear();
		System.out.println("/////////////// De score: " + score
				+ "\\\\\\\\\\\\\\\\\\");
		return score;
	}

	// Returns the words that were just played on the field
	public ArrayList<Word> getjustPlayedWords() {
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
		if (countOfTilesOnBoard == justFilledSquares.size()) {
			boardEmpty = false;
		}
		return boardEmpty;
	}

	// Method to check if the first word placed is on the star
	public boolean checkFirstWordOnStart() {
		boolean firstWordOnStar = false;
		for (Square sq : justFilledSquares) {
			if (sq.getValue().equals("*")) {
				firstWordOnStar = true;
			}
		}
		return firstWordOnStar;
	}

	public boolean tilesInOneLine() {
		int tempY = 0;
		boolean result = true;
		boolean firstY = true;
		boolean stopLoop = false;

		// Only counts if atleast 2 tiles are placed
		// Word is horizontal
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
								result = false;
							} else {
								wordHorizontal = true;
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
		// Word is vertical
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
									result = false;
								} else {
									wordHorizontal = false;
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
		Square square = justFilledSquares.get(0);

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
	public void setjustFilledSquares() {
		for (Square[] s : field) {
			for (Square sq : s) {
				if (sq.getTile() != null) {
					if (sq.getTile().getJustPlayed()) {
						justFilledSquares.add(sq);
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
			// wood is horizontal
			int testingXValue;
			int testingYValue1 = 0;
			for (Square sq : justFilledSquares) {
				testingXValue = sq.getXPos();
				testingYValue1 = sq.getYPos();
				word = new Word();

				for (int x = 0; x < 15; x++) {
					for (int y = 0; y < 15; y++) {

						if (field[x][y].getTile() != null) {
							// there is a tile on a square
							if (field[x][y].getXPos() == testingXValue) {
								// same X value
								if (isConnected(field[x][y], sq, "vertical")) {
									word.increaseScore(checkBonusOffSquare(
											field[x][y], word, field[x][y]
													.getTile().getValue()));
									word.addLetter(field[x][y].getTile()
											.getLetter());
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
			// calculate score horizontal
			word = new Word();
			for (int x = 0; x < 15; x++) {
				if (field[x][testingYValue1].getTile() != null) {
					// tiles on a square
					if (isConnected(
							field[x][testingYValue1],
							field[justFilledSquares.get(0).getXPos()][testingYValue1],
							"horizontal")) {
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
			// wood is vertical
			int testingYValue;
			int testingXValue1 = 0;

			for (Square sq : justFilledSquares) {
				testingYValue = sq.getYPos();
				testingXValue1 = sq.getXPos();
				word = new Word();

				for (int x = 0; x < 15; x++) {
					for (int y = 0; y < 15; y++) {
						if (field[x][y].getTile() != null) {
							if (field[x][y].getYPos() == testingYValue) {
								if (isConnected(field[x][y], sq, "horizontal")) {
									word.increaseScore(checkBonusOffSquare(
											field[x][y], word, field[x][y]
													.getTile().getValue()));
									word.addLetter(field[x][y].getTile()
											.getLetter());
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
			// value vertiaal
			word = new Word();
			for (int y = 0; y < 15; y++) {
				if (field[testingXValue1][y].getTile() != null) {
					if (isConnected(field[testingXValue1][y],
							field[testingXValue1][justFilledSquares.get(0)
									.getYPos()], "vertical")) {
						word.addLetter(field[testingXValue1][y].getTile()
								.getLetter());
						// checking for bonusen
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

	// Checks to bonus of a square
	// Uses the square, the word object and a score input
	// returns the bonus with the new score
	public int checkBonusOffSquare(Square square, Word word, int score1) {
		int score = score1;
		int x = square.getXPos();
		int y = square.getYPos();
		if (field[x][y].getTile().getJustPlayed()) {
			if (field[x][y].getValue() != null) {
				if (field[x][y].getValue().equals("DL")) {
					score = score * 2;
					System.out.println("***DL bonnus***");
				} else if (field[x][y].getValue().equals("TL")) {
					score = score * 3;
					System.out.println("***TL bonnus***");
				} else if (field[x][y].getValue().equals("TW")) {
					word.addWordBonus(3);
					System.out.println("***TW bonnus***");
				} else if (field[x][y].getValue().equals("DW")) {
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
					for (i = testSq.getXPos() - 1; i > playedSq.getXPos(); i--) {

						if (field[i][testSq.getYPos()].getTile() == null) {
							connected = false;
							// System.out.println("Connected if " + connected);
						}
					}

				} else if (i < 0) {
					// testSq ligt links van de playedSq
					for (i = testSq.getXPos() + 1; i < playedSq.getXPos(); i++) {

						if (field[i][testSq.getYPos()].getTile() == null) {
							connected = false;
							// System.out.println("Connected if " + connected);
						}
					}

				}
			} else if (direction.equals("vertical")) {
				int i = testSq.getYPos() - playedSq.getYPos();
				if (i > 0) {
					// testSq onder de playedSq
					for (i = testSq.getYPos() - 1; i > playedSq.getYPos(); i--) {
						if (field[testSq.getXPos()][i].getTile() == null) {
							connected = false;
							// System.out.println("Connected if " + connected);
						}
					}
				} else if (i < 0) {
					// testSq boven de playedSq
					for (i = testSq.getYPos() + 1; i < playedSq.getYPos(); i++) {
						if (field[testSq.getXPos()][i].getTile() == null) {
							connected = false;
							// System.out.println("Connected if " + connected);
						}
					}
				}
			}
		}
		return connected;
	}

	// This method checks if the played tiles are connected somehow
	// Returns true or false
	public boolean playedTilesConnected() {
		boolean result = true;
		boolean running = true;
		if (wordHorizontal == true) {
			int testingYValue = justFilledSquares.get(0).getYPos();
			// de y variabelen zijn opvolgend
			for (int i = 1; i < justFilledSquares.size(); i++) {
				if (justFilledSquares.get(i).getXPos() != (justFilledSquares
						.get(i - 1).getXPos() + 1)) {
					for (int p = 1; p < 15 && running; p++) {
						int x = justFilledSquares.get(i).getXPos();
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
			int testingXValue = justFilledSquares.get(0).getXPos();
			// de x variabelen zijn opvolgend
			for (int i = 1; i < justFilledSquares.size(); i++) {
				if (justFilledSquares.get(i).getYPos() != (justFilledSquares
						.get(i - 1).getYPos() + 1)) {
					int y = justFilledSquares.get(i).getYPos();
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