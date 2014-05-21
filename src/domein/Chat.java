package domein;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
 
public class Chat implements Runnable,ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
//	
//	private JFrame testFrame;
//	private JPanel chatPanel;
//	JScrollPane inputScrollPane;
//	JScrollPane chatScrollPane;
//	private JTextArea chatArea;
//	private JTextArea input;
//	private JButton sendButton;
//	private ArrayList<String> chatMessages = new ArrayList<String>();
//	private TestUserClass user1;
//	private TestUserClass user2;
//	private int gameID;
//	private String latestUpdatedMessageTimeDate;
//	private String latestUpdatedMessage;
//
//	private Thread runner;
//
//	DatabaseHandler dbh = DatabaseHandler.getInstance();
//
//	/**		eventually unnecessary	*/		private TestGameClass testGameClass = new TestGameClass();
//
//	/**		ALREADY unnecessary		*/		//private Timestamp latestTimeStamp = new Timestamp(latestDateStamp.getTime());
//
//	/**		eventually unnecessary	*/		//private TestUserClass testUser1 = new TestUserClass();
//
//	private void start() {
//		if (runner == null ) {
//			runner = new Thread(this);
//			runner.start();
//		}
//	}
//
//	@Override
//	public void run() {
//						chatArea.append("test 1!\n");										//TEST CODE
//						chatArea.append("hopelijk staaT dit op de volgende regel!\n");		//TEST CODE
//						/** 	 */		// REAL CODE // it recieves every message from this match that have ever been sent (all messages)
//
//		while (true) {
//			this.checkForMessages();
//			try { Thread.sleep(5000); } catch (Exception e) {}
//		}
//	}
//
//	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//	///////////////////////////////VRAAG VRAAG OVER BERICHTEN IN DATABASE- waarschijnlijk opgelost////////////////
//	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//	public Chat(TestUserClass user1, TestUserClass user2, int gameID) {				// I get the player object references and the gameID of this match from the class that invokes this constructor (Wouter, Mike)
//		this.user1 = user1;
//		this.user2 = user2;
//		this.gameID = gameID;
//
//		latestUpdatedMessageTimeDate = "";
//
//		testFrame = new JFrame();
//		testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		testFrame.setPreferredSize(new Dimension(300,500));
//		testFrame.setResizable(false);
//
//		chatPanel = new JPanel();
//		chatPanel.setBackground(Color.gray);
//
//		chatArea = new JTextArea();
//		chatArea.setAlignmentX(Component.CENTER_ALIGNMENT);
//		chatArea.setLineWrap(true);
//		chatArea.setEditable(false);
//
//		input = new JTextArea();
//		input.setAlignmentX(Component.CENTER_ALIGNMENT);
//		input.setLineWrap(true);
//
//		inputScrollPane = new JScrollPane(input);
//		inputScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//		inputScrollPane.setPreferredSize(new Dimension(250,90));
//
//		chatScrollPane = new JScrollPane(chatArea);
//		chatScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//		chatScrollPane.setPreferredSize(new Dimension(280,330));
//
//		//dbh.chatRecieve(this.gameID , latestUpdatedMessageTimeDate);						// REAL CODE!
//
//		sendButton = new JButton("Send");
//		sendButton.setAlignmentX(Component.CENTER_ALIGNMENT);
//		sendButton.addActionListener(this);
//
//		//latestUpdatedMessageTimeDate = Long.toString(System.currentTimeMillis());			// possibly wrong!
//
//		chatPanel.setLayout(new FlowLayout());
//
//		chatPanel.add(chatScrollPane);
//		chatPanel.add(Box.createRigidArea(new Dimension(0,30)));
//		chatPanel.add(inputScrollPane);
//		chatPanel.add(Box.createRigidArea(new Dimension(0,35)));
//		chatPanel.add(sendButton);
//
//		testFrame.add(chatPanel);
//
//		testFrame.pack();
//		testFrame.setVisible(true);
//	}
//
//	@Override
//	public void actionPerformed(ActionEvent arg0) {
//
//										//chatArea.append("knop ingedrukt\n");			TESTCODE
//		this.sendMessage();
//
//	}
//
//
//	public void sendMessage() {
//		if (input.equals(null)) {
//			return;
//		} else {
//
//
//		int gameId = this.gameID;								/**||||||||||||||||||||||||||||||||||||*/
//		String message = input.getText();									/**||||||||||||||||||||||||||||||||||||*/
//		String userName = user1.getName(); 							/**||||||||||||||||||||||||||||||||||||*/
//
//
//		//chatArea.append(userName + ": " + message + "\n"); 			//this is a   TEST line
//
//		dbh.chatSend(userName, gameId, message);  						//this is the REAL line
//
//		input.setText(null);											// clearing the input JTextArea for a possible new message
//		}
//
//	}
//
//	public void checkForMessages() {
//		chatMessages = dbh.chatReceive(this.gameID , latestUpdatedMessageTimeDate); 			// REAL CODE
//
//		for (String a : chatMessages) {
//						//System.out.println("latest updated message time date: " + latestUpdatedMessageTimeDate); TEST CODE
//						//System.out.println("latest updated message: "+ latestUpdatedMessage);		TEST CODE
//			String[] parts 			= a.split("---");
//			String senderUserName 	= parts[0];
//			String dateTime 		= parts[1];
//			String message 			= parts[2];
//						//System.out.println("new message's dateTime: " + dateTime);		TEST CODE	
//						//System.out.println("new message: " + message);		TEST CODE
//
//			if ((latestUpdatedMessageTimeDate.equals(""))) {					//if it IS the first incoming message since startup of application				
//				chatArea.append(senderUserName + ": ");	
//				chatArea.append(" \"" + message + "\" ");
//				chatArea.append("(" + dateTime + ")" + "\n");
//
//				latestUpdatedMessageTimeDate = dateTime;
//				latestUpdatedMessage = message;
//							//System.out.println("this is the first message");			TEST CODE
//							//System.out.println("");									TEST CODE
//			}
//
//			else {																//if it's NOT the first incoming message since startup of application
//								//System.out.println("this is NOT the first message");		TEST CODE
//				if (!latestUpdatedMessageTimeDate.equals(dateTime) && !latestUpdatedMessage.equals(message)) { 		//if the incoming message is NOT already in the chatscreen
//											//System.out.println("datetime and message are new");		TEST CODE
//					chatArea.append(senderUserName + ": ");
//					chatArea.append(" \"" + message + "\" ");
//					chatArea.append("(" + dateTime + ")" + "\n");
//
//					latestUpdatedMessageTimeDate = dateTime;
//					latestUpdatedMessage = message;
//								//System.out.println("latest timeDate: " + latestUpdatedMessageTimeDate);		TEST CODE
//								//System.out.println("latest message: " + latestUpdatedMessage);				TEST CODE
//								//System.out.println("");
//				}
//				else {							//EXTRA CODE FOR TESTING
//								//System.out.println("the incoming message is already in the chatscreen");	//EXTRA CODE FOR TESTING
//								//System.out.println("");		//EXTRA CODE FOR TESTING
//				}								//EXTRA CODE FOR TESTING
//			}
//		}
//	}*/
}