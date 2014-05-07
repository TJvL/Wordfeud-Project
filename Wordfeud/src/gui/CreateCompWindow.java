package gui;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class CreateCompWindow extends JFrame
{
	private JButton confirm = new JButton();
	private JPanel buttonPanel = new JPanel();
	private JPanel inputPanel = new JPanel();

	private JTextField endDate = new JTextField();
	private JTextField maxPlayers = new JTextField();

	private JPanel mainPanel = new JPanel();

	private JLabel endDateLabel = new JLabel();
	private JLabel maxPlayersLabel = new JLabel();
	private JLabel gameCreatedLabel = new JLabel();
	
	private String maxPlayersChosen = new String();
	
	

	public void createCompFrame()
	{
this.setTitle("Create Competition");
		createCompButtonpanel();
		createInputPanel();
		createMainPanel();
		this.setLayout(new BorderLayout());
		this.add(buttonPanel, BorderLayout.SOUTH);
		this.add(mainPanel, BorderLayout.CENTER);
		this.pack();
		this.setVisible(true);
		this.setResizable(false);

	}

	public void createCompButtonpanel()
	{
		buttonPanel.add(confirm);
		confirm.setText("Confirm ");
		confirm.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				// TODO Auto-generated method stub
				confirmClicked();
			}
			
		});
		
		
		

	}

	public void createInputPanel()
	{

		inputPanel.setLayout(new GridLayout(2, 2, 50, 25));

		endDateLabel.setText("End-date");
		maxPlayersLabel.setText("Max Players(2-24)");
		
		
		inputPanel.add(endDateLabel);
		inputPanel.add(endDate);
		

		endDate.setPreferredSize(new Dimension(200, 50));

		inputPanel.add(maxPlayersLabel);
		inputPanel.add(maxPlayers);

	}

	public void createMainPanel()
	{
		mainPanel.add(inputPanel);

	}
	
	public void confirmClicked()
	{
		if(endDate.getText().equals("") && maxPlayers.getText().equals(""))
		{

			gameCreatedLabel.setText("Missing Values !");
			buttonPanel.add(gameCreatedLabel);
			this.revalidate();
		}
		else
		{
			
			
			
			
			try{
				if(Integer.parseInt(maxPlayers.getText()) >=2 && Integer.parseInt(maxPlayers.getText()) <=24)
				{
					gameCreatedLabel.setText("Game Created");
					buttonPanel.add(gameCreatedLabel);
					this.revalidate();
				}
				else{
					gameCreatedLabel.setText("Invalid Values, max Player nr. was exceeded");
					buttonPanel.add(gameCreatedLabel);
					this.revalidate();
				}
			}
			catch(NumberFormatException nfe)
			{
				gameCreatedLabel.setText("Invalid Values");
				buttonPanel.add(gameCreatedLabel);
				this.revalidate();
			}
		
	
		
				
			
		
				
			
			

			
			
			
		}
	}
}
