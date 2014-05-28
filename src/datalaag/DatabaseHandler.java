package datalaag;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

//import javax.swing.plaf.basic.BasicInternalFrameTitlePane.RestoreAction; who added this

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

	private DatabaseHandler()
	{
		// Accessing driver from the JAR file
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e)
		{
			// e.printStackTrace();
			System.out.println("Driver not found");
		}
	}

	private synchronized void connection()
	{
		int retry = 0;
		boolean connected = false;
		while (!connected && retry < 100)
		{
			// Creating a variable for the connection
			try
			{
				con = DriverManager.getConnection(URL, USER, USERPASS);
				// System.out.println("verbonden");
				connected = true;
			} catch (SQLException e)
			{
				// e.printStackTrace();
				retry++;
				System.out.println("kan geen verbinding maken");
			}
		}
	}

	private synchronized void closeConnection() // closes the connection to the
												// database
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

	public synchronized boolean checkLoginInfo(String username, String password)
	{
		connection();
		boolean isLoggedIn = false;
		try
		{
			statement = con.prepareStatement("SELECT * FROM account WHERE naam = '" + username + "' AND wachtwoord = '"
					+ password + "'");
			result = statement.executeQuery();
	
			if (result.next())
			{
				if(result.getString(1).equals(username) && result.getString(2).equals(password)){
					isLoggedIn = true;
				}
				result.close();
			}
			statement.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("Query ERROR!!!!");
		} finally
		{
			closeConnection();
		}
		return isLoggedIn;
	}

	public synchronized String register(String username, String password)// return
																			// statement
																			// for
																			// register
																			// in/correct
																			// needs
																			// to
																			// be
																			// applied
	{
		connection();
		String registered = "Can not register account";
		try
		{
			statement = con.prepareStatement("SELECT * FROM account WHERE naam = '" + username + "'");

			result = statement.executeQuery();

			if (!result.next())
			{
				registered = "username is available, account is registered";
				result.close();
				statement.close();

				try
				{
					// Here we create our query where u state which fields u
					// want to insert data
					statement = con.prepareStatement("INSERT INTO account(naam, wachtwoord)VALUES(?,?)");
					// the ? represents anonymous values

					statement.setString(1, username);
					statement.setString(2, password);

					// execute your query
					statement.executeUpdate(); // if there is not result then u
												// use a executeUpdate()

					// closes the statement
					statement.close();

					statement = con.prepareStatement("INSERT INTO accountrol(account_naam, rol_type)VALUES(?,?)");

					statement.setString(1, username);
					statement.setString(2, "Player");

					statement.executeUpdate();

					statement.close();

				} catch (SQLException e)
				{
					e.printStackTrace();
					System.out.println("account register error");
				}
			}
			else
			{
				registered = "username is not available, cannot register your account";
				statement.close();
			}

		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("query error!!!");
		} finally
		{
			closeConnection();
		}
		return registered;
	}

	public synchronized void chatSend(String username, int gameID, String message)// it
																					// works
	{
		connection();
		boolean canSend = false;
		try
		{
			statement = con.prepareStatement("SELECT account_naam_uitdager, account_naam_tegenstander FROM spel WHERE spel_id = '" + gameID + "'");
			
			result = statement.executeQuery();
			
			if(result.next())
			{
				if(result.getString(1).equals(username) || result.getString(2).equals(username))
				{
					canSend = true;
				}
			}
			result.close();
			statement.close();
			
			if(canSend)
			{
				// Here we create our query where u state which fields u want to
				// insert data
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
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("chat message send error");
		} finally
		{
			closeConnection();
		}
	}

	public synchronized ArrayList<String> chatReceive(int gameID, String lastMessageTimestamp)// need
																								// to
																								// be
																								// return
																								// checked!!!
	{
		connection();
		ArrayList<String> chat = new ArrayList<String>();
		chat.clear();

		if (lastMessageTimestamp.equals(""))
		{
			lastMessageTimestamp = "2013-05-05 00:00:00";
		}
		Timestamp oldTimestamp = Timestamp.valueOf(lastMessageTimestamp); // converts
																			// String
																			// to
																			// Timestamp

		try
		{
			statement = con.prepareStatement("SELECT account_naam, tijdstip, bericht FROM chatregel WHERE spel_id = '"
					+ gameID + "' AND tijdstip >= '" + oldTimestamp + "' ORDER BY tijdstip ASC");

			result = statement.executeQuery();

			while (result.next())
			{
				chat.add(result.getString(1) + "---" + result.getTimestamp(2).toString() + "---" + result.getString(3));
				// System.out.println("sender: " + sender + " Time: " + chatTime
				// + " message: " + message);
			}
			result.close();
			statement.close();

		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("query error!!!!");
		} finally
		{
			closeConnection();
		}
		return chat;
	}

	public synchronized int createCompetition(String username, String end, String summary, int minParticipants, int maxParticipants) // works
	{
		connection();
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
			statement.execute(); // needs to be execute because executeQuery
									// doesn't work with PK retrieval

			result = statement.getGeneratedKeys(); // returns competitionID

			if (result.next())
			{
				compID = result.getInt(1);
				// System.out.println("do i get the PK " + compID);

				statement = con.prepareStatement("INSERT INTO deelnemer(account_naam, competitie_id)VALUES(?,?)");

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
		} finally
		{
			closeConnection();
		}
		return compID;
	}

	public synchronized int createGame(int competitionID, String username, String opponent, String privacy,
			String language)// works
	{
		connection();
		int gameID = 0;
		try
		{
			// Here we create our query where u state which fields u want to
			// insert data
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
			statement.execute(); // needs to be execute because executeQuery
									// doesn't work with PK retrieval

			result = statement.getGeneratedKeys(); // returns gameID

			if (result.next())
			{
				gameID = result.getInt(1);
				// System.out.println("do i get the PK " + gameID);
			}
			result.close();
			// closes the statement
			statement.close();

			statement = con.prepareStatement("INSERT INTO beurt(id, spel_id, account_naam, aktie_type)VALUES(?,?,?,?)");

			statement.setInt(1, 1);
			statement.setInt(2, gameID);
			statement.setString(3, username);
			statement.setString(4, "Begin");

			statement.executeUpdate();

			statement.close();

		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("create error");
		} finally
		{
			closeConnection();
		}
		return gameID;
	}

	public synchronized String checkTurn(String username, int gameID)// checks
																		// who's
																		// turn
																		// it
																		// is,
																		// works
	{
		connection();
		String turn = null;
		boolean myTurn = false;
		int turnID = 0;

		try
		{
			statement = con.prepareStatement("SELECT account_naam, id FROM beurt WHERE spel_id = '" + gameID
					+ "' ORDER BY id DESC LIMIT 1");

			result = statement.executeQuery();

			if (result.next())
			{
				if (result.getString(1).equals(username))
				{
					// System.out.println("it's not your turn");
					turnID = result.getInt(2);
				}
				else
				{
					// System.out.println("it's your turn");
					myTurn = true;
					turnID = result.getInt(2) + 1;
				}
				turn = myTurn + "---" + turnID;
			}

		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("Query ERROR!!!!");
		} finally
		{
			closeConnection();
		}
		return turn;
	}

	public synchronized void updateTurn(int turnID, int gameID, String username, int score, String action)// needs
																											// to
																											// be
																											// tested,
																											// probally
																											// works
	{
		connection();
		try
		{
			// Here we create our query where u state which fields u want to
			// insert data
			statement = con
					.prepareStatement("INSERT INTO beurt(id, spel_id, account_naam, score, aktie_type)VALUES(?,?,?,?,?)");
			// the ? represents anonymous values

			statement.setInt(1, turnID);
			statement.setInt(2, gameID);
			statement.setString(3, username);
			statement.setInt(4, score);
			statement.setString(5, action);

			// execute your query
			statement.executeUpdate(); // if there is not result then u use a
										// executeUpdate()

			// closes the statement
			statement.close();

		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("insert turn ERROR!!!");
		} finally
		{
			closeConnection();
		}
	}

	public synchronized int score(int gameID, String username) // gets the max
																// score of the
																// user
	{
		connection();
		int score = 0;

		try
		{
			statement = con.prepareStatement("SELECT totaalscore FROM score WHERE account_naam = '" + username
					+ "' AND spel_id = '" + gameID + "'");

			result = statement.executeQuery();

			if (result.next())
			{
				score = result.getInt(1);
			}
			result.close();
			statement.close();

		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("Query ERROR!!!");
		} finally
		{
			closeConnection();
		}
		return score;
	}

	public synchronized void addTileToHand(int gameID, ArrayList<Integer> tile, int turnID)// should
																							// work
	{
		connection();
		try
		{
			statement = con
					.prepareStatement("INSERT INTO letterbakjeletter(spel_id, letter_id, beurt_id)VALUES(?,?,?)");
			for (int i = 0; i < tile.size(); i++)
			{
				statement.setInt(1, gameID);
				statement.setInt(2, tile.get(i));
				statement.setInt(3, turnID);

				statement.addBatch();
			}
			statement.executeBatch();

			statement.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("Query Error!!!!");
		} finally
		{
			closeConnection();
		}
	}

	public synchronized ArrayList<String> squareCheck()// should work
	{
		connection();
		ArrayList<String> squareValue = new ArrayList<String>();

		try
		{
			statement = con.prepareStatement("SELECT x, y, tegeltype_soort FROM tegel ORDER BY y ASC, x ASC");

			result = statement.executeQuery();

			while (result.next())
			{
				squareValue.add(result.getInt(1) + "---" + result.getInt(2) + "---" + result.getString(3));
			}
			result.close();
			statement.close();

		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("Query ERROR!!!!");
		} finally
		{
			closeConnection();
		}
		return squareValue;
	}

	public synchronized boolean checkWord(String word, String language)// check
																		// works
	{
		connection();
		boolean validWord = false;

		try
		{
			statement = con.prepareStatement("SELECT woord FROM woordenboek WHERE woord = '" + word
					+ "' AND letterset_code = '" + language + "' AND status = 'accepted'");

			result = statement.executeQuery();

			if (result.next())
			{
				validWord = true;
			}
			result.close();
			statement.close();

		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("Query ERROR!!!");
		} finally
		{
			closeConnection();
		}
		return validWord;
	}

	public synchronized void gameStatusUpdate(int gameID, String status)// works
	{
		connection();
		try
		{
			statement = con.prepareStatement("UPDATE spel SET toestand_type = '" + status + "' WHERE id ='" + gameID
					+ "'");

			statement.executeUpdate();

			statement.close();

		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("QUERY ERROR!!!");
		} finally
		{
			closeConnection();
		}
	}

	public synchronized String getGameStatusValue(int gameID)
	{
		connection();
		String gameStatus = null;

		try
		{
			statement = con.prepareStatement("SELECT toestand_type FROM spel WHERE id ='" + gameID + "'");

			result = statement.executeQuery();

			if (result.next())
			{
				gameStatus = result.getString(1);
			}
			result.close();
			statement.close();

		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("Query ERROR!!!");
		} finally
		{
			closeConnection();
		}
		return gameStatus;
	}

	public synchronized String opponentName(int gameID)
	{
		connection();

		String players = null;
		try
		{
			statement = con
					.prepareStatement("SELECT account_naam_uitdager, account_naam_tegenstander FROM spel WHERE id ='"
							+ gameID + "'");

			result = statement.executeQuery();

			if (result.next())
			{
				players = result.getString(1) + "---" + result.getString(2);
			}
			result.close();
			statement.close();

		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("Query error!!!");
		} finally
		{
			closeConnection();
		}
		return players;
	}

	public synchronized ArrayList<String> jarContent(int gameID)// think it
																// works, dont
																// know if it
																// gets the
																// jokers
	{
		connection();
		ArrayList<String> jarContent = new ArrayList<String>();

		try
		{
			statement = con
					.prepareStatement("SELECT p.letter_id, lt.karakter, lt.waarde FROM pot AS p LEFT JOIN letter AS l ON p.spel_id = l.spel_id AND p.letter_id = l.id LEFT JOIN lettertype AS lt ON lt.karakter = l.lettertype_karakter AND l.lettertype_letterset_code = lt.letterset_code WHERE p.spel_id = '"
							+ gameID + "'");

			result = statement.executeQuery();

			while (result.next())
			{
				jarContent.add(result.getInt(1) + "---" + result.getString(2) + "---" + result.getInt(3));
			}
			result.close();
			statement.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("Query ERROR!!!!");
		} finally
		{
			closeConnection();
		}
		return jarContent;
	}

	public synchronized ArrayList<String> handContent(int gameID, int turnID)
	{
		connection();
		ArrayList<String> handContent = new ArrayList<String>();

		try
		{
			statement = con
					.prepareStatement("SELECT lbl.letter_id, lt.karakter, lt.waarde FROM letterbakjeletter AS lbl LEFT JOIN letter AS l ON lbl.spel_id = l.spel_id AND lbl.letter_id = l.id LEFT JOIN lettertype AS lt ON lt.karakter = l.lettertype_karakter AND l.lettertype_letterset_code = lt.letterset_code WHERE lbl.spel_id = '"
							+ gameID + "' AND lbl.beurt_id = '" + turnID + "'");

			result = statement.executeQuery();

			while (result.next())
			{
				handContent.add(result.getInt(1) + "---" + result.getString(2) + "---" + result.getInt(3));
			}
			result.close();
			statement.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("Query ERROR!!");
		} finally
		{
			closeConnection();
		}
		return handContent;
	}

	public synchronized void tileToBoard(int gameID, int turnID, int tileID, String blankTile, int cordX, int cordY)
	{
		connection();
		try
		{
			statement = con
					.prepareStatement("INSERT INTO gelegdeletter(letter_id, spel_id, beurt_id, tegel_x, tegel_y, tegel_bord_naam, blancoletterkarakter)VALUES(?,?,?,?,?,?,?)");

			statement.setInt(1, tileID);
			statement.setInt(2, gameID);
			statement.setInt(3, turnID);
			statement.setInt(4, cordX);
			statement.setInt(5, cordY);
			statement.setString(6, "Standard");

			if (blankTile != null)
			{
				statement.setString(7, blankTile);
			}
			else
			{
				statement.setString(7, null);
			}

			statement.executeUpdate();

			statement.close();

		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("Query ERROR!!!");
		} finally
		{
			closeConnection();
		}
	}

	public synchronized void surrender(int gameID, int turnID, String username, String opponent)// used to surrender the game
	{
		connection();
		try
		{
			statement = con
					.prepareStatement("INSERT INTO beurt(id, spel_id, account_naam, score, aktie_type) VALUES(?,?,?,?,?)");

			statement.setInt(1, turnID);
			statement.setInt(2, gameID);
			statement.setString(3, username);
			statement.setInt(4, 0);
			statement.setString(5, "Resign");

			statement.executeUpdate();
			statement.close();
			
			statement = con.prepareStatement("INSERT INTO beurt(id, spel_id, account_naam, score, aktie_type) VALUES(?,?,?,?,?)");
			
			statement.setInt(1, turnID + 1);
			statement.setInt(2, gameID);
			statement.setString(3, opponent);
			statement.setInt(4, 0);
			statement.setString(5, "End");

			statement.executeUpdate();
			statement.close();
			
			statement = con.prepareStatement("UPDATE spel SET toestand_type = 'Resigned' WHERE id = '"	+ gameID + "' AND toestand_type = 'Playing'");

			statement.executeUpdate();
			statement.close();

		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("Query ERROR!!!");
		}
		finally{ closeConnection();}
	}

	public synchronized void win(int gameID, int turnID, String username, int score, String opponent)
	{
		connection();
		try
		{

			statement = con.prepareStatement("INSERT INTO beurt(id, spel_id, account_naam, score, aktie_type) VALUES(?,?,?,?,?)");

			statement.setInt(1, turnID);
			statement.setInt(2, gameID);
			statement.setString(3, username);
			statement.setInt(4, score);
			statement.setString(5, "End");

			statement.executeUpdate();
			statement.close();
			
			statement = con.prepareStatement("INSERT INTO beurt(id, spel_id, account_naam, score, aktie_type) VALUES(?,?,?,?,?)");
			
			statement.setInt(1, turnID + 1);
			statement.setInt(2, gameID);
			statement.setString(3, opponent);
			statement.setInt(4, 0);
			statement.setString(5, "End");

			statement.executeUpdate();
			statement.close();

			statement = con.prepareStatement("UPDATE spel SET toestand_type = 'Finished' WHERE id = '" + gameID + "' AND toestand_type = 'Playing'");

			statement.executeUpdate();
			statement.close();

		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("Query ERROR!!!");
		}
		finally{ closeConnection();}
	}

	public synchronized ArrayList<String> fetchJoinedCompetitions(String username)
	{
		connection();
		ArrayList<String> myCompetitions = new ArrayList<String>();

		try
		{
			statement = con
					.prepareStatement("SELECT id, account_naam_eigenaar, `start`, einde, omschrijving, minimum_aantal_deelnemers, maximum_aantal_deelnemers FROM competitie LEFT JOIN deelnemer ON competitie.id = deelnemer.competitie_id WHERE deelnemer.account_naam LIKE '"
							+ username + "'");

			result = statement.executeQuery();

			while (result.next())
			{
				myCompetitions.add(result.getInt(1) + "---" + result.getString(2) + "---"
						+ result.getTimestamp(3).toString() + "---" + result.getTimestamp(4).toString() + "---"
						+ result.getString(5) + "---" + result.getInt(6) + "---" + result.getInt(7));
			}
			result.close();
			statement.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
		} finally{closeConnection();}
		return myCompetitions;
	}
	
	public synchronized ArrayList<String> fetchAllCompetitions(String username){
		connection();
		ArrayList<String> competitions = new ArrayList<String>();

		try
		{
			statement = con
					.prepareStatement("SELECT competitie.id, competitie.account_naam_eigenaar, competitie.`start`, competitie.einde, competitie.omschrijving, competitie.minimum_aantal_deelnemers, competitie.maximum_aantal_deelnemers FROM competitie WHERE competitie.id NOT IN ( SELECT competitie.id FROM competitie LEFT JOIN deelnemer ON competitie.id = deelnemer.competitie_id WHERE deelnemer.account_naam LIKE '" + username + "' )");

			result = statement.executeQuery();

			while (result.next())
			{
				competitions.add(result.getInt(1) + "---" + result.getString(2) + "---"
						+ result.getTimestamp(3).toString() + "---" + result.getTimestamp(4).toString() + "---"
						+ result.getString(5) + "---" + result.getInt(6) + "---" + result.getInt(7));
			}
			result.close();
			statement.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
		} finally
		{
			closeConnection();
		}
		return competitions;
	}
	
	public synchronized void createJar(int gameID, String language)
	{
		connection();

		ArrayList<String> newPot = new ArrayList<String>();
		int numOfTiles = 0;
		String letter = null;

		try
		{
			statement = con.prepareStatement("SELECT karakter, aantal FROM lettertype WHERE letterset_code ='"
					+ language + "'");

			result = statement.executeQuery();

			while (result.next())
			{
				numOfTiles = result.getInt(2);
				for (int x = 0; x < numOfTiles; x++)
				{
					letter = result.getString(1);
					newPot.add(letter);

				}
				// System.out.println(letter + "---" + numOfTiles);
				// System.out.println(newPot.size());
			}
			result.close();
			statement.close();

			statement = con
					.prepareStatement("INSERT INTO letter(id, spel_id, lettertype_letterset_code, lettertype_karakter)VALUES(?,?,?,?)");

			for (int i = 0; i < newPot.size(); i++)
			{

				statement.setInt(1, (i + 1));
				statement.setInt(2, gameID);
				statement.setString(3, language);
				statement.setString(4, newPot.get(i));

				statement.addBatch();
			}
			statement.executeBatch();
			statement.close();

		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("Query ERROR!!!!");
		} finally
		{
			closeConnection();
		}
	}
	
	public synchronized ArrayList<String> playedWords(int gameID)// works
	{
		connection();
		ArrayList<String> playedWord = new ArrayList<String>();

		try
		{
			statement = con.prepareStatement("SELECT * FROM gelegd WHERE spel_id = '" + gameID + "'");

			result = statement.executeQuery();

			while (result.next())
			{
				playedWord.add(result.getString(3) + "---" + result.getString(4) + "---" + result.getString(5) + "---"
						+ result.getString(2));
			}
			result.close();
			statement.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("Query ERROR!!!!");
		} finally
		{
			closeConnection();
		}
		return playedWord;
	}
	
	public synchronized void joinCompetition(int compID, String username)
	{
		connection();
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
		} finally
		{
			closeConnection();
		}
	}
	
	public synchronized String requestWord(String word, String language)
	{
		connection();
		String request = null;
		try
		{
			statement = con.prepareStatement("SELECT * FROM woordenboek WHERE woord = '" + word + "' AND letterset_code = '" + language + "'");
			
			result = statement.executeQuery();
			
			if(result.next()){
				if(result.getString(3).equals("Accepted"))
				{
					request = "The word: " + word + " is already in the dictionary";
				}
				else if(result.getString(3).equals("Denied"))
				{
					request = "The word: " + word + " is already been denied from entering the dictionary";
				}
				else
				{
					request = "The word: " + word + " is already pending acceptence ";
				}
			}
			else
			{
				statement = con.prepareStatement("INSERT INTO woordenboek(woord, letterset_code, status)VALUES(?,?,?)");

				statement.setString(1, word);
				statement.setString(2, language);
				statement.setString(3, "Pending");

				statement.executeUpdate();
				statement.close();
				request = "Your word: " + word + " is now pending acceptence";
			}
			
		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("QUERY ERROR!!!!!");
		} finally{closeConnection();}
		return request;
	}
	
	public synchronized void acceptDeniedWord(String word, String language, String status)
	{
		connection();
		try
		{
			statement = con.prepareStatement("UPDATE woordenboek SET status = '" + status + "' WHERE woord = '" + word
					+ "' AND letterset_code = '" + language + "'");

			statement.executeUpdate();
			statement.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("QUERY ERROR!!!");
		} finally
		{
			closeConnection();
		}
	}
	
	public synchronized ArrayList<String> pendingWords()
	{
		connection();
		ArrayList<String> pendingWord = new ArrayList<String>();

		try
		{
			statement = con.prepareStatement("SELECT woord, letterset_code FROM woordenboek WHERE status = 'Pending'");

			result = statement.executeQuery();

			while (result.next())
			{
				pendingWord.add(result.getString(1) + "---" + result.getString(2));
			}
			result.close();
			statement.close();

		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("QUERRY ERROR!!!!");
		} finally
		{
			closeConnection();
		}
		return pendingWord;
	}
	
	public synchronized void setRole(String username, String role)
	{
		connection();
		try
		{
			statement = con.prepareStatement("INSERT INTO accountrol(account_naam, rol_type)VALUES(?,?)");

			statement.setString(1, username);
			statement.setString(2, role);

			statement.executeUpdate();
			statement.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("QUERRY ERROR!!!");
		} finally
		{
			closeConnection();
		}
	}
	
	public synchronized ArrayList<String> getCurrentUserRole(String username)
	{
		connection();
		ArrayList<String> userRoles = new ArrayList<String>();

		try
		{
			statement = con.prepareStatement("SELECT rol_type FROM accountrol WHERE account_naam = '" + username
					+ "' ORDER BY rol_type ASC");

			result = statement.executeQuery();

			while (result.next())
			{
				userRoles.add(result.getString(1));
			}
			result.close();
			statement.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("QUERRY ERRROR!!!!");
		} finally
		{
			closeConnection();
		}
		return userRoles;
	}
	
	public synchronized void revokeRole(String username, String role)
	{
		connection();
		try
		{
			statement = con.prepareStatement("DELETE FROM accountrol WHERE account_naam = '" + username
					+ "' AND rol_type = '" + role + "'");

			statement.executeUpdate();
			statement.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("QUERRY ERROR!!!");
		} finally
		{
			closeConnection();
		}
	}
	
	public synchronized void acceptRejectGame(int turnID, int gameID, String username, String reaction)
	{
		connection();
		try
		{
			if (reaction.equalsIgnoreCase("Accepted"))
			{
				statement = con
						.prepareStatement("UPDATE spel SET toestand_type = 'Request', reaktie_type = 'Accepted', moment_reaktie = '"
								+ getCurrentTimeStamp() + "' WHERE id = '" + gameID + "'");

				statement.executeUpdate();
				statement.close();

				statement = con
						.prepareStatement("INSERT INTO beurt(id, spel_id, account_naam, aktie_type)VALUES(?,?,?,?)");

				statement.setInt(1, turnID);
				statement.setInt(2, gameID);
				statement.setString(3, username);
				statement.setString(4, "Begin");

				statement.executeUpdate();
				statement.close();
			}
			else if (reaction.equalsIgnoreCase("Rejected"))
			{
				statement = con
						.prepareStatement("UPDATE spel SET toestand_type = 'Resigned', reaktie_type = 'Rejected', moment_reaktie = '"
								+ getCurrentTimeStamp() + "' WHERE id = '" + gameID + "'");

				statement.executeUpdate();
				statement.close();

				statement = con
						.prepareStatement("INSERT INTO beurt(id, spel_id, account_naam, aktie_type)VALUES(?,?,?,?)");

				statement.setInt(1, turnID);
				statement.setInt(2, gameID);
				statement.setString(3, username);
				statement.setString(4, "resign");

				statement.executeUpdate();
				statement.close();
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("QUERRY ERROR!!!");
		} finally
		{
			closeConnection();
		}
	}
	
	public synchronized ArrayList<Integer> gameToLoad(String name){
		ArrayList<Integer> gameID = new ArrayList<Integer>();
		connection();
		try
		{
			statement = con.prepareStatement("SELECT id FROM spel WHERE account_naam_uitdager = '" + name + "' AND reaktie_type = 'Accepted' AND toestand_type = 'Request'");

			result = statement.executeQuery();
			
			if (result.next())
			{
				gameID.add(result.getInt(1));
			}
			result.close();
			statement.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("QUERRY ERROR!!!");
		} finally
		{
			closeConnection();
		}
		return gameID;
	}
	
	
	public synchronized boolean inviteExists(String challenger, String opponent, int compID){
		connection();
		boolean exists = true;
		try
		{
			statement = con.prepareStatement("SELECT * FROM spel WHERE ((account_naam_tegenstander = '" + challenger + "' AND account_naam_uitdager = '" + opponent + "') "
					+ "OR (account_naam_tegenstander = '" + opponent + "' AND account_naam_uitdager = '" + challenger + "')) AND toestand_type <> 'Finished' AND toestand_type <> 'Resigned' AND competitie_id = '" + compID + "'");

			result = statement.executeQuery();

			if (!result.next())
			{
				exists = false;
			}
			result.close();
			statement.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("QUERRY ERROR");
		} finally
		{
			closeConnection();
		}
		return exists;
	}
	
	public synchronized int letterValue(String language, String letter)
	{
		connection();
		int value = 0;
		try
		{
			statement = con.prepareStatement("SELECT waarde FROM lettertype WHERE karakter = '" + letter
					+ "' AND letterset_code = '" + language + "'");

			result = statement.executeQuery();

			if (result.next())
			{
				value = result.getInt(1);
			}
			result.close();
			statement.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("QUERRY ERROR");
		} finally
		{
			closeConnection();
		}
		return value;
	}
	
	public synchronized HashMap<Integer, String> gameTiles(int gameID)
	{
		connection();
		HashMap<Integer, String> tileContent = new HashMap<Integer, String>();

		try
		{
			statement = con.prepareStatement("SELECT id, lettertype_karakter FROM letter WHERE spel_id = '" + gameID
					+ "'");

			result = statement.executeQuery();

			while (result.next())
			{
				tileContent.put(result.getInt(1), result.getString(2));
			}
			result.close();
			statement.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("QUERRY ERROR!!!!");
		} finally
		{
			closeConnection();
		}
		return tileContent;
	}

	public synchronized String turnValue(int gameID, int turnID, String username)//
	{
		connection();
		String status = null;

		try
		{
			statement = con.prepareStatement("SELECT aktie_type FROM beurt WHERE spel_id = '" + gameID + "' AND id = '"
					+ turnID + "'");

			result = statement.executeQuery();

			if (result.next())
			{
				status = result.getString(1);
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("QUERRY ERROR!!!");
		} finally
		{
			closeConnection();
		}
		return status;
	}

	public synchronized ArrayList<Integer> gameScores(int gameID, String username)
	{
		connection();
		ArrayList<Integer> allScores = new ArrayList<Integer>();

		try
		{
			statement = con.prepareStatement("SELECT totaalscore FROM score WHERE account_naam = '" + username + "'");

			result = statement.executeQuery();

			while (result.next())
			{
				allScores.add(result.getInt(1));
			}
			result.close();
			statement.close();

		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("QUERRY ERROR!!");
		} finally
		{
			closeConnection();
		}
		return allScores;
	}

	public synchronized int turnScore(int gameID, int turnID)
	{
		connection();
		int score = 0;

		try
		{
			statement = con.prepareStatement("SELECT score FROM beurt WHERE id = '" + turnID + "' AND spel_id = '"
					+ gameID + "'");

			result = statement.executeQuery();

			if (result.next())
			{
				score = result.getInt(1);
				if (score < 0){
					score = 0;
				}
			}

			result.close();
			statement.close();

		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("Query ERROR!!!");
		} finally
		{
			closeConnection();
		}
		return score;
	}
	
	public synchronized ArrayList<String> fetchCompetitionParticipants(int compID)
	{
		connection();
		ArrayList<String> compStatistics = new ArrayList<String>();

		try
		{
			statement = con.prepareStatement("SELECT s.account_naam, rb.this_num_games, s.totaalscore, (s.totaalscore / rb.this_num_games), rw.wins, (rb.this_num_games - rw.wins), r.bayesian_rating FROM rank_bayesian AS rb LEFT JOIN rank_winnerscore AS rws ON rb.competitie_id = rws.competitie_id LEFT JOIN score AS s ON rws.spel_id = s.spel_id AND rb.account_naam = s.account_naam LEFT JOIN rank_winner AS rw ON rb.competitie_id = rw.competitie_id AND rws.competitie_id = rw.competitie_id AND rb.account_naam = rw.account_naam AND s.account_naam = rw.account_naam LEFT JOIN ranking AS r ON rb.competitie_id = r.competitie_id AND rw.competitie_id = r. competitie_id AND rws.competitie_id AND rb.account_naam = r.account_naam AND rw.account_naam = r.account_naam AND s.account_naam = r.account_naam WHERE rb.competitie_id = '" + compID + "'");
		
			result = statement.executeQuery();
			
			while(result.next())
			{
				compStatistics.add(result.getString(1) + "---" + result.getInt(2) + "---" + result.getInt(3) + "---" + result.getInt(4) + "---" +  result.getInt(5) + "---" + result.getInt(6) + "---" + result.getDouble(7));
			}
			result.close();
			statement.close();
		
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
		} 
		
		finally{closeConnection();}
		return compStatistics;			
	}

	public synchronized boolean triplePass(int gameID, String username)
	{
		connection();
		int passCounter = 0;
		boolean tripPass = false;

		try
		{
			statement = con.prepareStatement("SELECT aktie_type FROM beurt WHERE spel_id = '" + gameID
					+ "' AND account_naam = '" + username + "' ORDER BY id DESC LIMIT 3");

			result = statement.executeQuery();

			while (result.next())
			{
				if (result.getString(1).equals("Pass"))
				{
					passCounter++;
				}
			}
			result.close();
			statement.close();

			if (passCounter == 3)
			{
				tripPass = true;
			}

		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("QUERRY ERROR!!!");
		} finally
		{
			closeConnection();
		}
		return tripPass;
	}

	public synchronized ArrayList<String> adminSelectPlayer(String username)
	{
		connection();
		ArrayList<String> players = new ArrayList<String>();

		try
		{
			statement = con.prepareStatement("SELECT naam FROM account WHERE naam LIKE '" + username + "%'");

			result = statement.executeQuery();

			while (result.next())
			{
				players.add(result.getString(1));
			}
			result.close();
			statement.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("QUERRY ERROR");
		} finally
		{
			closeConnection();
		}
		return players;
	}

	public synchronized ArrayList<String> peopleInCompetition(int compID)
	{
		connection();
		ArrayList<String> compJoiners = new ArrayList<String>();

		try
		{
			statement = con.prepareStatement("SELECT account_naam FROM deelnemer WHERE competitie_id = '" + compID
					+ "'");

			result = statement.executeQuery();

			while (result.next())
			{
				compJoiners.add(result.getString(1));
			}
			result.close();
			statement.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("QUERRY ERROR!!");
		} finally
		{
			closeConnection();
		}
		return compJoiners;
	}


	public synchronized ArrayList<String> activeGames(String username)
	{
		connection();
		ArrayList<String> activeGames = new ArrayList<String>();

		try
		{
			statement = con
					.prepareStatement("SELECT spel.id, spel.account_naam_uitdager, spel.account_naam_tegenstander, competitie.omschrijving FROM spel LEFT JOIN competitie ON spel.competitie_id = competitie.id WHERE (account_naam_uitdager = '" + username + "' OR account_naam_tegenstander = '" + username + "') AND toestand_type = 'Playing'");

			result = statement.executeQuery();
	
			while (result.next())
			{
	
				if (result.getString(2).equals(username))
				{
					activeGames.add(result.getInt(1) + "," + result.getString(2) + " VS " + result.getString(3) + " From competition: "
							+ result.getString(4));
				}
				else
				{
					activeGames.add(result.getInt(1) + "," + result.getString(3) + " VS " + result.getString(2) + " From competition: "
							+ result.getString(4));
				}
			}
			result.close();
			statement.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("QUERRY ERROR");
		} finally
		{
			closeConnection();
		}
		return activeGames;
	}
	
	public synchronized ArrayList<String> pendingGames(String username)
	{
		connection();
		ArrayList<String> pendingGames = new ArrayList<String>();

		try
		{
			statement = con
					.prepareStatement("SELECT spel.id, spel.account_naam_uitdager, spel.account_naam_tegenstander, competitie.omschrijving FROM spel LEFT JOIN competitie ON spel.competitie_id = competitie.id WHERE (account_naam_tegenstander = '" + username + "' OR account_naam_uitdager = '" + username + "') AND reaktie_type = 'Unknown'");

			result = statement.executeQuery();

			while (result.next())
			{
				if (result.getString(3).equals(username))
				{
					pendingGames.add(result.getInt(1) + ",false," + result.getString(2) + " VS " + result.getString(3) + " from competition: "
							+  result.getString(4) + " waiting for you to accept");
				}
				else
				{
					pendingGames.add(result.getInt(1) + ",true," + result.getString(3) + " VS " + result.getString(2) + " from competition: "
							+  result.getString(4) + " waiting for his reaction");
				}
			}
			result.close();
			statement.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("QUERRY ERROR!!!!");
		} finally
		{
			closeConnection();
		}
		return pendingGames;
	}

	public synchronized ArrayList<String> spectateList()
	{
		connection();
		ArrayList<String> spectateGames = new ArrayList<String>();
		
		try
		{
			statement = con.prepareStatement("SELECT spel.id, spel.account_naam_uitdager, spel.account_naam_tegenstander, competitie.omschrijving FROM spel LEFT JOIN competitie ON spel.competitie_id = competitie.id WHERE toestand_type = 'Playing' AND zichtbaarheid_type = 'openbaar' ORDER BY id ASC");
			
			result = statement.executeQuery();
			
			while(result.next())
			{
				spectateGames.add(result.getInt(1) + "," + result.getString(2) + " VS " + result.getString(3) + " in competition " + result.getString(4));
			}
			result.close();
			statement.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("QUERRY ERROR");
		}finally{closeConnection();}
		return spectateGames;
	}
	
	
	public synchronized ArrayList<String> userInfo(String username)
	{
		connection();
		ArrayList<String> userInfo = new ArrayList<String>();
		
		try
		{
			statement = con.prepareStatement("SELECT * FROM account WHERE naam = '" + username + "'");
			
			result = statement.executeQuery();
			
			if(result.next())
			{
				userInfo.add(result.getString(1) + "---" + result.getString(2));
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("QUERRY ERROR");
		}finally{closeConnection();}
		return userInfo;
	}
	
	public synchronized String changeUsername(String oldUsername, String newUsername)
	{
		connection();
		String changeResult = "The username: " + newUsername + " is already taken";
		
		try
		{
			statement = con.prepareStatement("SELECT naam FROM account WHERE naam = '" + newUsername + "'");
			
			result = statement.executeQuery();
			
			if(!result.next())
			{
				try
				{
				statement = con.prepareStatement("UPDATE account SET naam = '" + newUsername + "' WHERE naam = '" + oldUsername + "'");

				statement.executeUpdate();
				
				statement.close();
				changeResult = "Your username has been succesfully changed";
				} catch (SQLException e)
				{
					e.printStackTrace();			
					System.out.println("QUERRY ERROR");
				}
			}

		} catch (SQLException e)
		{
			e.printStackTrace();			
			System.out.println("QUERRY ERROR");
		}finally{closeConnection();}
		return changeResult;
	}
	
	public synchronized String changePassword(String username, String password)
	{
		connection();
		String changeResult = null;

		try
		{
			statement = con.prepareStatement("UPDATE account SET wachtwoord = '" + password + "' WHERE naam = '" + username + "'");
		
			statement.executeUpdate();
				
			statement.close();
			changeResult = "Your password has been succesfully changed";

		} catch (SQLException e)
		{
			e.printStackTrace();			
			System.out.println("QUERRY ERROR");
		}finally{closeConnection();}
		return changeResult;
	}
	
	public synchronized String playerStatistics(String username)
	{
		connection();
		
		int competitionsWon = 0;
		int gamesWon = 0;
		String mostValuableWord = null;
		int numberOfGamesPlayed = 0;
		int highestGameScore = 0;
		
		String statistics = null;
		
		try
		{
			statement = con.prepareStatement("SELECT count(wins) FROM competitie AS c LEFT JOIN rank_winner AS rw ON c.id = rw.competitie_id WHERE rw.account_naam = '" + username + "' AND c.start < '" + getCurrentTimeStamp() + "' AND rw.wins = (SELECT max(wins) FROM rank_winner)");
			
			result = statement.executeQuery();
			if(result.next())
			{
				competitionsWon = result.getInt(1);
			}
			result.close();
			statement.close();
			
			statement = con.prepareStatement("SELECT count(rw.wins) FROM competitie AS c LEFT JOIN rank_winner AS rw ON c.id = rw.competitie_id WHERE rw.account_naam = '" + username + "'");

			result = statement.executeQuery();
			
			if(result.next())
			{
				gamesWon = result.getInt(1);
			}
			result.close();
			statement.close();
			
			statement = con.prepareStatement("SELECT g.woorddeel, b.score FROM spel AS s LEFT JOIN beurt AS b ON s.id = b.spel_id LEFT JOIN gelegd AS g ON s.id = g.spel_id AND b.spel_id = g.spel_id AND b.id = g.beurt_id WHERE b.account_naam = '" + username + "' ORDER BY score DESC LIMIT 1");
			
			result = statement.executeQuery();
			
			if(result.next())
			{
				mostValuableWord = result.getString(1) + "---" + result.getInt(2);
			}
			result.close();
			statement.close();
			
			statement = con.prepareStatement("SELECT count(id) FROM spel WHERE (toestand_type = 'Finished' OR toestand_type = 'Resigned') AND (account_naam_uitdager = '" + username + "' OR account_naam_tegenstander = '" + username + "')");
			
			result = statement.executeQuery();
			
			if(result.next())
			{
				numberOfGamesPlayed = result.getInt(1);
			}
			result.close();
			statement.close();
			
			statement = con.prepareStatement("SELECT max(totaalscore) FROM score WHERE account_naam = 'jager684'");
			
			result = statement.executeQuery();
			
			if(result.next())
			{
				highestGameScore = result.getInt(1);
			}
			result.close();
			statement.close();
			
			statistics = competitionsWon + "---" + gamesWon + "---" + mostValuableWord + "---" + numberOfGamesPlayed + "---" + highestGameScore;
			
		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("");
		}finally{closeConnection();}
		return statistics;
	}
	
	public synchronized String getPassword(String username)
	{
		connection();
		String password = null;
		
		try
		{
			statement = con.prepareStatement("SELECT wachtwoord FROM account WHERE naam = '" + username + "'");
			
			result = statement.executeQuery();
			
			if(result.next())
			{
				password = result.getString(1);
			}
			result.close();
			statement.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("QUERRY ERROR");
		}finally{closeConnection();}
		return password;
	}
	
	public synchronized HashMap<Integer,String> activeCompetitions()
	{
		connection();
		HashMap<Integer,String> activeComps = new HashMap<Integer,String>();
		
		try
		{
			statement = con.prepareStatement("SELECT * FROM competitie WHERE einde > '" + getCurrentTimeStamp() + "'");
			
			result = statement.executeQuery();
			
			while(result.next())
			{
//				activeComps.add(result.getInt(1) + "---" + result.getString(2) + "---" + result.getTimestamp(3) + "---" + result.getTimestamp(4) + "---" + result.getString(5) + "---" + result.getInt(6) + "---" + result.getString(7));
				activeComps.put(result.getInt(1), result.getString(2) + "---" + result.getTimestamp(3) + "---" + result.getTimestamp(4) + "---" + result.getString(5) + "---" + result.getInt(6) + "---" + result.getString(7));
			}
			result.close();
			statement.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("QUERRY ERROR");
		}
		return activeComps;
	}
	
	public synchronized String adminRegister(String username, String password, boolean admin, boolean moderator, boolean player)
	{
		connection();
		String registered = "Can not register account";
		try
		{
			statement = con.prepareStatement("SELECT * FROM account WHERE naam = '" + username + "'");
			
			result = statement.executeQuery();
			
			if (!result.next())
			{
				registered = "username is available, account is registered";
				result.close();
				statement.close();

				try
				{
					statement = con.prepareStatement("INSERT INTO account(naam, wachtwoord)VALUES(?,?)");

					statement.setString(1, username);
					statement.setString(2, password);

					statement.executeUpdate(); 
					statement.close();

				} catch (SQLException e)
				{
					e.printStackTrace();
					System.out.println("account register error");
				}
				try
				{
					if(admin)
					{
					statement = con.prepareStatement("INSERT INTO accountrol(account_naam, rol_type)VALUES(?,?)");

					statement.setString(1, username);
					statement.setString(2, "Administrator");

					statement.executeUpdate(); 
					statement.close();
					}
					
					if(moderator)
					{
					statement = con.prepareStatement("INSERT INTO accountrol(account_naam, rol_type)VALUES(?,?)");

					statement.setString(1, username);
					statement.setString(2, "Moderator");

					statement.executeUpdate(); 
					statement.close();
					}
					
					if(player)
					{
					statement = con.prepareStatement("INSERT INTO accountrol(account_naam, rol_type)VALUES(?,?)");

					statement.setString(1, username);
					statement.setString(2, "Player");

					statement.executeUpdate(); 
					statement.close();
					}

				} catch (SQLException e)
				{
					e.printStackTrace();
					System.out.println("role register error");
				}
			}
			else
			{
				registered = "username is not available, cannot register your account";
				statement.close();
			}

		} catch (SQLException e){
			e.printStackTrace();
			System.out.println("query error!!!");
		} finally{closeConnection();}
		return registered;
	}
	
	public synchronized ArrayList<String> getAllPlayers()
	{
		connection();
		ArrayList<String> allPlayers = new ArrayList<String>();
		
		try
		{
			statement = con.prepareStatement("SELECT naam FROM account");
			
			result = statement.executeQuery();
			
			while(result.next())
			{
				allPlayers.add(result.getString(1));
			}
			result.close();
			statement.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("Players retrieval error");
		}finally{closeConnection();}
		return allPlayers;
	}
	
	
}

/*
 * aanmaken door: DatabaseHandler dbh = DatabaseHandler.getInstance();
 * 
 * en aanroepen door: dbh. --------
 * 
 * aangemaakt door: Michael login check register check and register name and
 * password create competition, create game chat send, checkTurn, chat Receive
 * jarcontent set/getRole, ban player, add/rejectWords
 * 
 * 
 * to use multiple methods con.close(); needs to be removed. otherwise u get an
 * closed connection error. all needs to be checked for the rol of the player to
 * use multiple methods
 */
