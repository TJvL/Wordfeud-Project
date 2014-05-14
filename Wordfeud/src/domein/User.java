package domein;

import java.util.Scanner;

import datalaag.DatabaseHandler;

public class User {
	private Player player;
	private Administrator admin;
	private Spectator spec;
	private Moderator mod;

	private String name;

	public User() {
		player = new Player(false);
		admin = new Administrator(false);
		spec = new Spectator(true);
		mod = new Moderator(false);
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

	public String getName() {
		return name;
	}

	public String login(String usernameInput, String passwordInput) {


		String retValue = datalaag.DatabaseHandler.getInstance().login(usernameInput,
				passwordInput);

		datalaag.DatabaseHandler.getInstance().login(usernameInput,
				passwordInput);



		if (retValue.equalsIgnoreCase("Username and Password are correct")) {
			// set Username in User Class
			name = usernameInput;
		}

		System.out.println(retValue);
		return retValue;

	}

	public String register(String usernameInput, String passwordInput,
			String passConfirmInput) {

		String retValue = "Idle";

		// controleer of de gebruikersnaam tussen de 3 en 15 tekens is
		if (usernameInput.length() < 3 || usernameInput.length() > 15) {
			retValue = "Gebruikersnaam moet tussen de 3 en 15 tekens bevatten.";
		}

		// Controleer of wachtwoord minimaal 6 tekens bevat
		else if (passwordInput.length() < 6) {
			retValue = "Het wachtwoord moet minimaal 6 tekens bevatten.";

		}

		// Controleer of de opgegeven wachtwoorden overeen komen
		else if (!passwordInput.equals(passConfirmInput)) {
			retValue = "De opgegeven wachtwoorden komen niet overeen.";

		}

		// Voer de gebruiker in in de database

		else{
		retValue = DatabaseHandler.getInstance().register(usernameInput, passwordInput);
		DatabaseHandler.getInstance().register(usernameInput, passwordInput);



		}


		System.out.println(retValue);
		return retValue;

	}
}
