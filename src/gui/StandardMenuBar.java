package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class StandardMenuBar extends JMenuBar {
	private MainFrame mainFrame;
	protected JMenu optionsMenu;
	protected JMenu playerdataMenu;
	protected JMenu notificationsMenu;
	private RoleWindow rolewindow;
	private StatWindow statwindow;
	private AccDataWindow accdatawindow;

	public StandardMenuBar(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		rolewindow = new RoleWindow();
		statwindow = new StatWindow();
		accdatawindow = new AccDataWindow(mainFrame);
		
		this.createMenuBars();
		
		this.add(optionsMenu);
		this.add(playerdataMenu);
		this.add(notificationsMenu);
	}

	public void fillRoleWindow(ArrayList<String> roles) {
		rolewindow.fillRoleList(roles);
	}

	public void fillAccDataValues(String username, String password) {
		accdatawindow.setValues(username, password);
	}
	
	private void createMenuBars(){
		optionsMenu = new JMenu("Options");
		playerdataMenu = new JMenu("User data");
		notificationsMenu = new JMenu("Notifications");
		
		JMenuItem exitMenuItem = new JMenuItem("Exit");
		JMenuItem logoutMenuItem = new JMenuItem("Logout");
		JMenuItem changeroleMenuItem = new JMenuItem("Change role");
		JMenuItem statisticsMenuItem = new JMenuItem("Statistics");
		JMenuItem accountdataMenuItem = new JMenuItem("Account data");
		JMenuItem opennotificationsMenuItem = new JMenuItem("Open notifications");

		optionsMenu.add(changeroleMenuItem);
		optionsMenu.add(logoutMenuItem);
		optionsMenu.add(exitMenuItem);

		playerdataMenu.add(accountdataMenuItem);
		playerdataMenu.add(statisticsMenuItem);

		notificationsMenu.add(opennotificationsMenuItem);

		logoutMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logoutItemChosen();
			}
		});
		exitMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exitItemChosen();
			}
		});
		changeroleMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeRoleItemChosen();
			}
		});

		statisticsMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				statwindow.showStats(mainFrame);
			}
		});
		accountdataMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				accdatawindow.showAccData();
			}
		});

		opennotificationsMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				notificationItemChosen();
			}
		});
	}
	
	private void changeRoleItemChosen(){
		String result = rolewindow.showChangeRole();
		boolean isSuccesful = mainFrame.callChangeRoleAction(result);
		if (isSuccesful) {
			JOptionPane.showMessageDialog(mainFrame, "Role changed succesfully.");
			mainFrame.setCorrectRoleMainMenu();
		} else {
			JOptionPane.showMessageDialog(mainFrame, "Something happened, no role change occured.");
		}
	}
	
	private void logoutItemChosen(){
		mainFrame.setLoginScreen();
		mainFrame.setStartMenuBar();
		mainFrame.callLogoutAction();
	}
	
	private void exitItemChosen(){
		int retValue = JOptionPane.showConfirmDialog(mainFrame, "Are you sure you want to exit?", "Exit dialog", JOptionPane.YES_NO_OPTION);
		if (retValue == JOptionPane.YES_OPTION){
			System.exit(0);
		}
	}
	
	private void notificationItemChosen(){
		new NotificationWindow(mainFrame);
	}
}
