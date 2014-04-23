package gui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;

import domein.Match;
import domein.SecondThread;

public class GameScreen extends JPanel {
	private GameFieldPanel boardPanel;
	private GameButtonPanel buttonPanel;

	public GameScreen(){
		//this.setLayout(new BorderLayout());
		this.setBackground(Color.white);
		this.setLayout(null);
		boardPanel = new GameFieldPanel();
		boardPanel.setBounds(15, 10, 500, 550);
		this.add(boardPanel);
		buttonPanel = new GameButtonPanel(boardPanel);
		buttonPanel.setBounds(15, 559, 500, 50);
		this.add(buttonPanel);
		
		// Second thread for running the chat funcion and who's turn it is
		//SecondThread secondThread = new SecondThread(match, boardP, buttonP);
		//secondThread.start();
	}

	public void setMatch(Match match) {
		buttonPanel.setMatch(match);
		boardPanel.setMatch(match);
	}
	
	public GameFieldPanel getGameFieldPanel(){
		return boardPanel;
	}
}