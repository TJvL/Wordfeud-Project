package domein;

import java.util.ArrayList;

import datalaag.DatabaseHandler;

public class User {
	private Player player;
	private Administrator admin;
	private Spectator spec;
	private Moderator mod;

	private String username;
	private boolean isLoggedIn;
	
	private String currentRole;
	private final String defaultUsername = "Spectator";
	private final String defaultRole = "Spectator";

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

	public String login(String username, char[] passInputArray) {
		
		String passInput = "";
		for (char c : passInputArray){
			passInput = passInput + c;
		}
		
		String retValue = datalaag.DatabaseHandler.getInstance().login(username,
				passInput);

		if (retValue.equalsIgnoreCase("Username and Password are correct")) {
			// set Username in User Class
			this.username = username;
			this.setLoggedIn(true);
			this.checkRoles();
			this.changeRole("Player");
		}

		System.out.println(retValue);
		return retValue;
	}

	public String register(String username, char[] passInputArray,
			char[] passConfirmArray) {

		String passInput = "";
		for (char c : passInputArray){
			passInput = passInput + c;
		}
		String passConfirm = "";
		for (char c : passConfirmArray){
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

		else{
		retValue = DatabaseHandler.getInstance().register(username, passInput);
		DatabaseHandler.getInstance().register(username, passInput);



		}


		System.out.println(retValue);
		return retValue;

	}
	
	public boolean checkRoles()
	{
		boolean actionSuccesful = false;
		if (isLoggedIn){
			ArrayList<String> roles = DatabaseHandler.getInstance().getRole(username);
		
			if (!roles.isEmpty()){
				for (String role : roles){
					if (role.equals("Administrator")){
						admin.setHasPermissions(true);
						actionSuccesful = true;
					}
					else if (role.equals("Moderator")){
						mod.setHasPermissions(true);
						actionSuccesful = true;
					}
					else if (role.equals("Player")){
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
		if (role != null){
			if (role.equals("Administrator")){
				if (admin.HasPermissions()){
					this.setCurrentRole(role);
					actionSuccesful = true;
				}
			}
			else if (role.equals("Moderator")){
				if (mod.HasPermissions()){
					this.setCurrentRole(role);
					actionSuccesful = true;
				}
			}
			else if (role.equals("Player")){
				if (player.HasPermissions()){
					this.setCurrentRole(role);
					actionSuccesful = true;
				}
			}
			else if (role.equals("Spectator")){
				if (spec.HasPermissions()){
					this.setCurrentRole(role);
					actionSuccesful = true;
				}
			}
		}
		else{
			System.err.println("ERROR: invalid value received or value was null");
			System.out.println("No role change has occured.");
		}
		return actionSuccesful;
	}
}
