package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class AdminAccScreen extends JPanel {
	private JPanel listPanel;
	private JPanel buttonsPanel;
	private String players[] = { "palyer" };

	public AdminAccScreen() {
		this.setLayout(new BorderLayout());
		
		createPlayerList();
		createButtons();
		
		this.add(listPanel, BorderLayout.WEST);
		this.add(buttonsPanel, BorderLayout.EAST);
	}

	private void createPlayerList() {
		listPanel = new JPanel();
		JLabel label = new JLabel("Players:");
		JList<String> playersList = new JList<String>(players);
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
		JButton changeDataButton = new JButton("Change data");
		JButton compScreenButton = new JButton("Competitions screen");
		
		composedButtons.add(getDataButton);
		composedButtons.add(changeDataButton);
		composedButtons.add(compScreenButton);

		buttonsPanel.add(composedButtons, BorderLayout.WEST);
	}
}
