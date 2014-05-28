package domein;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import datalaag.DatabaseHandler;
import gui.GameButtonPanel;
import gui.GameChatPanel;
import gui.MainFrame;
import gui.ScorePanel;

// Thread for updating ScorePanel
// Updating active buttons in the ButtonPanel
// Updating ChatPanel
// Checks who's turn it is for the GameFieldPanel
public class GameThread extends Thread {
	private Match match;
	private GameButtonPanel buttonPanel;
	private DatabaseHandler dbh;
	private ScorePanel scorePanel;
	private GameChatPanel chatPanel;
	private MatchManager matchManager;
	private MainFrame framePanel;
	private int storeScore;
	private boolean running = true;
	private boolean turnSwap = true;

	public GameThread(GameChatPanel chatPanel, GameButtonPanel buttonPanel,
			ScorePanel scorePanel, MatchManager matchManager,
			MainFrame framePanel) {
		super("thread");
		this.chatPanel = chatPanel;
		this.buttonPanel = buttonPanel;
		this.scorePanel = scorePanel;
		this.dbh = DatabaseHandler.getInstance();
		this.matchManager = matchManager;
		this.framePanel = framePanel;
	}

	// The method that will be running
	public void run() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		// While running it will run
		while (running) {
			if (match != null) {
				running = true;
				// Gets the gameID;
				match.getMaxTurnID();
				int gameID = match.getGameID();
				// System.out.println("LOOK AT ME - THREAD " + gameID);
				// Setting the scores
				scorePanel
						.setEnemyScore(dbh.score(gameID, match.getEnemyName()));
				scorePanel.setOwnScore(dbh.score(gameID, match.getOwnName()));
				if (match.getMyTurn()) {
					scorePanel.setOwnName(match.getOwnName() + "**");
					scorePanel.setEnemyName(match.getEnemyName());
				} else {
					scorePanel.setOwnName(match.getOwnName());
					scorePanel.setEnemyName(match.getEnemyName() + "**");
				}

				// HERE HAS TO BE A METHOD TO CHECK SUBMITTED WORDS

				// Prints the current wordValue
				int currentScore = match.getScore();
				if (storeScore > currentScore || storeScore < currentScore) {
					scorePanel.setWordValue(currentScore);
					storeScore = currentScore;
				}

				// Update chat
				chatPanel.checkForMessages();

				// A method to disable swap when fewer then 7 tiles
				if (match.getJarSize() < 7) {
					buttonPanel.disableSwap();
				}

				/*
				 * String gameBegin; if (match.getMyTurn()) { gameBegin =
				 * dbh.turnValue(gameID, match.getMaxTurn(),
				 * match.getOwnName()); } else { gameBegin =
				 * dbh.turnValue(gameID, match.getMaxTurn(),
				 * match.getOwnName()); }
				 */

				// if (gameBegin.equals("Begin")) {
				// a loop to see if the turn is swapped
				try {
					if (!dbh.getGameStatusValue(gameID).equals("Finished")
							&& !dbh.getGameStatusValue(gameID).equals(
									"Resigned")) {
						if (match.getMyTurn()) {
							buttonPanel.setTurn(true);
							if (turnSwap) {
								match.updateField();
								JOptionPane.showMessageDialog(null,
										"YOUR TURN!", "Turn info",
										JOptionPane.INFORMATION_MESSAGE);
							}
							turnSwap = false;
						} else {
							// System.out.println("NIET MIJN BEURT");
							buttonPanel.setTurn(false);
							turnSwap = true;
						}
					} else {
						if (dbh.getGameStatusValue(gameID).equals("Finished")) {

							int enemyScore = dbh.score(gameID,
									match.getEnemyName());
							int ownScore = dbh
									.score(gameID, match.getOwnName());
							if (enemyScore > ownScore) {
								JOptionPane.showMessageDialog(null,
										"YOU LOST!", "Game over",
										JOptionPane.INFORMATION_MESSAGE);
							} else {
								JOptionPane.showMessageDialog(null, "YOU WON!",
										"Game over",
										JOptionPane.INFORMATION_MESSAGE);
							}
						}
						if (dbh.getGameStatusValue(gameID).equals("Resigned")) {
							if (match.getSurrender()) {
								JOptionPane.showMessageDialog(null,
										"The game is over, you surrenderd!",
										"Game over",
										JOptionPane.INFORMATION_MESSAGE);
							} else {
								JOptionPane.showMessageDialog(null,
										"The game is over, you won!",
										"Game over",
										JOptionPane.INFORMATION_MESSAGE);
							}
						}
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
				e.printStackTrace();
			}

			// Method to start games afther both players accepted
			ArrayList<Integer> gamesToLoad = dbh.gameToLoad(matchManager
					.getName());
			if (gamesToLoad != null) {
				for (Integer game : gamesToLoad) {
					matchManager.startGame(game, false, true);
				}
			}

			framePanel.updateNotificationList();

			// } else {
			// buttonPanel.setTurn(false);
			// buttonPanel.disableSurrender();
			// running = false;
			// }
		}
	}

	// A method to stop the loop the match loop
	public void setRunning(Match match) {
		this.match = match;
	}

	public void stopRunning() {
		this.match = null;
	}
}
