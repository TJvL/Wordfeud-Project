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
	private HashMap<String, ActiveMatch> activeMatches;
	private HashMap<String, ActiveMatch> myActiveMatches;
	private WordFeud wordFeud;

	public MatchManager(WordFeud wordFeud) {
		this.wordFeud = wordFeud;
		this.matches = new HashMap<String, Match>();
		this.pendingMatches = new HashMap<String, PendingMatch>();
		this.activeMatches = new HashMap<String, ActiveMatch>();
		this.myActiveMatches = new HashMap<String, ActiveMatch>();
	}

	public Set<Entry<String, PendingMatch>> getPendingMatches() {
		return pendingMatches.entrySet();
	}
	
	public Set<Entry<String, ActiveMatch>> getActiveMatches() {
		return activeMatches.entrySet();
	}
	
	public Set<Entry<String, ActiveMatch>> getMyActiveMatches() {
		return myActiveMatches.entrySet();
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
	
	public void loadAllActiveMatches(){
		if (activeMatches != null) {
			activeMatches.clear();
		}
		ArrayList<String> games = DatabaseHandler.getInstance().spectateList();
		for (String game : games) {
			String[] split = game.split(",");
			activeMatches.put(split[0], new ActiveMatch(Integer.parseInt(split[0]),
					split[1]));
		}
	}
	
	public void loadMyActiveMatches(){
		this.createNewMatches();
		
		if (myActiveMatches != null) {
			myActiveMatches.clear();
		}
		ArrayList<String> games = DatabaseHandler.getInstance().activeGames(
				wordFeud.getCurrentUsername());
		for (String game : games) {
			String[] split = game.split(",");
			myActiveMatches.put(split[0], new ActiveMatch(Integer.parseInt(split[0]),
					split[1]));
		}
	}

	// Depends if someone is spectating - starting new game
	// or want to load a game
	public synchronized Match startGame(int gameID, boolean spectate, boolean newGame) {
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
	
	private void createNewMatches(){
		// Method to start games after both players accepted
		ArrayList<Integer> gamesToLoad = DatabaseHandler.getInstance().gameToLoad(wordFeud.getCurrentUsername());
		if (gamesToLoad != null) {
			for (Integer loadID : gamesToLoad) {
//				Match match = matchManager.startGame(loadID, false, true);
				this.newMatchStartedByMe(loadID);
			}
		}
	}

	// This starts a Match that is made by me
	public void newMatchStartedByMe(int gameID) {
		Match.startNewGame(gameID);
	}

	// Loads a match
	public Match loadMatch(int gameID) {
		// Checks if the reference already exist
		if (matches.containsKey(Integer.toString(gameID))) {
			// Adds the observers
			Match match = matches.get(gameID);
			wordFeud.addObservers(match, false);
//			chatPanel.setChatVariables(match.getOwnName(), gameID);
			match.loadGame();
//			gameThread.setRunning(match);
			return match;
		} else {
			Match match = new Match(gameID, wordFeud.getUserPlayer(), wordFeud
					.getGameScreen().getGameFieldPanel(),
					wordFeud.getCurrentUsername());
			// Adds the observers
			wordFeud.addObservers(match, false);
			match.loadGame();
//			chatPanel.setChatVariables(match.getOwnName(), gameID);
			matches.put(Integer.toString(gameID), match);
//			gameThread.setRunning(match);
			return match;
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

	public void logout() {
		matches.clear();
		pendingMatches.clear();
		activeMatches.clear();
		myActiveMatches.clear();
	}
}
