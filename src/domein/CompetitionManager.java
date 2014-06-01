package domein;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JOptionPane;

import datalaag.DatabaseHandler;
import datalaag.WordFeudConstants;

public class CompetitionManager {
	private HashMap<String, Competition> joinedCompetitions;
	private HashMap<String, Competition> competitions;

	public CompetitionManager() {
		competitions = new HashMap<String, Competition>();
		joinedCompetitions = new HashMap<String, Competition>();
	}

	public void loadJoinedCompetitions(String username) {
		joinedCompetitions.clear();
		System.out.println("Loading joined competitions...");

		ArrayList<String> comps = DatabaseHandler.getInstance()
				.fetchJoinedCompetitions(username);
		if (!comps.isEmpty()) {
			for (String comp : comps) {
				String[] compData = comp.split("---");

				// --------TEST CODE---------
				System.out.println("RAW DATA PRINT COMP: " + compData[0] + " "
						+ compData[1] + " " + compData[2] + " " + compData[3]
						+ " " + compData[4] + " " + compData[5] + " "
						+ compData[6]);
				// --------TEST CODE---------

				joinedCompetitions.put(compData[0],
						new Competition(Integer.parseInt(compData[0]),
								compData[1], compData[2], compData[3],
								compData[4], Integer.parseInt(compData[5]),
								Integer.parseInt(compData[6])));

			}

			System.out.println("Succesfully loaded all joined competitions.");
		} else {
			System.out.println("No joined competitions to load.");
		}
	}

	public void loadAllCompetitions(String username) {
		competitions.clear();
		System.out.println("Loading all other competitions...");

		ArrayList<String> comps = DatabaseHandler.getInstance()
				.fetchAllCompetitions(username);
		if (!comps.isEmpty()) {
			for (String comp : comps) {
				String[] compData = comp.split("---");

				// --------TEST CODE---------
				System.out.println("RAW DATA PRINT COMP: " + compData[0] + " "
						+ compData[1] + " " + compData[2] + " " + compData[3]
						+ " " + compData[4] + " " + compData[5] + " "
						+ compData[6]);
				// --------TEST CODE---------

				competitions.put(compData[0],
						new Competition(Integer.parseInt(compData[0]),
								compData[1], compData[2], compData[3],
								compData[4], Integer.parseInt(compData[5]),
								Integer.parseInt(compData[6])));
			}

			System.out.println("Succesfully loaded all other competitions.");
		} else {
			System.out.println("No other competitions to load.");
		}
	}

	public boolean joinCompetition(String compID, String username) {
		boolean actionSuccesfull = false;
		if (competitions.get(compID).isRoomForMore()) {
			DatabaseHandler.getInstance().joinCompetition(
					Integer.parseInt(compID), username);
			Competition comp = competitions.get(compID);
			joinedCompetitions.put(compID, comp);
			competitions.remove(compID);
			actionSuccesfull = true;
			System.out
					.println("Joined competition succesfully! CompObject ID: "
							+ comp.getCompID());
		} else {
			System.err.println("ERROR: Could not join selected competition");
			JOptionPane.showMessageDialog(null, "Competion is full",
					"Can't join", JOptionPane.INFORMATION_MESSAGE);
		}
		return actionSuccesfull;
	}

	public String createCompetition(String currentUsername, String summary,
			String endDate, String minParticipants, String maxParticipants) {
		
		String retValue = WordFeudConstants.CREATE_COMP_FAIL_DEFAULT;
		String formatedDateTime = convertDateTime(endDate);
		
		int maxParts = 0;
		int minParts = 1;
		try {
			maxParts = Integer.parseInt(maxParticipants);
			minParts = Integer.parseInt(minParticipants);
		} catch (NumberFormatException nfe) {
			System.err.println(nfe.getMessage());
			retValue = WordFeudConstants.CREATE_COMP_FAIL_INPUT;
			return retValue;
		}
		
		if ((maxParts >= minParts) && (minParts >= 2) && (maxParts <= 24)) {
			DatabaseHandler.getInstance().createCompetition(currentUsername,
					formatedDateTime, summary, minParts, maxParts);
			retValue = WordFeudConstants.CREATE_COMP_SUCCES;
		} else {
			retValue = WordFeudConstants.CREATE_COMP_FAIL_NUMBERS;
		}
		return retValue;
	}

	public Set<Entry<String, Competition>> getAllCompEntries() {
		return competitions.entrySet();
	}

	public Set<Entry<String, Competition>> getJoinedCompEntries() {
		return joinedCompetitions.entrySet();
	}

	public Competition getOneCompetition(String key) {
		if (competitions.containsKey(key)) {
			return competitions.get(key);
		} else if (joinedCompetitions.containsKey(key)) {
			return joinedCompetitions.get(key);
		} else {
			return null;
		}
	}

	public Competition getOneJoinedCompetition(String key) {
		return joinedCompetitions.get(key);
	}

	public void logout() {
		joinedCompetitions.clear();
		competitions.clear();
	}

	private String convertDateTime(String endDate) {
		System.out.println(endDate);
		String[] dateTimeSplit = endDate.split(" ");
		String year = dateTimeSplit[5];
		String month = monthToInt(dateTimeSplit[1]);
		String day = dateTimeSplit[2];
		String[] timeSplit = dateTimeSplit[3].split(":");
		String hours = timeSplit[0];
		String minutes = timeSplit[1];
		String seconds = timeSplit[2];

		String convertedDateTime = (year + "-" + month + "-" + day + " "
				+ hours + ":" + minutes + ":" + seconds);

		return convertedDateTime;
	}

	private String monthToInt(String month) {
		String monthInt = "";

		if (month.equals("Jan")) {
			monthInt = "01";
		} else if (month.equals("Feb")) {
			monthInt = "02";
		} else if (month.equals("Mar")) {
			monthInt = "03";
		} else if (month.equals("Apr")) {
			monthInt = "04";
		} else if (month.equals("May")) {
			monthInt = "05";
		} else if (month.equals("Jun")) {
			monthInt = "06";
		} else if (month.equals("Jul")) {
			monthInt = "07";
		} else if (month.equals("Aug")) {
			monthInt = "08";
		} else if (month.equals("Sep")) {
			monthInt = "09";
		} else if (month.equals("Oct")) {
			monthInt = "10";
		} else if (month.equals("Nov")) {
			monthInt = "11";
		} else if (month.equals("Dec")) {
			monthInt = "12";
		}
		return monthInt;
	}
}
