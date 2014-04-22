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

public class AdminCompScreen extends JPanel {
	private JPanel listPanel;
	private JPanel buttonsPanel;

	public AdminCompScreen() {
		this.setLayout(new BorderLayout());

		createListPanel();
		createButtonsPanel();

		this.add(listPanel, BorderLayout.WEST);
		this.add(buttonsPanel, BorderLayout.EAST);
	}

	private void createListPanel() {
		listPanel = new JPanel();
		JLabel label = new JLabel("Active competitions:");
		JList<String> compsList = new JList<String>();
		JScrollPane scrollPane = new JScrollPane(compsList);

		scrollPane.setPreferredSize(new Dimension(800, 300));
		scrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		listPanel.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		listPanel.add(label);
		listPanel.add(scrollPane);
	}

	private void createButtonsPanel() {
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
