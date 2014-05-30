package domein;

import gui.MainFrame;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Observer;
import java.util.Set;

import javax.swing.SwingUtilities;

public class WordFeud {
	private User currentUser;
	private CompetitionManager compMan;
	private MainFrame framePanel;
	private MatchManager matchManager;
	private WordFeud wf;

	public WordFeud() {
		currentUser = new User();
		compMan = new CompetitionManager();
		wf = this;
	}

	public void init() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				framePanel = new MainFrame(wf);
				framePanel.init();
				matchManager = new MatchManager(wf, framePanel);
			}
		});		
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
		return compMan.createCompetition(currentUser.getUsername(), summary,
				endDate, minParticipants, maxParticipants);
	}

	public boolean doJoinCompAction(String compID) {
		return compMan.joinCompetition(compID, currentUser.getUsername());
	}

	public void doLoadAllCompetitionsAction() {
		compMan.loadAllCompetitions(currentUser.getUsername());
	}

	public void doLoadJoinedCompetitionsAction() {
		compMan.loadJoinedCompetitions(currentUser.getUsername());
	}

	public ArrayList<Competition> getJoinedCompetitions() {
		return compMan.getJoinedCompetitions();
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
	public String doChallengePlayerAction(String competitionID,
			String opponent, int privacy) {
		return matchManager.challengePlayer(competitionID,
				currentUser.getUsername(), opponent, "EN", privacy);
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
}
