package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import datalaag.DatabaseHandler;

@SuppressWarnings("serial")
public class NotificationWindow extends JFrame {
	//private String[] listItems;

	private JTextArea infoTextArea = new JTextArea();
	//private JScrollPane infoPane = new JScrollPane(infoTextArea);
	private JLabel title = new JLabel();
	private JList<String> notificationList;
	private JButton select = new JButton();
	private DefaultListModel<String> listModel = new DefaultListModel<String>();
	//private String itemSelected = "";
	private ArrayList<String> pendingGames;
	private DatabaseHandler dbh;
	private MainFrame mainFrame;

	public NotificationWindow(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		dbh = DatabaseHandler.getInstance();
	}

	public void openNotificationWindow() {

		// notificationList.setModel(listModel);
		fillNotificationList();
		this.setLayout(new BorderLayout());

		notificationList.setPreferredSize(new Dimension(400, 400));
		this.add(notificationList, BorderLayout.CENTER);

		title.setPreferredSize(new Dimension(50, 100));
		title.setText("You have : " + listModel.getSize() + " notifications.");
		this.add(title, BorderLayout.NORTH);

		//this.infoPane.setPreferredSize(new Dimension(100, 200));
		//this.add(infoPane, BorderLayout.EAST);

		select.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				selectClicked();
			}
		});

		select.setText("Select");
		this.add(select, BorderLayout.SOUTH);

		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);

	}

	public void selectClicked() {

	}

	public synchronized int fillNotificationList() {
		pendingGames = dbh.pendingGames(mainFrame.getName());
		String[] games = new String[pendingGames.size()];
		for (int i = 0; i < pendingGames.size(); i++) {
			games[i] = pendingGames.get(i);
		}
		if (notificationList != null) {
			this.remove(notificationList);
		}
		notificationList = new JList<String>(games);
		this.add(notificationList, BorderLayout.CENTER);
		notificationList.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				String input = notificationList.getSelectedValue();
				String[] splits = input.split(",");
				int gameID = Integer.parseInt(splits[0]);
				int reply = JOptionPane.showConfirmDialog(null,
						"Want to accept this game?", "Game accept",
						JOptionPane.YES_NO_OPTION);
				if (reply == JOptionPane.YES_OPTION) {
					dbh.acceptRejectGame(1, gameID, mainFrame.getName(), "Accepted");
				} else {
					dbh.acceptRejectGame(1, gameID, mainFrame.getName(), "Rejected");
				}
			}
		});
		this.repaint();
		this.revalidate();
		return pendingGames.size();
	}
}
