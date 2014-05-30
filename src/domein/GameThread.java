package domein;

import java.awt.List;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

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
	private Match storeMatch;
	private GameButtonPanel buttonPanel;
	private DatabaseHandler dbh;
	private ScorePanel scorePanel;
	private GameChatPanel chatPanel;
	private MatchManager matchManager;
	private MainFrame framePanel;
	private int storeScore;
	private updateTheGame updateTheGame;
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
		updateTheGame = new domein.GameThread.updateTheGame();
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
				storeMatch = match;
				running = true;
				// Gets the gameID;
				storeMatch.getMaxTurnID();
				int gameID = storeMatch.getGameID();
			//	System.out.println("LOOK AT ME - THREAD " + gameID);
				// Setting the scores
				scorePanel.setEnemyScore(dbh.score(gameID,
						storeMatch.getEnemyName()));
				scorePanel.setOwnScore(dbh.score(gameID,
						storeMatch.getOwnName()));
				if (storeMatch.getMyTurn()) {
					scorePanel.setOwnName(storeMatch.getOwnName() + "**");
					scorePanel.setEnemyName(storeMatch.getEnemyName());
				} else {
					scorePanel.setOwnName(storeMatch.getOwnName());
					scorePanel.setEnemyName(storeMatch.getEnemyName() + "**");
				}

				// HERE HAS TO BE A METHOD TO CHECK SUBMITTED WORDS

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
					if (!dbh.getGameStatusValue(gameID).equals("Finished")
							&& !dbh.getGameStatusValue(gameID).equals(
									"Resigned")) {
						if (storeMatch.getMyTurn()) {
							if (turnSwap) {
								updateTheGame.execute();
								storeMatch.updateField();
								JOptionPane.showMessageDialog(null,
										"YOUR TURN!", "Turn info",
										JOptionPane.INFORMATION_MESSAGE);
							}
							buttonPanel.setTurn(true);					
							if (!match.swapAllowed()) {
								buttonPanel.disableSwap();
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
									storeMatch.getEnemyName());
							int ownScore = dbh.score(gameID,
									storeMatch.getOwnName());
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
		}
	}

	// A method to stop the loop the match loop
	public void setRunning(Match match) {
		this.match = match;
	}

	public void stopRunning() {
		this.match = null;
	}

	public class updateTheGame extends SwingWorker<Integer, String> {

		@Override
		protected Integer doInBackground() throws Exception {		
			storeMatch.updateField();			
			return 1;
		}

		protected void process(List chunks) {
			// Messages received from the doInBackground() (when invoking the
			// publish() method)
		}
	}

}
