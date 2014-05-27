package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;

import domein.PendingMatch;

@SuppressWarnings("serial")
public class NotificationWindow extends JDialog {
	private JLabel title = new JLabel();
	private JList<PendingMatch> notificationList;
	private JButton refresh = new JButton();;
	private ArrayList<PendingMatch> pendingGames;
	private MainFrame mainFrame;
	private int numberOfNotes;

	public NotificationWindow(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		this.numberOfNotes = 0;
	}

	public void openNotificationWindow() {

		this.setModal(true);
		// notificationList.setModel(listModel);
		this.setPreferredSize(new Dimension(500,500));
		fillNotificationList();
		this.setLayout(new BorderLayout());

		notificationList.setPreferredSize(new Dimension(300, 400));
		this.add(notificationList, BorderLayout.CENTER);

		title.setPreferredSize(new Dimension(300, 100));
		title.setText("You have : " + numberOfNotes + " notifications.");
		this.add(title, BorderLayout.NORTH);

		// this.infoPane.setPreferredSize(new Dimension(100, 200));
		// this.add(infoPane, BorderLayout.EAST);

		refresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				numberOfNotes = fillNotificationList();
				title.setText("You have : " + numberOfNotes + " notifications.");
			}
		});

		refresh.setText("Refresh list");
		this.add(refresh, BorderLayout.SOUTH);

		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);

	}

	public synchronized int fillNotificationList() {
		pendingGames = mainFrame.getPendingGames();
		PendingMatch[] games = new PendingMatch[pendingGames.size()];
		for (int i = 0; i < pendingGames.size(); i++) {
			games[i] = pendingGames.get(i);
		}
		if (notificationList != null) {
			this.remove(notificationList);
		}
		notificationList = new JList<PendingMatch>(games);
		this.add(notificationList, BorderLayout.CENTER);
		notificationList.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				PendingMatch input = notificationList.getSelectedValue();
				if (input != null) {
					int reply = JOptionPane.showConfirmDialog(null,
							"Want to accept this game?", "Game accept",
							JOptionPane.YES_NO_OPTION);
					if (reply == JOptionPane.YES_OPTION) {
						mainFrame.acceptRejectGame("Accepted", 2,
								input.getGameID());
					} else if (reply == JOptionPane.NO_OPTION){
						mainFrame.acceptRejectGame("Rejected", 2,
								input.getGameID());
					}
				}
			}
		});
		this.repaint();
		this.revalidate();
		return pendingGames.size();
	}
}
