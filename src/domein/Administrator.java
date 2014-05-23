package domein;

import javax.swing.JOptionPane;

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
}
