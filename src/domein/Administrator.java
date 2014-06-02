package domein;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import datalaag.DatabaseHandler;

import java.util.Map.Entry;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class Administrator extends Role {
	private HashMap<String, Competition> adminCompetitions;

	public Administrator(boolean hasPermissions) {
		super(hasPermissions);
		adminCompetitions = new HashMap<String, Competition>();
	}

	public String changeUsername(String username) {
		String response = JOptionPane.showInputDialog(null,
				"What is your desired name?", "Enter your desired name",
				JOptionPane.QUESTION_MESSAGE);
		if ((!response.equals("")) && (response != null)) {
			System.out.println(response);
			if (response.length() > 2 && response.length() < 16) {
				System.out.println("good size");
				if (DatabaseHandler.getInstance().changeUsername(username, response).equals(
						"Your username has been succesfully changed")) {
					DatabaseHandler.getInstance().changeUsername(username, response);
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
				DatabaseHandler.getInstance().changePassword(username, response);
			} else {
				JOptionPane.showMessageDialog(null,
						"Password must contain at least 6 characters.",
						"ERROR", JOptionPane.WARNING_MESSAGE);
				response = DatabaseHandler.getInstance().getPassword(username);
			}
		} else {
			response = DatabaseHandler.getInstance().getPassword(username);
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
				DatabaseHandler.getInstance().setRole(username, playerButton.getText());
			}
			newRoles.add(playerButton.getText());
		} else {
			if (roles.contains(playerButton.getText())) {
				DatabaseHandler.getInstance().revokeRole(username, playerButton.getText());
			}
		}
		if (modButton.isSelected()) {
			if (!roles.contains(modButton.getText())) {
				DatabaseHandler.getInstance().setRole(username, modButton.getText());
			}
			newRoles.add(modButton.getText());
		} else {
			if (roles.contains(modButton.getText())) {
				DatabaseHandler.getInstance().revokeRole(username, modButton.getText());
			}
		}
		if (adminButton.isSelected()) {
			if (!roles.contains(adminButton.getText())) {
				DatabaseHandler.getInstance().setRole(username, adminButton.getText());
			}
			newRoles.add(adminButton.getText());
		} else {
			if (roles.contains(adminButton.getText())) {
				DatabaseHandler.getInstance().revokeRole(username, adminButton.getText());
			}
		}

		return newRoles;
	}

	public String adminRegister(String usernameField, char[] passInputField,
			char[] confirmPassInputField, boolean admin, boolean mod,
			boolean player) {
		boolean adminSelected = admin;
		boolean modSelected = mod;
		boolean playerSelected = player;
		boolean allFieldFilled = false;

		String username = usernameField;
		char[] passInput = passInputField;
		char[] confirmPassInput = confirmPassInputField;

		String retValue = "U need to fill in all the fields";

		String password = "";
		for (char c : passInput) {
			password = password + c;
		}
		String passConfirm = "";
		for (char c : confirmPassInput) {
			passConfirm = passConfirm + c;
		}

		if ((!username.isEmpty() && !password.isEmpty() && !passConfirm
				.isEmpty())) {
			if ((adminSelected || modSelected || playerSelected)) {
				if (username.length() < 3 || username.length() > 15) {
					retValue = "Username must be between 3 and 15 characters.";
				} else if (password.length() < 6) {
					retValue = "The password must contain at least 6 characters.";
				} else if (!password.equals(passConfirm)) {
					retValue = "The given passwords do not match.";
				} else {
					allFieldFilled = true;
					DatabaseHandler.getInstance().adminRegister(username, password, adminSelected,
							modSelected, playerSelected);
					retValue = "Register confirmed";
				}
			} else {
				retValue = "U need to select at least 1 role";
			}
		}
		return retValue + "---" + allFieldFilled;
	}

	public void loadAdminCompetitions() {
		adminCompetitions.clear();
		System.out.println("Admin: loading all active competitions...");
		ArrayList<String> activeComps = DatabaseHandler.getInstance().fetchAdminCompetitions();
		if (!activeComps.isEmpty()) {
			for (String comp : activeComps) {
				String[] compData = comp.split("---");
				
				// --------TEST CODE---------
				System.out.println("RAW DATA PRINT COMP: " + compData[0] + " "
						+ compData[1] + " " + compData[2] + " " + compData[3]
						+ " " + compData[4] + " " + compData[5] + " "
						+ compData[6]);
				// --------TEST CODE---------
				
				adminCompetitions.put(compData[0],
						new Competition(Integer.parseInt(compData[0]),
								compData[1], compData[2], compData[3],
								compData[4], Integer.parseInt(compData[5]),
								Integer.parseInt(compData[6])));
			}
			System.out.println("Succesfully loaded active competitions.");
		}
	}

	public Set<Entry<String, Competition>> getAdminCompEntries() {
		return adminCompetitions.entrySet();
	}

	public ArrayList<String> adminCompetitionParticipants(int compID) {
		return DatabaseHandler.getInstance().peopleInCompetition(compID);
	}
}
