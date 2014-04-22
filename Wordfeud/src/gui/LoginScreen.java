package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginScreen extends JPanel {
	private JPanel buttons;
	private JPanel dataField;
	private MainFrame mainFrame;

	public LoginScreen(MainFrame mainFrame) {
		this.mainFrame = mainFrame;

		this.setPreferredSize(getSize());
		this.setLayout(new BorderLayout());

		createButtons();
		createDataField();

		this.add(dataField, BorderLayout.NORTH);
		this.add(buttons);
	}

	private void createButtons() {
		buttons = new JPanel();
		JButton loginButton = new JButton("Login");
		JButton spectateButton = new JButton("Spectate");
		JButton registerButton = new JButton("Register");

		registerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mainFrame.setRegScreen();
			}
		});

		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainFrame.setPlayerScreen();
				mainFrame.setPlayerMenuBar();
			}

		});

		buttons.add(registerButton);
		buttons.add(spectateButton);
		buttons.add(loginButton);
	}

	private void createDataField() {
		dataField = new JPanel();
		JPanel labels = new JPanel(new GridLayout(2, 2, 0, 20));
		JPanel fields = new JPanel(new GridLayout(2, 1, 0, 20));

		JLabel usernameLabel = new JLabel("Username: ");
		JLabel passwordLabel = new JLabel("Password: ");
		JTextField usernameField = new JTextField(20);
		JPasswordField passwordField = new JPasswordField(20);

		labels.add(usernameLabel);
		labels.add(passwordLabel);

		fields.add(usernameField);
		fields.add(passwordField);

		dataField.add(labels);
		dataField.add(fields);
	}
}
