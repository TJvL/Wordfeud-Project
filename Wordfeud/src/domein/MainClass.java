package domein;

import gui.TempFramePanel;
import datalaag.DatabaseHandler;

	
public class MainClass
	{
		static TestUserClass testUser1 = new TestUserClass("Tomboy19");
		static TestUserClass testUser2 = new TestUserClass("Dannyboi54");
		public static void main(String[] args) {
				Chat chat = new Chat(testUser1,testUser2,558);
			}

	}
