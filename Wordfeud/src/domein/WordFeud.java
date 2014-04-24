package domein;

import gui.TempFramePanel;

import java.util.ArrayList;

public class WordFeud {
	private User user;
	private Match match;
	private ArrayList<Match> matches = new ArrayList<Match>();

	// Has to be changed to the correct FramePanel
	private TempFramePanel framePanel;

	public WordFeud(){
		user = new User();
		match = new Match(this.getUserPlayer());
		// De gebruiker laden vanaf de database
		framePanel = new TempFramePanel();
		framePanel.setMatch(match);
		
		newMatch();
	}

	public Player getUserPlayer() {
		return user.getPlayer();
	}

	public void newMatch(){
		match.startNewGame(framePanel.getGameFieldPanel());
	}
}
