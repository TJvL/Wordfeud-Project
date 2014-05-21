package domein;

import gui.MainFrame;

import java.util.ArrayList;
import java.util.Observer;

import datalaag.DatabaseHandler;

public class WordFeud {
	private final User currentUser;
	private final CompetitionManager compMan;
	private Match match;
	private ArrayList<Match> matches;
	private SecondThread secondThread;
	private MainFrame framePanel;
	private DatabaseHandler dbh;
	private MatchManager matchManager;

	public WordFeud() {
		dbh = DatabaseHandler.getInstance();
		currentUser = new User();
		compMan = new CompetitionManager(this);
		matches = new ArrayList<Match>();
		framePanel = new MainFrame(this);
		matchManager = new MatchManager(this, framePanel, secondThread);
	}

	/*
	 * // A method to initialize the Thread public void initializeThread() {
	 * secondThread = new SecondThread(framePanel.getGameScreen()
	 * .getGameChatPanel(), framePanel.getGameScreen() .getButtonPanel(),
	 * framePanel.getGameScreen().getScorePanel()); secondThread.start(); }
	 */

	// A method to start the Thread
	public void startThread() {
		matchManager.startThread();
	}

	// Stops the Thread
	public void stopThread() {
		matchManager.stopThread();
	}

	/*
	 * Returns the user from the player DOESNT WORK WELL
	 * ****************************************
	 */
	public Player getUserPlayer() {
		return currentUser.getPlayer();
	}

	// Returns the User
	public User getCurrentUser() {
		return currentUser;
	}

	public String doRegisterAction(String username, char[] passInput,
			char[] passConfirm) {
		return currentUser.register(username, passInput, passConfirm);
	}

	public String doLoginAction(String username, char[] password) {

		String result = currentUser.login(username, password);
		if (result.equals("Username and Password are correct")) {
			compMan.loadCompetitions(currentUser.getUsername());
		}
		return result;
	}

	public void doLogoutAction() {
		currentUser.logout();
	}

	public boolean doChangeRoleAction(String result) {
		return currentUser.changeRole(result);
	}

	public String getCurrentUserRole() {
		return currentUser.getCurrentRole();
	}

	public String getCurrentUsername() {
		return currentUser.getUsername();
	}

	// Returns active games
	public ArrayList<ActiveMatch> getActiveGames() {
		return matchManager.getActiveMatches();
	}

	// Returns the games for notifications
	public ArrayList<PendingMatch> getPendingGames() {
		return matchManager.getPendingMatchs();
	}

	// Returns my active games
	public ArrayList<ActiveMatch> myActiveGames() {
		return matchManager.getMyActiveMatches();
	}

	// Depends if someone is spectating - starting new game
	// or want to load a game
	public void startGame(int gameID, boolean spectate, boolean newGame) {
		matchManager.startGame(gameID, spectate, newGame);
	}

	// Adds the observers
	public void addObservers(Observer observer, boolean spectator) {
		framePanel.addObservers(observer, spectator);
	}

	// Method to accept/reject game in the database
	public void acceptRejectGame(String string, int competionID, int gameID) {
		matchManager.acceptRejectGame(competionID, gameID,
				getCurrentUsername(), string);
	}
}
