package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class SpecMenuBar extends JMenuBar{
	public SpecMenuBar(final MainFrame mainFrame){
		JMenu optionsMenu = new JMenu("Options");
		JMenuItem exitMenuItem = new JMenuItem("Exit");
		JMenuItem regMenuItem = new JMenuItem("Register");
		JMenuItem loginMenuItem = new JMenuItem("Login");
		
		exitMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		regMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				mainFrame.setRegScreen();
				mainFrame.setStartMenuBar();
			}
		});
		loginMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				mainFrame.setLoginScreen();
				mainFrame.setStartMenuBar();
			}			
		});
		
		optionsMenu.add(exitMenuItem);
		optionsMenu.add(regMenuItem);
		optionsMenu.add(loginMenuItem);
		
		this.add(optionsMenu);
	}
}
