package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class StartMenuBar extends JMenuBar{
	public StartMenuBar(){
		JMenu optionsMenu = new JMenu("Options");
		JMenuItem exitMenuItem = new JMenuItem("Exit");

		exitMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}

		});

		optionsMenu.add(exitMenuItem);
		this.add(optionsMenu);
	}
}
