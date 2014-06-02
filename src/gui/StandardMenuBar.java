package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import datalaag.WordFeudConstants;

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
	
	public void showNotificationWindow(){
		new NotificationWindow(mainFrame);
	}
	
	private void createMenuBars(){
		optionsMenu = new JMenu("Options");
		playerdataMenu = new JMenu("User data");
		notificationsMenu = new JMenu("Notifications");
		
		JMenuItem exitMenuItem = new JMenuItem("Exit");
		JMenuItem logoutMenuItem = new JMenuItem("Logout");
		JMenuItem changeRoleMenuItem = new JMenuItem("Change role");
		JMenuItem statisticsMenuItem = new JMenuItem("Statistics");
		JMenuItem accountdataMenuItem = new JMenuItem("Account data");
		JMenuItem openNotificationsMenuItem = new JMenuItem("Open notifications");

		optionsMenu.add(changeRoleMenuItem);
		optionsMenu.add(logoutMenuItem);
		optionsMenu.add(exitMenuItem);

		playerdataMenu.add(accountdataMenuItem);
		playerdataMenu.add(statisticsMenuItem);

		notificationsMenu.add(openNotificationsMenuItem);

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
		changeRoleMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeRoleItemChosen();
			}
		});

		statisticsMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				statwindow.showStats();
			}
		});
		accountdataMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				accdatawindow.showAccData();
			}
		});

		openNotificationsMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				notificationItemChosen();
			}
		});
	}
	
	private void notificationItemChosen() {
		mainFrame.startPendingMatchWorker();
	}

	private void changeRoleItemChosen(){
		String role = rolewindow.showChangeRole();
		int result = mainFrame.startChangeRoleWorker(role);
		if (result == WordFeudConstants.ROLE_CHANGE_SUCCES) {
			JOptionPane.showMessageDialog(mainFrame, "Role changed succesfully.");
			mainFrame.setCorrectRoleMainMenu();
		} else if (result == WordFeudConstants.ROLE_CHANGE_FAIL) {
			JOptionPane.showMessageDialog(mainFrame, "Something happened, no role change occured.");
		}
	}
	
	private void logoutItemChosen(){
		mainFrame.setStartMenuBar();
		mainFrame.setLoginScreen();
	}
	
	private void exitItemChosen(){
		int retValue = JOptionPane.showConfirmDialog(mainFrame, "Are you sure you want to exit?", "Exit dialog", JOptionPane.YES_NO_OPTION);
		if (retValue == JOptionPane.YES_OPTION){
			System.exit(0);
		}
	}
}
