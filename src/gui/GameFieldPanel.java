package gui;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import domein.Square;
import domein.Tile;

@SuppressWarnings("serial")
public class GameFieldPanel extends JPanel {
	private SquarePanel[][] squaresPanels;
	private ArrayList<TilePanel> tiles;
	private FieldPanel fieldPanel;
	private HandPanel handPanel;
	private boolean swapPressed;
	private ArrayList<TilePanel> tilesToSwap;
	private ObserverButtons observerButtons;
	private Tile selectedTile;

	public GameFieldPanel() {
		observerButtons = new ObserverButtons();
		squaresPanels = new SquarePanel[15][15];
		tiles = new ArrayList<TilePanel>();
		handPanel = new HandPanel();
		fieldPanel = new FieldPanel();
		this.setLayout(new BorderLayout());
		tilesToSwap = new ArrayList<TilePanel>();

		this.add(fieldPanel, BorderLayout.NORTH);
		this.add(handPanel, BorderLayout.SOUTH);
	}

	// Adds the observers
	public void addObserverToObserverButtons(Observer observer) {
		observerButtons.deleteObservers();
		observerButtons.addObserver(observer);
	}

	// Adds all the squares to the field and uses a Match as parameter
	public void addSquares(final Square square) {
		int y = square.getYPos();
		int x = square.getXPos();

		SquarePanel squarePanel;

		// Creates a SquarePanel
		squarePanel = new SquarePanel(square);
		if (square.getTile() != null) {
			squarePanel.addImage(square.getTile().getImage());
		}
		squaresPanels[x][y] = squarePanel;

		// Adds a mouseAdparter to the square
		squarePanel.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				SquarePanel sq = (SquarePanel) e.getSource();

				// Checks if the player is swapping tiles
				if (!swapPressed) {
					// Checks for selected Tiles
					if (selectedTile != null && !sq.getOccupied()) {
						// Adds the tiles to the board
						selectedTile.setXValue(sq.getXValue());
						selectedTile.setYValue(sq.getYValue());
						observerButtons.moveTile(selectedTile);
						sq.addImage(selectedTile.getImage());
						removeTileFromHand(selectedTile);
						selectedTile = null;
					}

					// Removes a tile from the board
					else if (selectedTile == null && sq.getOccupied()
							&& square.getTile().getJustPlayed()) {
						Tile t = new Tile(sq.getXValue(), sq.getYValue());
						observerButtons.moveTile(t);
						sq.removeImage();
					}
				}
			}
		});
		// Adds the square to the panels
		fieldPanel.addSquare(squarePanel);
	}

	// A method to make the tilePanels and uses a Match as parameter
	// And uses a Tile
	private TilePanel makeTilePanel(Tile tile) {

		// Makes a tilePanel and adds a listener
		TilePanel tilePanel = new TilePanel(tile);
		tilePanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				// Checks if you are swapping
				if (!swapPressed) {
					TilePanel tilePanel = (TilePanel) e.getSource();
					// Chechs if a tile is already selected
					if (selectedTile == tilePanel.getTile()) {
						selectedTile = null;
						tilePanel.setSelected(false);
						tilePanel.repaintPanel();
					}
					// Deslectes the tiles
					else {
						selectedTile = tilePanel.getTile();
						for (TilePanel t : tiles) {
							t.setSelected(false);
						}
						tilePanel.setSelected(true);
						tilePanel.repaintPanel();
						repaintBoard();
					}
				}
				// This part if swapping is enabled
				else {
					TilePanel tilePanel = (TilePanel) e.getSource();
					boolean containsTile = false;
					// Adds the tiles to be swapped
					for (TilePanel tts : tilesToSwap) {
						if (!containsTile) {
							if (tts == tilePanel) {
								containsTile = true;
							}
						}
					}
					// Removes the tile to be swapped
					if (containsTile) {
						tilesToSwap.remove(tilePanel);
						tilePanel.setSelected(false);
						tilePanel.repaintPanel();
					} else {
						// Adds the tile
						tilesToSwap.add(tilePanel);
						tilePanel.setSelected(true);
						tilePanel.repaintPanel();
					}
				}
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
	public void addTileToHand(ArrayList<Tile> tile) {
		tiles.clear();
		for (Tile t : tile) {
			@SuppressWarnings("unused")
			TilePanel tilePanel;	
			tiles.add(tilePanel = makeTilePanel(t));
			swapTiles();			
			handPanel.disposeTiles();
			for (TilePanel tp : tiles) {
				handPanel.addTile(tp);
			}
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

		// Cleares the hand
		handPanel.disposeTiles();
		for (TilePanel tp : tiles) {
			handPanel.addTile(tp);
			this.repaint();
			this.revalidate();
		}
	}

	// Adds a image to a squarePanel
	public void addSquarePanel(int x, int y, BufferedImage image) {
		squaresPanels[x][y].addImage(image);
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
		// match.swapTiles(tilesToSwap);
		tilesToSwap.clear();
		for (TilePanel t : tiles) {
			t.setSelected(false);
		}
	}

	// Gets the tiles that have to be swaped
	public ArrayList<TilePanel> getTilesToSwap() {
		return tilesToSwap;
	}

	// Checks if a squarePanel is occupied or not
	public boolean getOccupied(int x, int y) {
		return squaresPanels[x][y].getOccupied();
	}

	// Updates the board
	public void repaintBoard() {
		this.repaint();
		this.revalidate();
	}

	// Get tiles that were just placed on the board to calculate score
	// param the cords of the square / return tile on the square
	public SquarePanel getTileFromBoard(int x, int y) {
		return squaresPanels[x][y];
	}

	// Clears the field
	public void clearField() {
		fieldPanel.clearField();
		handPanel.disposeTiles();
		tiles.clear();
		for (int y = 0; y < 15; y++) {
			for (int x = 0; x < 15; x++) {
				squaresPanels[x][y] = null;
			}
		}
		repaintBoard();
	}

	// Clear the hand
	public void clearHand() {
		handPanel.disposeTiles();
	}

	// A class to be able to extend obserable for the buttons
	class ObserverButtons extends Observable {
		public void moveTile(Tile t) {
			// System.out.println("action requested: " + actionRequest);
			this.setChanged();
			this.notifyObservers(t);
		}
	}
}
