package gui;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class AdminMenuBar extends StandardMenuBar{

	public AdminMenuBar(MainFrame mainFrame) {
		super(mainFrame);
		
		JMenu adminoptionsMenu = new JMenu("Administrator options");
		JMenuItem playeroptionsMenuItem = new JMenuItem("Player options");
		JMenuItem competitionoptionsMenuItem = new JMenuItem(
				"Competition options");
		
		adminoptionsMenu.add(playeroptionsMenuItem);
		adminoptionsMenu.add(competitionoptionsMenuItem);

		this.add(adminoptionsMenu);
	}

}
