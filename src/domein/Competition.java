package domein;

public class Competition {
	
	private int databaseID;
	private String compOwner;
	private String startDate;
	private String endDate;
	private String summary;
	private int minParticipants;
	private int maxParticipants;
	private int amountParticipants;
	
	public Competition(int databaseID, String compOwner, String startDate, String endDate, String summary, int minParticipants, int maxParticipants){
		this.databaseID = databaseID;
		this.compOwner = compOwner;
		this.startDate = startDate;
		this.endDate = endDate;
		this.summary = summary;
		this.minParticipants = minParticipants;
		this.maxParticipants = maxParticipants;
		
	}

	public int getDatabaseID() {
		return databaseID;
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

	public int getAmountParticipants(){
			return amountParticipants;
		}

	public void setAmountParticipants(int amountParticipants){
			this.amountParticipants = amountParticipants;
		}
	
}
