package gui;

import java.awt.Dimension;
import javax.swing.JFrame;

public class MainFrame extends JFrame {
	private LoginScreen loginscreen;
	private SpecScreen specscreen;
	private RegScreen regscreen;
	private PlayerScreen playerscreen;
	private GameScreen gamescreen;
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

	public MainFrame() {
		startMenuBar = new StartMenuBar();
		specMenuBar = new SpecMenuBar(this);
		playerMenuBar = new PlayerMenuBar(this);
		modMenuBar = new ModMenuBar(this);
		adminMenuBar = new AdminMenuBar(this);
		loginscreen = new LoginScreen(this);
		specscreen = new SpecScreen();
		regscreen = new RegScreen(this);
		playerscreen = new PlayerScreen(this);
		gamescreen = new GameScreen();
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
	
	public void setGameScreen(){
		this.setContentPane(gamescreen);
		revalidate();
	}
	
	public void setSpecScreen(){
		this.setContentPane(specscreen);
		revalidate();
	}
	
	public void setJoinCompScreen(){
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
	
	public void setAdminCompScreen(){
		this.setContentPane(admincompscreen);
		revalidate();
	}
	
	public void setModScreen(){
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
}
