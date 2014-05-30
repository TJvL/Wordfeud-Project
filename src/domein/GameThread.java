package domein;

import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import datalaag.DatabaseHandler;
import datalaag.WordFeudConstants;
import gui.GameButtonPanel;
import gui.GameChatPanel;
import gui.ScorePanel;

// Thread for updating ScorePanel
// Updating active buttons in the ButtonPanel
// Updating ChatPanel
// Checks who's turn it is for the GameFieldPanel
public class GameThread implements Runnable {
	private Match match;
	private Match storeMatch;
	private GameButtonPanel buttonPanel;
	private ScorePanel scorePanel;
	private GameChatPanel chatPanel;
	private MatchManager matchManager;
	private int storeScore;
	private boolean running = true;
	private boolean isTurnJustSwapped = true;

	public GameThread() {
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
				
				final String currentUsername = matchManager.getName();
				// Method to start games after both players accepted
				ArrayList<Integer> gamesToLoad = DatabaseHandler.getInstance().gameToLoad(currentUsername);
				if (gamesToLoad != null) {
					for (Integer loadID : gamesToLoad) {
						Match match = matchManager.startGame(loadID, false, true);
//						chatPanel.setChatVariables(match.getOwnName(), match.getGameID());
//						this.setRunning(match);
					}
				}
				
				// Setting all values from current match
				storeMatch.getMaxTurnID();
				final int gameID = storeMatch.getGameID();
				final String enemyName = storeMatch.getEnemyName();
				final String ownName = storeMatch.getOwnName();
				final int enemyScore = DatabaseHandler.getInstance().score(gameID, enemyName);
				final int ownScore = DatabaseHandler.getInstance().score(gameID, ownName);
				final int currentScore = storeMatch.getScore();
				final boolean isMyTurn = storeMatch.getMyTurn();
				final boolean isSwapAllowed = match.swapAllowed();
				final boolean didISurrender = storeMatch.didISurrender();
				final String gameStatus = DatabaseHandler.getInstance().getGameStatusValue(gameID);
				
				// Instruct GUI to update score panels.
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						scorePanel.setEnemyScore(enemyScore);
						scorePanel.setOwnScore(ownScore);
						if (isMyTurn) {
							scorePanel.setOwnName(ownName + "**");
							scorePanel.setEnemyName(enemyName);
						} else {
							scorePanel.setOwnName(ownName);
							scorePanel.setEnemyName(enemyName + "**");
						}
					}
				});

				// HERE HAS TO BE A METHOD TO CHECK SUBMITTED WORDS

				// Prints the current wordValue
				if (storeScore > currentScore || storeScore < currentScore) {
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							scorePanel.setWordValue(currentScore);
						}
					});
					storeScore = currentScore;
				}

				// Update chat
				chatPanel.checkForMessages();

				// a loop to see if the turn is swapped
				try {
					// If the game status is "Playing" then the normal turn check is done
					if (!gameStatus
							.equals(WordFeudConstants.GAME_STATUS_FINISHED)
							&& !gameStatus
									.equals(WordFeudConstants.GAME_STATUS_RESIGNED)) {
						// Check if its this users turn.
						if (isMyTurn) {
							// Check if the turn has just been swapped and we need to update the GUI.
							if (isTurnJustSwapped) {
								storeMatch.updateField();
								SwingUtilities.invokeLater(new Runnable() {
									@Override
									public void run() {
										JOptionPane
												.showMessageDialog(
														null,
														"YOUR TURN!",
														"Turn info",
														JOptionPane.INFORMATION_MESSAGE);
									}
								});
							}
							SwingUtilities.invokeLater(new Runnable() {
								@Override
								public void run() {
									buttonPanel.setTurn(true);
								}
							});
							// Check if the current macth has enough letters in the jar so users may swap their letters.
							if (!isSwapAllowed) {
								SwingUtilities.invokeLater(new Runnable() {
									@Override
									public void run() {
										buttonPanel.disableSwap();
									}
								});
							}
							isTurnJustSwapped = false;
						// else update the GUI in a way so the current user can't use buttons he shouldn't be using because it's not his turn yet.
						} else {
							SwingUtilities.invokeLater(new Runnable() {
								@Override
								public void run() {
									buttonPanel.setTurn(false);
									isTurnJustSwapped = true;
								}
							});

						}
					// else check why the game is not "Playing" anymore.
					} else {
						// if the game is not "Playing" anymore then check if it's "Finished".
						if (gameStatus
								.equals(WordFeudConstants.GAME_STATUS_FINISHED)) {
							// if the current users score is lower then his opponents score, tell the GUI to tell the user he lost.
							if (enemyScore > ownScore) {
								SwingUtilities.invokeLater(new Runnable() {
									@Override
									public void run() {
										JOptionPane
												.showMessageDialog(
														null,
														"YOU LOST!",
														"Game over",
														JOptionPane.INFORMATION_MESSAGE);
									}
								});
							// else tell the GUI to tell the user he has won.
							} else {
								SwingUtilities.invokeLater(new Runnable() {
									@Override
									public void run() {
										JOptionPane
												.showMessageDialog(
														null,
														"YOU WON!",
														"Game over",
														JOptionPane.INFORMATION_MESSAGE);
									}
								});
							}
						}
						// if the game status is "Resigned".
						if (gameStatus
								.equals(WordFeudConstants.GAME_STATUS_RESIGNED)) {
							// if the game is resigned because the current user resigned it, tell he GUI to tell the user he Lost because he resigned.
							if (didISurrender) {
								SwingUtilities.invokeLater(new Runnable() {
									@Override
									public void run() {
										JOptionPane
												.showMessageDialog(
														null,
														"The game is over, you resigned!",
														"Game over",
														JOptionPane.INFORMATION_MESSAGE);
									}
								});
							// else tell the user his opponent resigned and he won.
							} else {
								SwingUtilities.invokeLater(new Runnable() {
									@Override
									public void run() {
										JOptionPane
												.showMessageDialog(
														null,
														"The game is over, opponent resigned!",
														"Game over",
														JOptionPane.INFORMATION_MESSAGE);
									}
								});
							}
						}
						// Finnaly tell the GUI that the user cannot play words anymore, like it wasn't his turn.
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								buttonPanel.setTurn(false);
								buttonPanel.disableSurrender();
							}
						});
						// Break the loop for it is no longer necessary to update the GUI with the newest data.
						running = false;
					}
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							scorePanel.repaint();
						}
					});
				} catch (NullPointerException e) {
				}
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	// A method to stop the match loop
	public void setRunning(Match match) {
		this.match = match;
	}

	public void stopRunning() {
		this.match = null;
	}
	
	public void setPanels(GameChatPanel chatPanel, GameButtonPanel buttonPanel,
			ScorePanel scorePanel, MatchManager matchManager) {
		this.chatPanel = chatPanel;
		this.buttonPanel = buttonPanel;
		this.scorePanel = scorePanel;
		this.matchManager = matchManager;
	}
}
