package gui;

///current problem: in de methode confirmclicked wordt de string op de juiste wijze geformatteerd en daarna wordt alle info naar de dbh gestuurd, deze accepteert het format niet, ondanks het feit dat de error melding zegt dat de string het format moet zijn dat ie al is.
import gui.CreateCompWindow;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.lavantech.gui.comp.DateTimePicker;
import com.toedter.calendar.JDateChooser;

@SuppressWarnings("serial")
public class CreateCompWindow extends JDialog
{
	@SuppressWarnings("unused")
	private static final DateFormat df = new SimpleDateFormat(
			"yyyy-dd-mm HH:MM:SS");
	private JDateChooser cal1 = new JDateChooser();
	private DateTimePicker dtp = new DateTimePicker();
	private JButton confirm = new JButton();
	private JPanel buttonPanel = new JPanel();
	private JPanel inputPanel = new JPanel();
	private JTextField maxPlayers = new JTextField();
	private JTextField summary = new JTextField();
	private JPanel mainPanel = new JPanel();
	private JLabel maxPlayersLabel = new JLabel();
	private JLabel gameCreatedLabel = new JLabel();
	private JLabel summaryLabel = new JLabel();
	private PlayerScreen playerScreen;
	private String endDate;
	private String hours;
	private String minutes;
	private String seconds;
	private String year;
	private String day;
	private String month;
	private String finalReturnString;
	Calendar cal = Calendar.getInstance();

	public CreateCompWindow(PlayerScreen playerScreen)
	{
		this.playerScreen = playerScreen;
		this.setTitle("Create Competition");
		this.setLayout(new BorderLayout());
		this.add(buttonPanel, BorderLayout.SOUTH);
		this.add(mainPanel, BorderLayout.CENTER);
		this.createCompButtonpanel();
		this.createInputPanel();
		this.createMainPanel();
		this.add(dtp, BorderLayout.NORTH);
		this.setModal(true);

		this.setBackground(Color.DARK_GRAY);
		mainPanel.setBackground(Color.DARK_GRAY);
		inputPanel.setBackground(Color.DARK_GRAY);
		buttonPanel.setBackground(Color.DARK_GRAY);
		confirm.setBackground(Color.CYAN);
		summaryLabel.setForeground(Color.WHITE);
		maxPlayersLabel.setForeground(Color.WHITE);

		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setResizable(false);
	}

	public void createCompButtonpanel()
	{
		dtp.setMinSelectableTime(new GregorianCalendar());
		cal1.setDateFormatString("yyyy-MM-dd HH:mm:SS");
		buttonPanel.add(confirm);
		confirm.setText("Confirm ");
		
		confirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				confirmClicked();
			}});

	}

	public void createInputPanel()
	{
		inputPanel.setLayout(new GridLayout(3, 2, 50, 25));
		summaryLabel.setText("Competition name");
		maxPlayersLabel.setText("Max Players(2-24)");
		
		inputPanel.add(summaryLabel);
		inputPanel.add(summary);
		inputPanel.add(maxPlayersLabel);
		inputPanel.add(maxPlayers);
	}

	public void createMainPanel()
	{
		mainPanel.add(inputPanel);
	}

	public void confirmClicked()
	{
		String errorMessage = "";
		if (!maxPlayers.getText().equals("") && !summary.getText().equals("")
				&& summary.getText().length() < 255)
		{
			try
			{
				if (Integer.parseInt(maxPlayers.getText()) >= 2
						&& Integer.parseInt(maxPlayers.getText()) <= 24)
				{

					// get the values we have to send to our database
					endDate = dtp.getDate().toString();

					// for enddate, we have to split up our string, edit the
					// month part to an int, and put it back together
					// /SPLITTING THE STRING FOR FORMATTING PURPOSES
					// //
					// //
					// // split the entire string
					String[] dateTimeSplit = endDate.split(" ");
					year = dateTimeSplit[5];
					month = monthToInt(dateTimeSplit[1]);
					day = dateTimeSplit[2];
					String[] timeSplit = dateTimeSplit[3].split(":");
					hours = timeSplit[0];
					minutes = timeSplit[1];
					seconds = timeSplit[2];
					finalReturnString = (year + "-" + month + "-" + day + " "
							+ hours + ":" + minutes + ":" + seconds);


//					System.out.println("finalreturnstring"+ finalReturnString);
					// //
					// //
					// // FORMATTED

//mainframe wordt hier aangeroepen omdat, als ik het in playerscreen doe je niet kan checken of confirmclicked al gedaan is 
					playerScreen.callCreateComp(summary.getText(), finalReturnString, 1, Integer.parseInt(maxPlayers.getText()));

					buttonPanel.add(gameCreatedLabel);
					JOptionPane.showMessageDialog(null, "Your competition has been made", "Confirm", JOptionPane.INFORMATION_MESSAGE);
					dispose();
				} 
				else
				{
					JOptionPane.showMessageDialog(null, "Invalid Values, max or min Player nr. was exceeded", "ERROR", JOptionPane.ERROR_MESSAGE);
					buttonPanel.add(gameCreatedLabel);
				}
			} catch (NumberFormatException nfe)	{
				JOptionPane.showMessageDialog(null, "Invalid Values", "ERROR", JOptionPane.ERROR_MESSAGE);
				buttonPanel.add(gameCreatedLabel);
			}
		} 
		else
		{
			JOptionPane.showMessageDialog(null, "Missing or invalid values!", "ERROR", JOptionPane.ERROR_MESSAGE);
			buttonPanel.add(gameCreatedLabel);
		}
	}

	public String monthToInt(String month)
	{
		String monthInt = "";

		if (month.equals("Jan"))
		{
			monthInt = "01";
		} else if (month.equals("Feb"))
		{
			monthInt = "02";
		} else if (month.equals("Mar"))
		{
			monthInt = "03";
		} else if (month.equals("Apr"))
		{
			monthInt = "04";
		} else if (month.equals("May"))
		{
			monthInt = "05";
		} else if (month.equals("Jun"))
		{
			monthInt = "06";
		} else if (month.equals("Jul"))
		{
			monthInt = "07";
		} else if (month.equals("Aug"))
		{
			monthInt = "08";
		} else if (month.equals("Sep"))
		{
			monthInt = "09";
		} else if (month.equals("Oct"))
		{
			monthInt = "10";
		} else if (month.equals("Nov"))
		{
			monthInt = "11";
		} else if (month.equals("Dec"))
		{
			monthInt = "12";
		}
		return monthInt;
	}

}
