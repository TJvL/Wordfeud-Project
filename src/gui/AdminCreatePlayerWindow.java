package gui;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import datalaag.DatabaseHandler;
	
@SuppressWarnings("serial")
public class AdminCreatePlayerWindow extends JDialog
{
	private DatabaseHandler dbh = DatabaseHandler.getInstance();
	
	private JPanel buttons;
	private JPanel dataField;
	private JPanel radioButtonPanel = new JPanel();
	private JButton confirmButton = new JButton("Confirm");
	private JButton cancelButton = new JButton("Cancel");
	
	private JLabel usernameLabel = new JLabel("Username:");
	private JLabel passwordLabel = new JLabel("Password:");
	private JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
	private JLabel chooseRole = new JLabel("Choose roles: ");
	
	private JTextField usernameField = new JTextField(20);
	private JPasswordField passwordField = new JPasswordField(20);
	private JPasswordField confirmPasswordField = new JPasswordField(20);
	
	private JRadioButton admin = new JRadioButton();
	private JRadioButton mod = new JRadioButton();
	private JRadioButton player = new JRadioButton();

	private boolean adminSelected = false;
	private boolean modSelected = false;
	private boolean playerSelected = false;
	private boolean allFieldFilled = false;
	
	public void ShowAdminCreatePlayer()
	{	
		this.setTitle("Account registration");	
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(600,180));
		this.setResizable(false);
		createDataField();
		createJRadioButtonPanel();
		createButtons();
		
		this.setModal(true);
					
		this.add(dataField, BorderLayout.NORTH);
		this.add(radioButtonPanel, BorderLayout.CENTER);
		this.add(buttons, BorderLayout.SOUTH);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	private void createDataField() {
		dataField = new JPanel();
		JPanel labels = new JPanel(new GridLayout(3, 1));
		JPanel fields = new JPanel(new GridLayout(3, 1));
			
		labels.add(usernameLabel);
		labels.add(passwordLabel);
		labels.add(confirmPasswordLabel);

		fields.add(usernameField);
		fields.add(passwordField);
		fields.add(confirmPasswordField);
			
		dataField.add(labels);
		dataField.add(fields);
	}
		
	public void createJRadioButtonPanel()
	{
		admin.setText("Administrator");
		mod.setText("Moderator");
		player.setText("Player");
			
		radioButtonPanel.add(chooseRole);
		radioButtonPanel.add(admin);
		radioButtonPanel.add(mod);
		radioButtonPanel.add(player);
	}
	
	private void createButtons() 
	{
		buttons = new JPanel();
		
		buttons.add(cancelButton);
		buttons.add(confirmButton);

		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose(); // removes the screen
				clearInput();	
			}
		});

		confirmButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				adminRegister();
				if(allFieldFilled)
				{
				dispose();
				}
			}
		});
	}

	public void adminRegister()
	{
		String retValue = "U need to fill in all the fields";
		String username = usernameField.getText();
		char[] passInput = passwordField.getPassword();
		char[] confirmPassInput = confirmPasswordField.getPassword();
		
		String password = "";
		for (char c : passInput){
			password = password + c;
		}
		String passConfirm = "";
		for (char c : confirmPassInput){
			passConfirm = passConfirm + c;
		}
		
		if(admin.isSelected())
		{
			adminSelected = true;
		}
		if(mod.isSelected())
		{
			modSelected = true;
		}
		if(player.isSelected())
		{
			playerSelected = true;
		}
		if((!username.isEmpty() && !password.isEmpty() && !passConfirm.isEmpty()))
		{
			
			if((adminSelected || modSelected || playerSelected))
			{
				if (username.length() < 3 || username.length() > 15) 
				{
					retValue = "Username must be between 3 and 15 characters.";
				}
				else if (password.length() < 6) 
				{
					retValue = "The password must contain at least 6 characters.";
				}
				else if (!password.equals(passConfirm)) 
				{
					retValue = "The given passwords do not match.";
				}
				else
				{	allFieldFilled = true;
					dbh.adminRegister(username, password, adminSelected, modSelected, playerSelected);
					retValue = "Register confirmed";
				}
			}
			else
			{
				retValue = "U need to select at least 1 role";
			}
		}
		JOptionPane.showMessageDialog(null, retValue, "ERROR", JOptionPane.WARNING_MESSAGE);	
	}
	
	private void clearInput()
	{
		usernameField.setText(null);
		passwordField.setText(null);
		confirmPasswordField.setText(null);
	}

}
