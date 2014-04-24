package domein;

public class Word {
	private String word;
	private int score;
	private int bonus;
	
	public Word(){
		this.bonus = 0;
		this.word = "";
	}
	
	public void addWordBonusToScore(){
		if (bonus == 0){
			bonus = 1;
		}
		score = score * bonus;
	}
	
	public void addLetter(String letter){
		word += letter;
	}
	
	public void addWordBonus(int bonus){
		this.bonus = this.bonus + bonus;
	}
	
	public int getLengthWord(){
		return word.length();
	}
	
	public String getWord(){
		return word;
	}
	
	public void increaseScore(int score){
		this.score += score;
	}
	
	public int getScore(){
		return score;
	}
}
