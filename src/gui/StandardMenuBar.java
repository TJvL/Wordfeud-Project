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
	private NotificationWindow notificationwindow;
	private AccDataWindow accdatawindow;
	private String notificationWindowName;

	public StandardMenuBar(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		rolewindow = new RoleWindow();
		notificationWindowName = "Notifications (0)";
		rolewindow = new RoleWindow();
		statwindow = new StatWindow();
		notificationwindow = new NotificationWindow(mainFrame);
		accdatawindow = new AccDataWindow(mainFrame);
		optionsMenu = new JMenu("Options");
		playerdataMenu = new JMenu("Playerdata");
		notificationsMenu = new JMenu(notificationWindowName);
		JMenuItem exitMenuItem = new JMenuItem("Exit");
		JMenuItem logoutMenuItem = new JMenuItem("Logout");
		JMenuItem changeroleMenuItem = new JMenuItem("Change role");
		JMenuItem statisticsMenuItem = new JMenuItem("Statistics");
		JMenuItem accountdataMenuItem = new JMenuItem("Account data");
		JMenuItem opennotificationsMenuItem = new JMenuItem(
				"Open notifications");

		optionsMenu.add(exitMenuItem);
		optionsMenu.add(logoutMenuItem);
		optionsMenu.add(changeroleMenuItem);

		playerdataMenu.add(statisticsMenuItem);
		playerdataMenu.add(accountdataMenuItem);

		notificationsMenu.add(opennotificationsMenuItem);

		logoutMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logoutItemChosen();
			}
		});
		exitMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		changeroleMenuItem.addActionListener(new ActionListener() {
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

		opennotificationsMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				notificationwindow.openNotificationWindow();
			}
		});

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

	public synchronized void updateNotificationList() {
		// notificationwindow.set
		notificationWindowName = "Notifications ("
				+ notificationwindow.fillNotificationList() + ")";
		notificationwindow.setTitle(notificationWindowName);
		notificationsMenu.setText(notificationWindowName);
		this.repaint();
		this.revalidate();
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
}
