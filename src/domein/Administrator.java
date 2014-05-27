package domein;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import datalaag.DatabaseHandler;

public class Administrator extends Role {
	private DatabaseHandler dbh = DatabaseHandler.getInstance();

	public Administrator(boolean hasPermissions) {
		super(hasPermissions);
	}

	public String changeUsername(String username) {
		String response = JOptionPane.showInputDialog(null,
				"What is your desired name?", "Enter your desired name",
				JOptionPane.QUESTION_MESSAGE);
		if ((!response.equals("")) && (response != null)) {
			System.out.println(response);
			if (response.length() > 2 && response.length() < 16) {
				System.out.println("good size");
				if (dbh.changeUsername(username, response).equals(
						"Your username has been succesfully changed")) {
					dbh.changeUsername(username, response);
				} else {
					JOptionPane.showMessageDialog(null, DatabaseHandler
							.getInstance().changeUsername(username, response),
							"ERROR", JOptionPane.WARNING_MESSAGE);
					response = username;
				}
			} else {
				System.out.println("wrong size");
				JOptionPane.showMessageDialog(null,
						"Username must contain 3-15 characters.", "ERROR",
						JOptionPane.WARNING_MESSAGE);
				response = username;
			}
		} else {
			response = username;
		}
		return response;
	}

	public String changePassword(String username) {
		String response = JOptionPane.showInputDialog(null,
				"What is your desired password?",
				"Enter your desired password", JOptionPane.QUESTION_MESSAGE);
		if (!response.equals("")) {
			System.out.println(response);
			if (response.length() > 5) {
				dbh.changePassword(username, response);
			} else {
				JOptionPane.showMessageDialog(null,
						"Password must contain at least 6 characters.",
						"ERROR", JOptionPane.WARNING_MESSAGE);
				response = dbh.getPassword(username);
			}
		} else {
			response = dbh.getPassword(username);
		}
		return response;
	}

	public ArrayList<String> changeRoles(String username,
			ArrayList<String> roles) {
		ArrayList<String> newRoles = new ArrayList<String>();
		JPanel radioButtonPanel = new JPanel();
		JRadioButton playerButton = new JRadioButton("Player");
		JRadioButton modButton = new JRadioButton("Moderator");
		JRadioButton adminButton = new JRadioButton("Administrator");
		radioButtonPanel.add(playerButton);
		radioButtonPanel.add(modButton);
		radioButtonPanel.add(adminButton);

		for (int i = 0; i < roles.size(); i++) {
			if (roles.get(i).equals(playerButton.getText())) {
				playerButton.setSelected(true);
			} else if (roles.get(i).equals(modButton.getText())) {
				modButton.setSelected(true);
			} else if (roles.get(i).equals(adminButton.getText())) {
				adminButton.setSelected(true);
			}
		}

		JOptionPane.showOptionDialog(null, radioButtonPanel, "Select roles",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
				null, null, null);

		if (playerButton.isSelected()) {
			if (!roles.contains(playerButton.getText())) {
				dbh.setRole(username, playerButton.getText());
			}
			newRoles.add(playerButton.getText());
		} else {
			if (roles.contains(playerButton.getText())) {
				dbh.revokeRole(username, playerButton.getText());
			}
		}
		if (modButton.isSelected()) {
			if (!roles.contains(modButton.getText())) {
				dbh.setRole(username, modButton.getText());
			}
			newRoles.add(modButton.getText());
		} else {
			if (roles.contains(modButton.getText())) {
				dbh.revokeRole(username, modButton.getText());
			}
		}
		if (adminButton.isSelected()) {
			if (!roles.contains(adminButton.getText())) {
				dbh.setRole(username, adminButton.getText());
			}
			newRoles.add(adminButton.getText());
		} else {
			if (roles.contains(adminButton.getText())) {
				dbh.revokeRole(username, adminButton.getText());
			}
		}

		return newRoles;
	}
	
	public String adminRegister(String usernameField, char[] passInputField, char[] confirmPassInputField, boolean admin, boolean mod, boolean player)
	{	
		boolean adminSelected = admin;
		boolean modSelected = mod;
		boolean playerSelected = player;
		boolean allFieldFilled = false;
		
		String username = usernameField;
		char[] passInput = passInputField;
		char[] confirmPassInput = confirmPassInputField;
		
		String retValue = "U need to fill in all the fields";

		String password = "";
		for (char c : passInput){
			password = password + c;
		}
		String passConfirm = "";
		for (char c : confirmPassInput){
			passConfirm = passConfirm + c;
		}

		if((!username.isEmpty() && !password.isEmpty() && !passConfirm.isEmpty()))
		{
			if((adminSelected || modSelected || playerSelected))
			{
				if (username.length() < 3 || username.length() > 15) 
				{
					retValue = "Username must be between 3 and 15 characters.";
				}
				else if (password.length() < 6) 
				{
					retValue = "The password must contain at least 6 characters.";
				}
				else if (!password.equals(passConfirm)) 
				{
					retValue = "The given passwords do not match.";
				}
				else
				{	
					allFieldFilled = true;
					dbh.adminRegister(username, password, adminSelected, modSelected, playerSelected);
					retValue = "Register confirmed";
				}
			}
			else
			{
				retValue = "U need to select at least 1 role";
			}
		}
		return retValue + "---" + allFieldFilled;
	}
	
	public HashMap<Integer, String> adminCompetitions()
	{
		return dbh.activeCompetitions();
	}
	
	public ArrayList<String> adminCompetitionParticipants(int compID)
	{
		return dbh.peopleInCompetition(compID);
	}
}
