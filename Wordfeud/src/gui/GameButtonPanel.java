package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import domein.Match;

public class GameButtonPanel extends JPanel {

	private static final long serialVersionUID = -1759296107629262333L;

	private Match match;
	private GameFieldPanel boardP;
	private boolean swapPressed;
	private JButton swap;
	private JButton play;
	private JButton pass;
	private JButton surr;
	private JButton clear;
	private JButton shuffle;

	public GameButtonPanel(GameFieldPanel boardP) {
		this.boardP = boardP;
		this.setBackground(Color.green);
		this.addButtons();
		this.swapPressed = false;
	}

	public void setMatch(Match match) {
		this.match = match;
	}
	
	public synchronized void setTurn(boolean turn){
		if (turn){
			swap.setEnabled(true);
			play.setEnabled(true);
			pass.setEnabled(true);
		} else {
			swap.setEnabled(false);
			play.setEnabled(false);
			pass.setEnabled(false);
		}
	}

	public void addButtons() {
		swap = new JButton("Swap");
		play = new JButton("Play");
		pass = new JButton("Pass");
		surr = new JButton("Surrender");
		clear = new JButton("Clear");
		shuffle = new JButton("Shuffle");

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

		this.add(play);
		this.add(pass);
		this.add(shuffle);
		this.add(swap);
		this.add(clear);
		this.add(surr);
	}

	// ////////////////////////////////////
	private void playTiles() {
		// if score > 0

		// Moet worden bijgewerkt
		// moet verwijzen naar match.playTiles
		// Daar moet alles worden geregeld
		/*
		 * 
		 * boardP.playTiles();
		 * 
		 * for (int y = 0; y < 15; y++) { for (int x = 0; x < 15; x++) { if
		 * (boardP.getTileFromBoard(x, y).getOccupied()) { //
		 * System.out.println(boardP.getTileFromBoard(x, // y).getLetter());
		 * match.addPlayedTilesToBoard(boardP.getTileFromBoard(x, y)
		 * .getTile()); } } }
		 */

		if (match.startCalculating()) {
			// if this is true, a score will be calculated
			System.err.println("De score is berekent");
			if (match.checkWords()){
				match.setTilesPlayed();
				match.fillHand();
				System.err.println("Het is door de woordcheck gekomen");
			} else {
				System.err.println("WOORDEN ZIJN FOUT");
				// hier moet de optie komen om ze te laten keuren
			}
			
		}
		/*
		 * boardP.playTiles(); if (ScoreCalculator.getInstance().getScore() > 0)
		 * { // woord komt voor in database?
		 * 
		 * // if true // tel score van player op met de score // 4. zet alle
		 * tiles op played = true // vul hand aan tot 7 (of meest haalbare) //
		 * verander van beurt } else { JOptionPane .showMessageDialog(null,
		 * "The tiles you have placed don't seem to be giving you a score."); }
		 */
	}

	private void skipTurn() {
		// DatabaseHandler.getInstance().changeTurn();
	}

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
			boardP.swapTiles();
			swapPressed = false;
			surr.setEnabled(true);
			play.setEnabled(true);
			pass.setEnabled(true);
			shuffle.setEnabled(true);
			clear.setEnabled(true);
			// swapTiles()
		} else {
			if (JOptionPane.showConfirmDialog(null,
					"Are you sure you want to start swapping tiles?"
							+ "\n Select the tiles you want to swap",
					"WARNING", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				swap.setText("Confirm swap");
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

	private void clearTiles() {
		match.clearTilesFromBoard();
	}

	private void surrenderGame() {
		// pop-up maken met score e.d.
		System.exit(0);
	}
}
