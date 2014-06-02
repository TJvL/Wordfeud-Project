package domein;

import gui.GameScreen;
import gui.GameSpecScreen;
import gui.MainFrame;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Observer;
import java.util.Set;

import javax.swing.SwingUtilities;

public class WordFeud {
	private User currentUser;
	private CompetitionManager compMan;
	private MainFrame mainFrame;
	private WordFeud wf;
	private MatchManager matchMan;

	public WordFeud() {
		currentUser = new User();
		compMan = new CompetitionManager();
		wf = this;
	}

	public void init() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				mainFrame = new MainFrame(wf);
				mainFrame.init();
			}
		});
		matchMan = new MatchManager(wf);
	}

	// Stops the Thread
	public void stopThread() {
		matchMan.stopThread();
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

	public boolean doLoginAction(String username, char[] password) {

		Boolean succesfulLogin = currentUser.login(username, password);
		if (succesfulLogin) {
			compMan.loadJoinedCompetitions(currentUser.getUsername());
		}
		return succesfulLogin;
	}

	public void doLogoutAction() {
		currentUser.logout();
		compMan.logout();
		matchMan.logout();
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
	public String doCreateCompAction(String summary, String endDate,
			String minParticipants, String maxParticipants) {
		return compMan.createCompetition(this.getCurrentUsername(), summary,
				endDate, minParticipants, maxParticipants);
	}

	public boolean doJoinCompAction(String compID) {
		return compMan.joinCompetition(compID, this.getCurrentUsername());
	}

	public void doLoadAllCompetitionsAction() {
		compMan.loadAllCompetitions(this.getCurrentUsername());
	}
	
	public void doAdminLoadActiveCompetitionsAction()
	{
		currentUser.getAdmin().adminCompetitions();
	}

	public void doLoadJoinedCompetitionsAction() {
		compMan.loadJoinedCompetitions(this.getCurrentUsername());
	}

	public String getCurrentUserRole() {
		return currentUser.getCurrentRole();
	}

	public ArrayList<String> getCurrentUserRoles() {
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
		return matchMan.getActiveMatches();
	}

	// Returns my active games
	public ArrayList<ActiveMatch> myActiveGames() {
		return matchMan.getMyActiveMatches();
	}

	// Depends if someone is spectating - starting new game
	// or want to load a game
	public void startGame(int gameID, boolean spectate, boolean newGame) {
		matchMan.startGame(gameID, spectate, newGame);
	}

	// Adds the observers
	public void addObservers(Observer observer, boolean spectator) {
		mainFrame.addObservers(observer, spectator);
	}

	// Method to accept/reject game in the database
	public void acceptRejectGame(String string, int competionID, int gameID) {
		matchMan.acceptRejectGame(competionID, gameID,
				this.getCurrentUsername(), string);
	}

	// Method to start a game
	public String doChallengePlayerAction(String competitionID,
			String opponent, int privacy) {
		return matchMan.challengePlayer(competitionID,
				this.getCurrentUsername(), opponent, "EN", privacy);
	}

	public CompetitionManager getCompMan() {
		return compMan;
	}

	public Set<Entry<String, Competition>> doGetAllCompetitionsAction() {
		return compMan.getAllCompEntries();
	}

	public Set<Entry<String, Competition>> doGetActiveCompetitionsAction() {
		return currentUser.getAdmin().getAllActiveCompEntries();
	}

	public Set<Entry<String, Competition>> doGetJoinedCompetitionsAction() {
		return compMan.getJoinedCompEntries();
	}

	public Competition doGetOneCompetitionAction(String key) {
		return compMan.getOneCompetition(key);
	}

	public GameScreen getGameScreen() {
		return mainFrame.getGameScreen();
	}

	public void updatePlayerGameList() {
		mainFrame.updatePlayerGameList();
	}

	public GameSpecScreen getSpecScreen() {
		return mainFrame.getSpecScreen();
	}

	public boolean doAskMatchOwnershipAction(String matchID) {
		return matchMan.askMatchOwnership(matchID);
	}

	public Set<Entry<String, PendingMatch>> doGetPendingGamesAction() {
		return matchMan.getPendingMatches();
	}

	public void doLoadPendingMatches() {
		matchMan.loadPendingMatches(this.getCurrentUsername());
	}
}
