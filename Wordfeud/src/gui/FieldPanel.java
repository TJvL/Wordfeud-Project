package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JPanel;

public class FieldPanel extends JPanel {
	private ArrayList<SquarePanel> sq = new ArrayList<SquarePanel>(225);

	public FieldPanel() {
		this.setPreferredSize(new Dimension(500,500));
		this.setBackground(Color.white);
		this.setLayout(new GridLayout(15,15));
		addSquares();		
	}

	public void addSquares() {
		for (int p = 0; p < 225; p++) {
			SquarePanel square = new SquarePanel(p);
	
			square.addMouseListener(new MouseAdapter() {
				
				public void mouseClicked(MouseEvent e) { // TODO Auto-generated
					System.out.println("Geklikt");
			//		if (bezet) {
			//			bezet = false;
			//			this.setBackground(Color.red);
			//		} else {
			//			bezet = true;
			//			this.setBackground(Color.yellow);
			//		}
				}

				@Override
				public void mouseEntered(MouseEvent e) { // TODO Auto-generated
			//		this.setBackground(Color.green);
				}

				@Override
				public void mouseExited(MouseEvent e) { // TODO Auto-generated
			//		this.setBackground(Color.black);
				}

				@Override
				public void mousePressed(MouseEvent e) { // TODO Auto-generated

				}

				@Override
				public void mouseReleased(MouseEvent e) { // TODO

				}
			
			});
			
			
			
			sq.add(square);
			this.add(square);
		}
	}
	
	
//	public void paintAll() {
//		for (int p = 0; p < 15; p++) {
//			for (int i = 0; i < 15; i++) {
//				sq[i][p].setBounds(sq[i][p].getX(), sq[i][p].getY(), width,
//						height);
//			}
//		}
//	}
	
}
