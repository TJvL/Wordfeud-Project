package gui;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class NotificationWindow extends JFrame
{
	private String listItems[] =
	{ "Competition Invite", "Invite Request for your Competition: name",
			"New word request" };

	private JTextArea infoTextArea = new JTextArea();
	private JScrollPane infoPane = new JScrollPane(infoTextArea);
	private JLabel title = new JLabel();
	private JList notificationList = new JList(listItems);
	private JButton select = new JButton();
	DefaultListModel listModel = new DefaultListModel();
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
