package datalaag;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;



public class DatabaseHandler
{
	final private static String URL = "jdbc:mysql://databases.aii.avans.nl:3306/manschou_db2"; // location of the database
	final private static String USER = "manschou"; // is the database user
	final private static String USERPASS = "Mschouten92"; // the password of the user


	private PreparedStatement statement = null;
	private ResultSet result = null;

	private Connection con;


	private static java.sql.Timestamp getCurrentTimeStamp()
	{
		java.util.Date today = new java.util.Date();
		return new java.sql.Timestamp(today.getTime());

	}

	private static DatabaseHandler databaseHandler = new DatabaseHandler();

	public static DatabaseHandler getInstance()
	{

		return databaseHandler;
	}


	public void changeTurn() {
		// zet de turn naar de tegenstander
	}

	private DatabaseHandler()
	{
		// Accessing driver from the JAR file
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e)
		{
			e.printStackTrace();
			System.out.println("Driver not found");
		}
		connection();
	}

	public void connection()
	{
		// Creating a variable for the connection
		try
		{
			con = DriverManager.getConnection(URL, USER, USERPASS);
		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("kan geen verbinding maken");
		}
	}

	public void closeConnection() // closes the connection to the database
	{
		try
		{
			// closes the connection
			con.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("ERROR in closing connection");
		}
	}

	public boolean login(String username, String password) // return statement for login in/correct needs to be applied
	{
		boolean login = false;
		try
		{
			statement = con
					.prepareStatement("SELECT * FROM account WHERE naam = '"
							+ username + "' AND wachtwoord = '" + password
							+ "'");

			result = statement.executeQuery();

			if (result.next())
			{
				System.out.println("username + password correct");
				login = true;
				result.close();
			}
			else
			{
				System.out.println("username or password incorrect");
			}
			statement.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("Query ERROR!!!!");
		}

		return login;
	}

	public boolean register(String username, String password)// return statement for register in/correct needs to be applied
	{
		boolean registered = false;
		try
		{
			statement = con
					.prepareStatement("SELECT * FROM account WHERE naam = '"
							+ username + "'");

			result = statement.executeQuery();

			if (!result.next())
			{
				System.out.println("username is available");

				try
				{
					// Here we create our query where u state which fields u want to insert data
					statement = con
							.prepareStatement("INSERT INTO account(naam, wachtwoord)VALUES(?,?)");
					// the ? represents anonymous values
					
					statement.setString(1, username);
					statement.setString(2, password);

					// execute your query
					statement.executeUpdate(); // if there is not result then u use a executeUpdate()

					// closes the statement
					statement.close();

					statement = con
							.prepareStatement("INSERT INTO accountrol(account_naam, rol_type)VALUES(?,?)");

					statement.setString(1, username);
					statement.setString(2, "Player");

					statement.executeUpdate();

					statement.close();

				} catch (SQLException e)
				{
					e.printStackTrace();
					System.out.println("account register error");
				}
				registered = true;
			}
			else
			{
				System.out.println("username = not available");
			}

		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("query error!!!");
		}
		return registered;
	}

	public void chatSend(String username, int gameID, String message)// it works
	{
		try
		{
			// Here we create our query where u state which fields u want to insert data
			statement = con
					.prepareStatement("INSERT INTO chatregel(account_naam, spel_id, tijdstip, bericht)VALUES(?,?,?,?)");
			// the ? represents anonymous values

			statement.setString(1, username);
			statement.setInt(2, gameID);
			statement.setTimestamp(3, getCurrentTimeStamp());
			statement.setString(4, message);

			// execute your query
			statement.executeUpdate();

			// closes the statement
			statement.close();

		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("chat message send error");
		}
	}

	public ArrayList<String> chatReceive(int gameID, String lastMessageTimestamp)// need to be return checked!!!
	{
		ArrayList<String> chat = new ArrayList<String>();
		chat.clear();

		if (lastMessageTimestamp.equals(""))
		{
			lastMessageTimestamp = "2013-05-05 00:00:00";
		}
		Timestamp oldTimestamp = Timestamp.valueOf(lastMessageTimestamp); // converts String to Timestamp

		try
		{
			statement = con
					.prepareStatement("SELECT account_naam, tijdstip, bericht FROM chatregel WHERE spel_id = '"
							+ gameID + "' AND tijdstip >= '" + oldTimestamp	+ "' ORDER BY tijdstip ASC");

			result = statement.executeQuery();

			while (result.next())
			{
				String sender = result.getString(1);
				String chatTime = result.getTimestamp(2).toString();
				String message = result.getString(3);
				chat.add(sender + "---" + chatTime + "---" + message);
				// System.out.println("sender: " + sender + " Time: " + chatTime
				// + " message: " + message);
			}

		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("query error!!!!");
		}
		return chat;
	}

	public int createCompetition(String username, String end, String summary, int minParticipants, int maxParticipants) // works
	{
		// convert string to timestamp
		java.sql.Timestamp compEnd = java.sql.Timestamp.valueOf(end);

		int compID = 0;

		try
		{
			// Here we create our query where u state which fields u want to
			// insert data
			statement = con
					.prepareStatement(
							"INSERT INTO competitie(account_naam_eigenaar, start, einde, omschrijving, minimum_aantal_deelnemers, maximum_aantal_deelnemers)VALUES(?,?,?,?,?,?)",
							PreparedStatement.RETURN_GENERATED_KEYS);
			// the ? represents anonymous values

			statement.setString(1, username);
			statement.setTimestamp(2, getCurrentTimeStamp());
			statement.setTimestamp(3, compEnd);
			statement.setString(4, summary);
			statement.setInt(5, minParticipants);
			statement.setInt(6, maxParticipants);

			// execute your query
			statement.execute(); // needs to be execute because executeQuery doesn't work with PK retrieval

			result = statement.getGeneratedKeys(); // returns competitionID

			if (result.next())
			{
				compID = result.getInt(1);
//				System.out.println("do i get the PK " + compID);

				statement = con
						.prepareStatement("INSERT INTO deelnemer(account_naam, competitie_id)VALUES(?,?)");

				statement.setString(1, username);
				statement.setInt(2, compID);
				statement.executeUpdate();
			}

			// closes the statement
			statement.close();

		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("create error");
		}
		return compID;
	}

	public int createGame(int competitionID, String username, String opponent, String privacy, String language)// works
	{
		int gameID = 0;
		try
		{
			// Here we create our query where u state which fields u want to insert data
			statement = con
					.prepareStatement(
							"INSERT INTO spel(competitie_id, account_naam_uitdager, account_naam_tegenstander, moment_uitdaging, bord_naam, letterset_naam, zichtbaarheid_type)VALUES(?,?,?,?,?,?,?)",
							PreparedStatement.RETURN_GENERATED_KEYS);
			// the ? represents anonymous values

			statement.setInt(1, competitionID);
			statement.setString(2, username);
			statement.setString(3, opponent);
			statement.setTimestamp(4, getCurrentTimeStamp());
			statement.setString(5, "Standard");
			statement.setString(6, language);
			statement.setString(7, privacy);

			// execute your query
			statement.execute(); // needs to be execute because executeQuery doesn't work with PK retrieval

			result = statement.getGeneratedKeys(); // returns gameID

			if (result.next())
			{
				gameID = result.getInt(1);
//				System.out.println("do i get the PK " + gameID);
			}

			// closes the statement
			statement.close();

		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("create error");
		}
		return gameID;
	}

	public boolean checkTurn(String username, int gameID)//checks who's turn it is, works
	{
		boolean turn = false;

		try
		{
			statement = con
					.prepareStatement("SELECT * FROM beurt WHERE spel_id = '" + gameID + "' AND account_naam = '" + username
							+ "' AND id = (SELECT max(id) FROM beurt)");

			result = statement.executeQuery();

			if (result.next())
			{
				System.out.println("it's your turn");
				turn = true;
			}
			else
			{
				System.out.println("it's not your turn");
			}

		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("Query ERROR!!!!");
		}
		return turn;
	}

	public void updateTurn(int beurtID, int gameID, String username, int score, String action)// needs to be tested, probally works
	{
		try
		{
			statement = con
					.prepareStatement("SELECT * FROM beurt WHERE spel_id = '" + gameID + "'");

			result = statement.executeQuery();

			if (result.next())
			{
				try
				{
					// Here we create our query where u state which fields u want to insert data
					statement = con
							.prepareStatement("INSERT INTO beurt(id, spel_id, account_naam, score, aktie_type)VALUES(?,?,?,?,?)");
					// the ? represents anonymous values

					statement.setInt(1, beurtID);
					statement.setInt(2, gameID);
					statement.setString(3, username);
					statement.setInt(4, score);
					statement.setString(5, action);

					// execute your query
					statement.executeUpdate(); // if there is not result then u use a executeUpdate()

					// closes the statement
					statement.close();

				} catch (SQLException e)
				{
					e.printStackTrace();
					System.out.println("insert turn ERROR!!!");
				}
			}

		} catch (SQLException e)
		{
			System.out.println("query ERROR!!!");
			e.printStackTrace();
		}
	}

	public int score(int gameID, String username) // gets the max score of the user
	{
		int score = 0;

		try
		{
			statement = con
					.prepareStatement("SELECT sum(score) FROM beurt WHERE account_naam = '"
							+ username + "' AND spel_id = '" + gameID + "'");

			result = statement.executeQuery();

			if (result.next())
			{
				score = result.getInt(1);
			}

		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("Query ERROR!!!");
		}
		return score;
	}

	public void addTileToHand(int gameID, ArrayList<Integer> tile, int turnID)// should work
	{
		try
		{
			for (int i = 0; i < tile.size(); i++)
			{
				statement = con
						.prepareStatement("INSERT INTO letterbakjeletter(spel_id, letter_id, beurt_id)VALUES(?,?,?)");

				statement.setInt(1, gameID);
				statement.setInt(2, tile.get(i));
				statement.setInt(3, turnID);

				statement.executeUpdate();
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("Query Error!!!!");
		}
	}

	public String squareCheck(int cordX, int cordY)// should work
	{
		String squareValue = null;

		try
		{
			statement = con
					.prepareStatement("SELECT tegeltype_soort FROM tegel WHERE x = '"
							+ cordX + "' AND y = '" + cordY + "'");

			result = statement.executeQuery();

			if (result.next())
			{
				squareValue = result.getString(1);
			}

		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("Query ERROR!!!!");
		}

		return squareValue;
	}

	public boolean checkWord(String word)// check works
	{
		boolean validWord = false;

		try
		{
			statement = con
					.prepareStatement("SELECT woord FROM woordenboek WHERE woord = '" + word + "'");

			result = statement.executeQuery();

			if (result.next())
			{
				validWord = true;
			}

		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("Query ERROR!!!");
		}
		return validWord;
	}

	public void gameStatusUpdate(int gameID, String status)// works
	{
		try
		{
			statement = con
					.prepareStatement("UPDATE spel SET toestand_type = '" + status + "' WHERE id ='" + gameID + "'");

			statement.executeUpdate();

		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("QUERY ERROR!!!");
		}
	}

	public String gameStatusValue(int gameID)
	{
		String gameStatus = null;

		try
		{
			statement = con
					.prepareStatement("SELECT toestand_type FROM spel WHERE id ='" + gameID + "'");

			result = statement.executeQuery();

			if (result.next())
			{
				gameStatus = result.getString(1);
			}

		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("Query ERROR!!!");
		}
		return gameStatus;
	}

	public String opponentName(int gameID)
	{
		String opponent = null;

		try
		{
			statement = con
					.prepareStatement("SELECT account_naam_tegenstander FROM spel WHERE id ='" + gameID + "'");

			result = statement.executeQuery();

			if (result.next())
			{
				opponent = result.getString(1);
			}

		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("Query error!!!");
		}
		return opponent;
	}

	public ArrayList<Integer> jarContent(int gameID)
	{
		ArrayList<Integer> jarContents = new ArrayList<Integer>();

		try
		{
			statement = con
					.prepareStatement("SELECT letter_id FROM pot WHERE spel_id ='" + gameID + "'");

			result = statement.executeQuery();

			while (result.next())
			{
				jarContents.add(result.getInt(1));
			}

		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("Query ERROR!!!!");
		}
		return jarContents;
	}

	public String handContent(int gameID, int turnID)
	{
		String handContent = null;

		try
		{
			statement = con
					.prepareStatement("SELECT inhoud FROM plankje WHERE spel_id = '"
							+ gameID + "' AND beurt_id = '" + turnID + "'");

			result = statement.executeQuery();

			if (result.next())
			{
				handContent = result.getString(1);
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("Query ERROR!!");
		}

		return handContent;
	}

	public void tileToBoard(int gameID, int turnID, int tileID,	String blankTile, int cordX, int cordY)
	{
		try
		{
			statement = con
					.prepareStatement("INSERT INTO gelegdeletter(letter_id, spel_id, beurt_id, tegel_x, tegel_y, tegel_bord_naam, blancoletterkarakter)VALUES(?,?,?,?,?,?,?");

			statement.setInt(1, tileID);
			statement.setInt(1, gameID);
			statement.setInt(2, turnID);
			statement.setInt(4, cordX);
			statement.setInt(5, cordY);
			statement.setString(6, "Standard");

			if (blankTile != null)
			{
				statement.setString(7, blankTile);
			}

			statement.executeUpdate();

		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("Query ERROR!!!");
		}
	}

	public void surrender(int gameID, int turnID, String username)// used to surrender the game
	{
		try
		{
			statement = con
					.prepareStatement("SELECT * FROM beurt WHERE spel_id ='"
							+ gameID + "'");

			result = statement.executeQuery();

			if (result.next())
			{
				statement = con
						.prepareStatement("INSERT INTO beurt(id, spel_id, account_naam, score, aktie_type) VALUES(?,?,?,?,?)");

				statement.setInt(1, turnID);
				statement.setInt(2, gameID);
				statement.setString(3, username);
				statement.setInt(4, 0);
				statement.setString(5, "End");

				statement.executeUpdate();
				statement.close();
			}
			
			statement = con.prepareStatement("SELECT * FROM spel WHERE id = '"
					+ gameID + "'");

			result = statement.executeQuery();

			if (result.next())
			{
				statement = con
						.prepareStatement("UPDATE spel SET toestand_type = 'Resigned' WHERE toestand_type = 'Playing'");

				statement.executeUpdate();
				statement.close();
			}

		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("Query ERROR!!!");
		}
	}

	public ArrayList<String> competitionOwner(String username)// seems to work
	{
		ArrayList<String> myCompetitions = new ArrayList<String>();

		try
		{
			statement = con
					.prepareStatement("SELECT id, start, einde, omschrijving, minimum_aantal_deelnemers, maximum_aantal_deelnemers FROM competitie");

			result = statement.executeQuery();

			while (result.next())
			{
				int compID = result.getInt(1);
				String startTime = result.getTimestamp(2).toString();
				String endTime = result.getTimestamp(3).toString();
				String summary = result.getString(4);
				int minParticipants = result.getInt(5);
				int maxParticipants = result.getInt(6);

				String myComps = compID + "---" + startTime + "---" + endTime
						+ "---" + summary + "---" + minParticipants + "---" + maxParticipants;
				myCompetitions.add(myComps);

			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return myCompetitions;
	}

	public ArrayList<String> competitionParticipant(String username)//seems to work
	{
		ArrayList<String> myParticipations = new ArrayList<String>();

		try
		{
			statement = con
					.prepareStatement("SELECT d.account_naam AS me, c.id AS compID, c.account_naam_eigenaar AS competition_Owner, c.start, c.einde, c.omschrijving FROM deelnemer AS d RIGHT JOIN competitie AS c ON d.competitie_id = c.id WHERE d.account_naam = '"
							+ username + "' AND c.account_naam_eigenaar NOT Like '" + username + "'");
			
			result = statement.executeQuery();
			
			while(result.next())
			{
				String user = result.getString(1);
				int compID = result.getInt(2);
				String owner = result.getString(3);
				String startTime = result.getTimestamp(4).toString();
				String endTime = result.getTimestamp(5).toString();
				String summary = result.getString(6);
				
				String participaticonRow = user + "---" + compID + "---" + owner + "---" + startTime + "---" + endTime + "---" + summary;
				myParticipations.add(participaticonRow);
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("Query ERROR!!!!");
		}
		return myParticipations;
	}

	public ArrayList<String> createPot(int gameID, String language)
	{
		ArrayList<String> newPot = new ArrayList<String>();
		int numOfTiles = 0;
		String letter = null;
		
		try
		{
			statement = con.prepareStatement("SELECT karakter, aantal FROM lettertype WHERE letterset_code ='" + language + "'");
			
			result = statement.executeQuery();
			
			while(result.next())
			{
				numOfTiles = result.getInt(2);
				for(int x = 0; x < numOfTiles; x++)
				{
					letter = result.getString(1);
					newPot.add(letter);
					
				}
				System.out.println(letter + "---" + numOfTiles);
//				System.out.println(newPot.size());
			}
			
			for(int i =0; i < newPot.size(); i++)
			{
				statement = con.prepareStatement("INSERT INTO letter(id, spel_id, lettertype_letterset_code, lettertype_karakter)VALUES(?,?,?,?)");
				
				statement.setInt(1, (i));
				statement.setInt(2, gameID);
				statement.setString(3, language);
				statement.setString(4, newPot.get(i));
				
				statement.executeUpdate();
			}
			
		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("Query ERROR!!!!");
		}
		return newPot;
	}

	public ArrayList<String> playedWords(int gameID)// works
	{
		ArrayList<String> playedWord = new ArrayList<String>();
		String word = null;
		String xPos = null;
		String yPos = null;
		
		try
		{
			statement = con.prepareStatement("SELECT * FROM gelegd");
		
			result = statement.executeQuery();
			
			while(result.next())
			{
				word = result.getString(3);
				xPos = result.getString(4);
				yPos = result.getString(5);
				
				playedWord.add(word + "---" + xPos + "---" + yPos);
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("Query ERROR!!!!");
		}
		return playedWord;
	}

	public int getTurn(int gameID, String username)
	{
		int turn = 0;
		
		try
		{
			statement = con.prepareStatement("SELECT max(id) FROM beurt WHERE spel_id = '" + gameID + "' AND account_naam = '" + username + "'");
			
			result = statement.executeQuery();
			
			if(result.next())
			{
				turn = result.getInt(1);
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("QUERY ERROR!!!");
		}
		return turn;
	}

	public void joinCompetition(int compID, String username)
	{
		try
		{
			statement = con.prepareStatement("INSERT INTO deelnemer(account_naam, competitie_id)VALUES(?,?)");
			
			statement.setString(1, username);
			statement.setInt(2, compID);
			
			statement.executeUpdate();
			
			statement.close();
			
		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("Query ERROR!!!!");
		}
	}

	public ArrayList<String> peopleInCompetition()
	{
		ArrayList<String> numOfPeopleInCompetition = new ArrayList<String>();
		int compID = 0;
		int numOfPeople = 0;
		
		try
		{
			statement = con.prepareStatement("SELECT distinct(competitie_id), count(account_naam) FROM deelnemer");
			
			result = statement.executeQuery();
			
			while(result.next())
			{
				compID = result.getInt(1);
				numOfPeople = result.getInt(2);
				
				numOfPeopleInCompetition.add(compID + "---" + numOfPeople);
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("QUERRY ERROR!!!!");
		}
		return numOfPeopleInCompetition;
	}

}

/*
 * aanmaken door: DatabaseHandler dbh = DatabaseHandler.getInstance();
 * 
 * en aanroepen door: dbh. --------
 * 
 * aangemaakt door: Michael login check register check and register name and
 * password create competition, create game chat send, checkTurn, chat Receive
 * jarcontent
 * 
 * 
 * to use multiple methods con.close(); needs to be removed. otherwise u get an closed connection error. 
 * all needs to be checked for the rol of the player to use multiple methods
 */
