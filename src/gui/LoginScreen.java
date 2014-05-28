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

@SuppressWarnings("serial")
public class LoginScreen extends JPanel
	{
		private JPanel buttonPanel;
		private JPanel inputPanel;
		private MainFrame mainFrame;
		private JTextField usernameField;
		private JPasswordField passwordField;

		public LoginScreen(MainFrame mainFrame)
			{
				this.mainFrame = mainFrame;
				usernameField = new JTextField(20);
				passwordField = new JPasswordField(20);
				
				this.setLayout(null);

				createButtonPanel();
				createInputPanel();
				
				inputPanel.setBounds(400, 250, 400, 100);
				buttonPanel.setBounds(400, 350, 400, 100);

				this.add(inputPanel);
				this.add(buttonPanel);
			}

		private void createButtonPanel()
			{
				buttonPanel = new JPanel();
				JButton loginButton = new JButton("Login");
				JButton spectateButton = new JButton("Spectate");
				JButton registerButton = new JButton("Register");

				registerButton.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
							{
								mainFrame.setRegScreen();
							}
					});

				spectateButton.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
							{
								mainFrame.setSpecScreen();
								mainFrame.setSpecMenuBar();
							}
					});

				loginButton.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
							{
								loginPressed();
							}
					});

				buttonPanel.add(registerButton);
				buttonPanel.add(spectateButton);
				buttonPanel.add(loginButton);
			}

		private void createInputPanel()
			{
				inputPanel = new JPanel();
				JPanel labels = new JPanel(new GridLayout(2, 2, 0, 20));
				JPanel fields = new JPanel(new GridLayout(2, 1, 0, 20));

				JLabel usernameLabel = new JLabel("Username: ");
				JLabel passwordLabel = new JLabel("Password: ");

				labels.add(usernameLabel);
				labels.add(passwordLabel);

				fields.add(usernameField);
				fields.add(passwordField);

				inputPanel.add(labels);
				inputPanel.add(fields);
			}

		private void loginPressed()
			{
				String username = this.usernameField.getText();
				char[] password = this.passwordField.getPassword();

				if ((!username.equals("")) && (password.length > 0))
					{
						boolean succesfulLogin = mainFrame.callLoginAction(
								username, password);

						if (succesfulLogin)
							{
								mainFrame.setPlayerScreen();
								mainFrame.setStandardMenuBar();
								mainFrame.fillRoleWindow();
								mainFrame.setAccDataValues();
								clearInput();
							} else
							{
								JOptionPane.showMessageDialog(this, "Username and/or password is incorrect!");
								clearInput();
							}
					}
			}

		private void clearInput()
			{
				usernameField.setText(null);
				passwordField.setText(null);
			}
	}
