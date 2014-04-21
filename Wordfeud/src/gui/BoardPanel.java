package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JPanel;

import domein.Match;


// Vervangen door GameFieldPanel
// kan dus weg gehaald worden
/*
public class BoardPanel extends JPanel {
	private SquarePanel[][] squaures;
	private ArrayList<TilePanel> tiles;
	private FieldPanel fieldPanel;
	private HandPanel handPanel;
	private Match match;

	public BoardPanel(Match match) {
		squaures = new SquarePanel[15][15];
		tiles = new ArrayList<TilePanel>();
		this.match = match;
		handPanel = new HandPanel();
		fieldPanel = new FieldPanel();
		this.setLayout(new BorderLayout());

		this.add(fieldPanel, BorderLayout.NORTH);
		this.add(handPanel, BorderLayout.SOUTH);
		this.addSquares();
		this.addTiles();
	}

	public void addSquares() {
		boolean j = true;
		for (int y = 0; y < 15; y++) {
			for (int x = 0; x < 15; x++) {
				SquarePanel square;
				// Deze waardes/plaatjes moeten worden opgevraagd aan match
				// match haalt het weer van bord af enz.
				if (j) {
					square = new SquarePanel(x, y, Color.red);
				} else {
					square = new SquarePanel(x, y, Color.blue);
				}

				squaures[x][y] = square;
				j = !j;
				// square.setBackground(Color.red);
				square.addMouseListener(new MouseAdapter() {

					public void mouseClicked(MouseEvent e) {
						SquarePanel sq = (SquarePanel) e.getSource();
						String output = "" + sq.getXValue() + " | "
								+ sq.getYValue();
						System.out.println(output);

						if (match.getTile() != 1111 && !sq.getOccupied()) {
							// het plaatje opvragen van de tile die wordt
							// geplaatst
							// deze wordt dan ook megegeven aan match of iets
							// dergelijks
							sq.setOccupied(true);
							sq.setBackgroundColor(Color.cyan);
							removeTile(match.getTile());
							match.storeTile(1111);
						}
					}

					@Override
					public void mouseEntered(MouseEvent e) {
					}

					@Override
					public void mouseExited(MouseEvent e) {
						// this.setBackground(Color.black);
					}

					@Override
					public void mousePressed(MouseEvent e) {

					}

					@Override
					public void mouseReleased(MouseEvent e) { // TODO

					}

				});

				fieldPanel.addSquare(square);
				// this.add(square);
			}
		}
	}

	public void addTiles() {

		for (int i = 0; i < 7; i++) {
			TilePanel tile;

			// Value wordt gelijk aan de value van een steen - elke steen krijgt
			// unieke value
			tile = new TilePanel(i, Color.red);

			tiles.add(tile);

			// square.setBackground(Color.red);
			tile.addMouseListener(new MouseAdapter() {

				public void mouseClicked(MouseEvent e) {
					TilePanel tp = (TilePanel) e.getSource();
				//	String output = "" + tp.getNumber();
				//	System.out.println(output);

					if (match.getTile() == tp.getNumber()) {
						match.storeTile(1111);
						System.out.println("Value is 1111");
					} else {
						match.storeTile(tp.getNumber());
						System.out.println("Value is " + match.getTile());
					}
				}

				@Override
				public void mouseEntered(MouseEvent e) {
				}

				@Override
				public void mouseExited(MouseEvent e) {
					// this.setBackground(Color.black);
				}

				@Override
				public void mousePressed(MouseEvent e) {

				}

				@Override
				public void mouseReleased(MouseEvent e) {
				}

			});

			handPanel.addTile(tile);
			// this.add(square);
		}
	}

	public void removeTile(int number) {
		for (TilePanel tp : tiles) {
			if (tp.getNumber() == number) {
				tiles.remove(tp);
				handPanel.disposeTiles();
				if (tiles.size() > 0) {
					for (TilePanel t : tiles) {
						handPanel.addTile(t);
						this.repaint();
						this.revalidate();
					}
				} else {
					System.out.println("test");
					this.repaint();
					this.revalidate();
				}
				break;
			}
		}
	}
}*/