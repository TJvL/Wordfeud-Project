package domein;

import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import datalaag.DatabaseHandler;

public class Administrator extends Role {
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
				if (DatabaseHandler.getInstance()
						.changeUsername(username, response)
						.equals("Your username has been succesfully changed")) {
					DatabaseHandler.getInstance().changeUsername(username,
							response);
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
		System.out.println("done");
		System.out.println(response);
		return response;
	}

	public String changePassword(String username) {
		String response = JOptionPane.showInputDialog(null,
				"What is your desired password?",
				"Enter your desired password", JOptionPane.QUESTION_MESSAGE);
		if (!response.equals("")) {
			System.out.println(response);
			if (response.length() > 5) {
				DatabaseHandler.getInstance()
						.changePassword(username, response);
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
				DatabaseHandler.getInstance().setRole(username,
						playerButton.getText());
			}
			newRoles.add(playerButton.getText());
		} else {
			if (roles.contains(playerButton.getText())) {
				DatabaseHandler.getInstance().revokeRole(username,
						playerButton.getText());
			}
		}
		if (modButton.isSelected()) {
			if (!roles.contains(modButton.getText())) {
				DatabaseHandler.getInstance().setRole(username,
						modButton.getText());
			}
			newRoles.add(modButton.getText());
		} else {
			if (roles.contains(modButton.getText())) {
				DatabaseHandler.getInstance().revokeRole(username,
						modButton.getText());
			}
		}
		if (adminButton.isSelected()) {
			if (!roles.contains(adminButton.getText())) {
				DatabaseHandler.getInstance().setRole(username,
						adminButton.getText());
			}
			newRoles.add(adminButton.getText());
		} else {
			if (roles.contains(adminButton.getText())) {
				DatabaseHandler.getInstance().revokeRole(username,
						adminButton.getText());
			}
		}

		return newRoles;
	}
}
