package domein;

import gui.TempFramePanel;

import java.util.ArrayList;

public class WordFeud {
	private User user;
	private Match match;
	private ArrayList<Match> matches;

	// Has to be changed to the correct FramePanel
	private TempFramePanel framePanel;

	public WordFeud(){
		user = new User();
		matches = new ArrayList<Match>();
		framePanel = new TempFramePanel();
	}

	public Player getUserPlayer() {
		return user.getPlayer();
	}
	
	// Depends if somein is spectating - starting new game
	// or want to load a game
	public void startGame(int gameID, boolean spectate){
		if (spectate){
			spectateMatch(gameID);
		} else {
			if (gameID == 0){
				newMatchStartedByMe();
			} else {
				loadMatch(gameID);
			}
		}
	}
	

	public void newMatchStartedByMe(){	
		// Nieuwe gameID generen
		// Kijken of de gameID nog niet bestaat in de database
		// Zoniet - dan meegeven aan match
		// Wel - nieuwe generen etc.
		match = new Match(12345, getUserPlayer());
		framePanel.setMatch(match);
		match.startNewGame(framePanel.getGameFieldPanel());	
		
		// Zetten van beurt naar mijzelf
		// De gene die het spel aanmaakt begint
		
		// Add the match to a ArrayList so alle games activated are stored
		matches.add(match);
	}
	
	public void loadMatch(int gameID){
		boolean exists = false;
		// Loop door de arrayList
		for (Match match: matches){
			if (match.getGameID() == gameID){
				exists = true;
				match = new Match(12345, getUserPlayer());
				framePanel.setMatch(match);
				match.loadGame(framePanel.getGameFieldPanel());	
			}
		}	
		if (!exists){
			// krijgen GameID wanneer wordfeud word aangeroepen
			match = new Match(12345, getUserPlayer());
			framePanel.setMatch(match);
			match.loadGame(framePanel.getGameFieldPanel());	
		}
		
		
	}
	
	public void spectateMatch(int gameID){
		match.loadSpecateGame();
	}
}
