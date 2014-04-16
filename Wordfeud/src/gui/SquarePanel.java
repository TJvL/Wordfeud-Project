package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

public class SquarePanel extends JPanel {//implements MouseListener {

	private final int size = 33;
	private boolean bezet;
	private int number;

	public SquarePanel(int number) {
		this.number = number;
		this.setPreferredSize(new Dimension(size, size));
		this.setBackground(Color.red);
	//	this.addMouseListener(this);
	}

	public void setBezet(boolean iets) {
		bezet = iets;
	}

	public boolean getBezet() {
		return bezet;
	}

	public int getNumber(){
		return number;
	}

}
