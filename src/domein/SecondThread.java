package domein;

import datalaag.DatabaseHandler;
import gui.GameButtonPanel;
import gui.GameChatPanel;
import gui.ScorePanel;

// Thread for updating ScorePanel
// Updating active buttons in the ButtonPanel
// Updating ChatPanel
// Checks who's turn it is for the GameFieldPanel
public class SecondThread extends Thread {
	private Match match;
	private GameButtonPanel buttonPanel;
	private DatabaseHandler dbh;
	private ScorePanel scorePanel;
	private GameChatPanel chatPanel;
	private int storeScore;
	private boolean running = true;
	private boolean turnSwap = true;

	public SecondThread(GameChatPanel chatPanel, GameButtonPanel buttonPanel,
			ScorePanel scorePanel, Match match) {
		super("thread");
		this.chatPanel = chatPanel;
		this.buttonPanel = buttonPanel;
		this.scorePanel = scorePanel;
		this.dbh = DatabaseHandler.getInstance();
		this.match = match;
	}

	// The method that will be running
	public void run() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// While running it will run
		while (running) {
			if (match != null) {
				running = true;
				// Gets the gameID;
				match.getMaxTurnID();
				int gameID = match.getGameID();
				//System.out.println("LOOK AT ME - THREAD " + gameID);
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

				// Update chat
				chatPanel.checkForMessages();

				String gameBegin;
				if (match.getMyTurn()) {
					gameBegin = dbh.turnValue(gameID, match.getMaxTurn(),
							match.getOwnName());
				} else {
					gameBegin = dbh.turnValue(gameID, match.getMaxTurn(),
							match.getOwnName());
				}

				if (gameBegin.equals("Begin")) {
					// a loop to see if the turn is swapped
					try {
						if (!dbh.getGameStatusValue(gameID).equals("Finished")
								&& !dbh.getGameStatusValue(gameID).equals(
										"Resigned")) {
							if (match.getMyTurn()) {
								buttonPanel.setTurn(true);
								if (turnSwap) {
									match.updateField();
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
				}
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				buttonPanel.setTurn(false);
				buttonPanel.disableSurrender();
				running = false;
			}
		}
	}

	// A method to stop the loop
	public void setRunning(boolean running) {
		this.running = running;
	}

}
