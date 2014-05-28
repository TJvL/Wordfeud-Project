package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import datalaag.DatabaseHandler;
import domein.User;

@SuppressWarnings("serial")
public class AdminAccScreen extends JPanel {
	private ArrayList<String> activePlayers;
	private JList playersList;
	private AccDataWindow accdatawindow;
	private AdminCreatePlayerWindow admincreateplayerwindow;
	private MainFrame mainFrame;
	private JPanel listPanel;
	private JPanel buttonsPanel;

	public AdminAccScreen(MainFrame mainFrame) {
		accdatawindow = new AccDataWindow(mainFrame);
		this.mainFrame = mainFrame;

		this.setLayout(new BorderLayout());

		createPlayerList();
		createButtons();

		this.add(listPanel, BorderLayout.WEST);
		this.add(buttonsPanel, BorderLayout.EAST);
	}

	private void createPlayerList() {
		listPanel = new JPanel();
		JLabel label = new JLabel("Players:");
		playersList = new JList();
		JScrollPane scrollPane = new JScrollPane(playersList);

		scrollPane.setPreferredSize(new Dimension(800, 300));
		scrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

		listPanel.add(label);
		listPanel.add(scrollPane);
	}

	private void createButtons() {
		buttonsPanel = new JPanel();
		JPanel composedButtons = new JPanel();

		composedButtons.setLayout(new GridLayout(4, 1, 0, 50));
		buttonsPanel.setLayout(new BorderLayout());

		buttonsPanel.setPreferredSize(new Dimension(300, 300));

		JButton getDataButton = new JButton("Player data");
		JButton compScreenButton = new JButton("Competitions screen");
		JButton addAccButton = new JButton("Create account");

		getDataButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				accdatawindow = new AccDataWindow(mainFrame);
				accdatawindow.setValues(
						playersList.getSelectedValue().toString(),
						DatabaseHandler.getInstance().getPassword(
								playersList.getSelectedValue().toString()));
				accdatawindow.setRoles(DatabaseHandler.getInstance().getRole(
						playersList.getSelectedValue().toString()));
				accdatawindow.showAdminAccData();
			}
		});
		
		addAccButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				admincreateplayerwindow = new AdminCreatePlayerWindow();
				admincreateplayerwindow.ShowAdminCreatePlayer();
			}
		});
		
		compScreenButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				mainFrame.setAdminCompScreen();
			}
		});

		composedButtons.add(getDataButton);
		composedButtons.add(addAccButton);
		composedButtons.add(compScreenButton);

		buttonsPanel.add(composedButtons, BorderLayout.WEST);
	}

	public void fillPlayerList() {
		System.out.println("Loading player list...");
		activePlayers = DatabaseHandler.getInstance().getAllPlayers();
		playersList.setListData(activePlayers.toArray());
	}
}
