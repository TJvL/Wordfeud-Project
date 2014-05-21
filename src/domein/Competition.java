package domein;

import java.util.ArrayList;
import java.util.HashMap;

import datalaag.DatabaseHandler;

public class Competition {

	private int compID;
	private String compOwner;
	private String startDate;
	private String endDate;
	private String summary;
	private int minParticipants;
	private int maxParticipants;
	private int amountParticipants;

	private ArrayList<CompPlayer> participants;

	public Competition(int databaseID, String compOwner, String startDate,
			String endDate, String summary, int minParticipants,
			int maxParticipants) {
		this.compID = databaseID;
		this.compOwner = compOwner;
		this.startDate = startDate;
		this.endDate = endDate;
		this.summary = summary;
		this.minParticipants = minParticipants;
		this.maxParticipants = maxParticipants;
		this.participants = new ArrayList<CompPlayer>();
		this.updateParticipantsAmmount();
	}

	public int getCompID() {
		return compID;
	}

	public String getCompOwner() {
		return compOwner;
	}

	public String getStartDate() {
		return startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public String getSummary() {
		return summary;
	}

	public int getMinParticipants() {
		return minParticipants;
	}

	public int getMaxParticipants() {
		return maxParticipants;
	}

	public int getAmountParticipants() {
		return amountParticipants;
	}

	public void updateParticipantsAmmount() {
		ArrayList<String> playerNames = DatabaseHandler.getInstance()
				.peopleInCompetition(compID);
		amountParticipants = playerNames.size();
	}

	public ArrayList<CompPlayer> getParticipants() {
		return participants;
	}

	public void updateParticipants() {
		participants.clear();
		ArrayList<String> playerNames = DatabaseHandler.getInstance()
				.peopleInCompetition(compID);
		HashMap<String, String> playerScores = DatabaseHandler.getInstance()
				.competitionBayesian(compID);
		HashMap<String, Double> playerRaitings = DatabaseHandler.getInstance()
				.competitionBayesianRaiting(compID);
		double raiting = 0.0;
		
		for (String name : playerNames) {
			String score;
			String[] scores = new String[2];
			if (playerScores.get(name) != null){
			score = playerScores.get(name);
			scores = score.split("---");
			} else {
				score = "0";
				scores[0] = "0.0";
				scores[1] = "0";
			}
			if (playerRaitings.get(name) != null){
				raiting = playerRaitings.get(name);				
			} else {
				raiting = 0.0;
			}
			participants.add(new CompPlayer(name, Double.parseDouble(scores[0]),
					Integer.parseInt(scores[1]), 0.0, 0.0, raiting, compID));
		}

		amountParticipants = participants.size();
	}

	public boolean isRoomForMore() {
		boolean roomForMore = false;

		this.updateParticipants();

		if (amountParticipants < maxParticipants) {
			roomForMore = true;
		}

		return roomForMore;
	}

}
