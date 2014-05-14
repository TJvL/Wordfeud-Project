package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import datalaag.DatabaseHandler;
import domein.WordFeud;

public class MainFrame extends JFrame {
	private LoginScreen loginscreen;
	private SpecScreen specscreen;
	private RegScreen regscreen;
	private PlayerScreen playerscreen;
	private GameScreen gameScreen;
	private GameSpecScreen specScreen;
	private AdminAccScreen adminaccscreen;
	private AdminCompScreen admincompscreen;
	private JoinCompScreen joincompscreen;
	private JoinedCompScreen joinedcompscreen;
	private ModScreen modscreen;
	private StartMenuBar startMenuBar;
	private PlayerMenuBar playerMenuBar;
	private SpecMenuBar specMenuBar;
	private AdminMenuBar adminMenuBar;
	private ModMenuBar modMenuBar;
	private WordFeud wf;

	public MainFrame(final WordFeud wf) {
		startMenuBar = new StartMenuBar();
		specMenuBar = new SpecMenuBar(this);
		playerMenuBar = new PlayerMenuBar(this);
		modMenuBar = new ModMenuBar(this);
		adminMenuBar = new AdminMenuBar(this);
		loginscreen = new LoginScreen(this);
		specscreen = new SpecScreen();
		regscreen = new RegScreen(this);
		playerscreen = new PlayerScreen(this);
		gameScreen = new GameScreen();
		specScreen = new GameSpecScreen();
		joincompscreen = new JoinCompScreen();
		joinedcompscreen = new JoinedCompScreen();
		adminaccscreen = new AdminAccScreen(this);
		admincompscreen = new AdminCompScreen(this);
		modscreen = new ModScreen();

		this.setPreferredSize(new Dimension(1200, 700));
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("WordFeud");

		this.setContentPane(loginscreen);
		this.setJMenuBar(startMenuBar);
		this.wf = wf;

		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
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

	public void setGameScreen() {
		this.setContentPane(gameScreen);
		revalidate();
	}

	public void setSpecScreen() {
		this.setContentPane(specscreen);
		revalidate();
	}

	public void setJoinCompScreen() {
		this.setContentPane(joincompscreen);
		revalidate();
	}

	public void setJoinedCompScreen() {
		this.setContentPane(joinedcompscreen);
		revalidate();
	}

	public void setAdminAccScreen() {
		this.setContentPane(adminaccscreen);
		revalidate();
	}

	public void setAdminCompScreen() {
		this.setContentPane(admincompscreen);
		revalidate();
	}

	public void setModScreen() {
		this.setContentPane(modscreen);
		revalidate();
	}

	public void setStartMenuBar() {
		this.setJMenuBar(startMenuBar);
		revalidate();
	}

	public void setSpecMenuBar() {
		this.setJMenuBar(specMenuBar);
		revalidate();
	}

	public void setPlayerMenuBar() {
		this.setJMenuBar(playerMenuBar);
		revalidate();
	}

	public void setModMenuBar() {
		this.setJMenuBar(modMenuBar);
		revalidate();
	}

	public void setAdminMenuBar() {
		this.setJMenuBar(adminMenuBar);
		revalidate();
	}

	// PlayGame/Spectator part
	// Adds the observers to all the panels
	// Sets the right ContentPane
	public void addObservers(Observer observer, boolean spectator) {
		if (spectator) {
			this.setJMenuBar(playerMenuBar);
			this.setContentPane(specScreen);
			this.pack();
			System.out.println("MainFRAMEPANEL - set content specScreen");
			specScreen.addObserverToObserverButtons(observer);
			revalidate();
		} else {
			this.setJMenuBar(playerMenuBar);
			this.setContentPane(gameScreen);
			this.pack();
			gameScreen.addObservers(observer);
			System.out.println("MainFRAMEPANEL - set content gameScreen");
			revalidate();
		}
	}

	public void startGame() {
		DatabaseHandler dbh = DatabaseHandler.getInstance();
		String name = JOptionPane.showInputDialog(null,
				"Please enter your GameID: ");
		if (name == null || name.equals("")) {
			int gameID = dbh.createGame(1, "jager684", "marijntje42",
					"openbaar", "EN");
			wf.startGame(gameID, false, true);
		} else if (name.equals("spec")) {
			String name2 = JOptionPane.showInputDialog(null,
					"Please enter your GameID: ");
			wf.startGame(Integer.parseInt(name2), true, false);

		} else {
			int gameID = Integer.parseInt(name);
			if (!dbh.getGameStatusValue(gameID).equals("Finished")
					|| !dbh.getGameStatusValue(gameID).equals("Resigend")) {
				wf.startGame(gameID, false, false);
			} else {
				JOptionPane.showMessageDialog(null, "Can't load this game",
						"Loading error!", JOptionPane.OK_OPTION);
			}
		}

	}

	// Gets a gameScreen
	public GameScreen getGameScreen() {
		return gameScreen.getGameScreen();
	}

	// Gets the specScreen
	public GameSpecScreen getSpecScreen() {
		return specScreen;
	}
}
