package gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import domein.ActiveMatch;

@SuppressWarnings("serial")
public class PlayerScreen extends JPanel {
	private final String defaultSelection = "default";
	
	private MainFrame mainFrame;
	private JPanel buttonsPanel;
	private JTable matchTable;
	private JScrollPane matchPane;
	private String currentSelection;
	private JButton createCompButton;
	private JButton refreshListButton;
	private JButton enterGameButton;
	private JButton joinCompButton;
	private JButton joinedCompButton;
	
	public PlayerScreen(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		buttonsPanel = new JPanel();
		this.setLayout(null);
		buttonsPanel.setLayout(new GridLayout(8, 1, 0, 15));
		createButtonsPanel();
		
		currentSelection = defaultSelection;
	}
	
	public void populateScreen(){
		this.removeAll();
		
		this.initMatchTable();
		
		matchPane.setBounds(190, 20, 400, 600);
		buttonsPanel.setBounds(610, 15, 300, 680);

		this.add(buttonsPanel);
		this.add(matchPane);
		mainFrame.revalidate();
	}

	private void createButtonsPanel() {
		createCompButton = new JButton("Create competition");
		refreshListButton = new JButton("Refresh list");
		enterGameButton = new JButton("Enter game");
		joinCompButton = new JButton("Join Competition");
		joinedCompButton = new JButton("Joined competitions");

		refreshListButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshButtonPressed();
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
				enterGameButtonPressed();
			}
		});

		buttonsPanel.add(createCompButton);
		buttonsPanel.add(refreshListButton);
		buttonsPanel.add(enterGameButton);
		buttonsPanel.add(joinedCompButton);
		buttonsPanel.add(joinCompButton);
	}
	
	private void initMatchTable(){
		matchTable = null;
		matchPane = null;
		
		String[] columnNames = { "ID", "Description" };
		Set<Entry<String, ActiveMatch>> activeMatches = mainFrame
				.callGetMyActiveMatchesAction();
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
			currentSelection = matchTable.getValueAt(matchTable.getSelectedRow(),
					0).toString();
		}
		matchPane = new JScrollPane(matchTable);
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
	
	private void enterGameButtonPressed(){
		if (!currentSelection.equals("")) {
			int response = JOptionPane.showConfirmDialog(null,
					"Are you sure you want to enter the selected game?", "", JOptionPane.OK_OPTION,
					JOptionPane.PLAIN_MESSAGE);
			if (response == JOptionPane.OK_OPTION) {
				mainFrame.startGame(Integer.parseInt(currentSelection), false);
			}
		}
	}
	
	private void refreshButtonPressed(){
		mainFrame.startMyActiveMatchesWorker();
	}
}
