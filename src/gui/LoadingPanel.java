package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class LoadingPanel extends JPanel implements Runnable {

	private Font font;
	private JLabel loadingLabel;
	private boolean running = true;
	private ArrayList<ImageIcon> icons;

	public LoadingPanel() {
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(1200, 700));
		font = new Font("Monospaced", Font.BOLD | Font.ITALIC, 30);
		icons = new ArrayList<ImageIcon>();
		icons.add(new ImageIcon("Plaatjes/loading1.jpg"));
		icons.add(new ImageIcon("Plaatjes/loading2.jpg"));
		icons.add(new ImageIcon("Plaatjes/loading3.jpg"));
		icons.add(new ImageIcon("Plaatjes/loading4.jpg"));

		loadingLabel = new JLabel();
		loadingLabel.setText("Loading");
		loadingLabel.setFont(font);
		loadingLabel.setForeground(Color.white);
		// loadingLabel.setBounds(580, 345, 300, 50);

		this.add(loadingLabel, BorderLayout.SOUTH);

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		int i = 1;
		while (running) {
			if (i > 3) {
				i = 0;
			}
			loadingLabel.setIcon(icons.get(i));
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			i++;
		}
	}

	public synchronized void setRunning(boolean running) {
		this.running = running;	
	}

}
