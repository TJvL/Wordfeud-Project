package domein;

import gui.GameButtonPanel;
import gui.GameFieldPanel;

// Thread for updating ScorePanel
// Updating active buttons in the ButtonPanel
// Updating ChatPanel
// Checks who's turn it is for the GameFieldPanel
public class SecondThread extends Thread {
	private Match match;
	private GameFieldPanel fieldPanel;
	private GameButtonPanel buttonPanel;

	public SecondThread(Match match, GameFieldPanel fieldPanel,
			GameButtonPanel buttonPanel) {
		super("thread");
		this.match = match;
		this.fieldPanel = fieldPanel;
		this.buttonPanel = buttonPanel;
	}

	public void run() {
		while (true) {
			System.out.println("Test");
			
			// fieldPanel update wie er aan de beurt is
			// buttonPanel update wie er aan de beurt is
			// gegevens opvragen via match
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
