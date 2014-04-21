package domein;

public class Match {
	private Tile tile;
	private int squareValue;
	private Jar jar = new Jar();
	private Board board = new Board();

	// private storeClicledTile

	public Match() {

		// vullen van de jar - een nieuwe jar aanmaken
		fillJar();
		
	}

	// Jar gedeelte
	public void fillJar() {
		// checke of er al een pot is
		// dus checke of het een nieuwe game is of een waar je verder in speelt
		// database bestaat nog niet
		
		jar.fillJar();
	}

	public Tile getTileFromJar(){
		return jar.getNewTile();
	}
	
	public void addTileToJar(Tile t){
		jar.addNewTile(t);
	}
	
	// einde jar gedeelte
	
	// board gedeelte
	public Square getSquare(int x, int y){
		return board.getSquare(x, y);
	}
	
	
	// in de GUI moet deze aan worden geroepen als een game wordt gestart
	// spel wordt geladen en einde van de beurt
	
	// einde van de board
	
	
	// bewaren van de tiles gedeelte
	public void storeTile(Tile t) {
		this.tile = t;
	}

	public void storeSquare(int value) {
		this.squareValue = value;
	}

	public Tile getTile() {
		return tile;
	}

	
	// Beurt wisselingen
	public void changeTurn(){
		  // verander van beurt
		 }
	
	// Adding the just played tiles to the board for calculation
	// input tile
	public void addPlayedTilesToBoard(Tile t){
		board.addTileToBoard(t);
	}
	
	// A method to start calculating
	public boolean startCalculating(){
		return board.startCalculating();
	}
}
