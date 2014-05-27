package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import domein.Administrator;

@SuppressWarnings("serial")
public class AdminCompScreen extends JPanel {
	private AdminCompWindow acw;
	private JPanel listPanel;
	private JPanel buttonsPanel;
	private MainFrame mainFrame;
	private Administrator admin = new Administrator(true);
	private int compID;

	public AdminCompScreen(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		this.setLayout(new BorderLayout());

		createListPanel();
		createButtonsPanel();

		this.add(listPanel, BorderLayout.WEST);
		this.add(buttonsPanel, BorderLayout.EAST);
	}

	private void createListPanel() {
		listPanel = new JPanel();
		listPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		JLabel label = new JLabel("Active competitions:");
		HashMap<Integer, String> participantList = admin.adminCompetitions();
		
		JList compsList = new JList(participantList.values().toArray());

		
		
		
		JScrollPane scrollPane = new JScrollPane(compsList);
		scrollPane.setPreferredSize(new Dimension(800, 300));
		scrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
		
		listPanel.add(label);
		listPanel.add(scrollPane);
	}

	private void createButtonsPanel() {
		buttonsPanel = new JPanel();
		buttonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 5, 0));
		JPanel composedButtons = new JPanel();

		composedButtons.setLayout(new GridLayout(4, 1, 0, 50));
		buttonsPanel.setLayout(new BorderLayout());

		buttonsPanel.setPreferredSize(new Dimension(300, 300));

		JButton compPartScreenButton = new JButton("Competition Participants");
		JButton compScreenButton = new JButton("Players screen");
		
		compPartScreenButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				acw = new AdminCompWindow();
			}});
		
		compScreenButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				mainFrame.setAdminAccScreen();
			}});
		
		composedButtons.add(compPartScreenButton);
		composedButtons.add(compScreenButton);

		buttonsPanel.add(composedButtons, BorderLayout.WEST);
	}

	public int getCompID()
	{
		return compID;
	}

	public void setCompID(int compID)
	{
		this.compID = compID;
	}
}
