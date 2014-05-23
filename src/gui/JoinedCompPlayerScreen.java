package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import domein.CompPlayer;

public class JoinedCompPlayerScreen extends JPanel {
	private RankingWindow rankingwindow;
	private JPanel listPanel;
	private JPanel buttonsPanel;
	private JList<CompPlayer> compsList;
	private MainFrame framePanel;

	public JoinedCompPlayerScreen(MainFrame framePanel) {
		rankingwindow = new RankingWindow();
		listPanel = new JPanel();
		this.setLayout(new BorderLayout());
		createButtons();

		this.add(listPanel, BorderLayout.WEST);
		this.add(buttonsPanel, BorderLayout.EAST);
		this.framePanel = framePanel;
	}

	public void fillCompList(ArrayList<CompPlayer> players) {
		if (players != null) {
			CompPlayer[] compPlayers = new CompPlayer[players.size()];
			for (int i = 0; i < players.size(); i++) {
				compPlayers[i] = players.get(i);
			}
			if (compsList != null) {
				this.remove(compsList);
				this.remove(listPanel);
			}
			createCompList(compPlayers);
			this.repaint();
			this.revalidate();
		} else {
			System.out
					.println("JoinedCompScreen heeft geen arrayList met deelnemers");
		}
	}

	private void createCompList(CompPlayer[] players) {
		compsList = new JList<CompPlayer>(players);
		compsList.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				CompPlayer player = compsList.getSelectedValue();
				if (player != null) {
					framePanel.challengePlayer(player.getCompetitionID(),
							framePanel.getName(), player.getName(), "EN");
				}
			}
		});

		JLabel label = new JLabel("Joined competitions:");
		JScrollPane scrollPane = new JScrollPane(compsList);

		listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

		scrollPane.setPreferredSize(new Dimension(1000, 300));
		scrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		listPanel.add(label);
		listPanel.add(scrollPane);
	}

	private void createButtons() {
		buttonsPanel = new JPanel();
		JPanel composedButtons = new JPanel();

		JButton infoButton = new JButton("Ranglist");

		infoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				rankingwindow.showRanking();
			}
		});

		composedButtons.setLayout(new GridLayout(4, 1, 0, 50));

		buttonsPanel.setLayout(new BorderLayout());
		buttonsPanel.setPreferredSize(new Dimension(100, 300));

		composedButtons.add(infoButton);

		buttonsPanel.add(composedButtons, BorderLayout.WEST);
	}
}
