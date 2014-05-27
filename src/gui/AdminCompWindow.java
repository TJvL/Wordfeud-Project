package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;

import domein.Administrator;

@SuppressWarnings("serial")
public class AdminCompWindow extends JDialog{

	private AdminCompScreen acs;
	private Administrator admin = new Administrator(true);
	private JPanel listPanel;
	
	public AdminCompWindow()
	{
		this.setTitle("Competition Participants");	
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(400,600));
		this.setResizable(false);
//		createDataField();
//		createJRadioButtonPanel();
//		createButtons();
		
		this.setModal(true);
							
//		this.add(dataField, BorderLayout.NORTH);
//		this.add(radioButtonPanel, BorderLayout.CENTER);
//		this.add(buttons, BorderLayout.SOUTH);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	public void createListField()
	{
		listPanel = new JPanel();
		listPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));	
		
		ArrayList<String> participantArray = admin.adminCompetitionParticipants(acs.getCompID());
		JList participantList = new JList(participantArray.toArray());
		
	}
	
}
