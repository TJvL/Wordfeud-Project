package gui;

import javax.swing.JPanel;

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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import domein.Competition;
import domein.CompetitionPlayer;

/*
 * This screen contains all active competitions that the current user is not a participant in.
 * It uses a JTable to populate a list where competitions can be selected to view the current rankings and to join it. 
 * A JList is updated every time another competition entry is selected to show its rankings. <<<<< CURRENTLY ONLY USERNAMES FOR JLIST
 * This class may only be directly called from MainFrame,
 */
@SuppressWarnings("serial")
public class JoinCompScreen extends JPanel {
	private final String defaultSelection = "default";
	
	private MainFrame mainFrame;
	private JPanel buttonPanel;
	private JTable compTable;
	private JTable partiTable;
	private JScrollPane compPane;
	private JScrollPane partiPane;
	private JLabel listLabel;
	private String currentSelection;
	private boolean neverViewed;

	/*
	 * Initialises the object.
	 */
	public JoinCompScreen(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 20));
		listLabel = new JLabel("Participant List:");
		this.setLayout(null);
		this.createButtons();
		neverViewed =  true;
		currentSelection = defaultSelection;
	}
	/*
	 * called when the this screen needs to be viewed
	 * neverViewed is to indicate if the user has ever viewed this screen in this session (reset when user logs out)
	 * When never viewed the table is loaded and database calls are made.
	 */
	public void populateScreen(){
		if(neverViewed){
			mainFrame.callLoadAllCompetitionsAction();
			this.initCompTable();
			this.initPartiTable();
		}
		
		listLabel.setBounds(670, 20, 100, 20);
		compPane.setBounds(0, 0, 650, 700);
		partiPane.setBounds(665, 50, 520, 500);
		buttonPanel.setBounds(650, 550, 550, 150);
		
		this.add(listLabel);
		this.add(compPane);
		this.add(partiPane);
		this.add(buttonPanel);
		neverViewed = false;
	}
	
	public void clearLists() {
		this.neverViewed = true;
		this.removeAll();
		this.revalidate();
	}
	/*
	 * Creates the 3 buttons in the screen
	 * Refresh: calls for a rebuild of the table and database calls are made.
	 * Join: The selected entry in the JTable is used as a reference to get the right compID and send it to the CompetitionManager to join.
	 * Back: Returns the user to the PlayerScreen.
	 */
	private void createButtons() {
		JButton refresh = new JButton("Refresh");
		JButton join = new JButton("Join");
		JButton back = new JButton("Back");
		
		refresh.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				mainFrame.callLoadAllCompetitionsAction();
				refreshCompetitionsList();
			}
		});
		join.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				Competition compName = mainFrame.callGetOneCompetitionAction(currentSelection);
				if (!currentSelection.equals("")){
					int response = JOptionPane.showConfirmDialog(null, "Are u sure u want to join: \"" + compName.getSummary() + "\"?",
			                   "", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE);
					if(response == JOptionPane.OK_OPTION){
					joinSelectedCompetition();
					refreshCompetitionsList();
					}
				}
			}
		});
		back.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				backButtonPressed();
			}
		});
		
		buttonPanel.add(join);
		buttonPanel.add(refresh);
		buttonPanel.add(back);
	}
	
	/*
	 * Initialises the table and its scroll pane to hold the most up to date data.
	 */
	private void initCompTable() {
		if(compPane != null){
			this.remove(compPane);
		}
		compTable = null;
		compPane = null;
		
		String[] columnNames = { "ID", "Summary", "Max Parts", "Current Parts", "Owner", "End Date/Time" };
		Set<Entry<String, Competition>> competitions = mainFrame.callGetAllCompetitionsAction();
		String[][] tableData = new String[competitions.size()][6];

		Iterator<Entry<String, Competition>> it = competitions
				.iterator();
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
			tableData[i][4] = pair.getValue()
					.getCompOwner();
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
					currentSelection = compTable.getValueAt(compTable.getSelectedRow(), 0).toString();
					refreshParticipantList();
				}
			}
		});
		compTable.setSelectionModel(selectModel);
		compTable.changeSelection(0, 0, false, false);
		if (compTable.getRowCount() > 0) {
		currentSelection = compTable.getValueAt(compTable.getSelectedRow(), 0).toString();
		}
		compPane = new JScrollPane(compTable);
	}
	
	private void initPartiTable(){
		if(partiPane != null){
			this.remove(partiPane);
		}
		partiTable = null;
		partiPane = null;
		
		if (!currentSelection.equals(defaultSelection)) {
		String[] columnNames = { "Username", "Total Score", "Avg Score", "Played", "Won", "Lost", "Bay Avg" };
		ArrayList<CompetitionPlayer> participants = mainFrame.callGetOneCompetitionAction(currentSelection).getParticipants();
		String[][] tableData = new String[participants.size()][7];
		
		int i = 0;
		for (CompetitionPlayer cp : participants){
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
		partiTable.getColumnModel().getColumn(5).setPreferredWidth(70);
		
		ForcedListSelectionModel selectModel = new ForcedListSelectionModel();
		partiTable.setSelectionModel(selectModel);
		}
		partiPane = new JScrollPane(partiTable);
	}
	/*
	 * This refreshes the JTable by removing it and rebuilding it.
	 */
	private void refreshParticipantList(){
		this.initPartiTable();
		partiPane.setBounds(665, 50, 520, 500);
		this.add(partiPane);
		this.revalidate();
		System.out.println("Refreshed participant list!");
	}
	
	private void refreshCompetitionsList(){
		this.initCompTable();
		compPane.setBounds(0, 0, 650, 700);
		this.add(compPane);
		this.revalidate();
		System.out.println("Refreshed competitions list!");
	}
	/*
	 * Is called when the user presses the join button
	 */
	private void joinSelectedCompetition(){
		mainFrame.callJoinCompetitionAction(currentSelection);
	}
	
	private void backButtonPressed(){
		mainFrame.setPlayerScreen();
		this.removeAll();
		this.revalidate();
	}
}
