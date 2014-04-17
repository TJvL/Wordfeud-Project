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
	private LoginScreen loginoptionsscreen;
	private RegistrationScreen registrationoptionsscreen;
	private PlayerOptionsScreen playeroptionsscreen;
	private JMenuBar basicMenuBar;
	private JMenuBar playerMenuBar;
	private JMenuBar spectatorMenuBar;
	private JMenuBar administratorMenuBar;
	private JMenuBar moderatorMenuBar;

	public MainFrame() {
		loginoptionsscreen = new LoginScreen(this);
		registrationoptionsscreen = new RegistrationScreen(this);
		playeroptionsscreen = new PlayerOptionsScreen();

		this.setPreferredSize(new Dimension(1200, 700));
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("WordFeud");

		createBasicMenuBar();
		createSpectatorMenuBar();
		createPlayerMenuBar();
		createModeratorMenuBar();
		createAdministratorMenuBar();

		this.setContentPane(loginoptionsscreen);
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

	public void setRegistrationOptionsScreen() {
		this.setContentPane(registrationoptionsscreen);
		revalidate();
	}

	public void setLoginOptionsScreen() {
		this.setContentPane(loginoptionsscreen);
		revalidate();
	}
	
	public void setPlayerOptionsScreen(){
		this.setContentPane(playeroptionsscreen);
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
