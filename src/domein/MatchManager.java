package domein;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

import datalaag.DatabaseHandler;
import datalaag.WordFeudConstants;

public class MatchManager {
	private HashMap<String, Match> matches;
	private HashMap<String, PendingMatch> pendingMatches;
	private ArrayList<ActiveMatch> activeMatches;
	private ArrayList<ActiveMatch> myActiveMatches;
	private WordFeud wordFeud;

	public MatchManager(WordFeud wordFeud) {
		this.wordFeud = wordFeud;

		this.matches = new HashMap<String, Match>();
		this.pendingMatches = new HashMap<String, PendingMatch>();
		this.activeMatches = new ArrayList<ActiveMatch>();
		this.myActiveMatches = new ArrayList<ActiveMatch>();
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

	// Depends if someone is spectating - starting new game
	// or want to load a game
	public synchronized Match startGame(int gameID, boolean spectate,
			boolean newGame) {
		if (spectate) {
			spectateMatch(gameID);
			return null;
		} else {
			if (newGame) {
				newMatchStartedByMe(gameID);
				return null;
			} else {
				Match match = loadMatch(gameID);
				return match;
			}
		}
	}

	// This starts a Match that is made by me
	public void newMatchStartedByMe(int gameID) {
		Match match = new Match(gameID, wordFeud.getCurrentUsername());
		match.startNewGame();
		wordFeud.updatePlayerGameList();
	}

	// Loads a match
	public Match loadMatch(int gameID) {
		// Checks if the reference already exist
		if (matches.containsKey(Integer.toString(gameID))) {
			// Adds the observers
			Match match = matches.get(gameID);
			wordFeud.addObservers(match, false);
			// chatPanel.setChatVariables(match.getOwnName(), gameID);
			match.loadGame();
			// gameThread.setRunning(match);
			return match;
		} else {
			Match match = new Match(gameID, wordFeud.getUserPlayer(), wordFeud
					.getGameScreen().getGameFieldPanel(),
					wordFeud.getCurrentUsername());
			// Adds the observers
			wordFeud.addObservers(match, false);
			match.loadGame();
			// chatPanel.setChatVariables(match.getOwnName(), gameID);
			matches.put(Integer.toString(gameID), match);
			return match;
			// gameThread.setRunning(match);
		}

	}

	// A method start spectating
	public void spectateMatch(int gameID) {
		System.out.println("GAMEID " + gameID);
		Match match = new Match(gameID);
		match.loadSpecateGame(wordFeud.getSpecScreen());
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
