package gui;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import datalaag.DatabaseHandler;

@SuppressWarnings("serial")
public class AccDataWindow extends JDialog {
	private MainFrame mainFrame;

	private ArrayList<String> currentRoles = new ArrayList<String>();
	private JPanel buttonPanel = new JPanel();
	private JPanel labelPanel = new JPanel();
	private JButton changeName = new JButton();
	private JButton changePassword = new JButton();
	private JButton changeRoles = new JButton();

	private JLabel userName = new JLabel();
	private JLabel userNameValue = new JLabel();
	private JLabel password = new JLabel();
	private JLabel passwordValue = new JLabel();
	private JLabel roles = new JLabel();
	private JList rolesValue = new JList();

	public AccDataWindow(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		this.setModal(true);
		this.setResizable(false);
		this.setLocationRelativeTo(null);

		rolesValue.setBackground(null);

		createButtonPanel();
		createLabelPanel();
	}
	
	public void showAccData() {
		this.setTitle(userNameValue.getText() + "'s data");

		changeName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				userNameValue.setText(mainFrame.getuser().changeUsername());
			}
		});
		changePassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				passwordValue.setText(mainFrame.getuser().changePassword());
			}
		});

		this.add(buttonPanel, BorderLayout.SOUTH);
		this.add(labelPanel, BorderLayout.CENTER);

		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public void showAdminAccData() {
		this.setTitle(userNameValue.getText() + "'s data");

		roles.setText("Current roles");
		labelPanel.add(roles);
		labelPanel.add(rolesValue);

		changeRoles.setText("Change roles");
		buttonPanel.add(changeRoles);

		changeName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				userNameValue.setText(mainFrame.getAdmin().changeUsername(
						userNameValue.getText()));
			}
		});
		changePassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				passwordValue.setText(mainFrame.getAdmin().changePassword(
						userNameValue.getText()));
			}
		});
		changeRoles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setRoles(mainFrame.getAdmin().changeRoles(userNameValue.getText(), 
						DatabaseHandler.getInstance().getRole(
								userNameValue.getText())));
			}
		});

		this.add(buttonPanel, BorderLayout.SOUTH);
		this.add(labelPanel, BorderLayout.CENTER);

		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public void createButtonPanel() {
		changeName.setText("Change name");
		changePassword.setText("Change password");

		buttonPanel.add(changeName);
		buttonPanel.add(changePassword);
	}

	public void createLabelPanel() {
		labelPanel.setLayout(new GridLayout(3, 2, 0, 5));
		labelPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		userName.setText("User Name:");
		password.setText("Password:");

		labelPanel.add(userName);
		labelPanel.add(userNameValue);
		labelPanel.add(password);
		labelPanel.add(passwordValue);
	}

	public void setValues(String username, String password) {
		userNameValue.setText(username);
		passwordValue.setText(password);
	}

	public void setRoles(ArrayList<String> roles) {
		currentRoles = roles;
		currentRoles.add("Spectator");
		rolesValue.setListData(currentRoles.toArray());
		this.pack();
	}

}
