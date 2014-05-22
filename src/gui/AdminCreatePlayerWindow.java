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

import domein.Administrator;
	
@SuppressWarnings("serial")
public class AdminCreatePlayerWindow extends JDialog
{
	private Administrator administrator = new Administrator(true);
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
				String ret[];
				System.out.println(usernameField.getText());
				System.out.println(passwordField.getPassword());
				System.out.println(confirmPasswordField.getPassword());
				System.out.println(admin.isSelected());
				System.out.println(mod.isSelected());
				System.out.println(player.isSelected());
				

				ret = administrator.adminRegister(usernameField.getText(), passwordField.getPassword(), confirmPasswordField.getPassword(), admin.isSelected(), mod.isSelected(), player.isSelected()).split("---");
			System.out.println(ret);

				JOptionPane.showMessageDialog(null, ret[0], "ERROR", JOptionPane.WARNING_MESSAGE);
			System.out.println(ret);

				if(ret[1].equals("true"))
				{
				dispose();
				}System.out.println(ret);
			}
		});
	}
	
	private void clearInput()
	{
		usernameField.setText(null);
		passwordField.setText(null);
		confirmPasswordField.setText(null);
	}

}
