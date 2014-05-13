package domein;

import gui.MainFrame;
import gui.TempFramePanel;
import datalaag.DatabaseHandler;


public class MainClass
	{

		public static void main(String[] args)
			{
				// TODO Auto-generated method stub
				new WordFeud();
				MainFrame mf = new MainFrame();
				DatabaseHandler dbh = DatabaseHandler.getInstance();
//				dbh.login("michael", "mans");
			}

	}
