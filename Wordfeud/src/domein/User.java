package domein;

import java.util.Scanner;

import datalaag.DatabaseHandler;

public class User {

	private String userName;
	private boolean isBanned;

	public void login(){
		Scanner reader = new Scanner(System.in);
		System.out.println("Voer gebruikersnaam in:");
		String userNameInput = reader.next();
		
		System.out.println("Voer wachtwoord in:");
		String passwordInput = reader.next();
		
		datalaag.DatabaseHandler.getInstance().login(userNameInput, passwordInput);
		
		
		
		
		
	}
	
	public void register() {
		Scanner reader = new Scanner(System.in);
	
		System.out.println("Voer gebruikersnaam in:");
		String userNameInput = reader.next();
		// controleer of de gebruikersnaam tussen de 3 en 15 tekens is
		while (userNameInput.length() < 3 || userNameInput.length() > 15) {
			System.out
					.println("De gebruikersnaam moet tussen de 3 en de 15 tekens bevatten. Vul het opnieuw in:");
			userNameInput = reader.next();
		}
		System.out.println("Voer wachtwoord in:");
		String passwordInput = reader.next();
		// Controleer of wachtwoord tussen de 6 en 15 tekens is
		while (passwordInput.length() < 6) {
			System.out
					.println("Het wachtwoord moet minimaal 6 tekens bevatten. Vul het opnieuw in:");
			passwordInput = reader.next();
		}
		System.out.println("Herhaal uw wachtwoord:");
		String passwordConfirmation = reader.next();
		
		// Controleer of de opgegeven wachtwoorden overeen komen
		while (!passwordInput.equals(passwordConfirmation)) {
			System.out
					.println("De opgegeven wachtwoorden komen niet overeen. Vul het wachtwoord opnieuw in:");
			passwordInput = reader.next();
			// Controleer of wachtwoord tussen de 6 en 20 tekens is
			while (passwordInput.length() < 6 || passwordInput.length() > 20) {
				System.out
						.println("Het wachtwoord moet minimaal 6 tekens bevatten. Vul het opnieuw in:");
				passwordInput = reader.next();
			}
			
			// Voer opnieuw het confirmatie wachtwoord in
			System.out.println("Herhaal uw wachtwoord:");
			passwordConfirmation = reader.next();
		}

		// Voer de gebruiker in in de database
		

		
		datalaag.DatabaseHandler.getInstance().register(userNameInput, passwordInput);

	}

}
