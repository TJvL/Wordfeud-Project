package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class AdminCompScreen extends JPanel {
	private JPanel listPanel;
	private JPanel buttonsPanel;
	private MainFrame mainFrame;

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
		JLabel label = new JLabel("Active competitions:");
		JList<String> compsList = new JList<String>();
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
		JPanel composedButtons = new JPanel();

		composedButtons.setLayout(new GridLayout(4, 1, 0, 50));
		buttonsPanel.setLayout(new BorderLayout());

		buttonsPanel.setPreferredSize(new Dimension(300, 300));

		JButton changeDataButton = new JButton("Set invisible");
		JButton compScreenButton = new JButton("Players screen");
		
		compScreenButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				mainFrame.setAdminAccScreen();
			}});
		
		composedButtons.add(changeDataButton);
		composedButtons.add(compScreenButton);

		buttonsPanel.add(composedButtons, BorderLayout.WEST);
	}
}
