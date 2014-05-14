package domein;

import java.awt.image.BufferedImage;

public class Square {
	private String value;
	private int x;
	private int y;
	private Tile tile;
	private BufferedImage image;
	
	public Square(int x, int y, String value, BufferedImage image){
		this.x=x;
		this.y=y;
		this.value = value;
		this.image = image;
	}
	
	// Returns X
	public int getXPos(){
		return x;
	}
	
	// Returns Y
	public int getYPos(){
		return y;
	}
	
	// Returns the value
	public String getValue(){
		return value;
	}
	
	// Returns the image
	public BufferedImage getImage(){
		return image;
	}
	
	// Sets the tile
	public void addTile(Tile t){
		this.tile = t;
	}
	
	// Returns the Tile
	public Tile getTile(){
		return tile;
	}
	
	// Removes the tile
	public void removeTile(){
		tile = null;
	}
}
