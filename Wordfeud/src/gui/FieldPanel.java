package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JPanel;

public class FieldPanel extends JPanel {
	//private ArrayList<SquarePanel> sq = new ArrayList<SquarePanel>(225);

	public FieldPanel() {
		this.setPreferredSize(new Dimension(500, 500));
		this.setBackground(Color.black);
		this.setLayout(new GridLayout(15, 15));
		// addSquares();
	}

	public void addSquare(SquarePanel s) {
		this.add(s);
	}

}