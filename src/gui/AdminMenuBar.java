package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class AdminMenuBar extends StandardMenuBar {

	public AdminMenuBar(final MainFrame mainFrame) {
		super(mainFrame);

		JMenuItem mainscreenMenuItem = new JMenuItem("Mainscreen");

		optionsMenu.add(mainscreenMenuItem);

		mainscreenMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mainFrame.setAdminAccScreen();
			}
		});
	}
}
