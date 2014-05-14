package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;

// The panel that contains all the squares
public class FieldPanel extends JPanel {

	public FieldPanel() {
		this.setPreferredSize(new Dimension(500, 500));
		this.setBackground(Color.black);
		this.setLayout(new GridLayout(15, 15));
	}

	// Adds the squares
	public void addSquare(SquarePanel s) {
		this.add(s);
	}

}