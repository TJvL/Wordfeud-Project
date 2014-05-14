package domein;

import gui.MainFrame;


import java.util.ArrayList;
import java.util.Observer;

public class WordFeud {
	private User user;
	private Match match;
	private ArrayList<Match> matches;
	private SecondThread secondThread;
	private MainFrame framePanel;

	public WordFeud() {
		user = new User();
		match = new Match(0);
		matches = new ArrayList<Match>();
		framePanel = new MainFrame(this);
		secondThread = new SecondThread(framePanel.getGameScreen()
				.getGameFieldPanel(), framePanel.getGameScreen().getButtonPanel(),
				framePanel.getGameScreen().getScorePanel());
	}

	// Gets the user from the player
	// DOESNT WORK GOOD ****************************************
	public Player getUserPlayer() {
		return user.getPlayer();
	}

	// Depends if someone is spectating - starting new game
	// or want to load a game
	public void startGame(int gameID, boolean spectate, boolean newGame) {
		if (spectate) {
			spectateMatch(gameID);
		} else {
			if (newGame) {
				newMatchStartedByMe(gameID);
			} else {
				loadMatch(gameID);
			}
		}
	}

	// This starts a Match that is made by me
	public void newMatchStartedByMe(int gameID) {
		match = new Match(gameID, getUserPlayer());
		// Adds the observers
		this.addObservers(match, false);
		// Sets the thread
		createSecondThread(match);
		match.startNewGame(framePanel.getGameScreen().getGameFieldPanel());
		framePanel.getGameScreen().getGameChatPanel().setChatVariables(match.getOwnName(), match.getEnemyName(), match.getGameID());
		matches.add(match);
	}

	// Loads a match
	public void loadMatch(int gameID) {
		boolean exists = false;
		// Checks if the refrences already exist
		for (Match match : matches) {
			if (match.getGameID() == gameID) {
				exists = true;
				match = new Match(gameID, getUserPlayer());
				// Adds the observers
				this.addObservers(match, false);
				createSecondThread(match);
				match.loadGame(framePanel.getGameScreen().getGameFieldPanel());
				framePanel.getGameScreen().getGameChatPanel().setChatVariables(match.getOwnName(), match.getEnemyName(), match.getGameID());
			}
		}
		if (!exists) {
			match = new Match(gameID, getUserPlayer());
			// Adds the observers
			this.addObservers(match, false);
			createSecondThread(match);
			match.loadGame(framePanel.getGameScreen().getGameFieldPanel());
			framePanel.getGameScreen().getGameChatPanel().setChatVariables(match.getOwnName(), match.getEnemyName(), match.getGameID());
		}

	}

	// Starting the thread
	public void createSecondThread(Match match) {
		// Second thread for running the chat funcion and who's turn it is
		try {
			setThreadStatus(false);
			secondThread.setMatch(match);
		} catch (NullPointerException e) {
			System.out.println("nullPointer - WordFeud");
		}

		secondThread.start();
		setThreadStatus(true);
	}

	// Sets the status, way to stop the thread
	public void setThreadStatus(boolean running) {
		secondThread.setRunning(running);
	}

	// A method start spectating
	public void spectateMatch(int gameID) {
		match = new Match(gameID);
		match.loadSpecateGame(framePanel.getSpecScreen());
		this.addObservers(match, true);
	}

	// Adds the observers
	public void addObservers(Observer observer, boolean spectator) {
		framePanel.addObservers(observer, spectator);
	}
}
