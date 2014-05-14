package domein;

import datalaag.DatabaseHandler;
import gui.GameButtonPanel;
import gui.GameFieldPanel;
import gui.ScorePanel;

// Thread for updating ScorePanel
// Updating active buttons in the ButtonPanel
// Updating ChatPanel
// Checks who's turn it is for the GameFieldPanel
public class SecondThread extends Thread {
	private Match match;
	private GameFieldPanel fieldPanel;
	private GameButtonPanel buttonPanel;
	private DatabaseHandler dbh;
	private ScorePanel scorePanel;
	private int storeScore;
	private boolean running = true;
	private boolean turnSwap = true;

	public SecondThread(GameFieldPanel fieldPanel, GameButtonPanel buttonPanel,
			ScorePanel scorePanel) {
		super("thread");
		this.fieldPanel = fieldPanel;
		this.buttonPanel = buttonPanel;
		this.scorePanel = scorePanel;
		this.dbh = DatabaseHandler.getInstance();
	}

	// Sets the current Match
	public synchronized void setMatch(Match match) {
		this.match = match;
	}

	// The method that will be running
	public void run() {

		// While running it will run
		while (running) {
			//System.out.println("LOOK AT ME - THREAD");
			if (match != null) {
				running = true;
				// Gets the gameID;
				int gameID = match.getGameID();

				// Setting the scores
				scorePanel
						.setEnemyScore(dbh.score(gameID, match.getEnemyName()));
				scorePanel.setOwnScore(dbh.score(gameID, match.getOwnName()));
				scorePanel.setOwnName(match.getOwnName());
				scorePanel.setEnemyName(match.getEnemyName());

				// HERE HAS TO BE A METHOD TO CHECK SUBMITTED WORDS

				// Prints the current wordValue
				int currentScore = match.getScore();
				if (storeScore > currentScore || storeScore < currentScore) {
					scorePanel.setWordValue(currentScore);
					storeScore = currentScore;
				}

				// a loop to see if the turn is swapped 
				try {
					if (!dbh.getGameStatusValue(gameID).equals("Finished")
							&& !dbh.getGameStatusValue(gameID).equals(
									"Resigned")) {
						match.getMaxTurnID();
						if (match.getMyTurn()) {
							buttonPanel.setTurn(true);
							if (turnSwap) {
								match.updateField();
								System.out.println("WORDT DIT AANGEROEPEN?");
							}
							turnSwap = false;
						} else {
							// System.out.println("NIET MIJN BEURT");
							buttonPanel.setTurn(false);
							turnSwap = true;
						}
					} else {
						buttonPanel.setTurn(false);
						buttonPanel.disableSurrender();
						running = false;
					}

					scorePanel.updatePanel();
				} catch (NullPointerException e) {

				}
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	// A method to stop the loop
	public void setRunning(boolean running) {
		this.running = running;
	}

}
