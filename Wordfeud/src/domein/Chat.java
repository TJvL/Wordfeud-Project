package domein;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import datalaag.DatabaseHandler;
 
public class Chat implements Runnable,ActionListener {
	private JFrame testFrame;
	private JPanel chatPanel;
	private JTextArea chatArea;
	private JTextArea input;
	private JButton sendButton;
	private ArrayList<String> chatMessages;
	private Player player1;
	private Player player2;
	private int gameID;
	
	/**eventually unnecessary	*/		private TestGameClass testGameClass = new TestGameClass();
	DatabaseHandler dbh = new DatabaseHandler();
	
	private String latestUpdatedMessageTimeDate;
	/**already unnecessary		*/		//private Timestamp latestTimeStamp = new Timestamp(latestDateStamp.getTime());
	
	/**eventually unnecessary	*/		private TestUserClass testUser1 = new TestUserClass("Ronnie376");
	
	
	@Override
	public void run() {
		chatArea.append("test 1!\n");
		chatArea.append("hopelijk staaT dit op de volgende regel!\n");
		this.checkForMessages();
		
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////VRAAG VRAAG OVER BERICHTEN IN DATABASE/////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public Chat(Player player1, Player player2, int gameID) {				// I get the player object references and the gameID of this match from the class that invokes this constructor (Wouter, Mike)
		this.player1 = player1;
		this.player2 = player2;
		this.gameID = gameID;
		
		testFrame = new JFrame();
		testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		testFrame.setPreferredSize(new Dimension(300,500));
		testFrame.setResizable(false);
		
		chatPanel = new JPanel();
		chatPanel.setBackground(Color.gray);
		
		chatArea = new JTextArea();
		chatArea.setAlignmentX(Component.CENTER_ALIGNMENT);
		chatArea.setLineWrap(true);
		chatArea.setEditable(false);
		
		input = new JTextArea();
		input.setAlignmentX(Component.CENTER_ALIGNMENT);
		input.setLineWrap(true);
		
		JScrollPane inputScrollPane = new JScrollPane(input);
		inputScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		inputScrollPane.setPreferredSize(new Dimension(250,90));
		
		JScrollPane chatScrollPane = new JScrollPane(chatArea);
		chatScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		chatScrollPane.setPreferredSize(new Dimension(280,330));
		
		
		sendButton = new JButton("Send");
		sendButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		sendButton.addActionListener(this);
		
		latestUpdatedMessageTimeDate = Long.toString(System.currentTimeMillis());
		
		chatPanel.setLayout(new FlowLayout());
		
		chatPanel.add(chatScrollPane);
		chatPanel.add(Box.createRigidArea(new Dimension(0,30)));
		chatPanel.add(inputScrollPane);
		chatPanel.add(Box.createRigidArea(new Dimension(0,35)));
		chatPanel.add(sendButton);
	
		testFrame.add(chatPanel);
		
		testFrame.pack();
		testFrame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		chatArea.append("knop ingedrukt\n");
		this.sendMessage();
		
	}
	
	
	public void sendMessage(/*Player player, String message*/) {
		if (input.equals(null)) {
			return;
		} else {
		
		
		int gameId = testGameClass.getSpelID();								/**||||||||||||||||||||||||||||||||||||*/
		String message = input.getText();									/**||||||||||||||||||||||||||||||||||||*/
		String userName = testUser1.getUserName(); 							/**||||||||||||||||||||||||||||||||||||*/
		
		
		//chatArea.append(userName + ": " + message + "\n"); 			//this is a   TEST line
		 										
		dbh.chatSend(userName, gameId, message);  						//this is the REAL line
		
		input.setText(null);											// clearing the input JTextArea for a possible new message
		}
		
	}
	
	public void checkForMessages() {
		
		
		//while (true) {
						
			/**chatMessages = dbh.chatRecieve(this.gameID , latestUpdatedMessageTimeDate);*/
			chatMessages.add("Baaz456---2014-03-04 10:45:23---Ik was nog niet thuis! :D");
			
			for (String a : chatMessages) {
				String[] parts 			= a.split("---");
				String senderUserName 	= parts[1];
				String dateTime 		= parts[2];
				String message 			= parts[3];
				
				System.out.println(senderUserName);
				System.out.println(dateTime);
				System.out.println(message);
			}
			
			
		//}
	}
}
