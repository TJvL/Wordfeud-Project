package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JDialog;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class StatWindow extends JDialog
{

	private JLabel playedGames = new JLabel();
	private JLabel gamesWon = new JLabel();
	private JLabel highScore = new JLabel();
	private JLabel compsWon = new JLabel();
	private JLabel playedGamesValue = new JLabel();
	private JLabel gamesWonValue = new JLabel();
	private JLabel highScoreValue = new JLabel();
	private JLabel compsWonValue = new JLabel();
	
	

	public void showStats(String playedGamesString, String gamesWonString, String compsWonString, String highScoreString)
	{
		this.setModal(true);
		this.setLayout(new GridLayout(4, 2, 10, 10));
		this.setPreferredSize(new Dimension(600, 500));
		this.setResizable(false);
		this.setTitle(" PlayerName's Statistics");
		playedGamesValue.setText(playedGamesString);
		gamesWonValue.setText(gamesWonString);
		compsWonValue.setText(compsWonString);
		highScoreValue.setText(highScoreString);
		createLabels();
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);

	}

	public void createLabels()
	{
		

		playedGames.setText("Played games:");
		playedGames.setFont(new Font("Serif", Font.BOLD, 17));

		gamesWon.setText("Games won:");
		gamesWon.setFont(new Font("Serif", Font.BOLD, 17));

		compsWon.setText("Competitions won:");
		compsWon.setFont(new Font("Serif", Font.BOLD, 17));

		highScore.setText("Highest gameScore:");
		highScore.setFont(new Font("Serif", Font.BOLD, 17));


		
		playedGamesValue.setFont(new Font("Serif", Font.PLAIN, 17));

		gamesWonValue.setFont(new Font("Serif", Font.PLAIN, 17));
		
		compsWonValue.setFont(new Font("Serif", Font.PLAIN, 17));
		
		highScoreValue.setFont(new Font("Serif", Font.PLAIN, 17));
		

		this.add(playedGames);
		this.add(playedGamesValue);

		this.add(gamesWon);
		this.add(gamesWonValue);
		
		
		this.add(compsWon);
		this.add(compsWonValue);
		
		this.add(highScore);
		this.add(highScoreValue);
		
		this.getContentPane().setBackground(Color.DARK_GRAY);
		playedGames.setForeground(Color.WHITE);
		gamesWon.setForeground(Color.WHITE);
		highScore.setForeground(Color.WHITE);
		compsWon.setForeground(Color.WHITE);
		playedGamesValue.setForeground(Color.WHITE);
		gamesWonValue.setForeground(Color.WHITE);
		highScoreValue.setForeground(Color.WHITE);
		compsWonValue.setForeground(Color.WHITE);
		
	}

	

}
