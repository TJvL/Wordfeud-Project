package gui;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class SpecScreen extends JPanel
{
	public SpecScreen(){
		createSpectatorScreen();
	}
	private String listArray[] =
	{

	"Active Game 1 ", "Active Game 2", "Active game 3", "Active Game 4",
			"Test", "NogMeerTest", "                        ", "" };
	private JScrollPane listScrollPane;
	private JScrollPane textAreaScrollPane;
	private JTextArea myTextArea = new JTextArea();
	private JPanel spectatorScreenButtonPanel = new JPanel();
	private JPanel spectatorScreenListPanel = new JPanel();
	private JPanel spectatorScreenTextareaPanel = new JPanel();

	private JButton selectGame = new JButton();
	private DefaultListModel<String> myListModel = new DefaultListModel<String>();
	private JList<String> activeGamesList = new JList<String>(listArray);

	
	//run this method before setting the screen as contentpane
	public void createSpectatorScreen()
	{
		this.createSpectatorScreenListPanel();
		this.createSpectatorScreenButtonPanel();
		this.createSpectatorScreenTextAreaPanel();
		this.setLayout(new BorderLayout());
		this.setPreferredSize(this.getSize());
		this.add(spectatorScreenListPanel, BorderLayout.CENTER);
		this.add(spectatorScreenButtonPanel, BorderLayout.SOUTH);
		this.add(spectatorScreenTextareaPanel, BorderLayout.EAST);

	}

	public void createSpectatorScreenListPanel()
	{

		// ///this panel contains our JList, where all the active games are
		// listed
		spectatorScreenListPanel.setLayout(new BorderLayout());
		spectatorScreenListPanel.setPreferredSize(this.getSize());
		// //

		// /this makes sure the text in the JList in centered
		DefaultListCellRenderer renderer = (DefaultListCellRenderer) activeGamesList
				.getCellRenderer();
		renderer.setHorizontalAlignment(JLabel.CENTER);
		//
		// activeGamesList.setCellRenderer(myAGLCR);
		activeGamesList.setModel(myListModel);
		// //

		listScrollPane = new JScrollPane(activeGamesList);
		listScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		spectatorScreenListPanel.add(listScrollPane, BorderLayout.CENTER);

		fillList();

	}

	public void createSpectatorScreenButtonPanel()
	{

		selectGame.setText("Select a match to view it");
		spectatorScreenButtonPanel.add(selectGame);
		selectGame.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				String test = activeGamesList.getSelectedValue().toString();
				myTextArea.setText("You have selected the following match:  "
						+ test);

			}
		});

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

	public void createSpectatorScreenTextAreaPanel()
	{
		this.setLayout(new BorderLayout());
		textAreaScrollPane = new JScrollPane(myTextArea);
		myTextArea.setWrapStyleWord(true);
		myTextArea.setLineWrap(true);

		textAreaScrollPane
				.setVerticalScrollBarPolicy(textAreaScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		textAreaScrollPane.setPreferredSize(new Dimension(500, 1200));
		spectatorScreenTextareaPanel.add(textAreaScrollPane,
				BorderLayout.CENTER);

	}
}
