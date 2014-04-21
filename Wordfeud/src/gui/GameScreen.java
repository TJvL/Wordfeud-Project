package gui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;

import domein.Match;
import domein.SecondThread;

public class GameScreen extends JPanel{
	private Match match;
	
	public GameScreen(){
		//this.setLayout(new BorderLayout());
		this.setBackground(Color.white);
		this.setLayout(null);
		match = new Match();
		GameFieldPanel boardP = new GameFieldPanel(match);
		boardP.setBounds(15, 10, 500, 550);
		this.add(boardP);
		GameButtonPanel buttonP = new GameButtonPanel(match, boardP);
		buttonP.setBounds(15, 559, 500, 50);
		this.add(buttonP);
		
		// Second thread for running the chat funcion and who's turn it is
		//SecondThread secondThread = new SecondThread(match, boardP, buttonP);
		//secondThread.start();
	}
}