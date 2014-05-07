package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

import domein.Match;

public class ScorePanel extends JPanel {
	private Match match;
	private GameFieldPanel boardPanel;
	private int wordValue;
	private int enemyScore;
	private int ownScore;

	public ScorePanel(GameFieldPanel boardpanel) {
		this.setPreferredSize(new Dimension(500, 100));
		// this.setBackground(Color.yellow);
		this.setLayout(null);
		this.boardPanel = boardPanel;
	}

	public void setMatch(Match match) {
		this.match = match;
	}

	public void paintComponent(Graphics g) {
		g.setColor(Color.yellow);
		g.fillRect(0, 0, 500, 100);

		g.setColor(Color.black);
		Font font = new Font("Monospaced", Font.BOLD | Font.ITALIC, 20);
		g.setFont(font);
		g.drawString("You", 300, 30);
		g.drawString("" + ownScore, 450, 30);

		
		// --- dit ding zorgt nog voor wat errors --- //	
		// Here is a try and catch because getEnemyName is often null
		// This catch stops the nullpointer
		try {
			g.drawString(match.getEnemyName(), 300, 70);
		} catch (NullPointerException e) {

		}
		g.drawString("" + enemyScore, 450, 70);

		// Current word value
		g.drawString("Current word value:", 15, 30);
		g.drawString("" + wordValue, 15, 70);

	}

	public synchronized void setWordValue(int value) {
		this.wordValue = value;
	}

	public synchronized void setOwnScore(int score) {
		this.ownScore = score;
	}

	public synchronized void setEnemyScore(int score) {
		this.enemyScore = score;
	}

	public synchronized void updatePanel() {
		this.revalidate();
		this.repaint();
	}
}
