package domein;

public class Joker extends Tile{

	private String letter;
	private int value;
	private String path;
	private boolean played;
	
	// Joker moet nog worden gemaakt, alleen een normale tile bestaat nu
	
	public Joker(String letter, int value, String path) {
		super(letter, value, path);
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	public String getLetter(){
		return letter;
	}
	
	public int getValue(){
		return value;
	}
	
	public String getPath(){
		return path;
	}
	
	public boolean getPlayed(){
		return played;
	}

}
