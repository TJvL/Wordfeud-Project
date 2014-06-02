package gui;

import java.awt.Dimension;
import java.util.Map.Entry;
import java.util.Observer;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import javax.swing.JFrame;
import javax.swing.SwingWorker;

import datalaag.WordFeudConstants;
import domein.ActiveMatch;
import domein.Administrator;
import domein.Competition;
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
	private WordFeud wordFeud;
	private DisabledGlassPane loadScreen;

	public MainFrame(WordFeud wordFeud) {
		this.wordFeud = wordFeud;
		startMenuBar = new StartMenuBar(this);
		loginScreen = new LoginScreen(this);
		loadScreen = new DisabledGlassPane();

		this.setPreferredSize(new Dimension(1200, 700));
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setTitle("WordFeud");
		this.setGlassPane(loadScreen);
		
		this.setLoginScreen();
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
		wordFeud.setPanelsReferences(gameScreen.getGameChatPanel(),
				gameScreen.getButtonPanel(), gameScreen.getScorePanel());
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
		this.fillRoleWindow();
		this.setAccDataValues();
	}

	public void setRegScreen() {
		regScreen.populateScreen();
		this.setContentPane(regScreen);
		wordFeud.stopThread();
		this.revalidate();
	}

	public void setLoginScreen() {
		startLogoutWorker();
		wordFeud.stopThread();
	}

	public void setPlayerScreen() {
		startMyActiveMatchesWorker();
		wordFeud.stopThread();
	}

	public void setGameScreen() {
		this.setContentPane(gameScreen);
		wordFeud.stopThread();
		revalidate();
	}

	public void setSpecScreen() {
		this.startAllActiveMatchesWorker();
		wordFeud.stopThread();
	}

	public void setJoinCompScreen() {
		this.startAllCompWorker();
		wordFeud.stopThread();
	}

	public void setJoinedCompScreen() {
		this.startJoinedCompWorker();
		wordFeud.stopThread();
	}

	public void setAdminAccScreen() {
		this.setContentPane(adminAccScreen);
		adminAccScreen.fillPlayerList();
		wordFeud.stopThread();
		revalidate();
	}

	public void setAdminCompScreen() {
		this.startAdminCompWorker();
		wordFeud.stopThread();
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

	public String startRegisterWorker(final String username, final char[] passInput,
			final char[] passConfirm) {
		loadScreen.activate("Please Wait...");
		
		SwingWorker<String, Integer> registerWorker = new SwingWorker<String, Integer>() {
			@Override
			protected String doInBackground() throws Exception {
				String result = wordFeud.doRegisterAction(username, passInput, passConfirm);
				return result;
			}
		};
		
		registerWorker.execute();
		String result = WordFeudConstants.REGISTER_FAIL_DEFAULT;
		try {
			result = registerWorker.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public String startCreateCompWorker(final String summary, final String compEnd,
			final String minPlayers, final String maxPlayers) {
		loadScreen.activate("Please Wait...");
		
		SwingWorker<String, Integer> createCompWorker = new SwingWorker<String, Integer>() {
			@Override
			protected String doInBackground() throws Exception {
				String result = wordFeud.doCreateCompAction(summary, compEnd, minPlayers, maxPlayers);
				return result;
			}
		};
		
		createCompWorker.execute();
		String result = WordFeudConstants.REGISTER_FAIL_DEFAULT;
		try {
			result = createCompWorker.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		loadScreen.deactivate();
		return result;
	}
	
	public String startCompJoinWorker(final String compID) {
		loadScreen.activate("Please Wait...");
		
		SwingWorker<String, Integer> compJoinWorker = new SwingWorker<String, Integer>() {
			@Override
			protected String doInBackground() throws Exception {
				String result = wordFeud.doJoinCompAction(compID);
				return result;
			}
		};
		
		compJoinWorker.execute();
		String result = WordFeudConstants.JOIN_COMP_FAIL;
		try {
			result = compJoinWorker.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public String startChallengeWorker(final String competitionID,
			final String opponent, final int privacy) {
		loadScreen.activate("Please Wait...");
		
		SwingWorker<String, Integer> challengeWorker = new SwingWorker<String, Integer>() {
			@Override
			protected String doInBackground() throws Exception {
				String result = wordFeud.doChallengePlayerAction(competitionID, opponent, privacy);
				return result;
			}
		};
		
		challengeWorker.execute();
		String result = WordFeudConstants.CHALLENGE_FAIL_DEFAULT;
		try {
			result = challengeWorker.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		loadScreen.deactivate();
		return result;
	}

	public int startLoginWorker(final String username, final char[] password) {
		loadScreen.activate("Please Wait...");
		
		SwingWorker<Integer, Integer> loginWorker = new SwingWorker<Integer, Integer>() {
			@Override
			protected Integer doInBackground() throws Exception {
				int result = wordFeud.doLoginAction(username, password);
				return result;
			}
		};
		
		loginWorker.execute();
		int result = WordFeudConstants.LOGIN_FAIL;
		try {
			result = loginWorker.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return result;
	}

	public int startChangeRoleWorker(final String role) {
		loadScreen.activate("Please Wait...");
		
		SwingWorker<Integer, Integer> loginWorker = new SwingWorker<Integer, Integer>() {
			@Override
			protected Integer doInBackground() throws Exception {
				int result = wordFeud.doChangeRoleAction(role);
				return result;
			}
		};
		
		loginWorker.execute();
		int result = WordFeudConstants.ROLE_CHANGE_FAIL;
		try {
			result = loginWorker.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		if(result == WordFeudConstants.ROLE_CHANGE_FAIL){
			loadScreen.deactivate();
		}
		return result;
	}

	public void startLogoutWorker() {
		loadScreen.activate("Please Wait...");
		
		SwingWorker<Integer, Integer> logoutWorker = new SwingWorker<Integer, Integer>() {
			@Override
			protected Integer doInBackground() throws Exception {
				wordFeud.doLogoutAction();
				return 1;
			}

			@Override
			protected void done() {
				super.done();
				try {
					setStartMenuBar();
					loginScreen.populateScreen();
					setContentPane(loginScreen);
					loadScreen.deactivate();
				} catch (Exception ignore) {
				}
			}
		};
		logoutWorker.execute();
	}
	
	public void startAllCompWorker() {
		loadScreen.activate("Please Wait...");
		
		SwingWorker<Integer, Integer> allCompWorker = new SwingWorker<Integer, Integer>() {
			@Override
			protected Integer doInBackground() throws Exception {
				wordFeud.doLoadAllCompetitionsAction();
				return 1;
			}

			@Override
			protected void done() {
				super.done();
				try {
					joinCompScreen.populateScreen();
					setContentPane(joinCompScreen);
						loadScreen.deactivate();
				} catch (Exception ignore) {
				}
			}
		};

		allCompWorker.execute();
	}

	public void startJoinedCompWorker() {
		loadScreen.activate("Please Wait...");
		
		SwingWorker<Integer, Integer> joinedCompWorker = new SwingWorker<Integer, Integer>() {
			@Override
			protected Integer doInBackground() throws Exception {
				wordFeud.doLoadJoinedCompetitionsAction();
				return 1;
			}

			@Override
			protected void done() {
				super.done();
				try {
					joinedCompScreen.populateScreen();
					setContentPane(joinedCompScreen);
					loadScreen.deactivate();
				} catch (Exception ignore) {
				}
			}
		};
		
		joinedCompWorker.execute();
	}
	
	public void startAdminCompWorker() {
		loadScreen.activate("Please Wait...");
		
		SwingWorker<Integer, Integer> adminCompWorker = new SwingWorker<Integer, Integer>() {
			@Override
			protected Integer doInBackground() throws Exception {
				wordFeud.doLoadAdminCompetitionsAction();
				return null;
			}

			@Override
			protected void done() {
				super.done();
				try {
					adminCompScreen.populateScreen();
					setContentPane(adminCompScreen);
					loadScreen.deactivate();
				} catch (Exception ignore) {
				}
			}
		};
		
		adminCompWorker.execute();
	}
	
	public void startMyActiveMatchesWorker() {
		loadScreen.activate("Please Wait...");
		
		SwingWorker<Integer, Integer> myMatchesWorker = new SwingWorker<Integer, Integer>() {
			@Override
			protected Integer doInBackground() throws Exception {
				wordFeud.doLoadMyActiveMatchesAction();
				return 1;
			}

			@Override
			protected void done() {
				try {
					super.done();
					playerScreen.populateScreen();
					setContentPane(playerScreen);
					loadScreen.deactivate();
				} catch (Exception ignore) {
				}
			}
		};
		
		myMatchesWorker.execute();
	}
	
	public void startPendingMatchWorker() {
		loadScreen.activate("Please Wait...");
		
		SwingWorker<Integer, Integer> pendingMatchesWorker = new SwingWorker<Integer, Integer>() {
			@Override
			protected Integer doInBackground() throws Exception {
				wordFeud.doLoadPendingMatches();
				return 1;
			}

			@Override
			protected void done() {
				try {
					super.done();
					loadScreen.deactivate();
					standardMenuBar.showNotificationWindow();
				} catch (Exception ignore) {
				}
			}
		};
		
		pendingMatchesWorker.execute();
	}
	
	// Method to accept/reject games
	public void startResponseWorker(final String string, final int competionID, final int gameID) {
		loadScreen.activate("Please Wait...");
		
		SwingWorker<Integer, Integer> responseWorker = new SwingWorker<Integer, Integer>() {
			@Override
			protected Integer doInBackground() throws Exception {
				wordFeud.doRespondChallengeAction(string, competionID, gameID);
				return null;
			}
		};
		
		responseWorker.execute();
		try {
			responseWorker.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		loadScreen.deactivate();
	}

	public void startAllActiveMatchesWorker() {
		loadScreen.activate("Please Wait...");
		
		SwingWorker<Integer, Integer> myMatchesWorker = new SwingWorker<Integer, Integer>() {
			@Override
			protected Integer doInBackground() throws Exception {
				wordFeud.doLoadActiveMatchesAction();
				return 1;
			}

			@Override
			protected void done() {
				try {
					super.done();
					specScreen.populateScreen();
					setContentPane(specScreen);
					loadScreen.deactivate();
				} catch (Exception ignore) {
				}
			}
		};
		
		myMatchesWorker.execute();
	}

	public void fillRoleWindow() {
		standardMenuBar.fillRoleWindow(wordFeud.getCurrentUserRoles());
	}

	public void setAccDataValues() {
		standardMenuBar.fillAccDataValues(wordFeud.getCurrentUsername(),
				wordFeud.getCurrentPassword());
	}
	
	public boolean callAskMatchOwnershipAction(String matchID) {
		return wordFeud.doAskMatchOwnershipAction(matchID);
	}
	
	public Competition callGetOneCompetitionAction(String key) {
		return wordFeud.doGetOneCompetitionAction(key);
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
	
	public String getCurrentUsername() {
		return wordFeud.getCurrentUsername();
	}

	public User getUser() {
		return wordFeud.getCurrentUser();
	}

	public Administrator getAdmin() {
		return wordFeud.getCurrentUser().getAdmin();
	}

	// Returns the gameScreen
	public GameScreen getGameScreen() {
		return gameScreen.getGameScreen();
	}

	// Returns the specScreen
	public GameSpecScreen getSpecScreen() {
		return gameSpecScreen;
	}

	public Set<Entry<String, Competition>> callGetAllCompetitionsAction() {
		return wordFeud.doGetAllCompetitionsAction();
	}

	public Set<Entry<String, Competition>> callGetAdminCompetitionsAction() {
		return wordFeud.doGetActiveCompetitionsAction();
	}

	public Set<Entry<String, Competition>> callGetJoinedCompetitionsAction() {
		return wordFeud.doGetJoinedCompetitionsAction();
	}

	public Set<Entry<String, PendingMatch>> callGetPendingGamesAction() {
		return wordFeud.doGetPendingGamesAction();
	}

	public Set<Entry<String, ActiveMatch>> callGetAllActiveMatchesAction() {
		return wordFeud.doGetAllActiveMatchesAction();
	}

	public Set<Entry<String, ActiveMatch>> callGetMyActiveMatchesAction() {
		return wordFeud.doGetMyActiveMatchesAction();
	}
}
