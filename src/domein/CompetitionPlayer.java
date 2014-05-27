package domein;

public class CompetitionPlayer {
	private String compID;
	private String username;

	private int totalGames;
	private int totalScore;
	private int averageScore;
	private int totalWins;
	private int totalLoss;
	private double bayesianAverage;
	
	public CompetitionPlayer(String compID, String username, int totalGames,
			int totalScore, int averageScore, int totalWins, int totalLoss,
			double bayesianAverage) {
		this.compID = compID;
		this.username = username;
		this.totalGames = totalGames;
		this.totalScore = totalScore;
		this.averageScore = averageScore;
		this.totalWins = totalWins;
		this.totalLoss = totalLoss;
		this.bayesianAverage = bayesianAverage;
	}

	public String getCompID() {
		return compID;
	}

	public String getUsername() {
		return username;
	}

	public int getTotalGames() {
		return totalGames;
	}

	public int getTotalScore() {
		return totalScore;
	}

	public int getAverageScore() {
		return averageScore;
	}

	public int getTotalWins() {
		return totalWins;
	}

	public int getTotalLoss() {
		return totalLoss;
	}

	public double getBayesianAverage() {
		return bayesianAverage;
	}

}
