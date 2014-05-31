package domein;

public class Word {
	private String word;
	private int score;
	private int bonus;

	// Sets standerd values
	public Word(){
		this.bonus = 1;
		this.word = "";
	}
	
	// Adds the word bonus
	// This has to be calculated when the word if complete
	public void addWordBonusToScore(){
		if (bonus == 0){
			bonus = 1;
		}
		score = score * bonus;
	}
	
	// Adds letters to the word
	public void addLetter(String letter){
		word += letter;
	}
	
	// Adds a wordBonus
	public void addWordBonus(int bonus){
		this.bonus = this.bonus * bonus;
	}
	
	// Gets the length of a word
	public int getLengthWord(){
		return word.length();
	}
	
	// Gets the word
	public String getWord(){
		return word;
	}
	
	// Increases the score
	public void increaseScore(int score){
		this.score += score;
	}
	
	// Gets the score
	public int getScore(){
		return score;
	}
}
