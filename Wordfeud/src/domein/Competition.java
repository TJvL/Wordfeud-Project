package domein;

public class Competition {
	
	private int databaseID;
	private String startDate;
	private String endDate;
	private String summary;
	private int maxParticipants;
	private int minParticipants;
	private String compOwner;
	
	public Competition(int databaseID, String startDate, String endDate, String summary, int maxParticipants, int minParticipants){
		this.databaseID = databaseID;
		this.startDate = startDate;
		this.endDate = endDate;
		this.summary = summary;
		this.maxParticipants = maxParticipants;
		this.minParticipants = minParticipants;
	}

	public int getDatabaseID() {
		return databaseID;
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

	public int getMaxParticipants() {
		return maxParticipants;
	}

	public int getMinParticipants() {
		return minParticipants;
	}

	public String getCompOwner() {
		return compOwner;
	}
	
}
