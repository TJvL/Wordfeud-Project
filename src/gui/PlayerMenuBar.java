package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

public class PlayerMenuBar extends StandardMenuBar {

	public PlayerMenuBar(final MainFrame mainFrame) {
		super(mainFrame);

		JMenuItem mainscreenMenuItem = new JMenuItem("Mainscreen");

		optionsMenu.add(mainscreenMenuItem);

		mainscreenMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mainFrame.setPlayerScreen();
			}
		});
	}
}
