package domein;

import gui.MainFrame;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Observer;

public class WordFeud {
	private final User currentUser;
	private final CompetitionManager compMan;
	private MainFrame framePanel;
	private MatchManager matchManager;

	public WordFeud() {
		currentUser = new User();
		compMan = new CompetitionManager(this);
		framePanel = new MainFrame(this);
		matchManager = new MatchManager(this, framePanel);
	}

	/*
	 * <<<<<<< HEAD Gets the user from the player DOESNT WORK WELL ======= // A
	 * method to initialize the Thread public void initializeThread() {
	 * secondThread = new SecondThread(framePanel.getGameScreen()
	 * .getGameChatPanel(), framePanel.getGameScreen() .getButtonPanel(),
	 * framePanel.getGameScreen().getScorePanel()); secondThread.start(); }
	 */

	// Stops the Thread
	public void stopThread() {
		matchManager.stopThread();
	}

	/*
	 * Returns the user from the player DOESNT WORK WELL >>>>>>>
	 * refs/remotes/origin/master-development
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
			compMan.loadJoinedCompetitions(currentUser.getUsername());
			compMan.updateEachParticipants();
		}
		return result;
	}

	public void doLogoutAction() {
		currentUser.logout();
	}

	public boolean doChangeRoleAction(String result) {
		return currentUser.changeRole(result);
	}

	/*
	 * param uitleg: summary = de naam of omschrijving die in alle lijsten terug
	 * komt als "naam". endDate = de eind datum van de competitie.
	 * minParticipants = minimaal aantal deelnemers die nodig zijn om de compo
	 * te starten !!MOET >2 zijn!! maxParticipants = maximaal aantal deelnemers
	 * dat in de compo mag zitten. - Thomas
	 */
	public void doCreateCompAction(String summary, String endDate,
			int minParticipants, int maxParticipants) {
		compMan.createCompetition(currentUser.getUsername(), summary, endDate,
				minParticipants, maxParticipants);

	}

	public boolean doJoinCompAction(int compID) {
		return compMan.joinCompetition(compID, currentUser.getUsername());
	}

	public void doLoadAllCompetitionsAction() {
		compMan.loadAllCompetitions(currentUser.getUsername());
	}

	public ArrayList<CompPlayer> getParticipantListAction(int compID) {
		return compMan.getParticipantList(compID);
	}
	
	public ArrayList<Competition> getJoinedCompetitions(){
		return compMan.getJoinedCompetitions();
	}

	public String getCurrentUserRole() {
		return currentUser.getCurrentRole();
	}

	public ArrayList<String> getCurrentUserPossibleRoles() {
		return currentUser.getRoles();
	}

	public String getCurrentUsername() {
		return currentUser.getUsername();
	}

	public String getCurrentPassword() {
		return currentUser.getPassword();
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

	// Method to start a game
	public void challengePlayer(int competitionID, String username,
			String opponent, String language) {
		matchManager.challengePlayer(competitionID, username, opponent,
				language);
	}
	
	public CompetitionManager getCompMan()
	{
		return compMan;
	}
	
}
