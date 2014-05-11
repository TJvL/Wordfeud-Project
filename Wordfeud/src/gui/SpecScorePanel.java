package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

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
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setScore(int score){
		this.maxScore = score;
		this.turnScore = score;
	}
	
	public void setTurnScore(int turnScore){
		this.turnScore = turnScore;
	}
	
	public void setTurn(boolean turn){
		this.myTurn = turn;
	}
	
	public void paintComponent(Graphics g){
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
