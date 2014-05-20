package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;

import datalaag.DatabaseHandler;
import domein.WordFeud;

@SuppressWarnings("serial")
public class StatWindow extends JFrame
{

	private DatabaseHandler dbh = DatabaseHandler.getInstance();
	private JLabel playedGames = new JLabel();
	private JLabel gamesWonLabel = new JLabel();
	private JLabel highScore = new JLabel();
	private JLabel mostValuableWordLabel = new JLabel();
	private JLabel compsWon = new JLabel();

	private JLabel playedGamesValue = new JLabel();
	private JLabel gamesWonValue = new JLabel();
	private JLabel highScoreValue = new JLabel();
	private JLabel mostValuableWordValue = new JLabel();
	private JLabel compsWonValue = new JLabel();

	private String highestGameScore;
	private String numGamesPlayed;
	private String mostValuableWord;
	private String gamesWon;
	private String competitionsWon;
	private String userName;
	private String statistics;

	WordFeud myLocalWordFeud;

	public void showStats(String username)
	{
		
		this.userName = username;
		
		

		this.highestGameScore = this.getHighestGameScore();
		this.numGamesPlayed = this.getNumGamesPlayed();
		this.mostValuableWord = this.mostValuableWord();
		this.gamesWon = this.getGamesWon();
		this.competitionsWon = this.getCompetitionsWon();

		this.setLayout(new GridLayout(6, 2, 10, 10));
		this.setPreferredSize(new Dimension(500, 500));
		this.setResizable(false);
		this.setTitle(" PlayerName's Statistics");
		createLabels();

		this.getContentPane().setBackground(Color.DARK_GRAY);
		playedGames.setForeground(Color.WHITE);
		gamesWonLabel.setForeground(Color.WHITE);
		highScore.setForeground(Color.WHITE);
		mostValuableWordLabel.setForeground(Color.WHITE);
		compsWon.setForeground(Color.WHITE);
		playedGamesValue.setForeground(Color.WHITE);
		gamesWonValue.setForeground(Color.WHITE);
		highScoreValue.setForeground(Color.WHITE);
		mostValuableWordValue.setForeground(Color.WHITE);
		compsWonValue.setForeground(Color.WHITE);

		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);

	}

	

	public String getHighestGameScore()
	{
		statistics = dbh.playerStatistics(userName);

		highestGameScore = statistics
				.substring(statistics.lastIndexOf('h') + 1);
		System.out.println(highestGameScore);
		return highestGameScore;

	}

	public String getNumGamesPlayed()
	{
		statistics = dbh.playerStatistics(userName);
		numGamesPlayed = statistics.substring(statistics.lastIndexOf('f') + 1,
				statistics.lastIndexOf('g'));
		System.out.println(numGamesPlayed);
		return numGamesPlayed;
	}

	public String mostValuableWord()
	{
		statistics = dbh.playerStatistics(userName);
		mostValuableWord = statistics.substring(
				statistics.lastIndexOf('d') + 1, statistics.lastIndexOf('e'));
		System.out.println(mostValuableWord);
		return mostValuableWord;
	}

	public String getGamesWon()
	{
		statistics = dbh.playerStatistics(userName);
		gamesWon = statistics.substring(statistics.lastIndexOf('b') + 1,
				statistics.lastIndexOf('c'));
		System.out.println(gamesWon);
		return gamesWon;
	}

	public String getCompetitionsWon()
	{
		statistics = dbh.playerStatistics(userName);
		competitionsWon = statistics.substring(0, statistics.lastIndexOf('a'));
		System.out.println(competitionsWon);
		return competitionsWon;
	}

	public void createLabels()
	{

		playedGames.setText("Played games:");
		playedGames.setFont(new Font("Serif", Font.BOLD, 17));

		gamesWonLabel.setText("Games won:");
		gamesWonLabel.setFont(new Font("Serif", Font.BOLD, 17));

		compsWon.setText("Competitions won:");
		compsWon.setFont(new Font("Serif", Font.BOLD, 17));

		highScore.setText("Highest gameScore:");
		highScore.setFont(new Font("Serif", Font.BOLD, 17));

		mostValuableWordLabel.setText("Most valuable word played:");
		mostValuableWordLabel.setFont(new Font("Serif", Font.BOLD, 17));

		// these are the actual values that will have to be retrieved from the
		// database somehow
		playedGamesValue.setFont(new Font("Serif", Font.PLAIN, 17));

		gamesWonValue.setFont(new Font("Serif", Font.PLAIN, 17));
		compsWonValue.setFont(new Font("Serif", Font.PLAIN, 17));

		highScoreValue.setFont(new Font("Serif", Font.PLAIN, 17));

		mostValuableWordValue.setFont(new Font("Serif", Font.PLAIN, 17));

		this.add(playedGames);

		this.add(playedGamesValue);

		this.add(gamesWonLabel);
		this.add(gamesWonValue);
		this.add(compsWon);
		this.add(compsWonValue);
		this.add(highScore);
		this.add(highScoreValue);
		this.add(mostValuableWordLabel);
		this.add(mostValuableWordValue);

		playedGamesValue.setText(numGamesPlayed);
		gamesWonValue.setText(gamesWon);
		compsWonValue.setText(competitionsWon);
		highScoreValue.setText(highestGameScore);
		mostValuableWordValue.setText(mostValuableWord);

	}

}
