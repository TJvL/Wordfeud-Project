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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ScrollPaneConstants;

import domein.CompPlayer;
import domein.Competition;

@SuppressWarnings("serial")
public class JoinedCompScreen extends JPanel {
	private RankingWindow rankingwindow;
	private JPanel listPanel;
	private JPanel buttonsPanel;
	private JList<Competition> compsList;
	private MainFrame framePanel;

	public JoinedCompScreen(MainFrame framePanel) {
		rankingwindow = new RankingWindow();
		listPanel = new JPanel();
		this.setLayout(new BorderLayout());
		createButtons();

		this.add(listPanel, BorderLayout.WEST);
		this.add(buttonsPanel, BorderLayout.EAST);
		this.framePanel = framePanel;
	}

	public void fillCompList(ArrayList<Competition> arrayList) {
		if (arrayList != null) {
			Competition[] comps = new Competition[arrayList.size()];
			for (int i = 0; i < arrayList.size(); i++) {
				comps[i] = arrayList.get(i);
			}
			if (compsList != null) {
				this.remove(compsList);
				this.remove(listPanel);
			}
			createCompList(comps);
			this.repaint();
			this.revalidate();
		} else {
			System.out
					.println("JoinedCompScreen heeft geen arrayList met deelnemers");
		}
	}

	private void createCompList(Competition[] comps) {
		compsList = new JList<Competition>(comps);
		compsList.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Competition comp = compsList.getSelectedValue();
				if (comp != null) {
					int reply3 = JOptionPane.showConfirmDialog(null,
							"Want to load the competion?", "Load competion",
							JOptionPane.YES_NO_OPTION);
					if (reply3 == JOptionPane.YES_OPTION) {
						framePanel.setJoinCompPlayerScreen(comp.getCompID());
					}
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
