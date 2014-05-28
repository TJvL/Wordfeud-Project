package domein;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import datalaag.DatabaseHandler;

public class User {
	public static final String ROLE_ADMINISTRATOR = "Administrator";
	public static final String ROLE_MODERATOR = "Moderator";
	public static final String ROLE_PLAYER = "Player";
	public static final String ROLE_SPECTATOR = "Spectator";
	private final String defaultUsername = "Spectator";
	private final String defaultRole = ROLE_SPECTATOR;
	
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

	public boolean login(String username, char[] passInputArray) {

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
			if(player.HasPermissions()){
				this.changeRole(User.ROLE_PLAYER);
			}
			else if(admin.HasPermissions()){
				this.changeRole(User.ROLE_ADMINISTRATOR);
			}
			else if(mod.HasPermissions()){
				this.changeRole(User.ROLE_MODERATOR);
			}
			else if(spec.HasPermissions()){
				this.changeRole(User.ROLE_SPECTATOR);
			}
		}
		return succesfulLogin;
	}

	public String register(String username, char[] passInputArray,
			char[] passConfirmArray) {

		String passInput = "";
		for (char c : passInputArray) {
			passInput = passInput + c;
		}
		String passConfirm = "";
		for (char c : passConfirmArray) {
			passConfirm = passConfirm + c;
		}

		String retValue = "Idle";

		// controleer of de gebruikersnaam tussen de 3 en 15 tekens is
		if (username.length() < 3 || username.length() > 15) {
			retValue = "Gebruikersnaam moet tussen de 3 en 15 tekens bevatten.";
		}

		// Controleer of wachtwoord minimaal 6 tekens bevat
		else if (passInput.length() < 6) {
			retValue = "Het wachtwoord moet minimaal 6 tekens bevatten.";

		}

		// Controleer of de opgegeven wachtwoorden overeen komen
		else if (!passInput.equals(passConfirm)) {
			retValue = "De opgegeven wachtwoorden komen niet overeen.";

		}

		// Voer de gebruiker in in de database

		else {
			retValue = DatabaseHandler.getInstance().register(username,
					passInput);
			DatabaseHandler.getInstance().register(username, passInput);

		}

		System.out.println(retValue);
		return retValue;

	}

	public boolean checkRoles() {
		boolean actionSuccesful = false;
		if (isLoggedIn) {
			ArrayList<String> roles = DatabaseHandler.getInstance().getCurrentUserRole(
					username);

			if (!roles.isEmpty()) {
				for (String role : roles) {
					if (role.equals(User.ROLE_ADMINISTRATOR)) {
						admin.setHasPermissions(true);
						actionSuccesful = true;
					} else if (role.equals(User.ROLE_MODERATOR)) {
						mod.setHasPermissions(true);
						actionSuccesful = true;
					} else if (role.equals(User.ROLE_PLAYER)) {
						player.setHasPermissions(true);
						actionSuccesful = true;
					}
				}
			}
		}
		return actionSuccesful;
	}

	public boolean changeRole(String role) {
		boolean actionSuccesful = false;
		if (role != null) {
			if (role.equals(User.ROLE_ADMINISTRATOR)) {
				if (admin.HasPermissions()) {
					this.setCurrentRole(role);
					actionSuccesful = true;
				}
			} else if (role.equals(User.ROLE_MODERATOR)) {
				if (mod.HasPermissions()) {
					this.setCurrentRole(role);
					actionSuccesful = true;
				}
			} else if (role.equals(User.ROLE_PLAYER)) {
				if (player.HasPermissions()) {
					this.setCurrentRole(role);
					actionSuccesful = true;
				}
			} else if (role.equals(User.ROLE_SPECTATOR)) {
				if (spec.HasPermissions()) {
					this.setCurrentRole(role);
					actionSuccesful = true;
				}
			}
		} else {
			System.err
					.println("ERROR: invalid value received or value was null");
			System.out.println("No role change has occured.");
		}
		return actionSuccesful;
	}

	public ArrayList<String> getRoles() {
		ArrayList<String> roles = DatabaseHandler.getInstance().getCurrentUserRole(
				username);
		roles.add("Spectator");

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
}
