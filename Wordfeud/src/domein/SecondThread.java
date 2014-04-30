package domein;

import datalaag.DatabaseHandler;
import gui.GameButtonPanel;
import gui.GameFieldPanel;
import gui.ScorePanel;

// Thread for updating ScorePanel
// Updating active buttons in the ButtonPanel
// Updating ChatPanel
// Checks who's turn it is for the GameFieldPanel
public class SecondThread extends Thread {
	private Match match;
	private GameFieldPanel fieldPanel;
	private GameButtonPanel buttonPanel;
	private DatabaseHandler dbh;
	private ScorePanel scorePanel;
	private int storeScore;

	public SecondThread(Match match, GameFieldPanel fieldPanel,
			GameButtonPanel buttonPanel, ScorePanel scorePanel) {
		super("thread");
		this.match = match;
		this.fieldPanel = fieldPanel;
		this.buttonPanel = buttonPanel;
		this.scorePanel = scorePanel;
		this.dbh = DatabaseHandler.getInstance();
	}

	public void run() {
		int counter = 0;
		while (true) {

			// Aan match vragen wat de GameID is voor database updates - Dit
			// constant doen
			// Dan is er meer een thread voor alle games

			// Hier komt de methode die the naam opvraagt van de tegenstander
			// Geeft de score aan de score panel
			// Idem voor eigen score
			counter++;
			scorePanel.setOwnScore(counter);

			// Hier komt een methode die constant de beurt opvraagd
			// Als je aan de beurt bent dan op true zetten
			// De beurt knoppen worden dan gedisabled
			// Else op false zetten

			// Bij het veranderen van de beurt - WordValue resetten
			/*
			 * if (counter > 5){ buttonPanel.setTurn(false); } else {
			 * buttonPanel.setTurn(true); }
			 */

			// Method die kijkt of er al 72 uur geen woord is gespeeld
			// Zoja - spel op non actief zetten
			// Waarschuwing of iets dergelijks geven

			if (match.getSubmittedWord() != "*") {
				// Checken database met het woord van match.getSubmittedWord();
				// if (goedgekeurd - woord toevoegen aan database
				System.out.println("Langs geweest");
				match.checkWord();
			}

			// Prints the current wordValue
			int currentScore = match.getScore();
			if (storeScore > currentScore || storeScore < currentScore) {
				scorePanel.setWordValue(currentScore);
				storeScore = currentScore;
			}

			// fieldPanel update wie er aan de beurt is
			// buttonPanel update wie er aan de beurt is
			// gegevens opvragen via match

			// Refreshes the scorePanel
			scorePanel.updatePanel();

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
