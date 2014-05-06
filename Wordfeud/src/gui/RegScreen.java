package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class RegScreen extends JPanel {
	private JPanel buttons;
	private JPanel dataField;
	private MainFrame mainFrame;
	private String username;
	private String password;
	private String repPassword;
	private String email;
	private JTextField usernameField = new JTextField(20);
	private JPasswordField passwordField = new JPasswordField(20);
	private JPasswordField confirmPasswordField = new JPasswordField(20);
	private JTextField emailField = new JTextField(20);
	

	public RegScreen(MainFrame mainFrame) {
		this.mainFrame = mainFrame;

		this.setLayout(new BorderLayout());

		createButtons();
		createDataField();

		this.add(dataField, BorderLayout.NORTH);
		this.add(buttons);
	}

	private void createButtons() {
		buttons = new JPanel();
		JButton confirmButton = new JButton("Confirm");
		JButton cancelButton = new JButton("Cancel");

		buttons.add(cancelButton);
		buttons.add(confirmButton);

		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mainFrame.setLoginScreen();
			}
		});

		confirmButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				register();
			}
		});
	}

	private void createDataField() {
		dataField = new JPanel();
		JPanel labels = new JPanel(new GridLayout(4, 1, 0, 20));
		JPanel fields = new JPanel(new GridLayout(4, 1, 0, 20));
		
		JLabel usernameLabel = new JLabel("Username:");
		JLabel passwordLabel = new JLabel("Password:");
		JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
		JLabel emailLabel = new JLabel("Email adres:");
		
		labels.add(usernameLabel);
		labels.add(passwordLabel);
		labels.add(confirmPasswordLabel);
		labels.add(emailLabel);
		
		fields.add(usernameField);
		fields.add(passwordField);
		fields.add(confirmPasswordField);
		fields.add(emailField);
		
		dataField.add(labels);
		dataField.add(fields);
	}
	
	private void register(){
		this.username = usernameField.getText();
		this.password = passwordField.getPassword().toString();
		this.repPassword = confirmPasswordField.getPassword().toString();
		this.email = emailField.getText();
	}
}
