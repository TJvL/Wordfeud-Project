package gui;

import javax.swing.JFrame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class AccDataWindow extends JFrame
{
	private JPanel buttonPanel = new JPanel();
	private JPanel labelPanel = new JPanel();
	private JButton changeName = new JButton();
	private JButton changeEmail = new JButton();

	private JLabel userName = new JLabel();
	private JLabel userNameValue = new JLabel();

	private JLabel emailAdress = new JLabel();
	private JLabel emailAdressValue = new JLabel();
	private JLabel password = new JLabel();
	private JLabel passwordValue = new JLabel();



	public void showAccData()
	{
		createButtonPanel();
		createLabelPanel();

		this.setPreferredSize(new Dimension(400, 500));

		this.setResizable(false);
		this.setTitle(" PlayerName's Statistics");


		

		this.add(buttonPanel, BorderLayout.SOUTH);
		this.add(labelPanel, BorderLayout.CENTER);

		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public void createButtonPanel()
	{
		changeName.setText("Change name");
		changeEmail.setText("Change Email");

		buttonPanel.add(changeName);
		buttonPanel.add(changeEmail);

		changeName.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				// TODO Auto-generated method stub

				String response = JOptionPane
						.showInputDialog(null, "What is your desired name?",
								"Enter your desired name",
								JOptionPane.QUESTION_MESSAGE);
				System.out.println(response);

			}
		});

		changeEmail.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				// TODO Auto-generated method stub
				String response = JOptionPane.showInputDialog(null,
						"What is your desired Email-adress?",
						"Enter your desired Email-adress",
						JOptionPane.QUESTION_MESSAGE);
				if (response != null)
				{
					System.out.println(response);
				}
				{
					System.out.println("No value was entered !");
				}

			}

		});
	}

	public void createLabelPanel()
	{
		labelPanel.setLayout(new GridLayout(4, 2, 20, 20));
		userName.setText("User Name:");
		userName.setFont(new Font("Serif", Font.BOLD, 17));

		emailAdress.setText("Email Adress:");
		emailAdress.setFont(new Font("Serif", Font.BOLD, 17));

		userNameValue.setText("value");
		userNameValue.setFont(new Font("Serif", Font.PLAIN, 17));

		emailAdressValue.setText("value");
		emailAdressValue.setFont(new Font("Serif", Font.PLAIN, 17));

		password.setText("Password:");
		password.setFont(new Font("Serif", Font.BOLD, 17));
		passwordValue.setText("value");
		passwordValue.setFont(new Font("Serif", Font.PLAIN, 17));

		labelPanel.add(userName);
		labelPanel.add(userNameValue);
		labelPanel.add(emailAdress);
		labelPanel.add(emailAdressValue);
		labelPanel.add(password);
		labelPanel.add(passwordValue);
	}

}
