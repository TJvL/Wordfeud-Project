package domein;

import gui.GameButtonPanel;
import gui.GameChatPanel;
import gui.GameFieldPanel;
import gui.GameScreen;
import gui.GameSpecScreen;
import gui.MainFrame;
import gui.ScorePanel;

import java.util.ArrayList;

import javax.swing.SwingUtilities;

import datalaag.DatabaseHandler;

public class MatchManager {
	public static final int PUBLIC_GAME = 0;
	public static final int PRIVATE_GAME = 1;
	public static final String CHALLENGE_FAIL_EXISTS = "There is already an open invite for this game";
	public static final String CHALLENGE_FAIL_CLOSED = "Competition is closed Are there enough participants yet? Also check the end date.";
	public static final String CHALLENGE_SUCCES = "Succesfully challenged player and Invite has been sent!";

	private Match match1;
	private DatabaseHandler dbh;
	private ArrayList<Match> matches;
	private ArrayList<PendingMatch> pendingMatchs;
	private ArrayList<ActiveMatch> activeMatches;
	private ArrayList<ActiveMatch> myActiveMatches;
	private WordFeud wf;
	private MainFrame framePanel;
	private GameThread gameThread;
	private GameChatPanel chatPanel;
	private GameButtonPanel buttonPanel;
	private GameFieldPanel gameField;
	private ScorePanel scorePanel;
	private GameScreen gameScreen;
	private GameSpecScreen specScreen;

	public MatchManager(WordFeud wf, final MainFrame framePanel) {
		this.dbh = DatabaseHandler.getInstance();
		this.matches = new ArrayList<Match>();
		this.pendingMatchs = new ArrayList<PendingMatch>();
		this.activeMatches = new ArrayList<ActiveMatch>();
		this.myActiveMatches = new ArrayList<ActiveMatch>();
		this.wf = wf;
		this.framePanel = framePanel;

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				gameScreen = framePanel.getGameScreen();
				gameField = gameScreen.getGameFieldPanel();
				chatPanel = gameScreen.getGameChatPanel();
				buttonPanel = gameScreen.getButtonPanel();
				scorePanel = gameScreen.getScorePanel();
				specScreen = framePanel.getSpecScreen();
				initializeThread();
			}
		});
		
	}

	public ArrayList<PendingMatch> getPendingMatchs() {
		if (pendingMatchs != null) {
			pendingMatchs.clear();
		}
		ArrayList<String> games = dbh.pendingGames(wf.getCurrentUsername());
		for (String game : games) {
			String[] split = game.split(",");
			boolean ownGame = false;
			if (split[1].equals("true")) {
				ownGame = true;
			}
			pendingMatchs.add(new PendingMatch(Integer.parseInt(split[0]),
					split[2], ownGame));
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
		this.gameThread = new GameThread(chatPanel, buttonPanel, scorePanel,
				this, framePanel);
		gameThread.start();
	}

	// Stops the Thread
	public void stopThread() {
		gameThread.stopRunning();
	}

	// Depends if someone is spectating - starting new game
	// or want to load a game
	public synchronized void startGame(int gameID, boolean spectate,
			boolean newGame) {
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
		match1 = new Match(gameID, wf.getCurrentUsername());
		match1.startNewGame();
		framePanel.updatePlayerGameList();
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
				chatPanel.setChatVariables(match.getOwnName(),
						match.getGameID());
				this.match1 = match;
//				SwingUtilities.invokeLater(new Runnable() {
//					@Override
//					public void run() {
//						match1.loadGame();
//					}
//				});
				match.loadGame();
				gameThread.setRunning(match);
			}
		}
		if (!exists) {
			match1 = new Match(gameID, wf.getUserPlayer(), gameField,
					wf.getCurrentUsername());
			// Adds the observers
			wf.addObservers(match1, false);
//			SwingUtilities.invokeLater(new Runnable() {
//				@Override
//				public void run() {
//					match1.loadGame();
//				}
//			});
			match1.loadGame();
			chatPanel.setChatVariables(match1.getOwnName(), match1.getGameID());
			matches.add(match1);
			gameThread.setRunning(match1);
		}

	}

	// A method start spectating
	public void spectateMatch(int gameID) {
		System.out.println("GAMEID " + gameID);
		match1 = new Match(gameID);
		match1.loadSpecateGame(specScreen);
		wf.addObservers(match1, true);
	}

	// Method to accept/reject game in the database
	public void acceptRejectGame(int competitionID, int gameID, String name,
			String string) {
		dbh.acceptRejectGame(competitionID, gameID, name, string);
	}

	// Method to start a new game
	public String challengePlayer(String competitionID, String username,
			String opponent, String language, int privacyInt) {

		String retValue;
		if (wf.doGetOneCompetitionAction(competitionID).canStartChallenging()) {
			if (!dbh.inviteExists(username, opponent,
					Integer.parseInt(competitionID))) {
				String privacy = "openbaar";
				if (privacyInt == MatchManager.PRIVATE_GAME) {
					privacy = "prive";
				}
				int gameID = dbh.createGame(Integer.parseInt(competitionID),
						username, opponent, privacy, language);
				System.out.println("MatchManager reports: GameID: " + gameID
						+ " match has been logged and invite has been sent!");
				framePanel.updateNotificationList();
				retValue = MatchManager.CHALLENGE_SUCCES;
			} else {
				retValue = MatchManager.CHALLENGE_FAIL_EXISTS;
			}
		} else {
			retValue = MatchManager.CHALLENGE_FAIL_CLOSED;
		}
		return retValue;
	}

	public synchronized String getName() {
		return wf.getCurrentUsername();
	}
}
