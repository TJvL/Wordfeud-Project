package gui;

import java.awt.Dimension;
import java.util.Map.Entry;
import java.util.Observer;
import java.util.Set;

import javax.swing.JFrame;

import datalaag.WordFeudConstants;
import domein.ActiveMatch;
import domein.Administrator;
import domein.Competition;
import domein.MatchManager;
import domein.PendingMatch;
import domein.User;
import domein.WordFeud;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	private LoginScreen loginScreen;
	private SpecScreen specScreen;
	private RegScreen regScreen;
	private PlayerScreen playerScreen;
	private GameScreen gameScreen;
	private GameSpecScreen gameSpecScreen;
	private AdminAccScreen adminAccScreen;
	private AdminCompScreen adminCompScreen;
	private JoinCompScreen joinCompScreen;
	private JoinedCompScreen joinedCompScreen;
	private ModScreen modScreen;
	private StartMenuBar startMenuBar;
	private StandardMenuBar standardMenuBar;
	private SpecMenuBar specMenuBar;
	private LoadingPanel loadingPanel;
	private Thread t;
	private WordFeud wordFeud;

	public MainFrame(WordFeud wordFeud) {
		this.wordFeud = wordFeud;
		startMenuBar = new StartMenuBar(this);
		loginScreen = new LoginScreen(this);

		this.setPreferredSize(new Dimension(1200, 700));
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setTitle("WordFeud");

		this.setContentPane(loginScreen);
		this.setJMenuBar(startMenuBar);

		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public void init() {
		specMenuBar = new SpecMenuBar(this);
		specScreen = new SpecScreen(this);
		regScreen = new RegScreen(this);
		playerScreen = new PlayerScreen(this);
		gameScreen = new GameScreen(this);
		gameSpecScreen = new GameSpecScreen(this);
		joinCompScreen = new JoinCompScreen(this);
		joinedCompScreen = new JoinedCompScreen(this);
		adminAccScreen = new AdminAccScreen(this);
		adminCompScreen = new AdminCompScreen(this);
		standardMenuBar = new StandardMenuBar(this);
		modScreen = new ModScreen();
		loadingPanel = new LoadingPanel();
		wordFeud.setPanelsReferences(gameScreen.getGameChatPanel(), gameScreen.getButtonPanel(), gameScreen.getScorePanel());
	}

	public void setLoadingScreen() {
		t = new Thread(loadingPanel);
		loadingPanel.setRunning(true);
		t.start();
		this.setContentPane(loadingPanel);
	}

	public void stopLoadingScreen() {
		loadingPanel.setRunning(false);
	}

	public void setRegScreen() {
		this.setContentPane(regScreen);
		wordFeud.stopThread();
		revalidate();
	}

	public void setLoginScreen() {
		this.setContentPane(loginScreen);
		wordFeud.stopThread();
		revalidate();
	}

	public void setPlayerScreen() {
		this.setContentPane(playerScreen);
		playerScreen.setGameList(wordFeud.myActiveGames(), this.getName());
		wordFeud.stopThread();
		stopLoadingScreen();
		revalidate();
	}

	public void setGameScreen() {
		this.setContentPane(gameScreen);
		wordFeud.stopThread();
		revalidate();
	}

	public void setSpecScreen() {
		specScreen.setGameList(wordFeud.getActiveGames());
		this.setContentPane(specScreen);
		wordFeud.stopThread();
		revalidate();
	}

	public void setJoinCompScreen() {
		joinCompScreen.populateScreen();
		this.setContentPane(joinCompScreen);
		wordFeud.stopThread();
		revalidate();
	}

	public void setJoinedCompScreen() {
		joinedCompScreen.populateScreen();
		this.setContentPane(joinedCompScreen);
		wordFeud.stopThread();
		revalidate();
	}

	public void setAdminAccScreen() {
		this.setContentPane(adminAccScreen);
		adminAccScreen.fillPlayerList();
		wordFeud.stopThread();
		revalidate();
	}

	public void setAdminCompScreen() {
		adminCompScreen.populateScreen();
		this.setContentPane(adminCompScreen);
		wordFeud.stopThread();
		revalidate();
	}

	public void setModScreen() {
		this.setContentPane(modScreen);
		modScreen.fillList();
		wordFeud.stopThread();
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
		return wordFeud.doRegisterAction(username, passInput, passConfirm);
	}

	public boolean callLoginAction(String username, char[] password) {
		return wordFeud.doLoginAction(username, password);
	}

	public boolean callChangeRoleAction(String result) {
		return wordFeud.doChangeRoleAction(result);
	}

	public void callLogoutAction() {
		wordFeud.doLogoutAction();
		joinCompScreen.clearLists();
		joinedCompScreen.clearLists();
	}

	public void fillRoleWindow() {
		standardMenuBar.fillRoleWindow(wordFeud.getCurrentUserRoles());
	}

	public void setAccDataValues() {
		standardMenuBar.fillAccDataValues(wordFeud.getCurrentUsername(),
				wordFeud.getCurrentPassword());
	}

	public String getCurrentUsername() {
		return wordFeud.getCurrentUsername();
	}

	public User getuser() {
		return wordFeud.getCurrentUser();
	}

	public Administrator getAdmin() {
		return wordFeud.getCurrentUser().getAdmin();
	}

	// PlayGame/Spectator part
	// Adds the observers to all the panels
	// Sets the right ContentPane
	public void addObservers(Observer observer, boolean spectator) {
		if (spectator) {
			this.setJMenuBar(specMenuBar);
			this.setContentPane(gameSpecScreen);
			this.pack();
			System.out.println("MainFRAMEPANEL - set content specScreen");
			gameSpecScreen.addObserverToObserverButtons(observer);
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
		// DatabaseHandler dbh = DatabaseHandler.getInstance();
		if (gameToLoad != 0 && !spectating) {
			wordFeud.startGame(gameToLoad, false, false);
			System.out.println("GAMEID IS " + gameToLoad);
		} else if (spectating) {
			wordFeud.startGame(gameToLoad, true, false);
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

	public String callChallengePlayerAction(String competitionID,
			String opponent, int privacy) {
		return wordFeud.doChallengePlayerAction(competitionID, opponent, privacy);
	}

	// Returns the gameScreen
	public GameScreen getGameScreen() {
		return gameScreen.getGameScreen();
	}

	// Returns the specScreen
	public GameSpecScreen getSpecScreen() {
		return gameSpecScreen;
	}

	public void setCorrectRoleMainMenu() {
		String currentRole = wordFeud.getCurrentUserRole();

		if (currentRole.equals(WordFeudConstants.ROLE_ADMINISTRATOR)) {
			this.setAdminCompScreen();
		} else if (currentRole.equals(WordFeudConstants.ROLE_MODERATOR)) {
			this.setModScreen();
		} else if (currentRole.equals(WordFeudConstants.ROLE_PLAYER)) {
			this.setPlayerScreen();
		} else if (currentRole.equals(WordFeudConstants.ROLE_SPECTATOR)) {
			this.setSpecScreen();
		}
	}

	// Update the mainscreen games from the player
	public void updatePlayerGameList() {
		playerScreen.setGameList(wordFeud.myActiveGames(), this.getName());
	}

	// Method to accept/reject games
	public void acceptRejectGame(String string, int competionID, int gameID) {
		wordFeud.acceptRejectGame(string, competionID, gameID);
	}

	public String callCreateCompAction(String summary, String compEnd,
			String minPlayers, String maxPlayers) {
		return wordFeud.doCreateCompAction(summary, compEnd, minPlayers, maxPlayers);
	}

	public Set<Entry<String, Competition>> callGetAllCompetitionsAction() {
		return wordFeud.doGetAllCompetitionsAction();
	}

	public Set<Entry<String, Competition>> adminCallActiveCompetitionAction() {
		return wordFeud.doGetActiveCompetitionsAction();
	}

	public Set<Entry<String, Competition>> callGetJoinedCompetitionsAction() {
		return wordFeud.doGetJoinedCompetitionsAction();
	}

	public Competition callGetOneCompetitionAction(String key) {
		return wordFeud.doGetOneCompetitionAction(key);
	}

	public void callLoadAllCompetitionsAction() {
		wordFeud.doLoadAllCompetitionsAction();
	}

	public void callJoinCompetitionAction(String compID) {
		wordFeud.doJoinCompAction(compID);
	}

	public void callLoadJoinedCompetitionsAction() {
		wordFeud.doLoadJoinedCompetitionsAction();
	}

	public boolean callAskMatchOwnershipAction(String matchID) {
		return wordFeud.doAskMatchOwnershipAction(matchID);
	}

	public Set<Entry<String, PendingMatch>> callGetPendingGamesAction() {
		return wordFeud.doGetPendingGamesAction();
	}

	public void callLoadPendingMatchesAction() {
		wordFeud.doLoadPendingMatches();
	}

	public Set<Entry<String, ActiveMatch>> callGetAllActiveMatchesAction() {
		return wordFeud.doGetAllActiveMatchesAction();
	}
}
