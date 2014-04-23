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
	final private String url = "jdbc:mysql://databases.aii.avans.nl:3306/manschou_db2"; // location
																						// of
																						// the
																						// database
	final private String user = "manschou"; // is the database user
	final private String userPass = "Mschouten92"; // the password of the user

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
			con = DriverManager.getConnection(url, user, userPass);
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

	public boolean login(String username, String password) // return statement
															// for login
															// in/correct needs
															// to be applied
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
			}
			else
			{
				System.out.println("username or password incorrect");
			}

		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return login;
	}

	public boolean register(String username, String password)// return statement
																// for register
																// in/correct
																// needs to be
																// applied
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
					// Here we create our query where u state which fields u
					// want to insert data
					statement = con
							.prepareStatement("INSERT INTO account(naam, wachtwoord)VALUES(?,?)");
					// // the ? represents anonymous values
					//
					statement.setString(1, username);
					statement.setString(2, password);

					// execute your query
					statement.executeUpdate(); // if there is not result then u
												// use a executeUpdate()

					// closes the statement
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
			// Here we create our query where u state which fields u want to
			// insert data
			statement = con
					.prepareStatement("INSERT INTO chatregel(account_naam, spel_id, tijdstip, bericht)VALUES(?,?,?,?)");
			// // the ? represents anonymous values
			//
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
		
		if(lastMessageTimestamp.equals(""))
		{
			lastMessageTimestamp = "2013-05-05 00:00:00";
		}
		Timestamp oldTimestamp = Timestamp.valueOf(lastMessageTimestamp); // converts String to Timestamp

		try
		{
			statement = con
					.prepareStatement("SELECT account_naam, tijdstip, bericht FROM chatregel WHERE spel_id = '"
							+ gameID + "' AND tijdstip >= '" + oldTimestamp + "' ORDER BY tijdstip ASC");

			result = statement.executeQuery();

			while (result.next())
			{
				String sender = result.getString(1);
				String chatTime = result.getTimestamp(2).toString();
				String message = result.getString(3);
				chat.add(sender + "---" + chatTime + "---" + message);
//				System.out.println("sender: " + sender + " Time: " + chatTime + " message: " + message);
			}

		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("query error!!!!");
		}
		return chat;
	}

	public int createCompetition(String username, String start, String end, String summary) // works
	{
		// convert string to timestamp
		java.sql.Timestamp compStart = java.sql.Timestamp.valueOf(start);
		java.sql.Timestamp compEnd = java.sql.Timestamp.valueOf(end);
		
		int compID =0;
		
		try
		{
			// Here we create our query where u state which fields u want to
			// insert data
			statement = con
					.prepareStatement(
							"INSERT INTO competitie(account_naam_eigenaar, start, einde, omschrijving)VALUES(?,?,?,?)",
							PreparedStatement.RETURN_GENERATED_KEYS);
			// // the ? represents anonymous values
			//
			statement.setString(1, username);
			statement.setTimestamp(2, compStart);
			statement.setTimestamp(3, compEnd);
			statement.setString(4, summary);

			// execute your query
			statement.execute(); // needs to be execute because executeQuery
									// doesn't work with PK retrieval

			result = statement.getGeneratedKeys(); // returns competitionID

			if (result.next())
			{
				compID = result.getInt(1);
				System.out.println("do i get the PK " + compID);

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

	public int createGame(int competitionID, String username, String opponent)// works
	{
		int gameID = 0;
		try
		{
			// Here we create our query where u state which fields u want to
			// insert data
			statement = con
					.prepareStatement(
							"INSERT INTO spel(competitie_id, account_naam_uitdager, account_naam_tegenstander, moment_uitdaging, bord_naam, letterset_naam)VALUES(?,?,?,?,?,?)",
							PreparedStatement.RETURN_GENERATED_KEYS);
			// // the ? represents anonymous values
			//
			statement.setInt(1, competitionID);
			statement.setString(2, username);
			statement.setString(3, opponent);
			statement.setTimestamp(4, getCurrentTimeStamp());
			statement.setString(5, "Standard");
			statement.setString(6, "NL");

			// execute your query
			statement.execute(); // needs to be execute because executeQuery
									// doesn't work with PK retrieval

			result = statement.getGeneratedKeys(); // returns gameID

			if (result.next())
			{
				gameID = result.getInt(1);
				System.out.println("do i get the PK " + gameID);

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
	
	public boolean checkTurn(String username, int gameID)// works
	{
		boolean turn = false;
		
		try
		{
			statement = con.prepareStatement("SELECT * FROM beurt WHERE spel_id = '" + gameID + "' AND account_naam = '" + username + "' AND id = (SELECT max(id) FROM beurt)");
			
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

	public void updateTurn(int gameID, String username,int score, String action)// needs to be tested, probally works
	{
		try 
		{
			statement = con.prepareStatement("SELECT * FROM beurt WHERE spel_id = '" + gameID + "'");
		
			result = statement.executeQuery();
			
			if(result.next())
			{
				try
				{
					// Here we create our query where u state which fields u
					// want to insert data
					statement = con
							.prepareStatement("INSERT INTO beurt(spel_id, account_naam, score, aktie_type)VALUES(?,?,?,?)");
					 // the ? represents anonymous values
					
					statement.setInt(1, gameID);
					statement.setString(2, username);
					statement.setInt(3, score);
					statement.setString(4, action);

					// execute your query
					statement.executeUpdate(); // if there is not result then u
												// use a executeUpdate()

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
	
	public int score(int gameID, String username) //gets the max score of the user
	{
		int score=0;
		
		try
		{
			statement = con.prepareStatement("SELECT sum(score) FROM beurt WHERE account_naam = '"+ username +"' AND spel_id = '" + gameID + "'");
			
			result = statement.executeQuery();
			
			if(result.next())
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
	
	public void addTileToJar(int gameID, String tile)
	{
		try
		{
			statement = con.prepareStatement("");
			
			statement.executeQuery();
			
			
		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("Query Error!!!!");
		}
		
		
		
		
	}
	
}

/*
 * aanmaken door: DatabaseHandler dbh = DatabaseHandler.getInstance();
 * 
 * en aanroepen door: dbh. --------
 * 
 * aangemaakt door: Michael login check register check and register name and
 * password create competition, create game chat send,
 * checkTurn, chat Receive
 * 
 * 
 * 
 * all needs to be checked for the rol of the player
 * to use multiple methods con.close(); needs to be removed. otherwise u get an
 * closed connection error.
 * 
 */
