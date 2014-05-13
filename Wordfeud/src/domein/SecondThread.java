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
	private boolean running = true;
	private boolean turnSwap = true;

	public SecondThread(Match match, GameFieldPanel fieldPanel,GameButtonPanel buttonPanel, ScorePanel scorePanel) {
		super("thread");
		this.match = match;
		this.fieldPanel = fieldPanel;
		this.buttonPanel = buttonPanel;
		this.scorePanel = scorePanel;
		this.dbh = DatabaseHandler.getInstance();
	}

	public void run() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		while (running) {
			running = true;
			// Aan match vragen wat de GameID is voor database updates - Dit
			// constant doen
			// Dan is er meer een thread voor alle games
			int gameID = match.getGameID();

			// Hier komt de methode die the naam opvraagt van de tegenstander
			// Geeft de score aan de score panel
			// Idem voor eigen score
			scorePanel.setEnemyScore(dbh.score(gameID, match.getEnemyName()));
			scorePanel.setOwnScore(dbh.score(gameID, match.getOwnName()));

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

			try {
				if (!dbh.getGameStatusValue(gameID).equals("Finished")
						&& !dbh.getGameStatusValue(gameID).equals("Resigned")) {
					match.getMaxTurnID();
					if (match.getMyTurn()) {	
						buttonPanel.setTurn(true);
						if (turnSwap){
							match.updateField();				
							System.out.println("WORDT DIT AANGEROEPEN?");
						}
						turnSwap = false;
					} else {
					//	System.out.println("NIET MIJN BEURT");
						buttonPanel.setTurn(false);
						turnSwap = true;
					}
				} else {
					buttonPanel.setTurn(false);
					buttonPanel.disableSurrender();
					running = false;
				}

				// fieldPanel update wie er aan de beurt is
				// buttonPanel update wie er aan de beurt is
				// gegevens opvragen via match

				// Refreshes the scorePanel
				scorePanel.updatePanel();

			} catch (NullPointerException e) {

			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

}
