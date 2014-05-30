package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

// ScorePanel from a normal game
@SuppressWarnings("serial")
public class ScorePanel extends JPanel {

	private int wordValue;
	private int enemyScore;
	private int ownScore;
	private String ownName;
	private String enemyName;

	public ScorePanel() {
		this.setPreferredSize(new Dimension(500, 100));
		this.setLayout(null);
		ownName = "You";
		enemyName = "Opponent";
	}

	// A paintCompontent to paint everything
	public void paintComponent(Graphics g) {
		g.setColor(Color.yellow);
		g.fillRect(0, 0, 500, 100);

		// Sets the font and colour
		g.setColor(Color.black);
		Font font = new Font("Monospaced", Font.BOLD | Font.ITALIC, 20);
		g.setFont(font);
		
		// Prints the player part
		g.drawString(ownName, 300, 30);
		g.drawString("" + ownScore, 450, 30);

		// Prints the enemy part
		g.drawString(enemyName, 300, 70);
		g.drawString("" + enemyScore, 450, 70);

		// Current word value
		g.drawString("Current word value:", 15, 30);
		g.drawString("" + wordValue, 15, 70);
	}

	// Sets the wordValue
	public synchronized void setWordValue(int value) {
		this.wordValue = value;
	}

	// Sets the ownScore
	public synchronized void setOwnScore(int score) {
		this.ownScore = score;
	}

	// Sets the enemyScore
	public synchronized void setEnemyScore(int score) {
		this.enemyScore = score;
	}
	
	// Sets the ownName
	public synchronized void setOwnName(String name) {
		this.ownName = name;
	}
	
	// Sets the enemyName
	public synchronized void setEnemyName(String name) {
		this.enemyName = name;
	}
}
