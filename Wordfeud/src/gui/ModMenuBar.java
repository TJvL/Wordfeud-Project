package gui;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class ModMenuBar extends StandardMenuBar{

	public ModMenuBar(MainFrame mainFrame) {
		super(mainFrame);
		
		JMenu modoptionsMenu = new JMenu("Moderator options");
		JMenuItem checkwordMenuItem = new JMenuItem("Check word");
		
		modoptionsMenu.add(checkwordMenuItem);
		
		this.add(modoptionsMenu);
	}

}
