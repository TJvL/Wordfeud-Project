package gui;

import java.awt.Dimension;

import javax.swing.JFrame;

public class StatWindow extends JFrame {
	private StatScreen statscreen;

	public StatWindow() {
		statscreen = new StatScreen();

		this.setPreferredSize(new Dimension(600, 350));
		this.setContentPane(statscreen);
	}
}
