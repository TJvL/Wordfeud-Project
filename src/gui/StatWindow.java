package gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JDialog;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class StatWindow extends JDialog {

	private JLabel playedGames = new JLabel();
	private JLabel gamesWon = new JLabel();
	private JLabel highScore = new JLabel();
	private JLabel mostValuableWord = new JLabel();
	private JLabel compsWon = new JLabel();

	private JLabel playedGamesValue = new JLabel();
	private JLabel gamesWonValue = new JLabel();
	private JLabel highScoreValue = new JLabel();
	private JLabel mostValuableWordValue = new JLabel();
	private JLabel compsWonValue = new JLabel();

	public void showStats(MainFrame mainFrame) {
		this.setModal(true);
		this.setLayout(new GridLayout(6, 2, 10, 10));
		this.setPreferredSize(new Dimension(500, 500));
		this.setResizable(false);
		this.setTitle(" PlayerName's Statistics");
		createLabels();

		this.pack();
		this.setLocationRelativeTo(mainFrame);
		this.setVisible(true);

	}

	public void createLabels() {

		playedGames.setText("Played games:");
		playedGames.setFont(new Font("Serif", Font.BOLD, 17));

		gamesWon.setText("Games won:");
		gamesWon.setFont(new Font("Serif", Font.BOLD, 17));

		compsWon.setText("Competitions won:");
		compsWon.setFont(new Font("Serif", Font.BOLD, 17));

		highScore.setText("Highest gameScore:");
		highScore.setFont(new Font("Serif", Font.BOLD, 17));

		mostValuableWord.setText("Most valuable word played:");
		mostValuableWord.setFont(new Font("Serif", Font.BOLD, 17));

		// these are the actual values that will have to be retrieved from the
		// database somehow
		playedGamesValue.setText("value");
		playedGamesValue.setFont(new Font("Serif", Font.PLAIN, 17));

		gamesWonValue.setText("value");

		gamesWonValue.setFont(new Font("Serif", Font.PLAIN, 17));
		compsWonValue.setText("value");
		compsWonValue.setFont(new Font("Serif", Font.PLAIN, 17));
		highScoreValue.setText("value");
		highScoreValue.setFont(new Font("Serif", Font.PLAIN, 17));
		mostValuableWordValue.setText("value");
		mostValuableWordValue.setFont(new Font("Serif", Font.PLAIN, 17));

		// playedGames.setForeground(Color.GRAY);
		// playedGamesValue.setForeground(Color.GRAY);
		// gamesWon.setForeground(Color.GRAY);
		// gamesWonValue.setForeground(Color.GRAY);
		// compsWon.setForeground(Color.GRAY);
		// compsWonValue.setForeground(Color.GRAY);
		// highScore.setForeground(Color.GRAY);
		// highScoreValue.setForeground(Color.GRAY);
		// mostValuableWord.setForeground(Color.GRAY);
		// mostValuableWordValue.setForeground(Color.GRAY);

		this.add(playedGames);

		this.add(playedGamesValue);

		this.add(gamesWon);
		this.add(gamesWonValue);
		this.add(compsWon);
		this.add(compsWonValue);
		this.add(highScore);
		this.add(highScoreValue);
		this.add(mostValuableWord);
		this.add(mostValuableWordValue);

	}
}
