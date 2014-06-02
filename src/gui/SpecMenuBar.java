package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class SpecMenuBar extends JMenuBar {
	MainFrame mainFrame;
	public SpecMenuBar(MainFrame mainFrame){
		this.mainFrame = mainFrame;
		JMenu optionsMenu = new JMenu("Options");
		JMenuItem exitMenuItem = new JMenuItem("Exit");
		JMenuItem regMenuItem = new JMenuItem("Register");
		JMenuItem loginMenuItem = new JMenuItem("Login");
		
		exitMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				exitItemChosen();
			}
		});
		regMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				registerItemChosen();
			}
		});
		loginMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				loginItemChosen();
			}			
		});
		
		optionsMenu.add(exitMenuItem);
		optionsMenu.add(regMenuItem);
		optionsMenu.add(loginMenuItem);
		
		this.add(optionsMenu);
	}

	private void exitItemChosen(){
		int retValue = JOptionPane.showConfirmDialog(mainFrame, "Are you sure you want to exit?", "Exit dialog", JOptionPane.YES_NO_OPTION);
		if (retValue == JOptionPane.YES_OPTION){
			System.exit(0);
		}
	}

	private void registerItemChosen() {
		mainFrame.setRegScreen();
		mainFrame.setStartMenuBar();
	}

	private void loginItemChosen() {
		mainFrame.setLoginScreen();
		mainFrame.setStartMenuBar();
	}
}
