package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class StandardMenuBar extends JMenuBar {
	protected JMenu optionsMenu;
	protected JMenu playerdataMenu;
	protected JMenu notificationsMenu;
	private RoleWindow rolewindow;
	private StatWindow statwindow;
	private NotificationWindow notificationwindow;
	private AccDataWindow accdatawindow;

	public StandardMenuBar(final MainFrame mainFrame) {
		rolewindow = new RoleWindow();	
		statwindow = new StatWindow();
		notificationwindow = new NotificationWindow();
		accdatawindow = new AccDataWindow();
		optionsMenu = new JMenu("Options");
		playerdataMenu = new JMenu("Playerdata");
		notificationsMenu = new JMenu("Notifications");
		JMenuItem exitMenuItem = new JMenuItem("Exit");
		JMenuItem logoffMenuItem = new JMenuItem("Log off");
		JMenuItem changeroleMenuItem = new JMenuItem("Change role");
		JMenuItem statisticsMenuItem = new JMenuItem("Statistics");
		JMenuItem accountdataMenuItem = new JMenuItem("Account data");
		JMenuItem opennotificationsMenuItem = new JMenuItem("Open notifications");

		optionsMenu.add(exitMenuItem);
		optionsMenu.add(logoffMenuItem);
		optionsMenu.add(changeroleMenuItem);

		playerdataMenu.add(statisticsMenuItem);
		playerdataMenu.add(accountdataMenuItem);
		
		notificationsMenu.add(opennotificationsMenuItem);

		logoffMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainFrame.setLoginScreen();
				mainFrame.setStartMenuBar();
			}
		});
		exitMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		changeroleMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				rolewindow.ShowchangeRole();
			}
		});
		
		statisticsMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				statwindow.showStats();
			}
		});
		accountdataMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				accdatawindow.showAccData();
			}});
		
		opennotificationsMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				notificationwindow.openNotificationWindow();
			}});
		

		this.add(optionsMenu);
		this.add(playerdataMenu);
		this.add(notificationsMenu);
	}
}
