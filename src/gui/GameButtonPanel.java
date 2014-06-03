package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import datalaag.WordFeudConstants;

@SuppressWarnings("serial")
public class GameButtonPanel extends JPanel {
	private GameFieldPanel boardP;
	private boolean swapPressed;
	private JButton swap;
	private JButton play;
	private JButton pass;
	private JButton surr;
	private JButton clear;
	private JButton shuffle;
	private ObserverButtons observerButtons;

	// This uses a gameFieldPanel
	public GameButtonPanel(GameFieldPanel boardP) {
		this.boardP = boardP;
		//this.setBackground(Color.green);
		this.setBackground(Color.cyan);
		this.addButtons();
		this.swapPressed = false;
		observerButtons = new ObserverButtons();
	}

	// Adds the observers
	public void addObserverToObserverButtons(Observer observer) {
		observerButtons.deleteObservers();
		observerButtons.addObserver(observer);
	}

	public boolean getSwapPressed() {
		return swapPressed;
	}

	// A method to disable the surrender button
	public synchronized void disableSurrender() {
		surr.setEnabled(false);
	}

	// A method to disable the swap button
	public synchronized void disableSwap() {
		swap.setEnabled(false);
	}

	// Enables or disables the buttons
	public synchronized void setTurn(boolean turn) {
		if (turn) {
			if (swapPressed) {
				swap.setEnabled(true);
				play.setEnabled(false);
				pass.setEnabled(false);
				surr.setEnabled(false);
			} else {
				swap.setEnabled(true);
				play.setEnabled(true);
				pass.setEnabled(true);
				surr.setEnabled(true);
			}
		} else {
			swap.setEnabled(false);
			play.setEnabled(false);
			pass.setEnabled(false);
		}
	}

	// Adds the buttons to the field
	public void addButtons() {
		swap = new JButton("Swap");
		play = new JButton("Play");
		pass = new JButton("Pass");
		surr = new JButton("Surrender");
		clear = new JButton("Clear");
		shuffle = new JButton("Shuffle");

		// Adds the listeners
		play.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				playTiles();
			}
		});

		pass.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				skipTurn();
			}
		});

		swap.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				swapTiles();
			}
		});

		surr.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				surrenderGame();
			}
		});

		clear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				clearTiles();
			}
		});

		shuffle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				shuffleTiles();
			}
		});

		// Adds the buttons
		this.add(play);
		this.add(pass);
		this.add(shuffle);
		this.add(swap);
		this.add(clear);
		this.add(surr);
	}

	// Sets the observer
	private void playTiles() {
		observerButtons.changeActionRequest("play");
	}

	// Sets the observer
	private void skipTurn() {
		observerButtons.changeActionRequest("pass");
	}

	// Sets the observer
	private void shuffleTiles() {
		boardP.suffleHand();
	}

	// methodes to allow swapping of tiles
	// If you press swap all buttons will be disabled
	// Select tiles and click on the confirm button
	private void swapTiles() {
		if (swapPressed) {
			swap.setText("Swap");
			boardP.setSwapPressed(false);
			// Stets the observer
			observerButtons.swapList(boardP.getTilesToSwap());
			// observerButtons.changeActionRequest("swap");
			swapPressed = false;
			surr.setEnabled(true);
			play.setEnabled(true);
			pass.setEnabled(true);
			shuffle.setEnabled(true);
			clear.setEnabled(true);
		}
		// Warning to show that you are going to swap
		else {
			if (JOptionPane.showConfirmDialog(null,"Are you sure you want to start swapping tiles?"+ "\n Select the tiles you want to swap","WARNING", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				swap.setText("Confirm swap");
				swap.setEnabled(true);
				swapPressed = true;
				surr.setEnabled(false);
				play.setEnabled(false);
				pass.setEnabled(false);
				shuffle.setEnabled(false);
				clear.setEnabled(false);
				boardP.setSwapPressed(true);

				// yes option
			}
		}
	}

	// Sets the observer
	private void clearTiles() {
		observerButtons.changeActionRequest("clear");
	}

	// Sets the observer
	private void surrenderGame() {
		observerButtons.changeActionRequest("surrender");
	}

	// A class to be able to extend obserable for the buttons
	class ObserverButtons extends Observable {
		public void changeActionRequest(String actionRequest) {
			System.out.println("action requested: " + actionRequest);
			this.setChanged();
			this.notifyObservers(actionRequest);
		}

		public void swapList(ArrayList<TilePanel> arrayList) {
			// System.out.println("action requested: " + actionRequest);
			this.setChanged();
			this.notifyObservers(arrayList);
		}
	}
}
