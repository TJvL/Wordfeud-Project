package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

public class TilePanel extends JPanel implements MouseListener {
	private final int size = 33;
	private int number;

	public TilePanel(int number) {
		this.number = number;
		this.setPreferredSize(new Dimension(size, size));
		this.setBackground(Color.red);
		this.addMouseListener(this);
	}

	public int getNumber() {
		return number;
	}

	@Override
	public void mouseClicked(MouseEvent e) { // TODO Auto-generated
		System.out.println("Geklikt");
		this.setBackground(Color.yellow);
	}

	@Override
	public void mouseEntered(MouseEvent e) { // TODO Auto-generated
		this.setBackground(Color.green);
	}

	@Override
	public void mouseExited(MouseEvent e) { // TODO Auto-generated
		this.setBackground(Color.black);
	}

	@Override
	public void mousePressed(MouseEvent e) { // TODO Auto-generated

	}

	@Override
	public void mouseReleased(MouseEvent e) { // TODO

	}
}
