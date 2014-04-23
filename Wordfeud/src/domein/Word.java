package domein;

public class Word {
	private String word;
	private int score;
	private int bonnus;
	
	public Word(){
		this.bonnus = 0;
	}
	
	public void addLetter(String letter){
		
	}
	
	public void addWordBonnus(int bonnus){
		this.bonnus = bonnus;
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
