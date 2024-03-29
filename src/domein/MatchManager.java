package domein;

import gui.GameButtonPanel;
import gui.GameChatPanel;
import gui.GameFieldPanel;
import gui.GameScreen;
import gui.GameSpecScreen;
import gui.ScorePanel;

import java.util.ArrayList;

import javax.swing.JOptionPane;
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

		if (!wordFeud.getContent()) {
			match = new Match(gameID, wordFeud.getCurrentUsername());
			match.startNewGame();
			wordFeud.updatePlayerGameList();
		}
	}

	// Loads a match
	public void loadMatch(int gameID) {
		if (DatabaseHandler.getInstance().gameInitialized(gameID)){

			if (DatabaseHandler.getInstance().getGameStatusValue(gameID)
					.equals("Finished")
					|| DatabaseHandler.getInstance().getGameStatusValue(gameID)
							.equals("Resigned")) {

				JOptionPane.showMessageDialog(null, "This game is over, please refresh the list",
						"Refresh list", JOptionPane.INFORMATION_MESSAGE);
			} else {	
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				match = new Match(gameID, wordFeud.getUserPlayer(), gameField,
						wordFeud.getCurrentUsername());

				wordFeud.addObservers(match, false);
				match.loadGame();

				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						buttonPanel.setTurn(false);
						// buttonPanel.disableSurrender();
						chatPanel.setChatVariables(match.getOwnName(),
								match.getGameID());
						gameThread.setRunning(match);
					}
				});
			}
		} else {
			if (DatabaseHandler.getInstance().gameOwner(wordFeud.getCurrentUsername(), gameID)){
				newMatchStartedByMe(gameID);
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				match = new Match(gameID, wordFeud.getUserPlayer(), gameField,
						wordFeud.getCurrentUsername());

				wordFeud.addObservers(match, false);
				match.loadGame();

				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						buttonPanel.setTurn(false);
						// buttonPanel.disableSurrender();
						chatPanel.setChatVariables(match.getOwnName(),
								match.getGameID());
						gameThread.setRunning(match);
					}
				});
				
			} else {
			JOptionPane.showMessageDialog(null, "This game is not inintialized",
					"Notice", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

	// A method start spectating
	public void spectateMatch(int gameID) {
		System.out.println("GAMEID " + gameID);
		match = new Match(gameID);
		wordFeud.addObservers(match, true);
		match.loadSpecateGame(specScreen);
	}

	// Method to accept/reject game in the database
	public void acceptRejectGame(int competitionID, int gameID, String name,
			String string) {
		DatabaseHandler.getInstance().acceptRejectGame(competitionID, gameID,
				name, string);
		if (string.equals("Accepted")){
			DatabaseHandler.getInstance().gameStatusUpdate(gameID, "Playing");
		}
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

	// Method to remove all active matches
	public void logout() {
		// TODO Auto-generated method stub
		matches.clear();
		activeMatches.clear();
		pendingMatches.clear();
		myActiveMatches.clear();
	}
}
