package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class StartMenuBar extends JMenuBar {
	MainFrame mainFrame;

	public StartMenuBar(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		JMenu optionsMenu = new JMenu("Options");
		JMenuItem exitMenuItem = new JMenuItem("Exit");

		exitMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exitItemChosen();
			}
		});

		optionsMenu.add(exitMenuItem);
		this.add(optionsMenu);
	}

	private void exitItemChosen() {
		int retValue = JOptionPane.showConfirmDialog(mainFrame,
				"Are you sure you want to exit?", "Exit dialog",
				JOptionPane.YES_NO_OPTION);
		if (retValue == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}
}
