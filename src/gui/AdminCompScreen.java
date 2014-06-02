package gui;

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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import domein.Competition;
import domein.CompetitionPlayer;

@SuppressWarnings("serial")
public class AdminCompScreen extends JPanel {
	
	private final String defaultSelection = "default";
	private MainFrame mainFrame;
	private JPanel buttonsPanel;
	private JTable compTable;
	private JTable partiTable;
	private JScrollPane compPane;
	private JScrollPane partiPane;
	private String compSelection;
	private String playerSelection;
	
	///////////////////// new ////////////////////////////////
	private String currentSelection;//////////////////////////
	private boolean neverViewed;//////////////////////////////
	//////////////////////////////////////////////////////////
	
	public AdminCompScreen(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout (FlowLayout.CENTER, 50, 20));
		this.setLayout(null);
		this.createButtons();
		neverViewed =  true;
		compSelection = defaultSelection;
		playerSelection = defaultSelection;
	}


	
	public void populateScreen(){
		this.refreshLists();
			this.initCompTable();
			this.initPartiTable();
		
		compPane.setBounds(0, 0, 750, 700);
		buttonsPanel.setBounds(750, 0, 550, 150);
		
		this.add(compPane);
		this.add(buttonsPanel);
		neverViewed = false;
	}
	
	public void clearLists() {
		this.removeAll();
		this.revalidate();
	}
	
	private void createButtons() {
		
		buttonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 5, 0));
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.PAGE_AXIS));

		JButton compScreenButton = new JButton("Players screen");
		JButton refresh = new JButton("Refresh");
	
		compScreenButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				mainFrame.setAdminAccScreen();
			}});	
		
		refresh.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				mainFrame.adminCallLoadActiveCompetitionAction();
				refreshCompetitionsList();
			}
		});
		buttonsPanel.add(compScreenButton);		
		buttonsPanel.add(Box.createRigidArea(new Dimension(10, 20)));
		buttonsPanel.add(refresh);
	}
	
	private void initCompTable() {
		if(compPane != null){
			this.remove(compPane);
		}
		compTable = null;
		compPane = null;
		
		String[] columnNames = { "ID", "Owner", "Start Date", "End Date", "Summary", "Min part", "Max part", "Currently" };
		Set<Entry<String, Competition>> competitions = mainFrame.adminCallActiveCompetitionAction();
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
		compTable.getColumnModel().getColumn(4).setPreferredWidth(230);
		compTable.getColumnModel().getColumn(5).setPreferredWidth(55);
		compTable.getColumnModel().getColumn(6).setPreferredWidth(55);
		compTable.getColumnModel().getColumn(7).setPreferredWidth(58);

		ForcedListSelectionModel selectModel = new ForcedListSelectionModel();
		selectModel.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) {
					compSelection = compTable.getValueAt(compTable.getSelectedRow(), 0).toString();
					refreshParticipantList();
				}
			}
		});
		compTable.setSelectionModel(selectModel);
		compTable.changeSelection(0, 0, false, false);
		if (compTable.getRowCount() > 0) {
		compSelection = compTable.getValueAt(compTable.getSelectedRow(), 0).toString();
		}
		compPane = new JScrollPane(compTable);
	}
	
	private void initPartiTable() {
		if (partiPane != null) {
			this.remove(partiPane);
		}
		partiTable = null;
		partiPane = null;

		if (!compSelection.equals(defaultSelection)) {
			String[] columnNames = { "Username", "Total Score", "Avg Score",
					"Played", "Won", "Lost", "Bay Avg" };
			ArrayList<CompetitionPlayer> participants = mainFrame
					.callGetOneActiveCompetitionAction(compSelection)
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
			partiTable.getColumnModel().getColumn(0).setPreferredWidth(80);
			partiTable.getColumnModel().getColumn(1).setPreferredWidth(70);
			partiTable.getColumnModel().getColumn(2).setPreferredWidth(70);
			partiTable.getColumnModel().getColumn(3).setPreferredWidth(50);
			partiTable.getColumnModel().getColumn(4).setPreferredWidth(45);
			partiTable.getColumnModel().getColumn(5).setPreferredWidth(45);
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
		this.initPartiTable();
		partiPane.setBounds(753, 147, 439, 500);
		this.add(partiPane);
	}
	
	//// refreshes your table ////
	private void refreshCompetitionsList(){
		this.initCompTable();
		compPane.setBounds(0, 0, 750, 900);
		this.add(compPane);
		System.out.println("Refreshed competitions list!");
	}
	
	private void refreshLists() {
		mainFrame.adminCallLoadActiveCompetitionAction();
		this.refreshCompetitionsList();
		this.refreshParticipantList();
		this.revalidate();
	}
	

	
}
