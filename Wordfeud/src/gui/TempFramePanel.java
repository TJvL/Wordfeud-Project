package gui;

import java.awt.Dimension;
import java.util.Observer;

import javax.swing.JFrame;

// A tempFram for crearting the playGame part
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
		//this.setPreferredSize(new Dimension(1050,640));
		this.setPreferredSize(new Dimension(1200,700));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.pack();	
		this.setVisible(true);
	}
	
	// Adds the observers to all the panels
	// Sets the right ContentPane
	public void addObservers(Observer observer, boolean spectator){
		if(spectator){
			this.setContentPane(specScreen); 
			this.pack();
			System.out.println("TEMPFRAMEPANEL - set content specScreen");
			specScreen.addObserverToObserverButtons(observer);	
		} else {
			this.setContentPane(gameScreen);	
			this.pack();
			gameScreen.addObservers(observer);
			System.out.println("TEMPFRAMEPANEL - set content gameScreen");
		}
	}
	
	// Gets a GameFieldPanel
	public GameFieldPanel getGameFieldPanel(){
		return gameScreen.getBoardPanel();
	}
	
	// Gets the gameScreen
	public GameScreen getGameScreen(){
		return gameScreen;
	}
	
	// Gets the specScreen
	public GameSpecScreen getSpecScreen(){
		return specScreen;
	}
}
