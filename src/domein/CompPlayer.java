package domein;

public class CompPlayer {
	private String name;
	private double num_wins;
	private int num_games;
	private Double avg_wins;
	private Double avg_games;
	private Double raiting;
	private int competitionID;

	public CompPlayer(String name, double num_wins, int num_games,
			Double avg_wins, Double avg_games, Double raiting, int competitionID) {

		this.name = name;
		this.num_wins = num_wins;
		this.num_games = num_games;
		this.avg_games = avg_games;
		this.avg_wins = avg_wins;
		this.raiting = raiting;
		this.competitionID = competitionID;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return name + " has " + num_wins + " wins in " + num_games
				+ " with a raiting of " + raiting + "!";
	}

	public String getName() {
		return name;
	}

	public double getNum_wins() {
		return num_wins;
	}

	public int getNum_games() {
		return num_games;
	}

	public Double getAvg_wins() {
		return avg_wins;
	}
	
	public Double getAvg_games() {
		return avg_games;
	}

	public Double getRaiting() {
		return raiting;
	}
	
	public int getCompetitionID(){
		return competitionID;
	}
}
