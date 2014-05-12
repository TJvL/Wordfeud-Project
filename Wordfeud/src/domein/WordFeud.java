package domein;

import gui.TempFramePanel;

import java.util.ArrayList;

import datalaag.DatabaseHandler;

public class WordFeud {
	private User user;
	private Match match;
	private ArrayList<Match> matches;
	private DatabaseHandler dbh;

	// Has to be changed to the correct FramePanel
	private TempFramePanel framePanel;

	public WordFeud() {
		user = new User();
		matches = new ArrayList<Match>();
		framePanel = new TempFramePanel();
		dbh = DatabaseHandler.getInstance();
	}

	public Player getUserPlayer() {
		return user.getPlayer();
	}

	// Depends if somein is spectating - starting new game
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

	public void newMatchStartedByMe(int gameID) {
		// Nieuwe gameID generen
		// Kijken of de gameID nog niet bestaat in de database
		// Zoniet - dan meegeven aan match
		// Wel - nieuwe generen etc.
		match = new Match(gameID, getUserPlayer());
		framePanel.setMatch(match);
		this.addObservers();
		match.startNewGame(framePanel.getGameFieldPanel());

		// hiermee kun je een game aanmaken - ik denk dat die moet gebeuren
		// voordat we hier komen
		// of alle instelling meegeven want die heb ik hier niet
		// dbh.createGame(int competitionID, String username, String opponent,
		// String privacy, String language);

		// Zetten van beurt naar mijzelf
		// De gene die het spel aanmaakt begint

		// Add the match to a ArrayList so alle games activated are stored
		matches.add(match);
	}

	public void loadMatch(int gameID) {
		boolean exists = false;
		// Loop door de arrayList
		for (Match match : matches) {
			if (match.getGameID() == gameID) {
				exists = true;
				match = new Match(gameID, getUserPlayer());
				framePanel.setMatch(match);
				match.loadGame(framePanel.getGameFieldPanel());

			}
		}
		if (!exists) {
			// krijgen GameID wanneer wordfeud word aangeroepen
			match = new Match(gameID, getUserPlayer());
			framePanel.setMatch(match);
			match.loadGame(framePanel.getGameFieldPanel());
		}

	}

	public void spectateMatch(int gameID) {
		
		// NIEUW //
		match = new Match(gameID);
		framePanel.setMatchSpecScreen(match);
		match.loadSpecateGame(framePanel.getSpecScreen());
	}
	
	public void addObservers(){
		framePanel.addObservers(match);
	}
}
