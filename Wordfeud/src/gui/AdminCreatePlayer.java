package gui;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

	import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
	
@SuppressWarnings("serial")
public class AdminCreatePlayer extends JFrame
{
	private JPanel buttons;
	private JPanel dataField;
	private JPanel radioButtonPanel = new JPanel();
	private JButton confirmButton = new JButton("Confirm");
	private JButton cancelButton = new JButton("Cancel");
	
	private JLabel usernameLabel = new JLabel("Username:");
	private JLabel passwordLabel = new JLabel("Password:");
	private JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
	private JLabel emailLabel = new JLabel("Email adres:");
	private JLabel chooseRole = new JLabel("Choose roles: ");
	
	private JTextField usernameField = new JTextField(20);
	private JPasswordField passwordField = new JPasswordField(20);
	private JPasswordField confirmPasswordField = new JPasswordField(20);
	private JTextField emailField = new JTextField(20);
	
	
	private JRadioButton admin = new JRadioButton();
	private JRadioButton mod = new JRadioButton();
	private JRadioButton player = new JRadioButton();


		
			public void ShowAdminCreatePlayer()
			{
				
				this.setLayout(new BorderLayout());
				this.setPreferredSize(new Dimension(600,250));
				createButtons();
				createDataField();
				createJRadioButtonPanel();
				

				this.add(dataField, BorderLayout.NORTH);
				this.add(radioButtonPanel, BorderLayout.CENTER);
				this.add(buttons, BorderLayout.SOUTH);
				this.pack();
				this.setVisible(true);
			}

			
		

		private void createButtons() {
			buttons = new JPanel();
			

			buttons.add(cancelButton);
			buttons.add(confirmButton);

			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					
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
	

}
