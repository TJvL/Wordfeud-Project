package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import domein.Statistics;
import domein.WordFeud;

@SuppressWarnings("serial")
public class StandardMenuBar extends JMenuBar {
	protected JMenu optionsMenu;
	protected JMenu playerdataMenu;
	protected JMenu notificationsMenu;
	private RoleWindow rolewindow;
	private StatWindow statwindow;
	private NotificationWindow notificationwindow;
	private AccDataWindow accdatawindow;
	private WordFeud wordFeud;
	private Statistics statistics;

	public StandardMenuBar(final MainFrame mainFrame, WordFeud wordfeud) {
		
		this.wordFeud = wordfeud;
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
				mainFrame.callLogoutAction();
			}
		});
		exitMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		changeroleMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				String result = rolewindow.showChangeRole();
				boolean isSuccesful = mainFrame.callChangeRoleAction(result);
				if (isSuccesful){
					System.out.println("Role changed succesfully.");
					mainFrame.setCorrectRoleMainMenu();
				}
				else{
					System.err.println("ERROR: Role not changed!");
				}
			}
		});
		
		statisticsMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				statistics = new Statistics();
				statistics.getUserName(mainFrame.getUsernameForStats());
				statwindow.showStats(statistics.getHighestGameScore(), statistics.getNumGamesPlayed(), statistics.mostValuableWord(), statistics.getGamesWon(), statistics.getCompetitionsWon());
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
