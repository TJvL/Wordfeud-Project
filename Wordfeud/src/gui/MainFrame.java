package gui;

import java.awt.Dimension;
import javax.swing.JFrame;

public class MainFrame extends JFrame {
	private LoginScreen loginscreen;
	private RegScreen regscreen;
	private PlayerScreen playerscreen;
	private AdminAccScreen adminAccscreen;
	private JoinedCompScreen joinedcompscreen;
	private StartMenuBar startMenuBar;
	private PlayerMenuBar playerMenuBar;
	private SpecMenuBar specMenuBar;
	private AdminMenuBar adminMenuBar;
	private ModMenuBar modMenuBar;

	public MainFrame() {
		startMenuBar = new StartMenuBar();
		specMenuBar = new SpecMenuBar(this);
		playerMenuBar = new PlayerMenuBar(this);
		modMenuBar = new ModMenuBar(this);
		adminMenuBar = new AdminMenuBar(this);
		loginscreen = new LoginScreen(this);
		regscreen = new RegScreen(this);
		playerscreen = new PlayerScreen(this);
		joinedcompscreen = new JoinedCompScreen();
		adminAccscreen = new AdminAccScreen();

		this.setPreferredSize(new Dimension(1200, 700));
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("WordFeud");

		this.setContentPane(loginscreen);
		this.setJMenuBar(startMenuBar);

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

	public void setJoinedCompScreen() {
		this.setContentPane(joinedcompscreen);
		revalidate();
	}

	public void setAdminAccScreen() {
		this.setContentPane(adminAccscreen);
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
}
