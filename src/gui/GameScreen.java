package gui;

import gui.GameButtonPanel.ObserverButtons;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GameScreen extends JPanel {
	private GameFieldPanel boardPanel;
	private GameButtonPanel buttonPanel;
	private ScorePanel scorePanel;
	private GameChatPanel chatPanel;
	private JButton back;
	private JButton refresh;
	private ObserverButtons observerButtons;

	public GameScreen(final MainFrame mainFrame) {
		// this.setLayout(new BorderLayout());
		this.setBackground(Color.DARK_GRAY);
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
		back = new JButton("Back to mainscreen");
		back.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				mainFrame.setPlayerScreen();	
			}});
		back.setBounds(780, 600, 250, 20);
		this.add(back);
		
		observerButtons = new ObserverButtons();
		refresh = new JButton("Refresh field");
		refresh.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				observerButtons.changeActionRequest("refresh");
			}});
		refresh.setBounds(530, 600, 250, 20);
		this.add(refresh);
		
	}
	
	// Adds the observers
	public void addObservers(Observer observer){
		buttonPanel.addObserverToObserverButtons(observer);
		boardPanel.addObserverToObserverButtons(observer);
		observerButtons.deleteObservers();
		observerButtons.addObserver(observer);
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
	

	// A class to be able to extend obserable for the buttons
	class ObserverButtons extends Observable {
		public void changeActionRequest(String actionRequest) {
			System.out.println("action requested: " + actionRequest);
			this.setChanged();
			this.notifyObservers(actionRequest);
		}
	}
}