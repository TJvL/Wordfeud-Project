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
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import domein.Competition;

/*
 * This screen contains all active competitions that the current user is not a participant in.
 * It uses a JTable to populate a list where competitions can be selected to view the current rankings and to join it. 
 * A JList is updated every time another competition entry is selected to show its rankings. <<<<< CURRENTLY ONLY USERNAMES FOR JLIST
 * This class may only be directly called from MainFrame,
 */
@SuppressWarnings("serial")
public class JoinCompScreen extends JPanel {

	private MainFrame mainFrame;
	private JPanel buttonPanel;
	private JTable table;
	private JScrollPane scrollTable;
	private JScrollPane scrollList;
	private ForcedListSelectionModel selectModel;
	private String currentSelection;
	private boolean neverViewed;
	private ArrayList<String> participants;

	/*
	 * Initialises the object.
	 */
	public JoinCompScreen(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		buttonPanel = new JPanel();
		participants = new ArrayList<String>();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 20));
		this.setLayout(null);
		this.createButtons();
		this.refreshParticipantList();
		neverViewed =  true;
	}
	/*
	 * called when the this screen needs to be viewed
	 * neverViewed is to indicate if the user has ever viewed this screen in this session (reset when user logs out)
	 * When never viewed the table is loaded and database calls are made.
	 */
	public void populateScreen(){
		currentSelection = "";
		if(neverViewed){
			mainFrame.callLoadAllCompetitionsAction();
			this.initTable();
		}
		
		scrollTable.setBounds(0, 0, 650, 700);
		scrollList.setBounds(650, 0, 550, 550);
		buttonPanel.setBounds(650, 550, 550, 150);
		
		this.add(scrollTable);
		this.add(scrollList);
		this.add(buttonPanel);
		neverViewed = false;
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
				refreshTable();
			}
		});
		join.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!currentSelection.equals("")){
					joinSelectedCompetition();
					refreshTable();
				}
			}
		});
		back.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				mainFrame.setPlayerScreen();
			}
		});
		
		buttonPanel.add(join);
		buttonPanel.add(refresh);
		buttonPanel.add(back);
	}
	
	/*
	 * Refreshes the JList with the current value of participants
	 */
	private void refreshParticipantList(){
		if (scrollList != null){
			this.remove(scrollList);
		}
		JList<Object> partiList = new JList<>(participants.toArray());
		scrollList = new JScrollPane(partiList);
		scrollList.setBounds(650, 0, 550, 550);
		this.add(scrollList);
	}
	/*
	 * Initialises the table and its scroll pane to hold the most up to date data.
	 */
	private void initTable() {
		if(scrollTable != null){
			this.remove(scrollTable);
		}
		table = null;
		scrollTable = null;
		
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
					.getMinParticipants());
			tableData[i][4] = pair.getValue()
					.getCompOwner();
			tableData[i][5] = pair.getValue().getEndDate();
			i++;
		}

		table = new JTable(tableData, columnNames) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table.getTableHeader().setResizingAllowed(false);
		table.getTableHeader().setReorderingAllowed(false);
		table.setColumnSelectionAllowed(false);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getColumnModel().getColumn(0).setPreferredWidth(30);
		table.getColumnModel().getColumn(1).setPreferredWidth(230);
		table.getColumnModel().getColumn(2).setPreferredWidth(80);
		table.getColumnModel().getColumn(3).setPreferredWidth(80);
		table.getColumnModel().getColumn(4).setPreferredWidth(80);
		table.getColumnModel().getColumn(5).setPreferredWidth(150);

		selectModel = new ForcedListSelectionModel();
		selectModel.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) {
					setCompInfo();
					refreshParticipantList();
				}
			}
		});
		table.setSelectionModel(selectModel);
		scrollTable = new JScrollPane(table);
	}
	/*
	 * This refreshes the JTable by removing it and rebuilding it.
	 */
	private void refreshTable(){
		this.initTable();
		scrollTable.setBounds(0, 0, 650, 700);
		this.add(scrollTable);
		this.revalidate();
	}
	/*
	 * Every time the selection is changed in the JTable this method is called to up date the currentSelection String with its corresponding ID
	 */
	private void setCompInfo(){
		currentSelection = table.getValueAt(table.getSelectedRow(), 0).toString();
		participants = null;
		participants = mainFrame.callGetOneCompetitionAction(currentSelection).getParticipants();
	}
	/*
	 * Is called when the user presses the join button
	 */
	private void joinSelectedCompetition(){
		mainFrame.callJoinCompetitionAction(currentSelection);
	}
	/*
	 * Is called when the user logs out
	 */
	public void clearLists() {
		this.neverViewed = true;
	}
}
