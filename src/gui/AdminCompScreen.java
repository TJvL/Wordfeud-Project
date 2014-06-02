package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import domein.Administrator;
import domein.Competition;
import domein.CompetitionPlayer;

@SuppressWarnings("serial")
public class AdminCompScreen extends JPanel {
	
	private final String defaultSelection = "default";
	
	private MainFrame mainFrame;
	private AdminCompWindow acw;
	
	private JPanel buttonsPanel;
	private JTable compTable;
	private JScrollPane compPane;
	
//	private JPanel listPanel;
	
	////////////////////////////////////////////////////////
	private Administrator admin = new Administrator(true);//
	////////////////////////////////////////////////////////
	private int compID;//
	/////////////////////
	
	///////////////////// new ////////////////////////////////
	private String currentSelection;//////////////////////////
	private boolean neverViewed;//////////////////////////////
	//////////////////////////////////////////////////////////
	
	public AdminCompScreen(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout (FlowLayout.CENTER, 50, 20));

//		createListPanel();

//		this.add(listPanel, BorderLayout.WEST);
//		this.add(buttonsPanel, BorderLayout.EAST);
		
		this.setLayout(null);
		this.createButtons();
		neverViewed =  true;
		currentSelection = defaultSelection;
	}

//	private void createListPanel() {
//		listPanel = new JPanel();
//		listPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
//		
//		JLabel label = new JLabel("Active competitions:");
//		HashMap<Integer, String> participantList = admin.adminCompetitions();
//		
//		JList compsList = new JList(participantList.values().toArray());
//
//		
//		
//		
//		JScrollPane scrollPane = new JScrollPane(compsList);
//		scrollPane.setPreferredSize(new Dimension(800, 300));
//		scrollPane
//				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
//		
//		listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
//		
//		listPanel.add(label);
//		listPanel.add(scrollPane);
//	}
	
	public void populateScreen(){
		if(neverViewed){
			mainFrame.callLoadAllCompetitionsAction();
			this.initCompTable();
//			this.initPartiTable();
		}
		
//		listLabel.setBounds(670, 20, 100, 20);
		compPane.setBounds(0, 0, 750, 700);
//		partiPane.setBounds(665, 50, 520, 500);
		buttonsPanel.setBounds(750, 0, 550, 150);
		
//		this.add(listLabel);
		this.add(compPane);
//		this.add(partiPane);
		this.add(buttonsPanel);
		neverViewed = false;
	}
	
	public void clearLists() {
		this.neverViewed = true;
		this.removeAll();
		this.revalidate();
	}
	
	private void createButtons() {
		
		buttonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 5, 0));
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.PAGE_AXIS));
//		buttonsPanel.setPreferredSize(new Dimension(300, 300));

		JButton compPartScreenButton = new JButton("Competition Participants");
		JButton compScreenButton = new JButton("Players screen");
		JButton refresh = new JButton("Refresh");
		
	
		compPartScreenButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				acw = new AdminCompWindow(mainFrame);
			}});
		
		compScreenButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				mainFrame.setAdminAccScreen();
			}});	
		
		refresh.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
//				mainFrame.callLoadAllCompetitionsAction();
				refreshCompetitionsList();
			}
		});
		buttonsPanel.add(compPartScreenButton);
		buttonsPanel.add(compScreenButton);
		buttonsPanel.add(refresh);
		buttonsPanel.add(Box.createRigidArea(new Dimension(10, 50)));
	}
	
	private void initCompTable() {
		if(compPane != null){
			this.remove(compPane);
		}
		compTable = null;
		compPane = null;
		
		String[] columnNames = { "ID", "Owner", "Start Date", "End Date", "Summary", "Min part", "Max part", "Current" };
		Set<Entry<String, Competition>> competitions = mainFrame.callGetAllCompetitionsAction();
		String[][] tableData = new String[competitions.size()][8];

		Iterator<Entry<String, Competition>> it = competitions.iterator();
		int i = 0;
		while (it.hasNext()) {
			Map.Entry<String, Competition> pair = (Map.Entry<String, Competition>) it.next();
			tableData[i][0] = Integer.toString(pair.getValue().getCompID());
			tableData[i][1] = pair.getValue().getCompOwner();
			tableData[i][2] = pair.getValue().getStartDate();
			tableData[i][3] = pair.getValue().getEndDate();
			tableData[i][4] = pair.getValue().getSummary();
			tableData[i][5] = Integer.toString(pair.getValue().getMinParticipants());
			tableData[i][6] = Integer.toString(pair.getValue().getMaxParticipants());
			tableData[i][7] = Integer.toString(pair.getValue().getAmountParticipants());
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
		compTable.getColumnModel().getColumn(1).setPreferredWidth(60);
		compTable.getColumnModel().getColumn(2).setPreferredWidth(130);
		compTable.getColumnModel().getColumn(3).setPreferredWidth(130);
		compTable.getColumnModel().getColumn(4).setPreferredWidth(235);
		compTable.getColumnModel().getColumn(5).setPreferredWidth(55);
		compTable.getColumnModel().getColumn(6).setPreferredWidth(55);
		compTable.getColumnModel().getColumn(7).setPreferredWidth(53);

		ForcedListSelectionModel selectModel = new ForcedListSelectionModel();
		selectModel.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) {
					currentSelection = compTable.getValueAt(compTable.getSelectedRow(), 0).toString();
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
	
	//// refreshes your table ////
	private void refreshCompetitionsList(){
		this.initCompTable();
		compPane.setBounds(0, 0, 750, 900);
		this.add(compPane);
		this.revalidate();
		System.out.println("Refreshed competitions list!");
	}
	
	public int getCompID()
	{
		return compID;
	}

	public void setCompID(int compID)
	{
		this.compID = compID;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////// test JTable /////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////
//	
//private final String defaultSelection = "default";
//	
//	private JPanel buttonPanel;
//	private JTable compTable;
//	private JTable partiTable;
//	private JScrollPane compPane;
//	private JScrollPane partiPane;
//	private JLabel listLabel;
//	private String currentSelection;
//	private boolean neverViewed;
//
//	/*
//	 * Initialises the object.
//	 */
//	public JoinCompScreen(MainFrame mainFrame) {
//		this.mainFrame = mainFrame;
//		buttonPanel = new JPanel();
//		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 20));
//		listLabel = new JLabel("Participant List:");
//		this.setLayout(null);
//		this.createButtons();
//		neverViewed =  true;
//		currentSelection = defaultSelection;
//	}
//	/*
//	 * called when the this screen needs to be viewed
//	 * neverViewed is to indicate if the user has ever viewed this screen in this session (reset when user logs out)
//	 * When never viewed the table is loaded and database calls are made.
//	 */
//	public void populateScreen(){
//		if(neverViewed){
//			mainFrame.callLoadAllCompetitionsAction();
//			this.initCompTable();
//			this.initPartiTable();
//		}
//		
//		listLabel.setBounds(670, 20, 100, 20);
//		compPane.setBounds(0, 0, 650, 700);
//		partiPane.setBounds(665, 50, 520, 500);
//		buttonPanel.setBounds(650, 550, 550, 150);
//		
//		this.add(listLabel);
//		this.add(compPane);
//		this.add(partiPane);
//		this.add(buttonPanel);
//		neverViewed = false;
//	}
//	
//	public void clearLists() {
//		this.neverViewed = true;
//		this.removeAll();
//		this.revalidate();
//	}
//	/*
//	 * Creates the 3 buttons in the screen
//	 * Refresh: calls for a rebuild of the table and database calls are made.
//	 * Join: The selected entry in the JTable is used as a reference to get the right compID and send it to the CompetitionManager to join.
//	 * Back: Returns the user to the PlayerScreen.
//	 */
//	private void createButtons() {
//		JButton refresh = new JButton("Refresh");
//		
//		refresh.addActionListener(new ActionListener() {			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				mainFrame.callLoadAllCompetitionsAction();
//				refreshCompetitionsList();
//			}
//		});
//
//		buttonPanel.add(refresh);
//	}
//	
//	/*
//	 * Initialises the table and its scroll pane to hold the most up to date data.
//	 */
//	private void initCompTable() {
//		if(compPane != null){
//			this.remove(compPane);
//		}
//		compTable = null;
//		compPane = null;
//		
//		String[] columnNames = { "ID", "Summary", "Max Parts", "Current Parts", "Owner", "End Date/Time" };
//		Set<Entry<String, Competition>> competitions = mainFrame.callGetAllCompetitionsAction();
//		String[][] tableData = new String[competitions.size()][6];
//
//		Iterator<Entry<String, Competition>> it = competitions
//				.iterator();
//		int i = 0;
//		while (it.hasNext()) {
//			Map.Entry<String, Competition> pair = (Map.Entry<String, Competition>) it
//					.next();
//			tableData[i][0] = Integer.toString(pair.getValue().getCompID());
//			tableData[i][1] = pair.getValue().getSummary();
//			tableData[i][2] = Integer.toString(pair.getValue()
//					.getMaxParticipants());
//			tableData[i][3] = Integer.toString(pair.getValue()
//					.getAmountParticipants());
//			tableData[i][4] = pair.getValue()
//					.getCompOwner();
//			tableData[i][5] = pair.getValue().getEndDate();
//			i++;
//		}
//
//		compTable = new JTable(tableData, columnNames) {
//			@Override
//			public boolean isCellEditable(int row, int column) {
//				return false;
//			}
//		};
//		compTable.getTableHeader().setResizingAllowed(false);
//		compTable.getTableHeader().setReorderingAllowed(false);
//		compTable.setColumnSelectionAllowed(false);
//		compTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//		compTable.getColumnModel().getColumn(0).setPreferredWidth(30);
//		compTable.getColumnModel().getColumn(1).setPreferredWidth(230);
//		compTable.getColumnModel().getColumn(2).setPreferredWidth(80);
//		compTable.getColumnModel().getColumn(3).setPreferredWidth(80);
//		compTable.getColumnModel().getColumn(4).setPreferredWidth(80);
//		compTable.getColumnModel().getColumn(5).setPreferredWidth(150);
//
//		ForcedListSelectionModel selectModel = new ForcedListSelectionModel();
//		selectModel.addListSelectionListener(new ListSelectionListener() {
//			@Override
//			public void valueChanged(ListSelectionEvent e) {
//				if (e.getValueIsAdjusting()) {
//					currentSelection = compTable.getValueAt(compTable.getSelectedRow(), 0).toString();
//					refreshParticipantList();
//				}
//			}
//		});
//		compTable.setSelectionModel(selectModel);
//		compTable.changeSelection(0, 0, false, false);
//		if (compTable.getRowCount() > 0) {
//		currentSelection = compTable.getValueAt(compTable.getSelectedRow(), 0).toString();
//		}
//		compPane = new JScrollPane(compTable);
//	}
//	
//	private void initPartiTable(){
//		if(partiPane != null){
//			this.remove(partiPane);
//		}
//		partiTable = null;
//		partiPane = null;
//		
//		if (!currentSelection.equals(defaultSelection)) {
//		String[] columnNames = { "Username", "Total Score", "Avg Score", "Played", "Won", "Lost", "Bay Avg" };
//		ArrayList<CompetitionPlayer> participants = mainFrame.callGetOneCompetitionAction(currentSelection).getParticipants();
//		String[][] tableData = new String[participants.size()][7];
//		
//		int i = 0;
//		for (CompetitionPlayer cp : participants){
//			tableData[i][0] = cp.getUsername();
//			tableData[i][1] = Integer.toString(cp.getTotalScore());
//			tableData[i][2] = Integer.toString(cp.getAverageScore());
//			tableData[i][3] = Integer.toString(cp.getTotalGames());
//			tableData[i][4] = Integer.toString(cp.getTotalWins());
//			tableData[i][5] = Integer.toString(cp.getTotalLoss());
//			tableData[i][6] = Double.toString(cp.getBayesianAverage());
//			i++;
//		}
//		
//		partiTable = new JTable(tableData, columnNames) {
//			@Override
//			public boolean isCellEditable(int row, int column) {
//				return false;
//			}
//		};
//		partiTable.getTableHeader().setResizingAllowed(false);
//		partiTable.getTableHeader().setReorderingAllowed(false);
//		partiTable.setColumnSelectionAllowed(false);
//		partiTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//		partiTable.getColumnModel().getColumn(0).setPreferredWidth(100);
//		partiTable.getColumnModel().getColumn(1).setPreferredWidth(70);
//		partiTable.getColumnModel().getColumn(2).setPreferredWidth(70);
//		partiTable.getColumnModel().getColumn(3).setPreferredWidth(70);
//		partiTable.getColumnModel().getColumn(4).setPreferredWidth(70);
//		partiTable.getColumnModel().getColumn(5).setPreferredWidth(70);
//		partiTable.getColumnModel().getColumn(5).setPreferredWidth(70);
//		
//		ForcedListSelectionModel selectModel = new ForcedListSelectionModel();
//		partiTable.setSelectionModel(selectModel);
//		}
//		partiPane = new JScrollPane(partiTable);
//	}
//	/*
//	 * This refreshes the JTable by removing it and rebuilding it.
//	 */
//	private void refreshParticipantList(){
//		this.initPartiTable();
//		partiPane.setBounds(665, 50, 520, 500);
//		this.add(partiPane);
//		this.revalidate();
//		System.out.println("Refreshed participant list!");
//	}
//	
//	private void refreshCompetitionsList(){
//		this.initCompTable();
//		compPane.setBounds(0, 0, 650, 700);
//		this.add(compPane);
//		this.revalidate();
//		System.out.println("Refreshed competitions list!");
//	}
//	/*
//	 * Is called when the user presses the join button
//	 */
//	private void joinSelectedCompetition(){
//		mainFrame.callJoinCompetitionAction(currentSelection);
//	}
//	
//	private void backButtonPressed(){
//		mainFrame.setPlayerScreen();
//		this.removeAll();
//		this.revalidate();
//	}
	
}
