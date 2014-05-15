package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import datalaag.DatabaseHandler;

@SuppressWarnings("serial")
public class ModScreen extends JPanel {
	private String listArray[] = new String[DatabaseHandler.getInstance()
			.pendingWords().size()];

	private JScrollPane listScrollPane;

	private JPanel modScreenButtonPanel = new JPanel();
	private JPanel modScreenListPanel = new JPanel();
	private JPanel screenTitle = new JPanel();
	private JPanel dictionaryOptions = new JPanel();
	private JPanel rightPanel = new JPanel();

	private JButton approveWord = new JButton();
	private JButton declineWord = new JButton();
	private JButton addWord = new JButton();
	private JButton removeWord = new JButton();
	private JTextField insertWord = new JTextField(20);

	private DefaultListModel<String> myListModel = new DefaultListModel<String>();
	private JList<String> wordReqList = new JList<String>(listArray);

	private JLabel titleLabel = new JLabel();
	private JLabel selectedWord = new JLabel();
	private JLabel selectedWordValue = new JLabel();
	private JLabel dictionaryTitle = new JLabel();
	private JLabel wordInput = new JLabel();

	// run this method before setting the screen as contentpane

	// //left side of the screen
	public ModScreen() {
		createmodScreen();
	}

	public void createmodScreen() {
		this.createRequestListPanel();
		this.createRequestsButtonPanel();
		this.rightPanel();
		this.setLayout(new BorderLayout());
		this.setPreferredSize(this.getSize());

		titleLabel.setText("Pending requests.");
		titleLabel.setFont(new Font("Serif", Font.BOLD, 25));
		screenTitle.add(titleLabel);
		screenTitle.setPreferredSize(new Dimension(0, 50));
		this.add(screenTitle, BorderLayout.NORTH);
		this.add(modScreenListPanel, BorderLayout.CENTER);
		this.add(modScreenButtonPanel, BorderLayout.SOUTH);
		this.add(rightPanel, BorderLayout.EAST);

	}

	public void createRequestListPanel() {

		// ///this panel contains our JList, where all the active games are
		// listed

		modScreenListPanel.setLayout(new BorderLayout());
		modScreenListPanel.setPreferredSize(this.getSize());
		DefaultListCellRenderer renderer = (DefaultListCellRenderer) wordReqList
				.getCellRenderer();
		renderer.setHorizontalAlignment(JLabel.CENTER);
		wordReqList.setModel(myListModel);
		listScrollPane = new JScrollPane(wordReqList);
		listScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		modScreenListPanel.add(listScrollPane, BorderLayout.CENTER);
		// //////

		fillList();

	}

	public void createRequestsButtonPanel()

	{

		approveWord.setText("Approve selected word");
		modScreenButtonPanel.add(approveWord);
		declineWord.setText("Decline selected word");
		modScreenButtonPanel.add(declineWord);
		selectedWord.setText("Selected Word:");
		selectedWord.setFont(new Font("Serif", Font.BOLD, 15));
		modScreenButtonPanel.add(selectedWord);
		modScreenButtonPanel.add(selectedWordValue);

		approveWord.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (wordReqList.getSelectedValue() != null) {
					selectedWordValue.setText(wordReqList.getSelectedValue()
							+ "(Approved).");
					String selected = wordReqList.getSelectedValue();
					String parts[] = selected.split("---");

					DatabaseHandler.getInstance().acceptDeniedWord(parts[0],
							parts[1], "Accepted");

					fillList();
				} else

				{
					selectedWordValue.setText("Nothing selected.");
				}
				modScreenButtonPanel.revalidate();
			}
		});

		declineWord.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (wordReqList.getSelectedValue() != null) {
					selectedWordValue.setText(wordReqList.getSelectedValue()
							+ "(Declined).");

					String selected = wordReqList.getSelectedValue();
					String parts[] = selected.split("---");

					DatabaseHandler.getInstance().acceptDeniedWord(parts[0],
							parts[1], "Denied");

					fillList();
				} else

				{
					selectedWordValue.setText("Nothing selected.");
				}
				modScreenButtonPanel.revalidate();
			}

		});

	}

	public void fillList() {
		listArray = DatabaseHandler.getInstance().pendingWords()
				.toArray(listArray);
		myListModel.clear();
		int counter = 0;
		while (counter < listArray.length) {

			myListModel.add(counter, listArray[counter]);
			counter++;

		}

	}

	// ////left side end

	// right side of the screen
	public void createDictionaryOptions() {
		JLabel languageLabel = new JLabel("Select dictionary language");
		String[] possibleLanguages = { "EN", "NL" };
		final JComboBox<String> dictionarySelect = new JComboBox<String>(
				possibleLanguages);

		dictionarySelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dictionaryTitle.setText("Dictionary: "
						+ dictionarySelect.getSelectedItem().toString());
			}
		});

		addWord.setText("Add Word");

		removeWord.setText("Remove Word");

		insertWord.setText("Insert a Word");
		dictionaryOptions.setLayout(new BoxLayout(dictionaryOptions,
				BoxLayout.PAGE_AXIS));

		dictionaryTitle.setText("Dictionary: "
				+ dictionarySelect.getSelectedItem().toString());
		dictionaryOptions.add(dictionaryTitle);
		dictionaryTitle.setFont(new Font("Serif", 20, 20));
		dictionaryTitle.setPreferredSize(new Dimension(300, 50));
		JLabel test = new JLabel();
		test.setPreferredSize(new Dimension(0, 100));
		JLabel test1 = new JLabel();
		test1.setPreferredSize(new Dimension(0, 100));
		dictionaryOptions.add(test1);
		dictionaryOptions.add(languageLabel);
		dictionaryOptions.add(dictionarySelect);
		dictionaryOptions.add(addWord);
		dictionaryOptions.add(removeWord);
		dictionaryOptions.add(test);
		dictionaryOptions.add(insertWord);

		dictionaryOptions.setBorder(BorderFactory.createLineBorder(Color.BLACK,
				2));

		wordInput
				.setText("Insert a word in the box above to add it to , or remove it from the dictionary.");
		dictionaryOptions.add(wordInput);
		
		addWord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DatabaseHandler.getInstance().requestWord(insertWord.getText(),
						dictionarySelect.getSelectedItem().toString());
				
				DatabaseHandler.getInstance().acceptDeniedWord(
						insertWord.getText(),
						dictionarySelect.getSelectedItem().toString(),
						"Accepted");
			}
		});
		removeWord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				DatabaseHandler.getInstance()
						.acceptDeniedWord(insertWord.getText(),
								dictionarySelect.getSelectedItem().toString(),
								"Denied");
			}
		});
	}

	public void rightPanel() {

		createDictionaryOptions();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));
		rightPanel.add(dictionaryOptions);
	}
}
