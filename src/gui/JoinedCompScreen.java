package gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import domein.Competition;
import domein.CompetitionPlayer;

@SuppressWarnings("serial")
public class JoinedCompScreen extends JPanel {
	private final String defaultSelection = "default";

	private MainFrame mainFrame;
	private JPanel buttonPanel;
	private JTable compTable;
	private JTable partiTable;
	private JScrollPane compPane;
	private JScrollPane partiPane;
	private String compSelection;
	private String playerSelection;
	private JLabel listLabel;

	public JoinedCompScreen(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 20));
		listLabel = new JLabel("Participant List:");
		this.setLayout(null);
		this.createButtons();
		compSelection = defaultSelection;
		playerSelection = defaultSelection;
	}

	public void populateScreen() {
		this.remove(listLabel);
		this.remove(buttonPanel);
		
		this.refreshCompetitionsList();
		this.refreshParticipantList();
		
		listLabel.setBounds(670, 20, 100, 20);
		buttonPanel.setBounds(650, 550, 550, 150);

		this.add(listLabel);
		this.add(buttonPanel);
		mainFrame.revalidate();
	}

	private void createButtons() {
		JButton refresh = new JButton("Refresh");
		JButton challenge = new JButton("Challenge");
		JButton back = new JButton("Back");

		refresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				refreshButtonPressed();
			}
		});
		challenge.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				challengeButtonPressed();
			}
		});
		back.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				backButtonPressed();
			}
		});

		buttonPanel.add(challenge);
		buttonPanel.add(refresh);
		buttonPanel.add(back);
	}

	private void initCompTable() {
		compTable = null;
		compPane = null;

		String[] columnNames = { "ID", "Summary", "Max Parts", "Current Parts",
				"Owner", "End Date/Time" };
		Set<Entry<String, Competition>> competitions = mainFrame
				.callGetJoinedCompetitionsAction();
		String[][] tableData = new String[competitions.size()][6];

		Iterator<Entry<String, Competition>> it = competitions.iterator();
		int i = 0;
		while (it.hasNext()) {
			Map.Entry<String, Competition> pair = (Map.Entry<String, Competition>) it
					.next();
			tableData[i][0] = Integer.toString(pair.getValue().getCompID());
			tableData[i][1] = pair.getValue().getSummary();
			tableData[i][2] = Integer.toString(pair.getValue()
					.getMaxParticipants());
			tableData[i][3] = Integer.toString(pair.getValue()
					.getAmountParticipants());
			tableData[i][4] = pair.getValue().getCompOwner();
			tableData[i][5] = pair.getValue().getEndDate();
			i++;
		}

		compTable = new JTable(tableData, columnNames) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		compTable.getTableHeader().setResizingAllowed(false);
		compTable.getTableHeader().setReorderingAllowed(false);
		compTable.setColumnSelectionAllowed(false);
		compTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		compTable.getColumnModel().getColumn(0).setPreferredWidth(30);
		compTable.getColumnModel().getColumn(1).setPreferredWidth(230);
		compTable.getColumnModel().getColumn(2).setPreferredWidth(80);
		compTable.getColumnModel().getColumn(3).setPreferredWidth(80);
		compTable.getColumnModel().getColumn(4).setPreferredWidth(80);
		compTable.getColumnModel().getColumn(5).setPreferredWidth(150);

		ForcedListSelectionModel selectModel = new ForcedListSelectionModel();
		selectModel.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) {
					compSelection = compTable.getValueAt(
							compTable.getSelectedRow(), 0).toString();
					refreshParticipantList();
				}
			}
		});
		compTable.setSelectionModel(selectModel);
		compTable.changeSelection(0, 0, false, false);
		if (compTable.getRowCount() > 0) {
			compSelection = compTable.getValueAt(compTable.getSelectedRow(), 0)
					.toString();
		}
		compPane = new JScrollPane(compTable);
	}

	private void initPartiTable() {
		partiTable = null;
		partiPane = null;

		if (!compSelection.equals(defaultSelection)) {
			String[] columnNames = { "Username", "Total Score", "Avg Score",
					"Played", "Won", "Lost", "Bay Avg" };
			ArrayList<CompetitionPlayer> participants = mainFrame
					.callGetOneCompetitionAction(compSelection)
					.getParticipants();
			String[][] tableData = new String[participants.size()][7];

			int i = 0;
			for (CompetitionPlayer cp : participants) {
				tableData[i][0] = cp.getUsername();
				tableData[i][1] = Integer.toString(cp.getTotalScore());
				tableData[i][2] = Integer.toString(cp.getAverageScore());
				tableData[i][3] = Integer.toString(cp.getTotalGames());
				tableData[i][4] = Integer.toString(cp.getTotalWins());
				tableData[i][5] = Integer.toString(cp.getTotalLoss());
				tableData[i][6] = Double.toString(cp.getBayesianAverage());
				i++;
			}

			partiTable = new JTable(tableData, columnNames) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			partiTable.getTableHeader().setResizingAllowed(false);
			partiTable.getTableHeader().setReorderingAllowed(false);
			partiTable.setColumnSelectionAllowed(false);
			partiTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			partiTable.getColumnModel().getColumn(0).setPreferredWidth(100);
			partiTable.getColumnModel().getColumn(1).setPreferredWidth(70);
			partiTable.getColumnModel().getColumn(2).setPreferredWidth(70);
			partiTable.getColumnModel().getColumn(3).setPreferredWidth(70);
			partiTable.getColumnModel().getColumn(4).setPreferredWidth(70);
			partiTable.getColumnModel().getColumn(5).setPreferredWidth(70);
			partiTable.getColumnModel().getColumn(6).setPreferredWidth(72);

			ForcedListSelectionModel selectModel = new ForcedListSelectionModel();
			selectModel.addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {
					if (e.getValueIsAdjusting()) {
						playerSelection = partiTable.getValueAt(
								partiTable.getSelectedRow(), 0).toString();
					}
				}
			});
			partiTable.setSelectionModel(selectModel);
			partiTable.changeSelection(0, 0, false, false);
			playerSelection = partiTable.getValueAt(
					partiTable.getSelectedRow(), 0).toString();
		}
		partiPane = new JScrollPane(partiTable);
	}

	private void refreshParticipantList() {
		if (partiPane != null){
			this.remove(partiPane);
		}
		
		this.initPartiTable();
		partiPane.setBounds(665, 50, 520, 500);
		this.add(partiPane);
	}
	
	private void refreshCompetitionsList(){
		if (compPane != null){
			this.remove(compPane);
		}
		
		this.initCompTable();
		compPane.setBounds(0, 0, 650, 700);
		this.add(compPane);
	}

	private void challengeButtonPressed() {
		if (playerSelection.equals(mainFrame.getCurrentUsername())) {
			JOptionPane.showMessageDialog(mainFrame,
					"You cannot challenge yourself.");
		} else {
			int retConfirm = JOptionPane.showConfirmDialog(mainFrame,
					"Are you sure you want to challenge: " + playerSelection
							+ "?", "Exit dialog", JOptionPane.YES_NO_OPTION);
			if (retConfirm == JOptionPane.YES_OPTION) {
				int retPublicPrivate = JOptionPane.showInternalOptionDialog(
						this, "Do you want the game to be public or private?",
						"Please choose", JOptionPane.DEFAULT_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, new String[] {
								"Public", "Private" }, null);

				String retValue = mainFrame.startChallengeWorker(
						compSelection, playerSelection, retPublicPrivate);

				JOptionPane.showMessageDialog(mainFrame, retValue);
			}
		}
	}
	
	private void refreshButtonPressed(){
		mainFrame.startJoinedCompWorker();
	}

	private void backButtonPressed() {
		mainFrame.setPlayerScreen();
	}
}
