package domein;

import java.util.ArrayList;

import datalaag.DatabaseHandler;

public class CompetitionManager {
	private WordFeud wf;
	private ArrayList<Competition> joinedCompetitions;

	public CompetitionManager(WordFeud wf) {
		this.wf = wf;
		joinedCompetitions = new ArrayList<Competition>();
	}

	public void loadCompetitions(String username) {
		
		DatabaseHandler dbh = DatabaseHandler.getInstance();
		joinedCompetitions.clear();
		System.out.println("Loading competitions...");

		ArrayList<String> comps = dbh.fetchCompetition(username);
		if (!comps.isEmpty()) {
			for (String comp : comps) {
				String[] compData = comp.split("---");

				// --------TEST CODE---------
				System.out.println("RAW DATA PRINT COMP: " + compData[0] + " "
						+ compData[1] + " " + compData[2] + " " + compData[3]
						+ " " + compData[4] + " " + compData[5] + " " + compData[6]);
				// --------TEST CODE---------

				joinedCompetitions.add(new Competition(Integer
						.parseInt(compData[0]), compData[1], compData[2], compData[3],
						compData[4], Integer.parseInt(compData[5]), Integer
								.parseInt(compData[6])));
			}
			System.out.println("Succesfully loaded competitions.");
		} else {
			System.out.println("No competitions to load.");
		}
	}
	
//	public void createCompetition(String currentUsername, String summary, String endDate, int maxParticipants) {
//		
//		DatabaseHandler dbh = DatabaseHandler.getInstance();
//		dbh.createCompetition(currentUsername, summary, endDate, maxParticipants);
//	}
}
