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

public class PlayerScreen extends JPanel {
	private MainFrame mainFrame;
	private String games[] = { "hoi", "hoii" };
	private JList<String> gameList;
	private JPanel listPanel;
	private JPanel buttonsPanel;
	private CreateCompWindow createcompwindow;

	public PlayerScreen(MainFrame mainFrame) {
		createcompwindow = new CreateCompWindow();
		this.mainFrame = mainFrame;

		this.setLayout(new BorderLayout());

		createGamesList();
		createButtons();

		this.add(listPanel, BorderLayout.WEST);
		this.add(buttonsPanel, BorderLayout.EAST);
	}

	private void createGamesList() {
		listPanel = new JPanel();
		gameList = new JList<String>(games);
		listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
		JScrollPane scrollPane = new JScrollPane(gameList);
		JLabel label = new JLabel("Active games:");

		scrollPane.setPreferredSize(new Dimension(800, 300));
		
		scrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		listPanel.add(label);
		listPanel.add(scrollPane);
	}

	private void createButtons() {
		buttonsPanel = new JPanel();
		JPanel composedButtons = new JPanel();

		JButton createCompButton = new JButton("Create competition");
		JButton openGameButton = new JButton("Open game");
		JButton joinCompButton = new JButton("Join Competition");
		JButton joinedCompButton = new JButton("Joined competitions");
		
		composedButtons.setLayout(new GridLayout(4, 1, 0, 50));
		
		buttonsPanel.setLayout(new BorderLayout());
		buttonsPanel.setPreferredSize(new Dimension(300, 300));


		openGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mainFrame.setGameScreen();
			}
		});
		joinedCompButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mainFrame.setJoinedCompScreen();
			}
		});
		joinCompButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				mainFrame.setJoinCompScreen();
			}});
		createCompButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				createcompwindow.createCompFrame();
			}});

		composedButtons.add(createCompButton);
		composedButtons.add(openGameButton);
		composedButtons.add(joinedCompButton);
		composedButtons.add(joinCompButton);

		buttonsPanel.add(composedButtons, BorderLayout.WEST);
	}
}
