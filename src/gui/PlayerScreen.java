package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import domein.ActiveMatch;

@SuppressWarnings("serial")
public class PlayerScreen extends JPanel {
	private MainFrame mainFrame;
	private ArrayList<ActiveMatch> activeGames;
	private JList<ActiveMatch> gameList;
	private JPanel listPanel;
	private JPanel buttonsPanel;

	private int storeLastClickGameID;
	private int clickCount = 0;
	public PlayerScreen(MainFrame mainFrame) {
		
		this.mainFrame = mainFrame;
		this.setLayout(new BorderLayout());
		createButtons();

		this.add(buttonsPanel, BorderLayout.EAST);
	}

	// Filling the list with games from then Database
	public synchronized void setGameList(ArrayList<ActiveMatch> matches, String userName) {
		this.activeGames = matches;
		ActiveMatch[] games = new ActiveMatch[activeGames.size()];
		for (int i = 0; i < activeGames.size(); i++) {
			games[i] = activeGames.get(i);
		}
		
		if (gameList != null) {
			this.remove(gameList);
			this.remove(listPanel);
		}
		createGamesList(games);
		this.add(listPanel, BorderLayout.WEST);
	}

	// Creating the gui to show the list
	// Adding a actionlistener
	// When a game is pressed it will trigger the mainFrame
	private synchronized void createGamesList(ActiveMatch[] games) {
		listPanel = new JPanel();
		gameList = new JList<ActiveMatch>(games);
		listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
		
		gameList.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				ActiveMatch input = gameList.getSelectedValue();
				if (input != null) {
					int gameID = input.getGameID();
					if (gameID != storeLastClickGameID) {
						clickCount = 0;
						storeLastClickGameID = gameID;
					} else {
						storeLastClickGameID = gameID;
					}
					clickCount++;
					if (clickCount == 2) {
						mainFrame.startGame(gameID, false);
						clickCount = 0;
					}
				}
			}
		});
		JScrollPane scrollPane = new JScrollPane(gameList);
		JLabel label = new JLabel("Active games:");

		scrollPane.setPreferredSize(new Dimension(800, 300));

		scrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		listPanel.add(label);
		listPanel.add(scrollPane);
		this.repaint();
		this.revalidate();
	}

	private void createButtons() {
		buttonsPanel = new JPanel();
		JPanel composedButtons = new JPanel();

		JButton createCompButton = new JButton("Create competition");
		JButton openGameButton = new JButton("Open game");
		JButton joinCompButton = new JButton("Join Competition");
		JButton joinedCompButton = new JButton("Joined competitions");

		composedButtons.setLayout(new GridLayout(4, 1, 0, 50));

		buttonsPanel.setLayout(new BorderLayout());
		buttonsPanel.setPreferredSize(new Dimension(300, 300));

		openGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mainFrame.setGameScreen();
			}
		});
		joinedCompButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mainFrame.setJoinedCompScreen();
			}
		});
		joinCompButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainFrame.setJoinCompScreen();
			}
		});
		createCompButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showCreateCompWindow();
			}
		});

		composedButtons.add(createCompButton);
		composedButtons.add(openGameButton);
		composedButtons.add(joinedCompButton);
		composedButtons.add(joinCompButton);

		buttonsPanel.add(composedButtons, BorderLayout.WEST);
	}
	
	public void showCreateCompWindow(){
		new CreateCompWindow(this);
	}
	
	public void callCreateComp(String summary, String compEnd, int i, int maxPlayersInt){
		mainFrame.callCreateCompAction(summary, compEnd, i, maxPlayersInt);
	}
}
