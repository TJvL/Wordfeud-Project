package gui;

///current problem: in de methode confirmclicked wordt de string op de juiste wijze geformatteerd en daarna wordt alle info naar de dbh gestuurd, deze accepteert het format niet, ondanks het feit dat de error melding zegt dat de string het format moet zijn dat ie al is.
import gui.CreateCompWindow;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import com.lavantech.gui.comp.DateTimePicker;

import domein.CompetitionManager;

@SuppressWarnings("serial")
public class CreateCompWindow extends JDialog {

	private MainFrame mainFrame;
	private DateTimePicker dateTimeChooser;
	private JButton confirmButton;
	private JPanel buttonPanel;
	private JPanel inputPanel;
	private JTextField maxPlayers;
	private JTextField summary;
	private JTextField minPlayers;
	private JLabel endDateLabel;
	private JLabel maxPlayersLabel;
	private JLabel minPlayersLabel;
	private JLabel summaryLabel;

	public CreateCompWindow(MainFrame mainFrame) {
		super(mainFrame);
		this.mainFrame = mainFrame;

		buttonPanel = new JPanel();
		inputPanel = new JPanel();

		this.createButtonPanel();
		this.createInputPanel();

		this.setTitle("Create a competition");
		this.setLayout(null);
		this.setModal(true);

		buttonPanel.setBounds(0, 250, 450, 50);
		inputPanel.setBounds(0, 0, 430, 230);

		this.add(buttonPanel);
		this.add(inputPanel);

		this.setPreferredSize(new Dimension(450, 350));
		this.setResizable(false);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setResizable(false);
	}

	private void createButtonPanel() {
		confirmButton = new JButton();
		buttonPanel.add(confirmButton);
		confirmButton.setText("Confirm ");

		confirmButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				confirmButtonPressed();
			}
		});

	}

	private void createInputPanel() {
		maxPlayersLabel = new JLabel("Max Players(2-24)");
		minPlayersLabel = new JLabel("Min Players(2-24)");
		endDateLabel = new JLabel("End date");
		summaryLabel = new JLabel("Competition name");
		maxPlayers = new JTextField();
		summary = new JTextField();
		minPlayers = new JTextField();
		dateTimeChooser = new DateTimePicker();

		dateTimeChooser.setMinSelectableTime(new GregorianCalendar());

		Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		inputPanel.setBorder(padding);
		inputPanel.setLayout(new GridLayout(4, 2, 10, 10));
		inputPanel.add(endDateLabel);
		inputPanel.add(dateTimeChooser);
		inputPanel.add(summaryLabel);
		inputPanel.add(summary);
		inputPanel.add(maxPlayersLabel);
		inputPanel.add(maxPlayers);
		inputPanel.add(minPlayersLabel);
		inputPanel.add(minPlayers);
	}

	private void confirmButtonPressed() {
		String retValue = mainFrame.callCreateCompAction(summary.getText(),
				dateTimeChooser.getDate().toString(), minPlayers.getText(),
				maxPlayers.getText());
		
		JOptionPane.showMessageDialog(mainFrame, retValue, "Ok",
				JOptionPane.INFORMATION_MESSAGE);
		if (retValue.equals(CompetitionManager.CREATE_COMP_SUCCES)) {
			this.dispose();
		}
	}
}
