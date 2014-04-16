package gui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;

public class BoardPanel extends JPanel{

	public BoardPanel(){
		HandPanel handPanel = new HandPanel();
		FieldPanel fieldPanel = new FieldPanel();
		this.setLayout(new BorderLayout());
		
		this.add(fieldPanel, BorderLayout.NORTH);
		this.add(handPanel, BorderLayout.SOUTH);
	}
	
}
