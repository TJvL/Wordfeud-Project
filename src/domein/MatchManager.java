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
import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;
import datalaag.DatabaseHandler;
import datalaag.WordFeudConstants;

public class MatchManager {
	public static final int PUBLIC_GAME = 0;
	public static final int PRIVATE_GAME = 1;
	public static final String CHALLENGE_FAIL_EXISTS = "There is already an open invite for this game";
	public static final String CHALLENGE_FAIL_CLOSED = "Competition is closed Are there enough participants yet? Also check the end date.";
	public static final String CHALLENGE_SUCCES = "Succesfully challenged player and Invite has been sent!";

	private DatabaseHandler dbh;
	private Match match;
	private ArrayList<Match> matches;
	private HashMap<String, PendingMatch> pendingMatches;
	private ArrayList<ActiveMatch> activeMatches;
	private ArrayList<ActiveMatch> myActiveMatches;
	private WordFeud wordFeud;

	private GameThread gameThread;
	private GameChatPanel chatPanel;
	private GameButtonPanel buttonPanel;
	private GameFieldPanel gameField;
	private ScorePanel scorePanel;
	private GameScreen gameScreen;
	private GameSpecScreen specScreen;

	public MatchManager(final WordFeud wordFeud) {
		this.dbh = DatabaseHandler.getInstance();
		this.wordFeud = wordFeud;

		this.matches = new ArrayList<Match>();
		this.pendingMatches = new HashMap<String, PendingMatch>();
		this.activeMatches = new ArrayList<ActiveMatch>();
		this.myActiveMatches = new ArrayList<ActiveMatch>();

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				gameScreen = wordFeud.getGameScreen();
				gameField = gameScreen.getGameFieldPanel();
				chatPanel = gameScreen.getGameChatPanel();
				buttonPanel = gameScreen.getButtonPanel();
				scorePanel = gameScreen.getScorePanel();
				specScreen = wordFeud.getSpecScreen();
				initializeThread();
			}
		});
	}

	public Set<Entry<String, PendingMatch>> getPendingMatches() {
		return pendingMatches.entrySet();
	}

	public void loadPendingMatches(String currentUsername) {
		if (pendingMatches != null) {
			pendingMatches.clear();
		}
		ArrayList<String> games = DatabaseHandler.getInstance().pendingGames(
				currentUsername);
		for (String game : games) {
			String[] split = game.split(",");
			boolean ownGame = false;
			if (split[1].equals("true")) {
				ownGame = true;
			}
			System.out.println(split[0] + " - " + split[1] + " - " + split[2]);
			pendingMatches.put(split[0],
					new PendingMatch(Integer.parseInt(split[0]), split[2],
							ownGame));
		}
	}

	public ArrayList<ActiveMatch> getActiveMatches() {
		if (activeMatches != null) {
			activeMatches.clear();
		}
		ArrayList<String> games = DatabaseHandler.getInstance().spectateList();
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
		ArrayList<String> games = DatabaseHandler.getInstance().activeGames(
				wordFeud.getCurrentUsername());
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
				this);
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
		match = new Match(gameID, wordFeud.getCurrentUsername());
		match.startNewGame();
		wordFeud.updatePlayerGameList();
	}

	// Loads a match
	public void loadMatch(int gameID) {
		boolean exists = false;
		// Checks if the refrences already exist
		for (Match match1 : matches) {
			if (match.getGameID() == gameID) {
				exists = true;
				// Adds the observers
				wordFeud.addObservers(match, false);
				chatPanel.setChatVariables(match.getOwnName(),
						match.getGameID());
				this.match = match1;
				// SwingUtilities.invokeLater(new Runnable() {
				// @Override
				// public void run() {
				// match1.loadGame();
				// }
				// });
				match.loadGame();
				gameThread.setRunning(match);
			}
		}
		if (!exists) {
			match = new Match(gameID, wordFeud.getUserPlayer(), gameField,
					wordFeud.getCurrentUsername());

			wordFeud.addObservers(match, false);
			// SwingUtilities.invokeLater(new Runnable() {
			// @Override
			// public void run() {
			// match1.loadGame();
			// }
			// });

			match.loadGame();
			chatPanel.setChatVariables(match.getOwnName(), match.getGameID());
			matches.add(match);
			gameThread.setRunning(match);
		}

	}

	// A method start spectating
	public void spectateMatch(int gameID) {
		System.out.println("GAMEID " + gameID);
		match = new Match(gameID);
		match.loadSpecateGame(specScreen);
		wordFeud.addObservers(match, true);

	}

	// Method to accept/reject game in the database
	public void acceptRejectGame(int competitionID, int gameID, String name,
			String string) {
		DatabaseHandler.getInstance().acceptRejectGame(competitionID, gameID,
				name, string);
	}

	// Method to start a new game
	public String challengePlayer(String competitionID, String username,
			String opponent, String language, int privacyInt) {

		String retValue;
		if (wordFeud.doGetOneCompetitionAction(competitionID)
				.canStartChallenging()) {
			if (!DatabaseHandler.getInstance().inviteExists(username, opponent,
					Integer.parseInt(competitionID))) {
				String privacy = "openbaar";
				if (privacyInt == WordFeudConstants.PRIVATE_GAME) {
					privacy = "prive";
				}
				int gameID = DatabaseHandler.getInstance().createGame(
						Integer.parseInt(competitionID), username, opponent,
						privacy, language);
				System.out.println("MatchManager reports: GameID: " + gameID
						+ " match has been logged and invite has been sent!");
				retValue = WordFeudConstants.CHALLENGE_SUCCES;
			} else {
				retValue = WordFeudConstants.CHALLENGE_FAIL_EXISTS;
			}
		} else {
			retValue = WordFeudConstants.CHALLENGE_FAIL_CLOSED;
		}
		return retValue;
	}

	public synchronized String getName() {
		return wordFeud.getCurrentUsername();
	}

	public boolean askMatchOwnership(String matchID) {
		return pendingMatches.get(matchID).isOwnGame();
	}
}
