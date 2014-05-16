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
	private GameChatPanel chatPanel;

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
		scorePanel = new ScorePanel();
		scorePanel.setBounds(530, 515, 500, 85);
		this.add(scorePanel);	
		chatPanel = new GameChatPanel();
		chatPanel.setBounds(530, 0, 500,500);
		this.add(chatPanel);
	}
	
	// Adds the observers
	public void addObservers(Observer observer){
		buttonPanel.addObserverToObserverButtons(observer);
	}

	// Returns the gameField
	public GameFieldPanel getGameFieldPanel() {
		return boardPanel;
	}
	
	// Returns the scorePanel
	public ScorePanel getScorePanel(){
		return scorePanel;
	}
	
	// Returns the buttonPanel
	public GameButtonPanel getButtonPanel(){
		return buttonPanel;
	}
	
	// Returns the gameScreen
	public GameScreen getGameScreen(){
		return this;
	}
	
	public GameChatPanel getGameChatPanel(){
		return chatPanel;
	}
}