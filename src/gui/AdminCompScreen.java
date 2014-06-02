package gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import domein.Competition;

@SuppressWarnings("serial")
public class AdminCompScreen extends JPanel {
	private final String defaultSelection = "default";
	
	private MainFrame mainFrame;
	private JLabel listLabel;
	private JPanel buttonsPanel;
	private JPanel buttonPanel;
	private JTable compTable;
	private JScrollPane compPane;
	private String currentSelection;

	private int compID;	
	private AdminCompWindow acw;
	
	public AdminCompScreen(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		buttonPanel =new JPanel();
		listLabel = new JLabel("Participant List:");
		this.setLayout(null);
		this.createButtonPanel();
		
		currentSelection = defaultSelection;
	}
	
	public void populateScreen(){
		this.remove(listLabel);
		this.remove(buttonPanel);
		
		this.refreshCompetitionsList();
		
		compPane.setBounds(0, 0, 750, 700);
		buttonsPanel.setBounds(750, 0, 550, 150);
		
		this.add(compPane);
		this.add(buttonsPanel);
	}
	
	private void createButtonPanel(){
		JButton refreshButtonPanel = new JButton("Refresh");
		JButton accountScreenButton = new JButton("Challenge");
		JButton compPartsWindowButton = new JButton("Back");

		refreshButtonPanel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				refreshButtonPressed();
			}
		});
		accountScreenButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				accountScreenButtonPressed();
			}
		});
		compPartsWindowButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				compPartsButtonPressed();
			}
		});

		buttonPanel.add(accountScreenButton);
		buttonPanel.add(refreshButtonPanel);
		buttonPanel.add(compPartsWindowButton);
	}
	
	private void initCompTable() {
		compTable = null;
		compPane = null;
		
		String[] columnNames = { "ID", "Owner", "Start Date", "End Date", "Summary", "Min part", "Max part", "Current" };
		Set<Entry<String, Competition>> competitions = mainFrame.callGetAdminCompetitionsAction();
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
	
	private void refreshCompetitionsList(){
		if(compPane != null){
			this.remove(compPane);
		}
		this.initCompTable();
		compPane.setBounds(0, 0, 650, 700);
		this.add(compPane);
		this.revalidate();
	}
	
	private void refreshButtonPressed(){
		mainFrame.startAdminCompWorker();
	}
	
	private void accountScreenButtonPressed(){
		mainFrame.setAdminAccScreen();
	}
	
	private void compPartsButtonPressed(){
		acw = new AdminCompWindow();
	}
	
	public int getCompID()
	{
		return compID;
	}

	public void setCompID(int compID)
	{
		this.compID = compID;
	}
	
//	private void createButtons() {
//		
//		buttonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 5, 0));
//		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.PAGE_AXIS));
//		buttonsPanel.setPreferredSize(new Dimension(300, 300));
//
//		JButton compPartScreenButton = new JButton("Competition Participants");
//		JButton compScreenButton = new JButton("Players screen");
//		JButton refresh = new JButton("Refresh");
//		
//	
//		compPartScreenButton.addActionListener(new ActionListener()
//		{
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				acw = new AdminCompWindow();
//			}});
//		
//		compScreenButton.addActionListener(new ActionListener(){
//			public void actionPerformed(ActionEvent arg0) {
//				mainFrame.setAdminAccScreen();
//			}});	
//		
//		refresh.addActionListener(new ActionListener() {			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				mainFrame.callLoadAllCompetitionsAction();
//				refreshCompetitionsList();
//			}
//		});
//		buttonsPanel.add(compPartScreenButton);
//		buttonsPanel.add(compScreenButton);
//		buttonsPanel.add(refresh);
//		buttonsPanel.add(Box.createRigidArea(new Dimension(10, 50)));
//	}	
}
