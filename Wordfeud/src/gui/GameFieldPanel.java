package gui;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JPanel;
import domein.Match;
import domein.Tile;

public class GameFieldPanel extends JPanel {

	private static final long serialVersionUID = -5454329691571947261L;

	private SquarePanel[][] squaresPanels;
	private ArrayList<TilePanel> tiles;
	private FieldPanel fieldPanel;
	private HandPanel handPanel;
	private Match match;
	private boolean swapPressed;
	private ArrayList<TilePanel> tilesToSwap;

	public GameFieldPanel() {
		squaresPanels = new SquarePanel[15][15];
		tiles = new ArrayList<TilePanel>();
		handPanel = new HandPanel();
		fieldPanel = new FieldPanel();
		this.setLayout(new BorderLayout());
		tilesToSwap = new ArrayList<TilePanel>();

		this.add(fieldPanel, BorderLayout.NORTH);
		this.add(handPanel, BorderLayout.SOUTH);
	}

	// Method to set the match loaded now
	public void setMatch(Match match) {
		this.match = match;
	}

	public void startNewGame() {
		this.addSquares();
	}

	public void addSquares() {
		// boolean j = true;

		for (int y = 0; y < 15; y++) {
			for (int x = 0; x < 15; x++) {

				// --- dit moet nog een in private method worden gezet
				// -------//////////

				SquarePanel squarePanel;

				squarePanel = new SquarePanel(match.getSquare(x, y));
				if (match.getSquare(x, y).getTile() != null){
					squarePanel.addImage(match.getSquare(x, y).getTile() .getImage());
				}
				squaresPanels[x][y] = squarePanel;
				// j = !j;
				// square.setBackground(Color.red);
				squarePanel.addMouseListener(new MouseAdapter() {

					public void mouseClicked(MouseEvent e) {
						SquarePanel sq = (SquarePanel) e.getSource();
						String output = "" + sq.getXValue() + " | "
								+ sq.getYValue();
						System.out.println(output);

						if (sq.getOccupied()) {
							System.out.println(match
									.getSquare(sq.getXValue(), sq.getYValue())
									.getTile().getJustPlayed()
									+ " is net gespeeld");
						}

						if (!swapPressed) {
							// Deze methode moet eigenlijk naar de match
							// verplaatst
							// worden
							// Dan wordt daar bepaald wat er gebeurd als er op
							// een
							// square wordt geklikt
							if (match.getSelectedTile() != null
									&& !sq.getOccupied()) {
								match.moveTileFromHandToBoard(sq.getXValue(),
										sq.getYValue());
								sq.addImage(match.getSelectedTile().getImage());
								// sq.setBackgroundColor(Color.cyan);
								removeTileFromHand(match.getSelectedTile());
								match.selectedTile(null);
							}

							// test om tegels weer weg te kunnen halen
							else if (match.getSelectedTile() == null
									&& sq.getOccupied()
									&& match.getSquare(sq.getXValue(),
											sq.getYValue()).getTile()
											.getJustPlayed()) {
								match.moveTileFromBoardToHand(sq.getXValue(),
										sq.getYValue());
								sq.removeImage();
							}
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

				fieldPanel.addSquare(squarePanel);
				// this.add(square);
			}
		}
	}

	private TilePanel makeTilePanel(Tile tile) {
		tile.setXValue(1111);
		tile.setYValue(1111);
		TilePanel tilePanel = new TilePanel(tile);

		tilePanel.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {

				// kijken of je niet aan het swappen bent
				if (!swapPressed) {
					TilePanel tilePanel = (TilePanel) e.getSource();
					// String output = "" + tp.getNumber();
					// System.out.println(output);

					// if (match.getTile() != null) {
					if (match.getSelectedTile() == tilePanel.getTile()) {
						match.selectedTile(null);
						System.out.println("Value is "
								+ match.getSelectedTile());
						tilePanel.setSelected(false);
						tilePanel.repaintPanel();
					} else {
						// nog maken als je op een andere tile klikt dan
						// selected dat hij de
						// waardes reset in match
						match.selectedTile(tilePanel.getTile());
						System.out.println("Value is "
								+ match.getSelectedTile().getLetter());

						for (TilePanel t : tiles) {
							t.setSelected(false);
						}
						tilePanel.setSelected(true);
						tilePanel.repaintPanel();
						repaintBoard();
					}
				}
				// Swap gedeelte - selecteren van stenen

				else {
					TilePanel tilePanel = (TilePanel) e.getSource();
					boolean containsTile = false;
					for (TilePanel tts : tilesToSwap) {
						if (!containsTile) {
							if (tts == tilePanel) {
								containsTile = true;
							}
						}
					}
					if (containsTile) {
						tilesToSwap.remove(tilePanel);
						tilePanel.setSelected(false);
						tilePanel.repaintPanel();
					} else {
						tilesToSwap.add(tilePanel);
						tilePanel.setSelected(true);
						tilePanel.repaintPanel();
					}
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

		return tilePanel;
	}

	// When you select a tile to place on a square
	// This will remove the tile you give it with a para
	// Remove it from the hand en update it
	public void removeTileFromHand(Tile tile) {
		for (TilePanel tp : tiles) {
			if (tp.getTile() == tile) {
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

	// You give it a tile to be add to the hand
	// The param is the tile to be add
	public void addTileToHand(Tile t) {
		TilePanel tile;
		tiles.add(tile = makeTilePanel(t));

		handPanel.disposeTiles();
		for (TilePanel tp : tiles) {
			handPanel.addTile(tp);
			this.repaint();
			this.revalidate();
		}
	}

	// Remove tiles from board afther clearing
	public void removeImageSquare(int x, int y) {
		squaresPanels[x][y].removeImage();
	}

	// Shuffle the tiles on the board
	public void suffleHand() {
		for (int i = 0; i < 10; i++) {
			TilePanel tp = tiles.get((int) (0 + (Math.random())
					* ((tiles.size() - 0))));
			tiles.remove(tp);
			tiles.add(tp);
		}

		handPanel.disposeTiles();
		for (TilePanel tp : tiles) {
			handPanel.addTile(tp);
			this.repaint();
			this.revalidate();
		}
	}

	// Var to see if swapping is enabled
	// parm if it's true or false
	public void setSwapPressed(boolean pressed) {
		this.swapPressed = pressed;
	}

	// Swap tiles from the hand for a other new tiles
	// When swap is pushed
	// it will give a message that allows you to
	// select some tiles to be swapped
	public void swapTiles() {
		match.swapTiles(tilesToSwap);
		tilesToSwap.clear();
		for (TilePanel t : tiles) {
			t.setSelected(false);
		}
		repaintBoard();
	}

	public boolean getOccupied(int x, int y) {
		return squaresPanels[x][y].getOccupied();
	}

	// When played is pressed set the tiles on played
	// Before this happeds all the checks need to be done

	// ------ check als woordenboek kijken - geldige woord - score berekeken //

	/*
	 * public void setAllTilesToPlayed() { for (SquarePanel[] s : squares) { for
	 * (SquarePanel sp : s) { if (sp.getOccupied()) {
	 * sp.getTile().setPlayed(true); } } } }
	 */

	/*
	 * public void fillHand() { while (tiles.size() < 7) { TilePanel tilePanel =
	 * makeTilePanel(match.getTileFromJar()); handPanel.addTile(tilePanel);
	 * tiles.add(tilePanel); this.repaint(); this.revalidate(); } }
	 */

	/*
	 * public void playTiles() {
	 * 
	 * // Test gedeelte ArrayList<Tile> playedTiles = new ArrayList<Tile>(); for
	 * (SquarePanel[] s : squares) { for (SquarePanel sp : s) { if
	 * (sp.getOccupied()) { if (sp.getTile().getXValue() == 1111) { // Tijdelijk
	 * als we op play douwen zijn de gelijk played // voor het testen
	 * sp.getTile().setXValue(sp.getXValue());
	 * sp.getTile().setYValue(sp.getYValue()); playedTiles.add(sp.getTile());
	 * System.out.println("Net neergelegde letter: " +
	 * sp.getTile().getLetter()); } } } } }
	 */

	public void repaintBoard() {
		this.repaint();
		this.revalidate();
	}

	// Get tiles that were just placed on the board to calculate score
	// param the cords of the square / return tile on the square
	public SquarePanel getTileFromBoard(int x, int y) {
		return squaresPanels[x][y];
	}
}