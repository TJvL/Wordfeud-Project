package gui;

import java.awt.Dimension;

import javax.swing.JFrame;

import domein.Match;

public class TempFramePanel extends JFrame {

	// TempFrame moet vervangen worden
	// Er moet ook nog een panel (gamePanel)
	// GamePanel moet een Match meegeven aan BoardPanel
	private GameScreen gameScreen;
	private GameSpecScreen specScreen;
	
	public TempFramePanel() {
		gameScreen = new GameScreen();
		specScreen = new GameSpecScreen();
		this.setResizable(false);
		// Deze waardes waaren 1200 bij 700
		this.setPreferredSize(new Dimension(1050,640));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//this.setContentPane(gameScreen);
		this.setContentPane(specScreen);
		//this.add(gameScreen);
		this.pack();	
		this.setVisible(true);
	}
	
	public void setMatch(Match match){
		gameScreen.setMatch(match);
	}
	
	public GameFieldPanel getGameFieldPanel(){
		return gameScreen.getGameFieldPanel(true);
	}
	
	// NIEUW //
	public void setMatchSpecScreen(Match match){
		specScreen.setMatch(match);
	}
	
	public GameSpecScreen getSpecScreen(){
		return specScreen;
	}
}
