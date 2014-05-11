package domein;

import gui.MainFrame;
import datalaag.DatabaseHandler;

import javax.swing.JOptionPane;

public class MainClass {

	public static void main(String[] args) {
		
		WordFeud wf = new WordFeud();
		/*
		DatabaseHandler dbh = DatabaseHandler.getInstance();	
		String name = JOptionPane.showInputDialog(
		null, "Please enter your GameID: ");
		if (name == null){
			int gameID = dbh.createGame(1, "jager684", "marijntje42", "openbaar","EN");
			wf.startGame(gameID, false, true);
		} else if (name.equals("spec")){
			String name2 = JOptionPane.showInputDialog(
					null, "Please enter your GameID: ");
			wf.startGame(Integer.parseInt(name2), true, false);
			
		} else {
			int gameID = Integer.parseInt(name);
			wf.startGame(gameID, false, false);
		}*/
		wf.startGame(511, true, false);
	
		// TODO Auto-generated method stub
		
		
		//System.out.println("DE GAMEID IS " + gameID);
	
		// new MainFrame();
	}

}
