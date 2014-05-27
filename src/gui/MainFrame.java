package gui;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Observer;
import java.util.Set;

import javax.swing.JFrame;
import domein.Administrator;
import domein.Competition;
import domein.PendingMatch;
import domein.User;
import domein.WordFeud;

@SuppressWarnings("serial")
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
	private StandardMenuBar standardMenuBar;
	private SpecMenuBar specMenuBar;
	private UpdateGUIThread guiThread;
	private WordFeud wf;

	public MainFrame(WordFeud wf) {
		this.wf = wf;
		startMenuBar = new StartMenuBar();
		loginscreen = new LoginScreen(this);
		standardMenuBar = new StandardMenuBar(this);
		this.setPreferredSize(new Dimension(1200, 700));
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("WordFeud");

		this.setContentPane(loginscreen);
		this.setJMenuBar(startMenuBar);

		this.pack();
		this.setLocationRelativeTo(null);
	}
	
	public void init(){
		specMenuBar = new SpecMenuBar(this);
		specscreen = new SpecScreen(this);
		regscreen = new RegScreen(this);
		playerscreen = new PlayerScreen(this);
		gameScreen = new GameScreen();
		specScreen = new GameSpecScreen();
		joincompscreen = new JoinCompScreen(this);
		joinedcompscreen = new JoinedCompScreen(this);
		adminaccscreen = new AdminAccScreen(this);
		admincompscreen = new AdminCompScreen(this);
		modscreen = new ModScreen();
		this.setVisible(true);
	}

	public void setRegScreen() {
		this.setContentPane(regscreen);
		wf.stopThread();
		revalidate();
	}

	public void setLoginScreen() {
		this.setContentPane(loginscreen);
		wf.stopThread();
		revalidate();
	}

	public void setPlayerScreen() {
		this.setContentPane(playerscreen);
		playerscreen.setGameList(wf.myActiveGames(), this.getName());
		wf.stopThread();
		revalidate();
	}

	public void setGameScreen() {
		this.setContentPane(gameScreen);
		wf.stopThread();
		revalidate();
	}

	public void setSpecScreen() {
		specscreen.setGameList(wf.getActiveGames());
		this.setContentPane(specscreen);
		wf.stopThread();
		revalidate();
	}

	public void setJoinCompScreen() {
		joincompscreen.populateScreen();
		this.setContentPane(joincompscreen);
		wf.stopThread();
		revalidate();
	}

	public void setJoinedCompScreen() {
		joinedcompscreen.populateScreen();
		this.setContentPane(joinedcompscreen);
		wf.stopThread();
		revalidate();
	}
	public void setAdminAccScreen() {
		this.setContentPane(adminaccscreen);
		adminaccscreen.fillPlayerList();
		wf.stopThread();
		revalidate();
	}

	public void setAdminCompScreen() {
		this.setContentPane(admincompscreen);
		wf.stopThread();
		revalidate();
	}

	public void setModScreen() {
		this.setContentPane(modscreen);
		modscreen.fillList();
		wf.stopThread();
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

	public void setStandardMenuBar() {
		this.setJMenuBar(standardMenuBar);
		revalidate();
	}

	public String callRegisterAction(String username, char[] passInput,
			char[] passConfirm) {
		return wf.doRegisterAction(username, passInput, passConfirm);
	}

	public String callLoginAction(String username, char[] password) {
		return wf.doLoginAction(username, password);
	}

	public boolean callChangeRoleAction(String result) {
		return wf.doChangeRoleAction(result);
	}

	public void callLogoutAction() {
		wf.doLogoutAction();
		joincompscreen.clearLists();
		joinedcompscreen.clearLists();
	}

	public void fillRoleWindow() {
		standardMenuBar.fillRoleWindow(wf.getCurrentUserPossibleRoles());
	}

	public void setAccDataValues() {
		standardMenuBar.fillAccDataValues(wf.getCurrentUsername(),
				wf.getCurrentPassword());
	}

	public String getName() {
		return wf.getCurrentUsername();
	}

	public User getuser() {
		return wf.getCurrentUser();
	}

	public Administrator getAdmin() {
		return wf.getCurrentUser().getAdmin();
	}

	// PlayGame/Spectator part
	// Adds the observers to all the panels
	// Sets the right ContentPane
	public void addObservers(Observer observer, boolean spectator) {
		if (spectator) {
			this.setJMenuBar(standardMenuBar);
			this.setContentPane(specScreen);
			this.pack();
			System.out.println("MainFRAMEPANEL - set content specScreen");
			specScreen.addObserverToObserverButtons(observer);
			revalidate();
		} else {
			this.setJMenuBar(standardMenuBar);
			this.setContentPane(gameScreen);
			this.pack();
			gameScreen.addObservers(observer);
			System.out.println("MainFRAMEPANEL - set content gameScreen");
			revalidate();
		}
	}

	public void startGame(int gameToLoad, boolean spectating) {
		//DatabaseHandler dbh = DatabaseHandler.getInstance();
		if (gameToLoad != 0 && !spectating) {
			wf.startGame(gameToLoad, false, false);
			System.out.println("GAMEID IS " + gameToLoad);
		} else if (spectating) {
			wf.startGame(gameToLoad, true, false);
			System.out.println("GAMEID IS " + gameToLoad);
		}
		/*
		 * else { String name = JOptionPane.showInputDialog(null,
		 * "Please enter your GameID: "); if (name == null || name.equals("")) {
		 * int gameID = dbh.createGame(1, "mike", "wouter", "openbaar", "EN");
		 * wf.startGame(gameID, false, true); System.out.println("GAMEID IS " +
		 * gameID); } else if (name.equals("spec")) { String name2 =
		 * JOptionPane.showInputDialog(null, "Please enter your GameID: ");
		 * wf.startGame(Integer.parseInt(name2), true, false);
		 * System.out.println("GAMEID IS " + Integer.parseInt(name2)); } else {
		 * int gameID = Integer.parseInt(name); if
		 * (!dbh.getGameStatusValue(gameID).equals("Finished") ||
		 * !dbh.getGameStatusValue(gameID).equals("Resigend")) {
		 * wf.startGame(gameID, false, false); System.out.println("GAMEID IS " +
		 * gameID); } else { JOptionPane.showMessageDialog(null,
		 * "Can't load this game", "Loading error!", JOptionPane.OK_OPTION); } }
		 * }
		 */
	}

	public void callChallengePlayerAction(String competitionID, String opponent) {
		wf.doChallengePlayerAction(competitionID, opponent);
	}

	// Returns the gameScreen
	public GameScreen getGameScreen() {
		return gameScreen.getGameScreen();
	}

	// Returns the specScreen
	public GameSpecScreen getSpecScreen() {
		return specScreen;
	}

	public void setCorrectRoleMainMenu() {
		String currentRole = wf.getCurrentUserRole();

		if (currentRole.equals("Administrator")) {
			this.setAdminCompScreen();
		} else if (currentRole.equals("Moderator")) {
			this.setModScreen();
		} else if (currentRole.equals("Player")) {
			this.setPlayerScreen();
		} else if (currentRole.equals("Spectator")) {
			this.setSpecScreen();
			
		}
	}

	// Everything in this method will be updated every 20 seconds
	// Use synchronized for the methods
	// That allows a method to be uses by multiple threads
	// Only the current contentPane will auto update
	public synchronized void updateGUI() {
		standardMenuBar.updateNotificationList();
		if (this.getContentPane() instanceof PlayerScreen) {
			updatePlayerGameList();
		} else if (this.getContentPane() instanceof SpecScreen) {
			specscreen.setGameList(wf.getActiveGames());
		}
	}

	// Update the mainscreen games from the player
	public void updatePlayerGameList() {
		playerscreen.setGameList(wf.myActiveGames(), this.getName());
	}

	// A method to start the Thread
	public void startThread() {
		guiThread = new UpdateGUIThread(this);
		guiThread.setRunning(true);
		guiThread.start();
	}

	// Stops the Thread
	public void stopThread() {
		if (guiThread != null) {
			guiThread.setRunning(false);
		}
	}

	// Returns a list of pending Games
	public ArrayList<PendingMatch> getPendingGames() {
		return wf.getPendingGames();
	}

	// Method to accept/reject games
	public void acceptRejectGame(String string, int competionID, int gameID) {
		wf.acceptRejectGame(string, competionID, gameID);
	}

	public void callCreateCompAction(String summaryString, String compEnd,
			int i, int maxPlayersInt) {
		wf.doCreateCompAction(summaryString, compEnd, i, maxPlayersInt);
	}

	public  Set<Entry<String, Competition>> callGetAllCompetitionsAction() {
		return wf.doGetAllCompetitionsAction();
	}
	
	public Set<Entry<String, Competition>> callGetJoinedCompetitionsAction() {
		return wf.doGetJoinedCompetitionsAction();
	}

	public Competition callGetOneCompetitionAction(String key) {
		return wf.doGetOneCompetitionAction(key);
	}
	
	public void callLoadAllCompetitionsAction(){
		wf.doLoadAllCompetitionsAction();
	}
	
	public void callJoinCompetitionAction(String compID){
		wf.doJoinCompAction(compID);
	}

	public void callLoadJoinedCompetitionsAction() {
		wf.doLoadJoinedCompetitionsAction();
	}
}
