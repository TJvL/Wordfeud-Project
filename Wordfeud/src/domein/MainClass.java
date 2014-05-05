package domein;


import gui.MainFrame;
import datalaag.DatabaseHandler;


public class MainClass
	{

		public static void main(String[] args)
			{
				DatabaseHandler dbh = DatabaseHandler.getInstance();
				// TODO Auto-generated method stub
				WordFeud wf = new WordFeud();
				int gameID = dbh.createGame(1, "jager684", "marijntje42", "openbaar", "EN");
				System.out.println("DE GAMEID IS " + gameID);
				wf.startGame(gameID, false, true);
				//new MainFrame();
			}

	}
