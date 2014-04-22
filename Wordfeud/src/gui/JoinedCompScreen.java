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

public class JoinedCompScreen extends JPanel {
	private JPanel listPanel;
	private JPanel buttonsPanel;
	private String comps[] = { "lol" };

	public JoinedCompScreen() {
		this.setLayout(new BorderLayout());

		createCompList();
		createButtons();

		this.add(listPanel, BorderLayout.WEST);
		this.add(buttonsPanel, BorderLayout.EAST);
	}

	private void createCompList() {
		listPanel = new JPanel();
		JList<String> compsList = new JList<String>(comps);
		JLabel label = new JLabel("Joined competitions:");
		JScrollPane scrollPane = new JScrollPane(compsList);

		listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

		scrollPane.setPreferredSize(new Dimension(800, 300));
		scrollPane
				.setVerticalScrollBarPolicy(scrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		listPanel.add(label);
		listPanel.add(scrollPane);
	}

	private void createButtons() {
		buttonsPanel = new JPanel();
		JPanel composedButtons = new JPanel();
		
		JButton infoButton = new JButton("Ranglist");
		
		composedButtons.setLayout(new GridLayout(4, 1, 0, 50));
		
		buttonsPanel.setLayout(new BorderLayout());
		buttonsPanel.setPreferredSize(new Dimension(300, 300));
		
		composedButtons.add(infoButton);
		
		buttonsPanel.add(composedButtons, BorderLayout.WEST);
	}
}
