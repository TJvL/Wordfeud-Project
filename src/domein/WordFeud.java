package domein;

import gui.MainFrame;

import java.util.ArrayList;
import java.util.Observer;

public class WordFeud {
	private final User currentUser;
	private final CompetitionManager compMan;
	private Match match;
	private ArrayList<Match> matches;
	private SecondThread secondThread;
	private MainFrame framePanel;

	public WordFeud() {
		currentUser = new User();
		compMan = new CompetitionManager(this);
		matches = new ArrayList<Match>();
		framePanel = new MainFrame(this);
	}

	// A method to initialize the Thread
	public void initializeThread() {
		secondThread = new SecondThread(framePanel.getGameScreen()
				.getGameChatPanel(), framePanel.getGameScreen()
				.getButtonPanel(), framePanel.getGameScreen().getScorePanel());
		secondThread.start();
	}

	// A method to start the Thread
	public void startThread() {
		initializeThread();
		secondThread.setRunning(true);
	}

	public void stopThread() {
		if (secondThread != null) {
			secondThread.setRunning(false);
		}
	}

	/*
	 * Returns the user from the player DOESNT WORK WELL
	 * ****************************************
	 */
	public Player getUserPlayer() {
		return currentUser.getPlayer();
	}

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

	// Depends if someone is spectating - starting new game
	// or want to load a game
	public void startGame(int gameID, boolean spectate, boolean newGame) {
		startThread();
		if (spectate) {
			spectateMatch(gameID);
		} else {
			if (newGame) {
				newMatchStartedByMe(gameID);
			} else {
				loadMatch(gameID);
			}
		}
	}

	// This starts a Match that is made by me
	public void newMatchStartedByMe(int gameID) {
		match = new Match(gameID, getUserPlayer(), framePanel.getGameScreen()
				.getGameFieldPanel(), getCurrentUsername());
		// Adds the observers
		this.addObservers(match, false);
		// Sets the thread
		secondThread.setMatch(match);
		match.startNewGame();
		framePanel
				.getGameScreen()
				.getGameChatPanel()
				.setChatVariables(match.getOwnName(), match.getEnemyName(),
						match.getGameID());
		matches.add(match);
		secondThread.setRunning(true);
	}

	// Loads a match
	public void loadMatch(int gameID) {
		boolean exists = false;
		// Checks if the refrences already exist
		for (Match match : matches) {
			if (match.getGameID() == gameID) {
				exists = true;
				// Adds the observers
				this.addObservers(match, false);
				secondThread.setMatch(match);
				framePanel
						.getGameScreen()
						.getGameChatPanel()
						.setChatVariables(match.getOwnName(),
								match.getEnemyName(), match.getGameID());
				match.loadGame();
				secondThread.setRunning(true);
			}
		}
		if (!exists) {
			match = new Match(gameID, getUserPlayer(), framePanel
					.getGameScreen().getGameFieldPanel(), getCurrentUsername());
			// Adds the observers
			this.addObservers(match, false);
			secondThread.setMatch(match);
			match.loadGame();
			framePanel
					.getGameScreen()
					.getGameChatPanel()
					.setChatVariables(match.getOwnName(), match.getEnemyName(),
							match.getGameID());
			matches.add(match);
			secondThread.setRunning(true);
		}

	}

	// A method start spectating
	public void spectateMatch(int gameID) {
		match = new Match(gameID);
		match.loadSpecateGame(framePanel.getSpecScreen());
		this.addObservers(match, true);
	}

	// Adds the observers
	public void addObservers(Observer observer, boolean spectator) {
		framePanel.addObservers(observer, spectator);
	}
}
