package domein;


import gui.MainFrame;
import datalaag.DatabaseHandler;


public class MainClass
	{

		public static void main(String[] args)
			{
				// TODO Auto-generated method stub
				WordFeud wf = new WordFeud();
				wf.startGame(0, false);
				//new MainFrame();
				DatabaseHandler dbh = DatabaseHandler.getInstance();
//				dbh.login("michael", "mans");
			}

	}
