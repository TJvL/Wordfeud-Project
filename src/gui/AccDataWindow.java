package gui;


import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import datalaag.DatabaseHandler;

@SuppressWarnings("serial")
public class AccDataWindow extends JDialog {
	private JPanel buttonPanel = new JPanel();
	private JPanel labelPanel = new JPanel();
	private JButton changeName = new JButton();
	private JButton changePassword = new JButton();

	private JLabel userName = new JLabel();
	private JLabel userNameValue = new JLabel();
	private JLabel password = new JLabel();
	private JLabel passwordValue = new JLabel();
	private String response;
	private String dbResponse;

	public void showAccData() {
		createButtonPanel();
		createLabelPanel();

		this.setModal(true);
		this.setResizable(false);
		this.setTitle("PlayerName's Statistics");

		this.add(buttonPanel, BorderLayout.SOUTH);
		this.add(labelPanel, BorderLayout.CENTER);

		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public void createButtonPanel() {
		changeName.setText("Change name");
		changePassword.setText("Change password");

		buttonPanel.add(changeName);
		buttonPanel.add(changePassword);

		changeName.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				response = JOptionPane.showInputDialog(null, "What is your desired name?",
								"Enter your desired name", JOptionPane.QUESTION_MESSAGE);
				if (!response.equals(""))
				{
					System.out.println(response);
					dbResponse = DatabaseHandler.getInstance().changeUsername(userNameValue.getText(), response);
					if (response.length() > 2 && response.length() < 16) 
					{
						if (dbResponse.equals("Your username has been succesfully changed")) 
						{
							DatabaseHandler.getInstance().changeUsername(userNameValue.getText(), response);
							userNameValue.setText(response);
						} 
						else 
						{
							JOptionPane.showMessageDialog(null, dbResponse,
									"ERROR", JOptionPane.WARNING_MESSAGE);
						}
					} 
					else 
					{
						JOptionPane.showMessageDialog(null, "Username must contain 3-15 characters.",
								"ERROR", JOptionPane.WARNING_MESSAGE);
					}
				}
			}});

		changePassword.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				response = JOptionPane.showInputDialog(null, "What is your desired password?", 
						"Enter your desired password", JOptionPane.QUESTION_MESSAGE);
				if (!response.equals("")) 
				{
					System.out.println(response);
					if (response.length() > 5) 
					{
						DatabaseHandler.getInstance().changePassword(
								userNameValue.getText(), response);
						passwordValue.setText(response);
					} 
					else 
					{
						JOptionPane.showMessageDialog(null,	"Password must contain at least 6 characters.",
								"ERROR", JOptionPane.WARNING_MESSAGE);
					}
				}
			}});
	}

	public void createLabelPanel() {
		labelPanel.setLayout(new GridLayout(2, 2, 0, 5));
		labelPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		userName.setText("User Name:");
		password.setText("Password:");

		labelPanel.add(userName);
		labelPanel.add(userNameValue);
		labelPanel.add(password);
		labelPanel.add(passwordValue);
	}

	public void setValues(String username, String password) {
		userNameValue.setText(username);
		passwordValue.setText(password);
	}

	public String getInput() {
		return response;
	}

}
