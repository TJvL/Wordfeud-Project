package gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import datalaag.WordFeudConstants;

@SuppressWarnings("serial")
public class RegScreen extends JPanel {
	private JPanel buttonPanel;
	private JPanel inputPanel;
	private MainFrame mainFrame;

	private JTextField usernameField;
	private JPasswordField passwordField;
	private JPasswordField confirmPasswordField;

	public RegScreen(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		buttonPanel = new JPanel();
		inputPanel = new JPanel();

		this.setLayout(null);

		createButtonPanel();
		createInputPanel();
	}

	public void populateScreen() {
		this.removeAll();

		inputPanel.setBounds(400, 250, 400, 150);
		buttonPanel.setBounds(400, 400, 400, 100);

		this.add(inputPanel);
		this.add(buttonPanel);
	}

	private void createButtonPanel() {
		JButton confirmButton = new JButton("Confirm");
		JButton cancelButton = new JButton("Cancel");

		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelButtonPressed();
			}
		});

		confirmButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				registerPressed();
			}
		});

		buttonPanel.add(cancelButton);
		buttonPanel.add(confirmButton);
	}

	private void createInputPanel() {
		JPanel labels = new JPanel(new GridLayout(4, 1, 0, 20));
		JPanel fields = new JPanel(new GridLayout(4, 1, 0, 20));

		JLabel usernameLabel = new JLabel("Username:");
		JLabel passwordLabel = new JLabel("Password:");
		JLabel confirmPasswordLabel = new JLabel("Confirm Password:");

		usernameField = new JTextField(20);
		passwordField = new JPasswordField(20);
		confirmPasswordField = new JPasswordField(20);

		labels.add(usernameLabel);
		labels.add(passwordLabel);
		labels.add(confirmPasswordLabel);

		fields.add(usernameField);
		fields.add(passwordField);
		fields.add(confirmPasswordField);

		inputPanel.add(labels);
		inputPanel.add(fields);
	}

	private void cancelButtonPressed() {
		mainFrame.setLoginScreen();
		clearInput();
	}

	private void registerPressed() {
		String username = usernameField.getText();
		char[] password = passwordField.getPassword();
		char[] repPassword = confirmPasswordField.getPassword();

		String retValue = mainFrame.startRegisterWorker(username, password,
				repPassword);

		if (retValue.equals(WordFeudConstants.REGISTER_SUCCESS)) {
			JOptionPane.showMessageDialog(this, retValue);
			mainFrame.setLoginScreen();
		} else {
			JOptionPane.showMessageDialog(this, retValue);
		}
	}

	private void clearInput() {
		usernameField.setText(null);
		passwordField.setText(null);
		confirmPasswordField.setText(null);
	}
}