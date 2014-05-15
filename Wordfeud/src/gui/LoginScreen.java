package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class LoginScreen extends JPanel {
	private JPanel buttons;
	private JPanel dataField;
	private MainFrame mainFrame;
	private JTextField usernameField = new JTextField(20);
	private JPasswordField passwordField = new JPasswordField(20);
	private JLabel commentLabel = new JLabel();

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

		spectateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainFrame.setSpecScreen();
				mainFrame.setSpecMenuBar();
			}
		});

		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				login();
			}
		});

		loginButton.setBackground(Color.CYAN);
		registerButton.setBackground(Color.CYAN);
		spectateButton.setBackground(Color.CYAN);
		commentLabel.setForeground(Color.WHITE);

		buttons.setBackground(Color.DARK_GRAY);

		JButton shortCut;
		shortCut = new JButton("PRESS ME TO CHEAT");
		shortCut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// These are inputs optionspanes to start the game
				mainFrame.startGame();
			}
		});	
		buttons.add(shortCut);
		
		
		buttons.add(registerButton);
		buttons.add(spectateButton);
		buttons.add(loginButton);
		buttons.add(commentLabel);
	}

	private void createDataField() {
		dataField = new JPanel();
		JPanel labels = new JPanel(new GridLayout(2, 2, 0, 20));
		JPanel fields = new JPanel(new GridLayout(2, 1, 0, 20));

		JLabel usernameLabel = new JLabel("Username: ");
		JLabel passwordLabel = new JLabel("Password: ");

		labels.add(usernameLabel);
		labels.add(passwordLabel);

		fields.add(usernameField);
		fields.add(passwordField);

		usernameLabel.setForeground(Color.WHITE);
		passwordLabel.setForeground(Color.WHITE);

		labels.setBackground(Color.DARK_GRAY);
		fields.setBackground(Color.DARK_GRAY);
		dataField.setBackground(Color.DARK_GRAY);

		dataField.add(labels);
		dataField.add(fields);
	}

	private void login(){
		String ret;
		String username = this.usernameField.getText();
		char[] password = this.passwordField.getPassword();

		ret = mainFrame.callLoginAction(username, password);

		commentLabel.setText(ret);

		if(ret.equals("Username and Password are correct")){
			mainFrame.setPlayerScreen();
			mainFrame.setPlayerMenuBar();

			clearInput();
		}
		
		mainFrame.getPlayerMenuBar().getRoleWindow().fillRoles(mainFrame.callGetRoles());
	}

	private void clearInput(){
		usernameField.setText(null);
		passwordField.setText(null);
		commentLabel.setText(null);
	}
}