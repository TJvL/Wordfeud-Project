package gui;

import javax.swing.JFrame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class AccDataWindow extends JFrame {
	private JPanel buttonPanel = new JPanel();
	private JPanel labelPanel = new JPanel();
	private JButton changeName = new JButton();
	private JButton changePassword = new JButton();

	private JLabel userName = new JLabel();
	private JLabel userNameValue = new JLabel();
	private JLabel password = new JLabel();
	private JLabel passwordValue = new JLabel();
	private String response;

	public void showAccData() {
		createButtonPanel();
		createLabelPanel();

		this.setPreferredSize(new Dimension(400, 500));

		this.setResizable(false);
		this.setTitle(" PlayerName's Statistics");

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
				// TODO Auto-generated method stub

				response = JOptionPane
						.showInputDialog(null, "What is your desired name?",
								"Enter your desired name",
								JOptionPane.QUESTION_MESSAGE);
				if (!response.equals("")) {
					System.out.println(response);
					userNameValue.setText(response);
				}
			}
		});

		changePassword.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				response = JOptionPane.showInputDialog(null,
						"What is your desired Email-adress?",
						"Enter your desired Email-adress",
						JOptionPane.QUESTION_MESSAGE);
				if (!response.equals("")) {
					System.out.println(response);
					passwordValue.setText(response);
				}
			}

		});
	}

	public void createLabelPanel() {
		labelPanel.setLayout(new GridLayout(4, 2));

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
