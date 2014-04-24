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
	
	public int getXPos(){
		return x;
	}
	
	public int getYPos(){
		return y;
	}
	
	public String getValue(){
		return value;
	}
	
	public BufferedImage getImage(){
	//	FileHandler fh = FileHandler.getInstance();
	//	BufferedImage image = ;
		return image;
	}
	
	public void addTile(Tile t){
		this.tile = t;
	}
	
	public Tile getTile(){
		return tile;
	}
	
	public void removeTile(){
		tile = null;
	}
}
