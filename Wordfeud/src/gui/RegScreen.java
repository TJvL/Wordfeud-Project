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
				mainFrame.setLoginOptionsScreen();
			}
		});

		confirmButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
	}

	private void createDataField() {
		dataField = new JPanel();
		JPanel labels = new JPanel(new GridLayout(4, 1));
		JPanel fields = new JPanel(new GridLayout(4, 1));
		
		JLabel usernameLabel = new JLabel("Username:");
		JLabel passwordLabel = new JLabel("Password:");
		JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
		JLabel emailLabel = new JLabel("Email adres:");
		
		JTextField usernameField = new JTextField(20);
		JPasswordField passwordField = new JPasswordField(20);
		JPasswordField confirmPasswordField = new JPasswordField(20);
		JTextField emailField = new JTextField(20);
		
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
}
