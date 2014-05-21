package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

// A panel so show the score during spectating
@SuppressWarnings("serial")
public class SpecScorePanel extends JPanel{
	private String name;
	private int maxScore;
	private boolean myTurn;
	private int turnScore;

	public SpecScorePanel(){
		this.name = "Klaas";
		this.maxScore = 0;	
		this.turnScore = 0;
		this.setPreferredSize(new Dimension(250,150));	
	}
	
	// Sets the name
	public void setName(String name){
		this.name = name;
	}
	
	// Sets the score
	public void setScore(int score){
		this.maxScore = score;
		this.turnScore = score;
	}
	
	// Setc the turnscore
	public void setTurnScore(int turnScore){
		this.turnScore = turnScore;
	}
	
	// Sets if it's this players turn or not
	public void setTurn(boolean turn){
		this.myTurn = turn;
	}
	
	// Paints the text
	public void paintComponent(Graphics g){
		// If it is my turn it will paint green
		// Els if will paint red
		if (myTurn){
			g.setColor(Color.green);
		} else {
			g.setColor(Color.red);
		}
		g.fillRect(0, 0, 250, 150);
		
		g.setColor(Color.black);
		Font font = new Font("Monospaced", Font.BOLD | Font.ITALIC, 20);
		g.setFont(font);
		g.drawString(name, 10, 30);
		g.drawString("Total score: " + maxScore, 10, 80);
		g.drawString("Score this turn: " + turnScore, 10, 130);
	}
}
