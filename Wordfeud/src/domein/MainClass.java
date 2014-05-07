package domein;

import gui.MainFrame;
import datalaag.DatabaseHandler;

import javax.swing.JOptionPane;

public class MainClass {

	public static void main(String[] args) {
		DatabaseHandler dbh = DatabaseHandler.getInstance();
		WordFeud wf = new WordFeud();
		String name = JOptionPane.showInputDialog(
		null, "Please enter your GameID: ");
		if (name == null){
			int gameID = dbh.createGame(1, "jager684", "marijntje42", "openbaar","EN");
			wf.startGame(gameID, false, false);
		} else {
			int gameID = Integer.parseInt(name);
			wf.startGame(gameID, false, true);
		}
	
		// TODO Auto-generated method stub
		
		
		//System.out.println("DE GAMEID IS " + gameID);
	
		// new MainFrame();
	}

}
