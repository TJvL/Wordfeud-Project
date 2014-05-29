package domein;

import java.util.ArrayList;

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
	private ArrayList<CompetitionPlayer> participants;
	private boolean canStartChallenging;

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
		this.participants = new ArrayList<CompetitionPlayer>();
		this.updateParticipants();
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

	public ArrayList<CompetitionPlayer> getParticipants() {
		return participants;
	}
	
	public boolean canStartChallenging(){
		updateParticipants();
		return canStartChallenging;
	}

	public void updateParticipants() {
		participants.clear();
		ArrayList<String> parts = DatabaseHandler.getInstance()
				.fetchCompetitionParticipants(this.getCompID());

		for (String s : parts) {
			String[] data = s.split("---");
			participants.add(new CompetitionPlayer(Integer.toString(this
					.getCompID()), data[0], Integer.parseInt(data[1]), Integer
					.parseInt(data[2]), Integer.parseInt(data[3]), Integer
					.parseInt(data[4]), Integer.parseInt(data[5]), Double
					.parseDouble(data[6])));
		}
		amountParticipants = participants.size();
		if(amountParticipants >= minParticipants){
			canStartChallenging = true;
		}
		else{
			canStartChallenging = false;
		}
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
