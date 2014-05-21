package domein;

import gui.MainFrame;

import java.util.ArrayList;

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
	private SecondThread secondThread;

	public MatchManager(WordFeud wf, MainFrame framePanel) {
		this.dbh = DatabaseHandler.getInstance();
		this.matches = new ArrayList<Match>();
		this.pendingMatchs = new ArrayList<PendingMatch>();
		this.activeMatches = new ArrayList<ActiveMatch>();
		this.myActiveMatches = new ArrayList<ActiveMatch>();
		this.wf = wf;
		this.framePanel = framePanel;
	}

	public ArrayList<PendingMatch> getPendingMatchs() {
		if (pendingMatchs != null) {
			pendingMatchs.clear();
		}
		ArrayList<String> games = dbh.pendingGames(wf.getCurrentUsername());
		for (String game : games) {
			String[] split = game.split(",");
			pendingMatchs.add(new PendingMatch(Integer.parseInt(split[0]),
					Integer.parseInt(split[1]), split[2]));
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
			activeMatches.add(new ActiveMatch(Integer.parseInt(split[0]), split[1]));
		}
		return activeMatches;
	}

	public ArrayList<ActiveMatch> getMyActiveMatches(){
		if (myActiveMatches != null) {
			myActiveMatches.clear();
		}	
		ArrayList<String> games = dbh.activeGames(wf.getCurrentUsername());
		for (String game : games) {
			String[] split = game.split(",");
			myActiveMatches.add(new ActiveMatch(Integer.parseInt(split[0]), split[1]));
		}
		return myActiveMatches;
	}
	
	// A method to initialize the Thread
	public void initializeThread(Match match) {
		this.secondThread = new SecondThread(framePanel.getGameScreen()
				.getGameChatPanel(), framePanel.getGameScreen()
				.getButtonPanel(), framePanel.getGameScreen().getScorePanel(), match);
		secondThread.start();
	}

	// A method to start the Thread
	public void startThread(Match match) {
		initializeThread(match);
		secondThread.setRunning(true);
	}

	// Stops the Thread
	public void stopThread() {
		if (secondThread != null) {
			secondThread.setRunning(false);
		}
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
		match = new Match(gameID, wf.getUserPlayer(), framePanel
				.getGameScreen().getGameFieldPanel(), wf.getCurrentUsername());
		// Adds the observers
		wf.addObservers(match, false);
		// Sets the thread
		startThread(match);
		match.startNewGame();
		framePanel
				.getGameScreen()
				.getGameChatPanel()
				.setChatVariables(match.getOwnName(),
						match.getGameID());
		matches.add(match);
		secondThread.setRunning(true);
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
				startThread(match);
				framePanel
						.getGameScreen()
						.getGameChatPanel()
						.setChatVariables(match.getOwnName(), match.getGameID());
				match.loadGame();
				secondThread.setRunning(true);
			}
		}
		if (!exists) {
			match = new Match(gameID, wf.getUserPlayer(), framePanel
					.getGameScreen().getGameFieldPanel(),
					wf.getCurrentUsername());
			// Adds the observers
			wf.addObservers(match, false);
			startThread(match);
			match.loadGame();
			framePanel
					.getGameScreen()
					.getGameChatPanel()
					.setChatVariables(match.getOwnName(),
							match.getGameID());
			matches.add(match);
			secondThread.setRunning(true);
		}

	}

	// A method start spectating
	public void spectateMatch(int gameID) {
		match = new Match(gameID);
		match.loadSpecateGame(framePanel.getSpecScreen());
		wf.addObservers(match, true);
	}

	// Method to accept/reject game in the database
	public void acceptRejectGame(int competionID, int gameID, String name,
			String string) {
		dbh.acceptRejectGame(competionID, gameID, name, string);
	}

}
