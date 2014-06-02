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
	private JPanel labelPanel = new JPanel();

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

		rolesValue.setBackground(null);
		
		createLabelPanel();
	}

	public void showAccData() {
		JPanel buttonPanel = new JPanel();
		JButton changeName = new JButton();
		JButton changePassword = new JButton();

		changeName.setText("Change name");
		changePassword.setText("Change password");

		buttonPanel.removeAll();
		buttonPanel.add(changeName);
		buttonPanel.add(changePassword);

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
		this.setLocationRelativeTo(mainFrame);
		this.setVisible(true);
	}

	public void showAdminAccData() {
		JPanel buttonPanel = new JPanel();
		JButton changeName = new JButton();
		JButton changePassword = new JButton();
		JButton changeRoles = new JButton();

		changeName.setText("Change name");
		changePassword.setText("Change password");
		
		buttonPanel.removeAll();
		buttonPanel.add(changeName);
		buttonPanel.add(changePassword);

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
				setRoles(mainFrame.getAdmin().changeRoles(
						userNameValue.getText(),
						DatabaseHandler.getInstance().getCurrentUserRole(
								userNameValue.getText())));
			}
		});

		this.add(buttonPanel, BorderLayout.SOUTH);
		this.add(labelPanel, BorderLayout.CENTER);

		this.pack();
		this.setLocationRelativeTo(mainFrame);
		this.setVisible(true);
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
