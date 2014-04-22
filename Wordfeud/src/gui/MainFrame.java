package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class MainFrame extends JFrame {
	private LoginScreen loginscreen;
	private RegScreen regscreen;
	private PlayerScreen playerscreen;
	private AdminAccScreen adminAccScreen;
	private JoinedCompScreen joinedcompscreen;
	private JMenuBar basicMenuBar;
	private JMenuBar playerMenuBar;
	private JMenuBar spectatorMenuBar;
	private JMenuBar administratorMenuBar;
	private JMenuBar moderatorMenuBar;

	public MainFrame() {
		loginscreen = new LoginScreen(this);
		regscreen = new RegScreen(this);
		playerscreen = new PlayerScreen(this);
		joinedcompscreen = new JoinedCompScreen();
		adminAccScreen = new AdminAccScreen();

		this.setPreferredSize(new Dimension(1200, 700));
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("WordFeud");

		createBasicMenuBar();
		createSpectatorMenuBar();
		createPlayerMenuBar();
		createModeratorMenuBar();
		createAdministratorMenuBar();

		this.setContentPane(loginscreen);
		this.setJMenuBar(basicMenuBar);

		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	private void createPlayerMenuBar() {
		playerMenuBar = new JMenuBar();

		JMenu optionsMenu = new JMenu("Options");
		JMenu playerdataMenu = new JMenu("Playerdata");
		JMenu notificationsMenu = new JMenu("Notifications");
		JMenuItem exitMenuItem = new JMenuItem("Exit");
		JMenuItem mainscreenMenuItem = new JMenuItem("Mainscreen");
		JMenuItem logoffMenuItem = new JMenuItem("Log off");
		JMenuItem changeroleMenuItem = new JMenuItem("Change role");
		JMenuItem statisticsMenuItem = new JMenuItem("Statistics");
		JMenuItem accountdataMenuItem = new JMenuItem("Account data");

		optionsMenu.add(mainscreenMenuItem);
		optionsMenu.add(changeroleMenuItem);
		optionsMenu.add(logoffMenuItem);
		optionsMenu.add(exitMenuItem);

		playerdataMenu.add(statisticsMenuItem);
		playerdataMenu.add(accountdataMenuItem);
		
		mainscreenMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				setPlayerScreen();
			}
		});
		logoffMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				setLoginScreen();
				setBasicMenuBar();
			}
		});
		exitMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		playerMenuBar.add(optionsMenu);
		playerMenuBar.add(playerdataMenu);
		playerMenuBar.add(notificationsMenu);
	}

	private void createAdministratorMenuBar() {
		administratorMenuBar = new JMenuBar();

		JMenu optionsMenu = new JMenu("Options");
		JMenu playerdataMenu = new JMenu("Playerdata");
		JMenu notificationsMenu = new JMenu("Notifications");
		JMenu adminoptionsMenu = new JMenu("Administrator options");
		JMenuItem exitMenuItem = new JMenuItem("Exit");
		JMenuItem logoffMenuItem = new JMenuItem("Log off");
		JMenuItem changeroleMenuItem = new JMenuItem("Change role");
		JMenuItem statisticsMenuItem = new JMenuItem("Statistics");
		JMenuItem accountdataMenuItem = new JMenuItem("Account data");
		JMenuItem playeroptionsMenuItem = new JMenuItem("Player options");
		JMenuItem competitionoptionsMenuItem = new JMenuItem(
				"Competition options");

		optionsMenu.add(changeroleMenuItem);
		optionsMenu.add(logoffMenuItem);
		optionsMenu.add(exitMenuItem);

		playerdataMenu.add(statisticsMenuItem);
		playerdataMenu.add(accountdataMenuItem);

		adminoptionsMenu.add(playeroptionsMenuItem);
		adminoptionsMenu.add(competitionoptionsMenuItem);

		logoffMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				setLoginScreen();
				setBasicMenuBar();
			}
		});
		exitMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		administratorMenuBar.add(optionsMenu);
		administratorMenuBar.add(playerdataMenu);
		administratorMenuBar.add(notificationsMenu);
		administratorMenuBar.add(adminoptionsMenu);
	}

	private void createModeratorMenuBar() {
		moderatorMenuBar = new JMenuBar();

		JMenu optionsMenu = new JMenu("Options");
		JMenu playerdataMenu = new JMenu("Playerdata");
		JMenu notificationsMenu = new JMenu("Notifications");
		JMenu modoptionsMenu = new JMenu("Moderator options");
		JMenuItem exitMenuItem = new JMenuItem("Exit");
		JMenuItem logoffMenuItem = new JMenuItem("Log off");
		JMenuItem changeroleMenuItem = new JMenuItem("Change role");
		JMenuItem statisticsMenuItem = new JMenuItem("Statistics");
		JMenuItem accountdataMenuItem = new JMenuItem("Account data");
		JMenuItem checkwordMenuItem = new JMenuItem("Check word");

		optionsMenu.add(changeroleMenuItem);
		optionsMenu.add(logoffMenuItem);
		optionsMenu.add(exitMenuItem);

		playerdataMenu.add(statisticsMenuItem);
		playerdataMenu.add(accountdataMenuItem);

		modoptionsMenu.add(checkwordMenuItem);

		logoffMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				setLoginScreen();
				setBasicMenuBar();
			}
		});
		exitMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		moderatorMenuBar.add(optionsMenu);
		moderatorMenuBar.add(playerdataMenu);
		moderatorMenuBar.add(notificationsMenu);
		moderatorMenuBar.add(modoptionsMenu);
	}

	private void createSpectatorMenuBar() {
		spectatorMenuBar = new JMenuBar();
	}

	private void createBasicMenuBar() {
		basicMenuBar = new JMenuBar();
		JMenu optionsMenu = new JMenu("Options");
		JMenuItem exitMenuItem = new JMenuItem("Exit");

		exitMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}

		});

		optionsMenu.add(exitMenuItem);
		basicMenuBar.add(optionsMenu);
	}

	public void setRegScreen() {
		this.setContentPane(regscreen);
		revalidate();
	}

	public void setLoginScreen() {
		this.setContentPane(loginscreen);
		revalidate();
	}

	public void setPlayerScreen() {
		this.setContentPane(playerscreen);
		revalidate();
	}

	public void setJoinedCompScreen() {
		this.setContentPane(joinedcompscreen);
		revalidate();
	}
	
	public void setAdminAccScreen(){
		this.setContentPane(adminAccScreen);
		revalidate();
	}

	public void setBasicMenuBar() {
		this.setJMenuBar(basicMenuBar);
		revalidate();
	}

	public void setSpectatorMenuBar() {
		this.setJMenuBar(spectatorMenuBar);
		revalidate();
	}

	public void setPlayerMenuBar() {
		this.setJMenuBar(playerMenuBar);
		revalidate();
	}

	public void setModeratorMenuBar() {
		this.setJMenuBar(moderatorMenuBar);
		revalidate();
	}

	public void setAdministratorMenuBar() {
		this.setJMenuBar(administratorMenuBar);
		revalidate();
	}
}
