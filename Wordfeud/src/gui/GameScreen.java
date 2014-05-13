package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Observer;

import javax.swing.JPanel;

import domein.Match;
import domein.SecondThread;

public class GameScreen extends JPanel {
	private GameFieldPanel boardPanel;
	private GameButtonPanel buttonPanel;
	private ScorePanel scorePanel;
	private SecondThread secondThread;

	public GameScreen() {
		// this.setLayout(new BorderLayout());
		this.setBackground(Color.white);
		this.setLayout(null);
		boardPanel = new GameFieldPanel();
		boardPanel.setBounds(15, 10, 500, 550);
		this.add(boardPanel);
		buttonPanel = new GameButtonPanel(boardPanel);
		buttonPanel.setBounds(15, 565, 500, 35);
		this.add(buttonPanel);
		scorePanel = new ScorePanel(boardPanel);
		scorePanel.setBounds(530, 515, 500, 85);
		this.add(scorePanel);	
	}
	
	public void addObservers(Observer observer){
		buttonPanel.addObserverToObserverButtons(observer);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void setMatch(Match match) {
		// Second thread for running the chat funcion and who's turn it is 
		try {
		setThreadStatus(false);
		} catch (NullPointerException e){
			System.out.println("nullPointer - GameScreen");
		}
		secondThread = new SecondThread(match, boardPanel, buttonPanel, scorePanel);
		secondThread.start();
	}
	
	public GameFieldPanel getGameFieldPanel(boolean running) {
		setThreadStatus(running);
		return boardPanel;
	}
	
	public void setThreadStatus(boolean running){
		secondThread.setRunning(running);
	}
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public ScorePanel getScorePanel(){
		return scorePanel;
	}
	
	public GameButtonPanel getButtonPanel(){
		return buttonPanel;
	}
	
	public GameFieldPanel getBoardPanel(){
		return boardPanel;
	}
}