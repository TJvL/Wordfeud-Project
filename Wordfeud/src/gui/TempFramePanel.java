package gui;

import java.awt.Dimension;

import javax.swing.JFrame;

public class TempFramePanel extends JFrame {

	public TempFramePanel() {
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		BoardPanel boardPanel = new BoardPanel();
		this.add(boardPanel);
		
		this.pack();	
		this.setVisible(true);
	}
}
