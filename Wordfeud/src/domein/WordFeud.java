package domein;

import gui.TempFramePanel;

import java.util.ArrayList;
import java.util.Observer;

import datalaag.DatabaseHandler;

public class WordFeud {
	private User user;
	private Match match;
	private ArrayList<Match> matches;
	private DatabaseHandler dbh;
	private SecondThread secondThread;

	// Has to be changed to the correct FramePanel
	private TempFramePanel framePanel;

	public WordFeud() {
		user = new User();
		match = new Match(0);
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
		this.addObservers(match,false);
		createSecondThread(match);
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
				createSecondThread(match);
				match.loadGame(framePanel.getGameFieldPanel());

			}
		}
		if (!exists) {
			// krijgen GameID wanneer wordfeud word aangeroepen
			match = new Match(gameID, getUserPlayer());
			createSecondThread(match);
			match.loadGame(framePanel.getGameFieldPanel());
		}

	}
	
	public void createSecondThread(Match match) {
		// Second thread for running the chat funcion and who's turn it is 
		try {
		setThreadStatus(false);
		} catch (NullPointerException e){
			System.out.println("nullPointer - WordFeud");
		}
		secondThread = new SecondThread(match, framePanel.getGameScreen().getBoardPanel(), framePanel.getGameScreen().getButtonPanel(), framePanel.getGameScreen().getScorePanel());
		secondThread.start();
	}
	
	public void setThreadStatus(boolean running){
		secondThread.setRunning(running);
	}

	public void spectateMatch(int gameID) {
		
		// NIEUW //
		match = new Match(gameID);
		match.loadSpecateGame(framePanel.getSpecScreen());
		this.addObservers(match, true);
	}
	
	public void addObservers(Observer observer, boolean spectator){
		framePanel.addObservers(observer, spectator);
	}
}
