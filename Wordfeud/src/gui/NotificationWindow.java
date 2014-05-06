package gui;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class NotificationWindow extends JFrame
{
	private String listItems[] =
	{ "Competition Invite", "Invite Request for your Competition: name",
			"New word request" };

	private JTextArea infoTextArea = new JTextArea();
	private JScrollPane infoPane = new JScrollPane(infoTextArea);
	private JLabel title = new JLabel();
	private JList<String> notificationList = new JList<String>(listItems);
	private JButton select = new JButton();
	private DefaultListModel<String> listModel = new DefaultListModel<String>();
	private String itemSelected = "";
	
	
	
	public void openNotificationWindow()
	{

		
		notificationList.setModel(listModel);
		fillNotificationList();
		this.setLayout(new BorderLayout());
		
		notificationList.setPreferredSize(new Dimension(200, 400));
		this.add(notificationList, BorderLayout.CENTER);

		title.setPreferredSize(new Dimension(50, 100));
		title.setText("You have : " + listModel.getSize() + " notifications.");
		this.add(title, BorderLayout.NORTH);

		this.infoPane.setPreferredSize(new Dimension(200, 200));
		this.add(infoPane, BorderLayout.EAST);

		select.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				// TODO Auto-generated method stub
				selectClicked();
			}
		});

		select.setText("Select");
		this.add(select, BorderLayout.SOUTH);

		this.setVisible(true);
		this.pack();

	}
	
	public void selectClicked()
	{
		
	
		
	}

	public void fillNotificationList()
	{
		int counter = 0;
		while (counter < listItems.length)
		{
			listModel.add(counter, listItems[counter]);
			counter++;
		}
	}
}
