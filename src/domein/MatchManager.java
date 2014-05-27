package domein;

import gui.MainFrame;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import datalaag.DatabaseHandler;

public class MatchManager {
	private Match match;
	private DatabaseHandler dbh;
	private ArrayList<Match> matches;
	private ArrayList<PendingMatch> pendingMatchs;
	private ArrayList<ActiveMatch> activeMatches;
	private ArrayList<ActiveMatch> myActiveMatches;
	private WordFeud wf;
	private MainFrame framePanel;
	private GameThread gameThread;

	public MatchManager(WordFeud wf, MainFrame framePanel) {
		this.dbh = DatabaseHandler.getInstance();
		this.matches = new ArrayList<Match>();
		this.pendingMatchs = new ArrayList<PendingMatch>();
		this.activeMatches = new ArrayList<ActiveMatch>();
		this.myActiveMatches = new ArrayList<ActiveMatch>();
		this.wf = wf;
		this.framePanel = framePanel;
		initializeThread();
	}

	public ArrayList<PendingMatch> getPendingMatchs() {
		if (pendingMatchs != null) {
			pendingMatchs.clear();
		}
		ArrayList<String> games = dbh.pendingGames(wf.getCurrentUsername());
		for (String game : games) {
			String[] split = game.split(",");
			pendingMatchs.add(new PendingMatch(Integer.parseInt(split[0]),
					split[1]));
		}
		return pendingMatchs;
	}

	public ArrayList<ActiveMatch> getActiveMatches() {
		if (activeMatches != null) {
			activeMatches.clear();
		}
		ArrayList<String> games = dbh.spectateList();
		for (String game : games) {
			String[] split = game.split(",");
			activeMatches.add(new ActiveMatch(Integer.parseInt(split[0]),
					split[1]));
		}
		return activeMatches;
	}

	public ArrayList<ActiveMatch> getMyActiveMatches() {
		if (myActiveMatches != null) {
			myActiveMatches.clear();
		}
		ArrayList<String> games = dbh.activeGames(wf.getCurrentUsername());
		for (String game : games) {
			String[] split = game.split(",");
			myActiveMatches.add(new ActiveMatch(Integer.parseInt(split[0]),
					split[1]));
		}
		return myActiveMatches;
	}

	// A method to initialize the Thread
	public void initializeThread() {
		this.gameThread = new GameThread(framePanel.getGameScreen()
				.getGameChatPanel(), framePanel.getGameScreen()
				.getButtonPanel(), framePanel.getGameScreen().getScorePanel(), this);
		gameThread.start();
	}

	// Stops the Thread
	public void stopThread() {
		gameThread.stopRunning();
	}

	// Depends if someone is spectating - starting new game
	// or want to load a game
	public synchronized void startGame(int gameID, boolean spectate, boolean newGame) {
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
		match = new Match(gameID, wf.getCurrentUsername());
		// Adds the observers
//		wf.addObservers(match, false);
		// Sets the thread
		match.startNewGame();
//		framePanel.getGameScreen().getGameChatPanel()
//				.setChatVariables(match.getOwnName(), match.getGameID());
//		matches.add(match);
//		gameThread.setRunning(match);
	}

	// Loads a match
	public void loadMatch(int gameID) {
		boolean exists = false;
		// Checks if the refrences already exist
		for (Match match : matches) {
			if (match.getGameID() == gameID) {
				exists = true;
				// Adds the observers
				wf.addObservers(match, false);
				framePanel
						.getGameScreen()
						.getGameChatPanel()
						.setChatVariables(match.getOwnName(), match.getGameID());
				match.loadGame();
				gameThread.setRunning(match);
			}
		}
		if (!exists) {
			match = new Match(gameID, wf.getUserPlayer(), framePanel
					.getGameScreen().getGameFieldPanel(),
					wf.getCurrentUsername());
			// Adds the observers
			wf.addObservers(match, false);
			match.loadGame();
			framePanel.getGameScreen().getGameChatPanel()
					.setChatVariables(match.getOwnName(), match.getGameID());
			matches.add(match);
			gameThread.setRunning(match);
		}

	}

	// A method start spectating
	public void spectateMatch(int gameID) {
		System.out.println("GAMEID " + gameID);
		match = new Match(gameID);
		match.loadSpecateGame(framePanel.getSpecScreen());
		wf.addObservers(match, true);
	}

	// Method to accept/reject game in the database
	public void acceptRejectGame(int competitionID, int gameID, String name,
			String string) {
		dbh.acceptRejectGame(competitionID, gameID, name, string);
	}

	// Method to start a new game
	public void challengePlayer(int competitionID, String username,
			String opponent, String language) {
		String privacy = "prive";

		int reply1 = JOptionPane.showConfirmDialog(null, "Want to invite "
				+ opponent + " for a game?", "Game invite",
				JOptionPane.YES_NO_OPTION);
		if (!dbh.inviteExists(username, opponent, competitionID)) {
			if (reply1 == JOptionPane.YES_OPTION) {
				int reply2 = JOptionPane.showConfirmDialog(null,
						"Want a public game?", "Public game",
						JOptionPane.YES_NO_OPTION);
				if (reply2 == JOptionPane.YES_OPTION) {
					privacy = "openbaar";
				}

				int gameID = dbh.createGame(competitionID, username, opponent,
						privacy, language);
				System.out.println("MatchMananger - GameID " + gameID
						+ " is aangemaakt!");
				// int reply3 = JOptionPane.showConfirmDialog(null,
				// "Want to load the game?", "Load game",
				// JOptionPane.YES_NO_OPTION);
				// if (reply3 == JOptionPane.YES_OPTION) {
				// wf.startGame(gameID, false, true);
				// }
			}
		} else {
			JOptionPane.showMessageDialog(null,
					"There is already a open invite for this game",
					"Game active", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	public synchronized String getName(){
		return wf.getCurrentUsername();
	}
}
