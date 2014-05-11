package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;

import datalaag.FileHandler;
import domein.Match;
import domein.Tile;

public class GameSpecScreen extends JPanel {
	private Match match;
	private SquarePanel[][] squaresPanels;
	private FieldPanel fieldPanel;
	private ArrayList<TilePanel> tilesP1;
	private HandPanel handPanelP1;
	private SpecScorePanel scorePanelP1;
	private ArrayList<TilePanel> tilesP2;
	private HandPanel handPanelP2;
	private SpecScorePanel scorePanelP2;
	private TilePanel tilePanel;
	private JButton forward;
	private JButton backward;

	public GameSpecScreen() {
		squaresPanels = new SquarePanel[15][15];
		tilesP1 = new ArrayList<TilePanel>();
		tilesP2 = new ArrayList<TilePanel>();
		handPanelP1 = new HandPanel();
		handPanelP2 = new HandPanel();
		scorePanelP1 = new SpecScorePanel();
		scorePanelP2 = new SpecScorePanel();
		fieldPanel = new FieldPanel();
		forward = new JButton("Forward");
		backward = new JButton("Backward");
		this.setBackground(Color.white);
		this.setLayout(null);
		fieldPanel.setBounds(275, 55, 500, 500);
		this.add(fieldPanel);
		handPanelP1.setBounds(275, 5, 500, 50);
		this.add(handPanelP1);
		handPanelP2.setBounds(275, 555, 500, 50);
		this.add(handPanelP2);
		scorePanelP1.setBounds(10, 5, 260, 150);
		this.add(scorePanelP1);
		scorePanelP2.setBounds(785, 455, 260, 150);
		this.add(scorePanelP2);
		forward.setBounds(785, 300, 260, 50);
		forward.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				match.updateSpecTurn(true);
			}
		});
		this.add(forward);
		backward.setBounds(5, 300, 260, 50);
		backward.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				match.updateSpecTurn(false);
			}
		});
		this.add(backward);
	}

	public void setMatch(Match match) {
		this.match = match;
	}

	// Creating the field
	public void createField() {
		for (int y = 0; y < 15; y++) {
			for (int x = 0; x < 15; x++) {
				SquarePanel squarePanel;
				squarePanel = new SquarePanel(match.getSquare(x, y));
				if (match.getSquare(x, y).getTile() != null) {
					squarePanel.addImage(match.getImage(x, y));
				}
				squaresPanels[x][y] = squarePanel;
				fieldPanel.addSquare(squarePanel);
			}
		}
	}

	// Restting the board
	public void resestField() {
		for (int y = 0; y < 15; y++) {
			for (int x = 0; x < 15; x++) {
				if (match.getSquare(x, y).getTile() != null) {
					squaresPanels[x][y].addImage(match.getImage(x, y));
				} else {
					squaresPanels[x][y].removeImage();
				}
			}
		}
	}

	// Method to add image letters
	public void addImageToBoard(BufferedImage image, int x, int y) {
		squaresPanels[x][y].addImage(image);
	}

	// Setting hands op both playeys
	public void addTilesP1(Tile t) {
		tilePanel = new TilePanel(t);
		tilesP1.add(tilePanel);
		handPanelP1.disposeTiles();
		for (TilePanel tp : tilesP1) {
			handPanelP1.addTile(tp);
		}
	}

	public void addTilesP2(Tile t) {
		tilePanel = new TilePanel(t);
		tilesP2.add(tilePanel);
		handPanelP2.disposeTiles();
		for (TilePanel tp : tilesP2) {
			handPanelP2.addTile(tp);
		}
	}

	public void resetHands() {
		tilesP1.clear();
		tilesP2.clear();
		handPanelP1.disposeTiles();
		handPanelP2.disposeTiles();
		repaintBoard();
	}

	// end of the hand part

	// Setting the score panel
	public void setNameP1(String name) {
		scorePanelP1.setName(name);
	}

	public void setScoreP1(int score) {
		scorePanelP1.setScore(score);
	}

	public void setNameP2(String name) {
		scorePanelP2.setName(name);
	}

	public void setScoreP2(int score) {
		scorePanelP2.setScore(score);
	}

	public void setTurnScoreP1(int score) {
		scorePanelP1.setTurnScore(score);
	}

	public void setTurnScoreP2(int score) {
		scorePanelP2.setTurnScore(score);
	}

	public void setTurn(boolean P1turn) {
		if (P1turn) {
			scorePanelP1.setTurn(true);
			scorePanelP2.setTurn(false);
		} else {
			scorePanelP1.setTurn(false);
			scorePanelP2.setTurn(true);
		}
	}

	// End of scorepanel part

	public void repaintBoard() {
		this.repaint();
		this.revalidate();
		this.repaint();
	}
}
