package domein;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JOptionPane;

import datalaag.DatabaseHandler;
import datalaag.WordFeudConstants;

public class User {
	private final String defaultUsername = "Spectator";
	private final String defaultRole = WordFeudConstants.ROLE_SPECTATOR;

	private Player player;
	private Administrator admin;
	private Spectator spec;
	private Moderator mod;
	private String username;
	private boolean isLoggedIn;
	private String currentRole;

	public User() {
		player = new Player(false);
		admin = new Administrator(false);
		spec = new Spectator(true);
		mod = new Moderator(false);
		isLoggedIn = false;
		username = defaultUsername;
		currentRole = defaultRole;
	}

	public Player getPlayer() {
		return player;
	}

	public Administrator getAdmin() {
		return admin;
	}

	public Spectator getSpec() {
		return spec;
	}

	public Moderator getMod() {
		return mod;
	}

	public String getUsername() {
		return username;
	}

	public boolean isLoggedIn() {
		return isLoggedIn;
	}

	public void setLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}

	public String getCurrentRole() {
		return currentRole;
	}

	public String getPassword() {
		return DatabaseHandler.getInstance().getPassword(username);
	}

	/*
	 * use the changeRole() method for changing the role of the user.
	 */
	private void setCurrentRole(String currentRole) {
		this.currentRole = currentRole;
	}

	public void logout() {
		username = defaultUsername;
		this.setLoggedIn(false);
		player.setHasPermissions(false);
		admin.setHasPermissions(false);
		mod.setHasPermissions(false);
		spec.setHasPermissions(true);
		this.changeRole(defaultRole);
	}

	public int login(String username, char[] passInputArray) {

		String passInput = "";
		for (char c : passInputArray) {
			passInput = passInput + c;
		}

		boolean succesfulLogin = DatabaseHandler.getInstance().checkLoginInfo(
				username, passInput);

		if (succesfulLogin) {
			// set Username in User Class
			this.username = username;
			this.setLoggedIn(true);
			this.checkRoles();
			return WordFeudConstants.LOGIN_SUCCES;
		}
		else {
			return WordFeudConstants.LOGIN_FAIL;
		}
	}

	public String register(String username, char[] passInputArray,
			char[] passConfirmArray) {
		String retValue;

		String passInput = "";
		for (char c : passInputArray) {
			passInput = passInput + c;
		}
		String passConfirm = "";
		for (char c : passConfirmArray) {
			passConfirm = passConfirm + c;
		}

		// controleer of de gebruikersnaam tussen de 3 en 15 tekens is
		if (username.length() < 3 || username.length() > 15) {
			retValue = WordFeudConstants.FAIL_NAME_LENGTH;
		}

		// Controleer of wachtwoord minimaal 6 tekens bevat
		else if (passInput.length() < 6) {
			retValue = WordFeudConstants.FAIL_PASS_LENGTH;
		}

		// Controleer of de opgegeven wachtwoorden overeen komen
		else if (!passInput.equals(passConfirm)) {
			retValue = WordFeudConstants.FAIL_NO_MATCHING_PASS;
		}

		// Voer de gebruiker in in de database
		else {
			retValue = DatabaseHandler.getInstance().register(username,
					passInput);

		}
		return retValue;
	}

	public void checkRoles() {
		if (isLoggedIn) {
			ArrayList<String> roles = DatabaseHandler.getInstance()
					.getCurrentUserRole(username);

			if (!roles.isEmpty()) {
				for (String role : roles) {
					if (role.equals(WordFeudConstants.ROLE_ADMINISTRATOR)) {
						admin.setHasPermissions(true);
					} else if (role.equals(WordFeudConstants.ROLE_MODERATOR)) {
						mod.setHasPermissions(true);
					} else if (role.equals(WordFeudConstants.ROLE_PLAYER)) {
						player.setHasPermissions(true);
					}
				}
			}
		}
		if (player.HasPermissions()) {
			this.changeRole(WordFeudConstants.ROLE_PLAYER);
		} else if (admin.HasPermissions()) {
			this.changeRole(WordFeudConstants.ROLE_ADMINISTRATOR);
		} else if (mod.HasPermissions()) {
			this.changeRole(WordFeudConstants.ROLE_MODERATOR);
		} else if (spec.HasPermissions()) {
			this.changeRole(WordFeudConstants.ROLE_SPECTATOR);
		}
	}

	public int changeRole(String role) {
		int result = WordFeudConstants.ROLE_CHANGE_FAIL;
		if (role != null) {
			if (role.equals(WordFeudConstants.ROLE_ADMINISTRATOR)) {
				if (admin.HasPermissions()) {
					this.setCurrentRole(role);
					result = WordFeudConstants.ROLE_CHANGE_SUCCES;
				}
			} else if (role.equals(WordFeudConstants.ROLE_MODERATOR)) {
				if (mod.HasPermissions()) {
					this.setCurrentRole(role);
					result = WordFeudConstants.ROLE_CHANGE_SUCCES;
				}
			} else if (role.equals(WordFeudConstants.ROLE_PLAYER)) {
				if (player.HasPermissions()) {
					this.setCurrentRole(role);
					result = WordFeudConstants.ROLE_CHANGE_SUCCES;
				}
			} else if (role.equals(WordFeudConstants.ROLE_SPECTATOR)) {
				if (spec.HasPermissions()) {
					this.setCurrentRole(role);
					result = WordFeudConstants.ROLE_CHANGE_SUCCES;
				}
			}
		}
		return result;
	}

	public ArrayList<String> getRoles() {
		ArrayList<String> roles = new ArrayList<String>();
		if (spec.HasPermissions()) {
			roles.add(WordFeudConstants.ROLE_SPECTATOR);
		}
		if (player.HasPermissions()) {
			roles.add(WordFeudConstants.ROLE_PLAYER);
		}
		if (admin.HasPermissions()) {
			roles.add(WordFeudConstants.ROLE_ADMINISTRATOR);
		}
		if (mod.HasPermissions()) {
			roles.add(WordFeudConstants.ROLE_MODERATOR);
		}
		return roles;
	}

	public String changeUsername() {
		String response = JOptionPane.showInputDialog(null,
				"What is your desired name?", "Enter your desired name",
				JOptionPane.QUESTION_MESSAGE);
		if (!response.equals("")) {
			System.out.println(response);
			if (response.length() > 2 && response.length() < 16) {
				if (DatabaseHandler.getInstance()
						.changeUsername(getUsername(), response)
						.equals("Your username has been succesfully changed")) {
					DatabaseHandler.getInstance().changeUsername(getUsername(),
							response);
					username = response;
				} else {
					JOptionPane.showMessageDialog(
							null,
							DatabaseHandler.getInstance().changeUsername(
									getUsername(), response), "ERROR",
							JOptionPane.WARNING_MESSAGE);
					response = getUsername();
				}
			} else {
				JOptionPane.showMessageDialog(null,
						"Username must contain 3-15 characters.", "ERROR",
						JOptionPane.WARNING_MESSAGE);
				response = getUsername();
			}
		} else {
			response = getUsername();
		}
		return response;
	}

	public String changePassword() {
		String response = JOptionPane.showInputDialog(null,
				"What is your desired password?",
				"Enter your desired password", JOptionPane.QUESTION_MESSAGE);
		if (!response.equals("")) {
			System.out.println(response);
			if (response.length() > 5) {
				DatabaseHandler.getInstance().changePassword(getUsername(),
						response);
			} else {
				JOptionPane.showMessageDialog(null,
						"Password must contain at least 6 characters.",
						"ERROR", JOptionPane.WARNING_MESSAGE);
				response = getPassword();
			}
		} else {
			response = getPassword();
		}
		return response;
	}

	public void loadAdminCompetitionsAction() {
		admin.loadAdminCompetitions();
	}
	
	public Set<Entry<String, Competition>>  getAdminCompEntriesAction(){
		return admin.getAdminCompEntries();
	}
}
