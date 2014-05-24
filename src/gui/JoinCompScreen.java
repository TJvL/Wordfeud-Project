package gui;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import domein.Competition;

/////this screen contains all public active  competitions 
@SuppressWarnings("serial")
public class JoinCompScreen extends JPanel {

	private MainFrame mainFrame;
	private JPanel infoPanel;
	private JPanel buttonPanel;
	private JTable table;
	private JScrollPane scrollTable;
	private ListSelectionModel selectModel;
	private String currentSelection;
	private boolean neverViewed;

	public JoinCompScreen(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		infoPanel = new JPanel();
		buttonPanel = new JPanel();
		infoPanel.setLayout(null);
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 20));
		this.setLayout(null);
		this.createButtons();
		neverViewed =  true;
		buttonPanel.setBackground(Color.BLACK);		
	}
	
	public void populateScreen(){
		currentSelection = "";
		if(neverViewed){
			mainFrame.callLoadAllCompetitionsAction();
			this.initTable();
		}
		
		scrollTable.setBounds(0, 0, 650, 700);
		infoPanel.setBounds(650, 0, 550, 550);
		buttonPanel.setBounds(650, 550, 550, 150);
		
		this.add(scrollTable);
		this.add(infoPanel);
		this.add(buttonPanel);
		neverViewed = false;
	}

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
				}
			}
		});
		table.setSelectionModel(selectModel);
		scrollTable = new JScrollPane(table);
	}
	
	private void refreshTable(){
		this.initTable();
		scrollTable.setBounds(0, 0, 650, 700);
		this.add(scrollTable);
		this.revalidate();
	}
	
	private void setCompInfo(){
		currentSelection = table.getValueAt(table.getSelectedRow(), 0).toString();
	}
	
	private void joinSelectedCompetition(){
		mainFrame.callJoinCompetitionAction(currentSelection);
	}

	public void clearLists() {
		this.neverViewed = true;
	}
}
