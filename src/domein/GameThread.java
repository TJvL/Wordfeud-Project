package domein;

import java.util.ArrayList;

import javax.swing.JOptionPane;
import datalaag.DatabaseHandler;
import gui.GameButtonPanel;
import gui.GameChatPanel;
import gui.ScorePanel;

// Thread for updating ScorePanel
// Updating active buttons in the ButtonPanel
// Updating ChatPanel
// Checks who's turn it is for the GameFieldPanel
public class GameThread extends Thread {
	private Match match;
	private Match storeMatch;
	private GameButtonPanel buttonPanel;
	private DatabaseHandler dbh;
	private ScorePanel scorePanel;
	private GameChatPanel chatPanel;
	private MatchManager matchManager;
	private int storeScore;
	// private updateTheGame updateTheGame;
	private boolean running = true;
	private boolean turnSwap = true;

	public GameThread(GameChatPanel chatPanel, GameButtonPanel buttonPanel,
			ScorePanel scorePanel, MatchManager matchManager) {
		super("thread");
		this.chatPanel = chatPanel;
		this.buttonPanel = buttonPanel;
		this.scorePanel = scorePanel;
		this.dbh = DatabaseHandler.getInstance();
		this.matchManager = matchManager;
		// updateTheGame = new domein.GameThread.updateTheGame();
	}

	// The method that will be running
	public void run() {
		// try {
		// Thread.sleep(500);
		// } catch (InterruptedException e1) {
		// e1.printStackTrace();
		// }
		// While running it will run
		while (running) {
			if (match != null) {
				if (storeMatch != match) {

					scorePanel.setEnemyScore(match.getScoreP2());
					scorePanel.setOwnScore(match.getScoreP1());
					if (match.getMyTurn()) {
						scorePanel.setOwnName(match.getOwnName() + "**");
						scorePanel.setEnemyName(match.getEnemyName());
					} else {
						scorePanel.setOwnName(match.getOwnName());
						scorePanel.setEnemyName(match.getEnemyName() + "**");
						buttonPanel.disableSwap();
						turnSwap = false;
					}

					if (!match.getMyTurn()) {
						turnSwap = false;
					}
				}
				storeMatch = match;
				storeMatch.getMaxTurnID();
				running = true;
				// Gets the gameID;
				// storeMatch.getMaxTurnID();
				// System.out.println("LOOK AT ME - THREAD " + gameID);

				// Prints the current wordValue
				int currentScore = storeMatch.getScore();
				if (storeScore > currentScore || storeScore < currentScore) {
					scorePanel.setWordValue(currentScore);
					storeScore = currentScore;
				}

				// Update chat
				chatPanel.checkForMessages();

				// if (gameBegin.equals("Begin")) {
				// a loop to see if the turn is swapped
				try {
					if (!storeMatch.getGameStatus().equals("Finished")
							&& !storeMatch.getGameStatus().equals("Resigned")) {
						if (storeMatch.getMyTurn()) {
							if (turnSwap) {
								try {
									Thread.sleep(3000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								scorePanel.setOwnName(storeMatch.getOwnName()
										+ "**");
								scorePanel.setEnemyName(storeMatch
										.getEnemyName());
								// updateTheGame.execute();
								storeMatch.updateField();
								JOptionPane.showMessageDialog(null,
										"YOUR TURN!", "Turn info",
										JOptionPane.INFORMATION_MESSAGE);
								scorePanel.setEnemyScore(storeMatch
										.getScoreP2());
								scorePanel.setOwnScore(storeMatch.getScoreP1());
							}
							buttonPanel.setTurn(true);
							if (!match.swapAllowed()) {
								buttonPanel.disableSwap();
							}
							turnSwap = false;
						} else {
							// System.out.println("NIET MIJN BEURT");
							if (!turnSwap) {
								buttonPanel.setTurn(false);
								scorePanel.setOwnName(storeMatch.getOwnName());
								scorePanel.setEnemyName(storeMatch
										.getEnemyName() + "**");
								scorePanel.setEnemyScore(storeMatch
										.getScoreP2());
								scorePanel.setOwnScore(storeMatch.getScoreP1());
							}
							turnSwap = true;
						}
					} else {
						buttonPanel.setTurn(false);
						buttonPanel.disableSurrender();
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (storeMatch.getGameStatus().equals("Finished")) {

							int enemyScore = storeMatch.getScoreP2();
							int ownScore = storeMatch.getScoreP1();
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
						else if (storeMatch.getGameStatus().equals("Resigned")) {
							if (storeMatch.getSurrender()) {
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
						match = null;
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

//			// Method to start games after both players accepted
//			ArrayList<Integer> gamesToLoad = dbh.gameToLoad(matchManager
//					.getName());
//			if (gamesToLoad != null) {
//				for (Integer game : gamesToLoad) {
//					matchManager.startGame(game, false, true);
//				}
//			}
		}
	}

	// A method to stop the loop the match loop
	public void setRunning(Match match) {
		this.match = match;
	}

	public void stopRunning() {
		this.match = null;
	}

	// public class updateTheGame extends SwingWorker<Integer, String> {
	//
	// @Override
	// protected Integer doInBackground() throws Exception {
	// Thread.sleep(500);
	// storeMatch.updateField();
	// return 1;
	// }
	//
	// protected void process(List chunks) {
	// // Messages received from the doInBackground() (when invoking the
	// // publish() method)
	// }
	// }

}
