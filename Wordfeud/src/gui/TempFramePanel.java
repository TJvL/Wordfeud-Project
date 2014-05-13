package gui;

import java.awt.Dimension;
import java.util.Observer;

import javax.swing.JFrame;


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
		//this.setContentPane(specScreen); ////////////////////////////////////------------------------
		//this.add(gameScreen);
		this.pack();	
		this.setVisible(true);
	}
	
	public void addObservers(Observer observer, boolean spectator){
		if(spectator){
			this.setContentPane(specScreen);
			specScreen.addObserverToObserverButtons(observer);	
		} else {
			this.setContentPane(gameScreen);
			gameScreen.addObservers(observer);
		}
	}
	
	public GameFieldPanel getGameFieldPanel(){
		return gameScreen.getGameFieldPanel(true);
	}
	
	public GameScreen getGameScreen(){
		return gameScreen;
	}
	
	public GameSpecScreen getSpecScreen(){
		return specScreen;
	}
}
