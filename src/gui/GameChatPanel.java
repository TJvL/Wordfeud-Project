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
public class GameChatPanel extends JPanel {

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

	public void setChatVariables(String ownName, int gameID) {
		latestUpdatedMessageTimeDate = "";
		latestUpdatedMessage = "";
		if (input != null) {
			input.setText(null);
		}
		chatArea.setText(null);
		chatMessages.clear();
		this.user1 = ownName;
		this.gameID = gameID;
		checkForMessages();
		this.repaint();
		this.revalidate();
		// start();
	}

	public GameChatPanel() { // I get the player object references and the
								// gameID of this match from the class that
								// invokes this constructor (Wouter, Mike)
		this.user1 = "You";

		latestUpdatedMessageTimeDate = "";

		this.setPreferredSize(new Dimension(500, 500));
		this.setBackground(Color.gray);

		chatArea = new JTextArea();
		chatArea.setAlignmentX(Component.CENTER_ALIGNMENT);
		chatArea.setLineWrap(true);
		chatArea.setEditable(false);

		input = new JTextArea();
		input.setAlignmentX(Component.CENTER_ALIGNMENT);
		input.setLineWrap(true);

		inputScrollPane = new JScrollPane(input);
		inputScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		inputScrollPane.setPreferredSize(new Dimension(475, 50));

		chatScrollPane = new JScrollPane(chatArea);
		chatScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		chatScrollPane.setPreferredSize(new Dimension(475, 400));

		sendButton = new JButton("Send");
		sendButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		sendButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				sendMessage();
			}
		});

		this.setLayout(new FlowLayout());

		this.add(chatScrollPane);
		this.add(Box.createRigidArea(new Dimension(0, 30)));
		this.add(inputScrollPane);
		this.add(Box.createRigidArea(new Dimension(0, 35)));
		this.add(sendButton);
	}

	public void sendMessage() {
		if (input.equals(null)) {
			return;
		} else {
			int gameId = this.gameID;
			String message = input.getText();
			String userName = user1;

			dbh.chatSend(userName, gameId, message); // this is the REAL line

			input.setText(null); // clearing the input JTextArea for a possible
									// new message
		}
	}

	public synchronized void checkForMessages() {
		chatMessages = dbh.chatReceive(this.gameID,
				latestUpdatedMessageTimeDate); // REAL CODE

		for (String a : chatMessages) {
			String[] parts = a.split("---");
			String senderUserName = parts[0];
			String dateTime = parts[1];
			if (parts.length > 2) {
				String message = parts[2];
				if ((latestUpdatedMessageTimeDate.equals(""))) {
					// if it IS the first incoming message since startup of
					// application
					chatArea.append(senderUserName);
					chatArea.append("(" + dateTime + "):" + "\n");
					chatArea.append("   \"" + message + "\"\n");

					latestUpdatedMessageTimeDate = dateTime;
					latestUpdatedMessage = message;

				} else { // if it's NOT the first incoming message since startup
							// of
							// application
					if (!latestUpdatedMessageTimeDate.equals(dateTime)) {
						chatArea.append(senderUserName);
						chatArea.append(" (" + dateTime + "):" + "\n");
						chatArea.append("   \"" + message + "\"\n");

						latestUpdatedMessageTimeDate = dateTime;
						latestUpdatedMessage = message;

					}
				}
			}
		}
	}
}
