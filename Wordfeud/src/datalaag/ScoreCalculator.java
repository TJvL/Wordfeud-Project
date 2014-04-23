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

	private ScoreCalculator() {
		field = new Square[15][15];
		justPlayedTiles = new ArrayList<Square>();
		wordHorizontal = true;
		playedWords = new ArrayList<Word>();
	}

	public static ScoreCalculator getInstance() {
		return scoreCalculator;
	}

	public void addSquaresToBoard(Square[][] field) {
		// field[square.getXPos()][square.getYPos()] = square;
		this.field = field;
	}

	public int startCalculating() {

		int score = 0;
		this.getJustPlayedTiles();

		if (tilesInOneLine()) {
			// alle gelegde tiles liggen op dezelfde lijn

			if (justPlayedTiles.size() > 1) {
				// //////////////////////////////////////////////////////////////////////////////////////////
				// klopt nog niet. er moet ook nog gekeken worden of die ene
				// tile verbonden is met andere tiles
				// //////////////////////////////////////////////////////////////////////////////////////////

				// er is meer dan 1 tile gelegd

				if (playedTilesConnected()) {
					// alle gelegde tiles liggen aan elkaar
					System.out.println("alle checks zijn geslaagd");
					score = calculateScore();
				}
			}
		}

		// method om te kijken of alle woorden bestaan in de database
		// worden opgeslagen in playedWords
		for (Word word : playedWords) {
			System.out.println("Gespeeld: " + word.getWord());
		}

		justPlayedTiles.clear();
		playedWords.clear();
		System.out.println(score);
		return score;
	}

	public boolean tilesInOneLine() {
		int tempY = 0;
		boolean result = true;
		boolean firstY = true;
		boolean stopLoop = false;
		// Iets apart maken als er maar een letter is gespeeld

		// Woord is horizontaal
		for (int y = 0; y < 15 && !stopLoop; y++) {
			for (int x = 0; x < 15 && !stopLoop; x++) {
				if (field[x][y].getTile() != null) {
					if (field[x][y].getTile().getJustPlayed()) {
						System.out.println(x + " " + y + " "
								+ field[x][y].getTile().getJustPlayed());
						if (firstY) {
							tempY = field[x][y].getYPos();
							firstY = false;
						} else {
							if (tempY != field[x][y].getYPos()) {
								stopLoop = true;
								wordHorizontal = false;
								System.out.println("Woord is niet horizontaal");
								result = false;
							} else {
								wordHorizontal = true;
								System.out.println("Woord is horizontaal");
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
									System.out
											.println("Woord is niet verticaal");
									result = false;
								} else {
									wordHorizontal = false;
									System.out.println("Woord is verticaal");
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

	public void getJustPlayedTiles() {
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
		int score = 0;
		String direction = "";

		if (wordHorizontal) {
			// woord is horizontaal
			System.out.println("---- woord is horizontaal");
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
								if (isConnected(field[x][y], sq, "")) {
									// if
									// (!field[x][y].getTile().getJustPlayed())
									// {

									// de tile is verbonden met de net
									// geplaatste tile
									word.increaseScore(field[x][y].getTile()
											.getValue());
									// score = score
									// + field[x][y].getTile()
									// .getValue();
									word.addLetter(field[x][y].getTile()
											.getLetter());
									System.out
											.println("Letter: "
													+ field[x][y].getTile()
															.getLetter());
								}
								// }
								// dit zijn de just played tiles dus
								/*
								 * else {
								 * word.increaseScore(field[x][y].getTile()
								 * .getValue());
								 * word.addLetter(field[x][y].getTile()
								 * .getLetter()); System.out .println("Letter: "
								 * + field[x][y].getTile() .getLetter());
								 * 
								 * }
								 */
							}
						}
					}
				}
				if (word.getLengthWord() > 1 && word != null) {
					playedWords.add(word);
				}
			}
			// waarde berekende horizontaal
			word = new Word();
			for (int x = 0; x < 15; x++) {
				if (field[x][testingYValue1].getTile() != null) {
					// er ligt een tile op de square
					if (isConnected(field[x][testingYValue1],
							field[justPlayedTiles.get(0).getXPos()][testingYValue1],
							"horizontal")) {
						// if
						// (!field[x][testingYValue1].getTile().getJustPlayed())
						// {
						System.out
								.println("Letter: "
										+ field[x][testingYValue1].getTile()
												.getLetter()
										+ " waarde: "
										+ field[x][testingYValue1].getTile()
												.getValue());
						word.addLetter(field[x][testingYValue1].getTile()
								.getLetter());
						word.increaseScore(field[x][testingYValue1].getTile()
								.getValue());

					}
				}
			}
			playedWords.add(word);
		} else {
			// woord is verticaal
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
									word.increaseScore(field[x][y].getTile()
											.getValue());
									// score = score
									// + field[x][y].getTile()
									// .getValue();
									word.addLetter(field[x][y].getTile()
											.getLetter());
									System.out
											.println("Letter: "
													+ field[x][y].getTile()
															.getLetter());
								}
							}
						}
					}
				}
				if (word.getLengthWord() > 1 && word != null) {
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
									.getYPos()], "")) {
						// if
						// (!field[testingXValue1][y].getTile().getJustPlayed())
						// {
						System.out
								.println("Letter: "
										+ field[testingXValue1][y].getTile()
												.getLetter()
										+ " waarde: "
										+ field[testingXValue1][y].getTile()
												.getValue());
						word.addLetter(field[testingXValue1][y].getTile()
								.getLetter());
						word.increaseScore(field[testingXValue1][y].getTile()
								.getValue());
					}
					// }
				}
			}
			playedWords.add(word);

		}

		// alle aanliggende woorden zijn berekend

		// score van gelegde letters toevoegen aan score
		/*
		 * for (Square sq : justPlayedTiles) { score = score +
		 * sq.getTile().getValue(); }
		 */

		score = 0;
		for (Word word : playedWords) {
			score = score + word.getScore();
		}

		return score;
	}

	public boolean isConnected(Square testSq, Square playedSq, String direction) {
		boolean connected = true;
		if (testSq != playedSq) {
			if (direction.equals("horizontal")) {
				int i = testSq.getXPos() - playedSq.getXPos();
				if (i > 0) {
					// testSq ligt rechts van de playedSq
					System.out.println(testSq.getTile().getLetter()
							+ " ligt rechts van de "
							+ playedSq.getTile().getLetter());
					for (i = testSq.getXPos() - 1; i > playedSq.getXPos(); i--) {

						if (field[i][testSq.getYPos()].getTile() == null) {
							connected = false;
							System.out.println("Connected if " + connected);
						}
					}

				} else if (i < 0) {
					// testSq ligt links van de playedSq
					System.out.println(testSq.getTile().getLetter()
							+ " ligt links van de "
							+ playedSq.getTile().getLetter());
					for (i = testSq.getXPos() + 1; i < playedSq.getXPos(); i++) {

						if (field[i][testSq.getYPos()].getTile() == null) {
							connected = false;
							System.out.println("Connected if " + connected);
						}
					}

				}
			} 
		}
		System.out.println("De tegels zijn: " + connected);
		return connected;
	}

	public boolean playedTilesConnected() {
		boolean result = true;

		if (wordHorizontal == true) {
			// de y variabelen zijn opvolgend
			for (int i = 1; i < justPlayedTiles.size(); i++) {
				System.out.println(justPlayedTiles.get(i).getXPos() + " ! = "
						+ (justPlayedTiles.get(i - 1).getXPos() + 1));
				if (justPlayedTiles.get(i).getXPos() != (justPlayedTiles.get(
						i - 1).getXPos() + 1)) {
					result = false;
				}
			}
		} else {
			// de x variabelen zijn opvolgend
			for (int i = 1; i < justPlayedTiles.size(); i++) {
				if (justPlayedTiles.get(i).getYPos() != (justPlayedTiles.get(
						i - 1).getYPos() + 1)) {
					result = false;
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