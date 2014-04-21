package domein;

import java.awt.image.BufferedImage;

import datalaag.FileHandler;

public class Square {
	private String value;
	private String path;
	private FileHandler fh = FileHandler.getInstance();
	private int x;
	private int y;
	private Tile tile;
	
	public Square(int x, int y, String value, String path){
		this.x=x;
		this.y=y;
		this.path = path;
		this.value = value;
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
		BufferedImage image = fh.readImage(path);
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
