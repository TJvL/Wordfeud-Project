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
	
	private ArrayList<String> participants;

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
	
	public ArrayList<String> getParticipants(){
		return participants;
	}

	public void updateParticipants() {
		this.participants = DatabaseHandler.getInstance().peopleInCompetition(compID);
		amountParticipants = participants.size();
	}
	
	public boolean isRoomForMore(){
		boolean roomForMore = false;
		
		this.updateParticipants();
		
		if (amountParticipants < maxParticipants){
			roomForMore = true;
		}
		
		return roomForMore;
	}

}
