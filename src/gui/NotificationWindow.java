package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import datalaag.WordFeudConstants;
import domein.PendingMatch;

@SuppressWarnings("serial")
public class NotificationWindow extends JDialog {
	private final String defaultSelection = "default";

	private JButton refreshButton;
	private JButton respondButton;
	private JPanel buttonPanel;
	private JTable notifTable;
	private JScrollPane notifPane;
	private MainFrame mainFrame;
	private String currentSelection;
	private int numberOfNotes;

	public NotificationWindow(MainFrame mainFrame) {
		super(mainFrame);
		this.mainFrame = mainFrame;
		currentSelection = defaultSelection;
		this.numberOfNotes = 0;

		buttonPanel = new JPanel();

		refreshList();
		createButtonPanel();

		this.setTitle("You have: " + numberOfNotes + " notifications");
		this.setLayout(null);
		this.setModal(true);

		buttonPanel.setBounds(10, 325, 600, 50);

		this.add(buttonPanel);

		this.setPreferredSize(new Dimension(600, 400));
		this.setResizable(false);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	private void createButtonPanel() {
		respondButton = new JButton("Respond");
		refreshButton = new JButton("Refresh");

		buttonPanel.setLayout(null);

		respondButton.setBounds(25, 0, 100, 30);
		refreshButton.setBounds(450, 0, 100, 30);

		buttonPanel.add(respondButton);
		buttonPanel.add(refreshButton);

		respondButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				checkButtonPressed();
			}
		});
		refreshButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				refreshButtonPressed();
			}
		});
	}

	private void initNotifTable() {
		notifTable = null;
		notifPane = null;

		String[] columnNames = { "ID", "Challenge description"};
		Set<Entry<String, PendingMatch>> pendingGames = mainFrame.callGetPendingGamesAction();
		String[][] tableData = new String[pendingGames.size()][6];
		Iterator<Entry<String, PendingMatch>> it = pendingGames.iterator();
		int i = 0;
		while (it.hasNext()) {
			Map.Entry<String, PendingMatch> pair = (Map.Entry<String, PendingMatch>) it
					.next();
			tableData[i][0] = Integer.toString(pair.getValue().getGameID());
			tableData[i][1] = pair.getValue().getDiscription();
			i++;
		}
		
		notifTable = new JTable(tableData, columnNames) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		notifTable.getTableHeader().setResizingAllowed(false);
		notifTable.getTableHeader().setReorderingAllowed(false);
		notifTable.setColumnSelectionAllowed(false);
		notifTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		notifTable.getColumnModel().getColumn(0).setPreferredWidth(35);
		notifTable.getColumnModel().getColumn(1).setPreferredWidth(537);
		
		ForcedListSelectionModel selectModel = new ForcedListSelectionModel();
		selectModel.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) {
					currentSelection = notifTable.getValueAt(
							notifTable.getSelectedRow(), 0).toString();
				}
			}
		});
		notifTable.setSelectionModel(selectModel);
		notifTable.changeSelection(0, 0, false, false);
		if (notifTable.getRowCount() > 0) {
			currentSelection = notifTable.getValueAt(notifTable.getSelectedRow(),
					0).toString();
		}
		notifPane = new JScrollPane(notifTable);
	}

	private void refreshList() {
		if (notifPane != null) {
			this.remove(notifPane);
		}
		initNotifTable();
		numberOfNotes = notifTable.getRowCount();
		notifPane.setBounds(10, 10, 575, 300);
		this.add(notifPane);
	}
	
	private void refreshButtonPressed(){
		mainFrame.startPendingMatchWorker();
		this.dispose();
	}

	private void checkButtonPressed() {
		if (mainFrame.callAskMatchOwnershipAction(currentSelection)) {
			JOptionPane.showMessageDialog(mainFrame,
					"Can't respond to your own challenge!", "Can't respond",
					JOptionPane.INFORMATION_MESSAGE);
		} else {
			int reply = JOptionPane.showConfirmDialog(null,
					"Do you want to accept this challenge?",
					"Accept challenge?", JOptionPane.YES_NO_OPTION);
			if (reply == JOptionPane.YES_OPTION) {
				mainFrame.startResponseWorker(
						WordFeudConstants.CHALLENGE_ACCEPTED, 2,
						Integer.parseInt(currentSelection));
			} else if (reply == JOptionPane.NO_OPTION) {
				mainFrame.startResponseWorker(
						WordFeudConstants.CHALLENGE_REJECTED, 2,
						Integer.parseInt(currentSelection));
			}
			this.refreshList();
		}
	}
}
