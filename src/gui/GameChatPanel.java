package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import datalaag.DatabaseHandler;
 

@SuppressWarnings("serial")
public class GameChatPanel extends JPanel implements ActionListener {

	private JScrollPane inputScrollPane;
	private JScrollPane chatScrollPane;
	private JTextArea chatArea;
	private JTextArea input;
	private JButton sendButton;
	private ArrayList<String> chatMessages = new ArrayList<String>();
	private String user1;
	private int gameID;
	private String latestUpdatedMessageTimeDate;
	private String latestUpdatedMessage;

	private DatabaseHandler dbh = DatabaseHandler.getInstance();

	/**		eventually unnecessary	*/		//private TestGameClass testGameClass = new TestGameClass();

	/**		ALREADY unnecessary		*/		//private Timestamp latestTimeStamp = new Timestamp(latestDateStamp.getTime());

	/**		eventually unnecessary	*/		//private TestUserClass testUser1 = new TestUserClass();


	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////VRAAG VRAAG OVER BERICHTEN IN DATABASE- waarschijnlijk opgelost////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void setChatVariables(String ownName, int gameID){
		this.user1 = ownName;
		this.gameID = gameID;
		//start();	
	}
	
	public GameChatPanel() {				// I get the player object references and the gameID of this match from the class that invokes this constructor (Wouter, Mike)
		this.user1 = "You";
		
		latestUpdatedMessageTimeDate = "";

		this.setPreferredSize(new Dimension(500,500));
		this.setBackground(Color.gray);

		chatArea = new JTextArea();
		chatArea.setAlignmentX(Component.CENTER_ALIGNMENT);
		chatArea.setLineWrap(true);
		chatArea.setEditable(false);

		input = new JTextArea();
		input.setAlignmentX(Component.CENTER_ALIGNMENT);
		input.setLineWrap(true);

		inputScrollPane = new JScrollPane(input);
		inputScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		inputScrollPane.setPreferredSize(new Dimension(475,50));

		chatScrollPane = new JScrollPane(chatArea);
		chatScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		chatScrollPane.setPreferredSize(new Dimension(475,400));

		//dbh.chatRecieve(this.gameID , latestUpdatedMessageTimeDate);						// REAL CODE!

		sendButton = new JButton("Send");
		sendButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		sendButton.addActionListener(this);

		//latestUpdatedMessageTimeDate = Long.toString(System.currentTimeMillis());			// possibly wrong!

		this.setLayout(new FlowLayout());

		this.add(chatScrollPane);
		this.add(Box.createRigidArea(new Dimension(0,30)));
		this.add(inputScrollPane);
		this.add(Box.createRigidArea(new Dimension(0,35)));
		this.add(sendButton);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

										//chatArea.append("knop ingedrukt\n");			TESTCODE
		this.sendMessage();

	}


	public void sendMessage() {
		if (input.equals(null)) {
			return;
		} else {


		int gameId = this.gameID;								/**||||||||||||||||||||||||||||||||||||*/
		String message = input.getText();									/**||||||||||||||||||||||||||||||||||||*/
		String userName = user1; 							/**||||||||||||||||||||||||||||||||||||*/


		//chatArea.append(userName + ": " + message + "\n"); 			//this is a   TEST line

		dbh.chatSend(userName, gameId, message);  						//this is the REAL line

		input.setText(null);											// clearing the input JTextArea for a possible new message
		}

	}

	public synchronized void checkForMessages() {

		chatMessages = dbh.chatReceive(this.gameID , latestUpdatedMessageTimeDate); 			// REAL CODE

		for (String a : chatMessages) {
						//System.out.println("latest updated message time date: " + latestUpdatedMessageTimeDate); TEST CODE
						//System.out.println("latest updated message: "+ latestUpdatedMessage);		TEST CODE
			String[] parts 			= a.split("---");
			String senderUserName 	= parts[0];
			String dateTime 		= parts[1];
			String message 			= parts[2];
						//System.out.println("new message's dateTime: " + dateTime);		TEST CODE	
						//System.out.println("new message: " + message);		TEST CODE

			if ((latestUpdatedMessageTimeDate.equals(""))) {					//if it IS the first incoming message since startup of application				
				chatArea.append(senderUserName);	
				chatArea.append("(" + dateTime + "):" + "\n");
				chatArea.append("   \"" + message + "\"\n");

				latestUpdatedMessageTimeDate = dateTime;
				latestUpdatedMessage = message;
							//System.out.println("this is the first message");			TEST CODE
							//System.out.println("");									TEST CODE
			}

			else {																//if it's NOT the first incoming message since startup of application
								//System.out.println("this is NOT the first message");		TEST CODE
				if (!latestUpdatedMessageTimeDate.equals(dateTime) && !latestUpdatedMessage.equals(message)) { 		//if the incoming message is NOT already in the chatscreen
											//System.out.println("datetime and message are new");		TEST CODE
					chatArea.append(senderUserName);	
					chatArea.append(" (" + dateTime + "):" + "\n");
					chatArea.append("   \"" + message + "\"\n");

					latestUpdatedMessageTimeDate = dateTime;
					latestUpdatedMessage = message;
								//System.out.println("latest timeDate: " + latestUpdatedMessageTimeDate);		TEST CODE
								//System.out.println("latest message: " + latestUpdatedMessage);				TEST CODE
								//System.out.println("");
				}
				else {							//EXTRA CODE FOR TESTING
								//System.out.println("the incoming message is already in the chatscreen");	//EXTRA CODE FOR TESTING
								//System.out.println("");		//EXTRA CODE FOR TESTING
				}								//EXTRA CODE FOR TESTING
			}
		}
	}
}
