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
public class LoginScreen extends JPanel {
	private JPanel buttonPanel;
	private JPanel inputPanel;
	private MainFrame mainFrame;
	private JTextField usernameField;
	private JPasswordField passwordField;

	public LoginScreen(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		inputPanel = new JPanel();
		buttonPanel = new JPanel();

		this.setLayout(null);

		createButtonPanel();
		createInputPanel();
	}

	public void populateScreen() {
		this.removeAll();

		inputPanel.setBounds(400, 250, 400, 100);
		buttonPanel.setBounds(400, 350, 400, 100);

		this.add(inputPanel);
		this.add(buttonPanel);

		mainFrame.revalidate();
	}

	private void createButtonPanel() {
		JButton loginButton = new JButton("Login");
		JButton spectateButton = new JButton("Spectate");
		JButton registerButton = new JButton("Register");

		registerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				registerButtonPressed();
			}
		});

		spectateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				spectateButtonPressed();
			}
		});

		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loginButtonPressed();
			}
		});

		buttonPanel.add(registerButton);
		buttonPanel.add(spectateButton);
		buttonPanel.add(loginButton);
	}

	private void createInputPanel() {
		JPanel labels = new JPanel(new GridLayout(2, 2, 0, 20));
		JPanel fields = new JPanel(new GridLayout(2, 1, 0, 20));

		JLabel usernameLabel = new JLabel("Username: ");
		JLabel passwordLabel = new JLabel("Password: ");

		usernameField = new JTextField(20);
		passwordField = new JPasswordField(20);

		labels.add(usernameLabel);
		labels.add(passwordLabel);

		fields.add(usernameField);
		fields.add(passwordField);

		inputPanel.add(labels);
		inputPanel.add(fields);
	}

	private void registerButtonPressed() {
		mainFrame.setRegScreen();
	}

	private void spectateButtonPressed() {
		mainFrame.setSpecScreen();
		mainFrame.setSpecMenuBar();
	}

	private void loginButtonPressed() {
		String username = this.usernameField.getText();
		char[] password = this.passwordField.getPassword();

		if ((!username.equals("")) && (password.length > 0)) {
			int result = mainFrame.startLoginWorker(username, password);

			if (result == WordFeudConstants.LOGIN_SUCCES) {
				mainFrame.setCorrectRoleMainMenu();
				mainFrame.setStandardMenuBar();
				mainFrame.fillRoleWindow();
				mainFrame.setAccDataValues();
				clearInput();
			} else if (result == WordFeudConstants.LOGIN_FAIL) {
				JOptionPane.showMessageDialog(this,
						"Username and/or password is incorrect!");
				clearInput();
			}
		}
	}

	private void clearInput() {
		usernameField.setText(null);
		passwordField.setText(null);
	}
}
