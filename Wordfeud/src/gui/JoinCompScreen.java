package gui;
///als ik via een constructor mijn joincompscreen "door geef" aan een andere klasse, en ik roep in die klasse de remove object aan, wordt dit hierin ook aangepast? 
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

/////this screen contains all public active  competitions 
public class JoinCompScreen extends JPanel

{

	private String listArray[] =
	{

	"Active Comp 1 ", "Active Comp 2", "Active Comp 3", "Active Comp 4" };
	private JScrollPane listScrollPane;
	private JScrollPane textAreaScrollPane;
	private JTextArea selectedTextArea = new JTextArea();
	private JPanel joinCompScreenButtonPanel = new JPanel();
	private JPanel joinCompScreenListPanel = new JPanel();
	private JPanel infoLabelsPanel = new JPanel();
	private String selectedValue = new String();
	private JButton selectComp = new JButton();
	private DefaultListModel<String> myListModel = new DefaultListModel<String>();
	private JList<String> activeCompsList = new JList<String>(listArray);
	private JButton requestInvite = new JButton();

	private JLabel compName = new JLabel();
	private JLabel NrOfparticipants = new JLabel();
	private JLabel compPrivate = new JLabel();
	private JLabel startsIn = new JLabel();
	private JLabel bordType = new JLabel();

	
	//run this method before setting the screen as contentpane
	
	public void createJoinCompScreen()
	{
		this.createjoinCompScreenListPanel();
		this.createjoinCompScreenButtonPanel();
		this.createjoinCompTextareaPanel();
		this.createInfoLabelsPanel();
		this.setLayout(new BorderLayout());
		this.setPreferredSize(this.getSize());
		this.add(joinCompScreenListPanel, BorderLayout.CENTER);
		this.add(joinCompScreenButtonPanel, BorderLayout.SOUTH);
		this.add(infoLabelsPanel, BorderLayout.EAST);
		// this.add(joinCompTextareaPanel, BorderLayout.EAST);
	}

	// /the following 3 methods are used to create 2 separate panels which are
	// both added to our main screen in the method above
	public void createjoinCompScreenListPanel()
	{

		// ///this panel contains our JList, where all the active games are
		// listed
		joinCompScreenListPanel.setLayout(new BorderLayout());
		joinCompScreenListPanel.setPreferredSize(this.getSize());
		DefaultListCellRenderer renderer = (DefaultListCellRenderer) activeCompsList
				.getCellRenderer();
		renderer.setHorizontalAlignment(JLabel.CENTER);
		activeCompsList.setModel(myListModel);
		listScrollPane = new JScrollPane(activeCompsList);
		listScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		joinCompScreenListPanel.add(listScrollPane, BorderLayout.CENTER);
		// //////

		textAreaScrollPane = new JScrollPane(selectedTextArea);
		selectedTextArea.setWrapStyleWord(true);
		selectedTextArea.setLineWrap(true);

		textAreaScrollPane
				.setVerticalScrollBarPolicy(textAreaScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		textAreaScrollPane.setPreferredSize(new Dimension(500, 1200));
		// joinCompTextareaPanel.add(textAreaScrollPane, BorderLayout.CENTER);
		joinCompScreenListPanel.add(selectedTextArea, BorderLayout.SOUTH);

		fillList();

	}

	public void createjoinCompScreenButtonPanel()

	{

		selectComp.setText("Select an active competition to view it");
		joinCompScreenButtonPanel.add(selectComp);
		selectComp.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				selectCompClicked();

			}
		});
		
		requestInvite.setText("Request Invite");
		joinCompScreenButtonPanel.add(requestInvite);
		requestInvite.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				// TODO Auto-generated method stub
				requestInviteClicked();
			}
			
		});
		

	}
	
	
	public void requestInviteClicked()
	{
		////checks if the user actually selected something 
		if(activeCompsList.getSelectedValue()!= null)
		{
			String itemSelected = activeCompsList.getSelectedValue().toString();
			selectedTextArea.setText("You have requested an invite for : " + itemSelected);
		}
		else
		{
			selectedTextArea.setText("Nothing was selected !");
	
		}
	}

	public void createjoinCompTextareaPanel()
	{
		this.setLayout(new BorderLayout());

	}

	public void createInfoLabelsPanel()
	{
		compName.setText("compName:");
		compName.setFont(new Font("Serif", Font.BOLD, 17));
		NrOfparticipants.setText("NrOfparticipants:");
		NrOfparticipants.setFont(new Font("Serif", Font.BOLD, 17));
		compPrivate.setText("compPrivate:");
		compPrivate.setFont(new Font("Serif", Font.BOLD, 17));
		startsIn.setText("startsIn:");
		startsIn.setFont(new Font("Serif", Font.BOLD, 17));
		bordType.setText("bordType:");
		bordType.setFont(new Font("Serif", Font.BOLD, 17));

		infoLabelsPanel.setLayout(new GridLayout(4, 2, 50, 50));
		infoLabelsPanel.add(compName);
		infoLabelsPanel.add(NrOfparticipants);
		infoLabelsPanel.add(compPrivate);
		infoLabelsPanel.add(startsIn);
		infoLabelsPanel.add(bordType);

	}

	public void fillList()
	{
		int counter = 0;
		while (counter < listArray.length)
		{

			myListModel.add(counter, listArray[counter]);
			counter++;

		}

	}

	public void selectCompClicked()

	{

		// /this method is called if an item is selected and the button is
		// clicked
		if (activeCompsList.getSelectedValue() != null)
		{

			selectedValue = activeCompsList.getSelectedValue().toString();
			selectedTextArea.setText("You have selected:  " + selectedValue
					);

		}

		else
		{
			selectedTextArea.setText("Nothing was selected !");
		}
	}

}
