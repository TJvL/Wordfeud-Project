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

	private DatabaseHandler dbh;

	private JLabel playedGames;
	private JLabel gamesWonLabel;
	private JLabel highScore;
	private JLabel mostValuableWordLabel;
	private JLabel compsWon;
	private JLabel playedGamesValue;
	private JLabel gamesWonValue;
	private JLabel highScoreValue;
	private JLabel mostValuableWordValue;
	private JLabel compsWonValue;

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
		// initialising
		dbh = DatabaseHandler.getInstance();
		playedGames = new JLabel();
		gamesWonLabel = new JLabel();
		highScore = new JLabel();
		mostValuableWordLabel = new JLabel();
		compsWon = new JLabel();
		playedGamesValue = new JLabel();
		gamesWonValue = new JLabel();
		highScoreValue = new JLabel();
		mostValuableWordValue = new JLabel();
		compsWonValue = new JLabel();
		//

		// retrieving values from database
		this.userName = username;
		this.highestGameScore = this.getHighestGameScore();
		this.numGamesPlayed = this.getNumGamesPlayed();
		this.mostValuableWord = this.getMostValuableWord();
		this.gamesWon = this.getGamesWon();
		this.competitionsWon = this.getCompetitionsWon();
		//

		// setting up frame
		this.setLayout(new GridLayout(6, 2, 10, 10));
		this.setPreferredSize(new Dimension(500, 500));
		this.setResizable(false);
		this.setTitle(" PlayerName's Statistics");
		this.getContentPane().setBackground(Color.DARK_GRAY);
		//

		createLabels();

		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);

	}

	public void createLabels()
	{
		// setting text for titles and setting fonts for all values
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
		playedGamesValue.setFont(new Font("Serif", Font.PLAIN, 17));
		gamesWonValue.setFont(new Font("Serif", Font.PLAIN, 17));
		compsWonValue.setFont(new Font("Serif", Font.PLAIN, 17));
		highScoreValue.setFont(new Font("Serif", Font.PLAIN, 17));
		mostValuableWordValue.setFont(new Font("Serif", Font.PLAIN, 17));

		playedGamesValue.setText(numGamesPlayed);
		gamesWonValue.setText(gamesWon);
		compsWonValue.setText(competitionsWon);
		highScoreValue.setText(highestGameScore);
		mostValuableWordValue.setText(mostValuableWord);

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

	}

	// the following methods are used to retrieve data from the database

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
		numGamesPlayed = statistics.substring(statistics.lastIndexOf('f')+1,
				statistics.lastIndexOf('g'));
		System.out.println(numGamesPlayed);
		return numGamesPlayed;
	}

	public String getMostValuableWord()
	{
		statistics = dbh.playerStatistics(userName);
		mostValuableWord = statistics.substring(
				statistics.lastIndexOf('d') + 1, statistics.lastIndexOf('e'));
		if(mostValuableWord.equals("null"))
		{
			mostValuableWord = "No word found yet";
		}
		
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
}
