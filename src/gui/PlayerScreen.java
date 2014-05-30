package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import domein.ActiveMatch;
import domein.Competition;

@SuppressWarnings("serial")
public class PlayerScreen extends JPanel {
	private final String defaultSelection = "default";
	
	private MainFrame mainFrame;
	private ArrayList<ActiveMatch> activeGames;
	private JList<ActiveMatch> gameList;
	private JPanel listPanel;
	private JPanel buttonsPanel;
	private JTable matchTable;
	private JScrollPane matchPane;
	private String currentSelection;
	private JButton createCompButton;
	private JButton refreshList;
	private JButton enterGameButton;
	private JButton joinCompButton;
	private JButton joinedCompButton;

	private int storeLastClickGameID;
	private int clickCount = 0;
	
	public PlayerScreen(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		buttonsPanel = new JPanel();
		this.setLayout(null);
		buttonsPanel.setLayout(new GridLayout(8, 1, 0, 15));
		createButtons();
		
		currentSelection = defaultSelection;
	}
	
	public void populateScreen(){
		refreshList();
		
		buttonsPanel.setBounds(900, 0, 300, 700);

		this.add(buttonsPanel);
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

		createCompButton = new JButton("Create competition");
		refreshList = new JButton("Refresh list");
		enterGameButton = new JButton("Enter game");
		joinCompButton = new JButton("Join Competition");
		joinedCompButton = new JButton("Joined competitions");
		
		buttonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

		refreshList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshList();
			}
		});
		joinedCompButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				joinedCompButtonPressed();
			}
		});
		joinCompButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				joinCompButtonPressed();
			}
		});
		createCompButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createCompButtonPressed();
			}
		});
		enterGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});

		buttonsPanel.add(createCompButton);
		buttonsPanel.add(refreshList);
		buttonsPanel.add(enterGameButton);
		buttonsPanel.add(joinedCompButton);
		buttonsPanel.add(joinCompButton);
	}
	
	private void initMatchTable(){
		if (matchPane != null) {
			this.remove(matchPane);
		}
		matchTable = null;
		matchPane = null;
		
		String[] columnNames = { "ID", "Summary", "Max Parts", "Current Parts",
				"Owner", "End Date/Time" };
		Set<Entry<String, ActiveMatch>> activeMatches = mainFrame
				.callGetAllActiveMatchesAction();
		String[][] tableData = new String[activeMatches.size()][6];

		Iterator<Entry<String, ActiveMatch>> it = activeMatches.iterator();
		int i = 0;
		while (it.hasNext()) {
			Map.Entry<String, ActiveMatch> pair = (Map.Entry<String, ActiveMatch>) it
					.next();
			tableData[i][0] = 
			tableData[i][1] = 
			tableData[i][2] = 
			tableData[i][3] = 
			tableData[i][4] = 
			tableData[i][5] = 
			i++;
		}
	}
	
	private void refreshList(){
		this.initMatchTable();
		matchPane.setBounds(10, 10, 980, 680);
		this.add(matchPane);
		this.revalidate();
	}
	
	private void createCompButtonPressed(){
		new CreateCompWindow(mainFrame);
	}
	
	private void joinCompButtonPressed(){
		mainFrame.setJoinCompScreen();
	}
	
	private void joinedCompButtonPressed(){
		mainFrame.setJoinedCompScreen();
	}
}
