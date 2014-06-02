package gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import domein.ActiveMatch;

@SuppressWarnings("serial")
public class SpecScreen extends JPanel {
	private final String defaultSelection = "default";

	private JPanel buttonsPanel;
	private JTable matchTable;
	private JScrollPane matchPane;
	private MainFrame mainFrame;
	private String currentSelection;
	private JButton spectateButton;
	private JButton refreshListButton;

	// private JButton selectGame;
	// private JList<ActiveMatch> activeGamesList;
	// private ArrayList<ActiveMatch> activeGames;
	// private int storeLastClickGameID;
	// private int clickCount = 0;
	// private JScrollPane listScrollPane;
	// private JScrollPane textAreaScrollPane;
	// private JTextArea myTextArea;
	// private JPanel spectatorScreenButtonPanel;
	// private JPanel spectatorScreenListPanel;
	// private JPanel spectatorScreenTextareaPanel;

	public SpecScreen(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		buttonsPanel = new JPanel();
		this.setLayout(null);
		buttonsPanel.setLayout(new GridLayout(8, 1, 0, 15));
		createButtonsPanel();

		currentSelection = defaultSelection;

		// myTextArea = new JTextArea();
		// spectatorScreenButtonPanel = new JPanel();
		// spectatorScreenTextareaPanel = new JPanel();
	}

	public void populateScreen() {
		this.removeAll();

		this.initMatchTable();

		matchPane.setBounds(190, 20, 400, 600);
		buttonsPanel.setBounds(610, 15, 300, 680);

		this.add(buttonsPanel);
		this.add(matchPane);
		mainFrame.revalidate();
	}

	private void createButtonsPanel() {
		spectateButton = new JButton("Spectate");
		refreshListButton = new JButton("Refresh");
		
		spectateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				spectateButtonPressed();
			}
		});
		refreshListButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				refreshButtonPressed();
			}
		});
		
		buttonsPanel.add(spectateButton);
		buttonsPanel.add(refreshListButton);
	}

	private void initMatchTable() {
		matchTable = null;
		matchPane = null;

		String[] columnNames = { "ID", "Description" };
		Set<Entry<String, ActiveMatch>> activeMatches = mainFrame
				.callGetAllActiveMatchesAction();
		String[][] tableData = new String[activeMatches.size()][6];

		Iterator<Entry<String, ActiveMatch>> it = activeMatches.iterator();
		int i = 0;
		while (it.hasNext()) {
			Map.Entry<String, ActiveMatch> pair = (Map.Entry<String, ActiveMatch>) it
					.next();
			tableData[i][0] = Integer.toString(pair.getValue().getGameID());
			tableData[i][1] = pair.getValue().getDiscription();
			i++;
		}

		matchTable = new JTable(tableData, columnNames) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		matchTable.getTableHeader().setResizingAllowed(false);
		matchTable.getTableHeader().setReorderingAllowed(false);
		matchTable.setColumnSelectionAllowed(false);
		matchTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		matchTable.getColumnModel().getColumn(0).setPreferredWidth(30);
		matchTable.getColumnModel().getColumn(1).setPreferredWidth(370);

		ForcedListSelectionModel selectModel = new ForcedListSelectionModel();
		selectModel.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) {
					currentSelection = matchTable.getValueAt(
							matchTable.getSelectedRow(), 0).toString();
				}
			}
		});
		matchTable.setSelectionModel(selectModel);
		matchTable.changeSelection(0, 0, false, false);
		if (matchTable.getRowCount() > 0) {
			currentSelection = matchTable.getValueAt(
					matchTable.getSelectedRow(), 0).toString();
		}
		matchPane = new JScrollPane(matchTable);
	}

	private void spectateButtonPressed() {
		mainFrame.startGame(Integer.parseInt(currentSelection), true);
	}

	private void refreshButtonPressed() {
		mainFrame.startAllActiveMatchesWorker();
	}
}

// // Filling the list with games from then Database
// public synchronized void setGameList(ArrayList<ActiveMatch> arrayList) {
// this.activeGames = arrayList;
// ActiveMatch[] games = new ActiveMatch[activeGames.size()];
// for (int i = 0; i < activeGames.size(); i++) {
// games[i] = activeGames.get(i);
// }
// if (activeGamesList != null) {
// this.remove(activeGamesList);
// this.remove(spectatorScreenListPanel);
// }
// createGamesList(games);
// this.createSpectatorScreenListPanel();
// this.add(spectatorScreenListPanel, BorderLayout.CENTER);
// }
//
// // Creating the gui to show the list
// // Adding a actionlistener
// // When a game is pressed it will trigger the mainFram
// public synchronized void createGamesList(ActiveMatch[] games) {
// spectatorScreenListPanel = new JPanel();
// activeGamesList = new JList<ActiveMatch>(games);
// spectatorScreenListPanel.setLayout(new BoxLayout(
// spectatorScreenListPanel, BoxLayout.Y_AXIS));
// activeGamesList.addMouseListener(new MouseAdapter() {
// @Override
// public void mousePressed(MouseEvent e) {
// ActiveMatch input = activeGamesList.getSelectedValue();
// if (input != null) {
// int gameID = input.getGameID();
// if (gameID != storeLastClickGameID) {
// clickCount = 0;
// storeLastClickGameID = gameID;
// } else {
// storeLastClickGameID = gameID;
// }
// clickCount++;
// if (clickCount == 2) {
//
// clickCount = 0;
// }
// }
// }
// });
//
// // ///this panel contains our JList, where all the active games are
// // listed
// // spectatorScreenListPanel.setLayout(new BorderLayout());
// // spectatorScreenListPanel.setPreferredSize(this.getSize());
// // //
//
// // /this makes sure the text in the JList in centered
// DefaultListCellRenderer renderer = (DefaultListCellRenderer) activeGamesList
// .getCellRenderer();
// renderer.setHorizontalAlignment(JLabel.CENTER);
// //
// // activeGamesList.setCellRenderer(myAGLCR);
// // activeGamesList.setModel(myListModel);
// // //
//
// listScrollPane = new JScrollPane(activeGamesList);
// listScrollPane
// .setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
// spectatorScreenListPanel.add(listScrollPane, BorderLayout.CENTER);
// }
//
//
//
// public void createSpectatorScreenButtonPanel() {
// selectGame.setText("Select a match to view it");
// spectatorScreenButtonPanel.add(selectGame);
// selectGame.addActionListener(new ActionListener() {
//
// @Override
// public void actionPerformed(ActionEvent e) {
// String test = activeGamesList.getSelectedValue().toString();
// myTextArea.setText("You have selected the following match:  "
// + test);
//
// }
// });
//
// }
//
// public void createSpectatorScreenTextAreaPanel() {
// this.setLayout(new BorderLayout());
// textAreaScrollPane = new JScrollPane(myTextArea);
// myTextArea.setWrapStyleWord(true);
// myTextArea.setLineWrap(true);
//
// textAreaScrollPane
// .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
// textAreaScrollPane.setPreferredSize(new Dimension(500, 1200));
// spectatorScreenTextareaPanel.add(textAreaScrollPane,
// BorderLayout.CENTER);
//
// }
// }
