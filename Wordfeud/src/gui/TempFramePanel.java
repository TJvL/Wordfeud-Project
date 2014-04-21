package gui;

import java.awt.Dimension;

import javax.swing.JFrame;

import domein.Match;

public class TempFramePanel extends JFrame {

	// TempFrame moet vervangen worden
	// Er moet ook nog een panel (gamePanel)
	// GamePanel moet een Match meegeven aan BoardPanel
	
	
	public TempFramePanel() {
		this.setResizable(false);
		this.setPreferredSize(new Dimension(1200,700));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		GameScreen gameScreen = new GameScreen();
		this.add(gameScreen);
		this.pack();	
		this.setVisible(true);
	}
}
