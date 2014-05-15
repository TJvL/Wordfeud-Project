package domein;

import java.util.ArrayList;

import datalaag.DatabaseHandler;

public class CompetitionManager {
	private WordFeud wf;
	private ArrayList<Competition> joinedCompetitions;
	private ArrayList<Competition> ownedCompetitions;

	public CompetitionManager(WordFeud wf) {
		this.wf = wf;
		joinedCompetitions = new ArrayList<Competition>();
		ownedCompetitions = new ArrayList<Competition>();
	}

	public void loadCompetitions(String username) {
		DatabaseHandler dbh = DatabaseHandler.getInstance();

		System.out.println("Loading competitions...");

		ArrayList<String> ownedComps = dbh.competitionOwner(username);
		if (!ownedComps.isEmpty()) {
			for (String comp : ownedComps) {
				String[] compData = comp.split("---");

				// --------TEST CODE---------
				System.out.println("RAW DATA PRINT COMP: " + compData[0] + " "
						+ compData[1] + " " + compData[2] + " " + compData[3]
						+ " " + compData[4] + " " + compData[5]);
				// --------TEST CODE---------

				ownedCompetitions.add(new Competition(Integer
						.parseInt(compData[0]), compData[1], compData[2],
						compData[3], Integer.parseInt(compData[4]), Integer
								.parseInt(compData[5])));
			}
			System.out.println("Succesfully loaded owned competitions.");
		} else {
			System.out.println("No owned competitions to load.");
		}

		ArrayList<String> joinedComps = dbh.competitionParticipant(username);
		if (!ownedComps.isEmpty()) {
			for (String comp : joinedComps) {
				String[] compData = comp.split("---");

				// --------TEST CODE---------
				System.out.println("RAW DATA PRINT COMP: " + compData[0] + " " 
						+ compData[1] + " " + compData[2] + " " + compData[3]
						+ " " + compData[4] + " " + compData[5]);
				// --------TEST CODE---------

				joinedCompetitions.add(new Competition(Integer
						.parseInt(compData[0]), compData[1], compData[2],
						compData[3], Integer.parseInt(compData[4]), Integer
								.parseInt(compData[5])));
			}
			System.out.println("Succesfully loaded joined competitions.");
		} else {
			System.out.println("No joined competitions to load.");
		}
	}
}
