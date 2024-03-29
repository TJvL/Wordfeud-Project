package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import domein.ActiveMatch;

@SuppressWarnings("serial")
public class SpecScreen extends JPanel {
	private JScrollPane listScrollPane;
	private JScrollPane textAreaScrollPane;
	private JTextArea myTextArea;
	private JPanel spectatorScreenButtonPanel;
	private JPanel spectatorScreenListPanel;
	private JPanel spectatorScreenTextareaPanel;

	private JButton selectGame;
	private JList<ActiveMatch> activeGamesList;
	private ArrayList<ActiveMatch> activeGames;
	private MainFrame mainFrame;
	private int storeLastClickGameID;
	private int clickCount = 0;

	public SpecScreen(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		this.setLayout(new BorderLayout());
		this.setPreferredSize(this.getSize());
		myTextArea = new JTextArea();
		spectatorScreenButtonPanel = new JPanel();
		spectatorScreenTextareaPanel = new JPanel();
	}

	// Filling the list with games from then Database
	public synchronized void setGameList(ArrayList<ActiveMatch> arrayList) {
		this.activeGames = arrayList;
		ActiveMatch[] games = new ActiveMatch[activeGames.size()];
		for (int i = 0; i < activeGames.size(); i++) {
			games[i] = activeGames.get(i);
		}
		if (activeGamesList != null) {
			this.remove(activeGamesList);
			this.remove(spectatorScreenListPanel);
		}
		createGamesList(games);
		this.createSpectatorScreenListPanel();
		this.add(spectatorScreenListPanel, BorderLayout.CENTER);
	}

	// Creating the gui to show the list
	// Adding a actionlistener
	// When a game is pressed it will trigger the mainFram
	public synchronized void createGamesList(ActiveMatch[] games) {
		spectatorScreenListPanel = new JPanel();
		activeGamesList = new JList<ActiveMatch>(games);
		spectatorScreenListPanel.setLayout(new BoxLayout(
				spectatorScreenListPanel, BoxLayout.Y_AXIS));
		activeGamesList.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				ActiveMatch input = activeGamesList.getSelectedValue();
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
						mainFrame.startGame(gameID, true);
						clickCount = 0;
					}
				}
			}
		});

		// ///this panel contains our JList, where all the active games are
		// listed
		// spectatorScreenListPanel.setLayout(new BorderLayout());
		// spectatorScreenListPanel.setPreferredSize(this.getSize());
		// //

		// /this makes sure the text in the JList in centered
		DefaultListCellRenderer renderer = (DefaultListCellRenderer) activeGamesList
				.getCellRenderer();
		renderer.setHorizontalAlignment(JLabel.CENTER);
		//
		// activeGamesList.setCellRenderer(myAGLCR);
		// activeGamesList.setModel(myListModel);
		// //

		listScrollPane = new JScrollPane(activeGamesList);
		listScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		spectatorScreenListPanel.add(listScrollPane, BorderLayout.CENTER);
	}

	public void createSpectatorScreenListPanel() {

	}

	public void createSpectatorScreenButtonPanel() {

		selectGame.setText("Select a match to view it");
		spectatorScreenButtonPanel.add(selectGame);
		selectGame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String test = activeGamesList.getSelectedValue().toString();
				myTextArea.setText("You have selected the following match:  "
						+ test);

			}
		});

	}

	public void createSpectatorScreenTextAreaPanel() {
		this.setLayout(new BorderLayout());
		textAreaScrollPane = new JScrollPane(myTextArea);
		myTextArea.setWrapStyleWord(true);
		myTextArea.setLineWrap(true);

		textAreaScrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		textAreaScrollPane.setPreferredSize(new Dimension(500, 1200));
		spectatorScreenTextareaPanel.add(textAreaScrollPane,
				BorderLayout.CENTER);

	}
}
