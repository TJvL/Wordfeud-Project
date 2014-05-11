package datalaag;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHandler {
	final private static String URL = "jdbc:mysql://databases.aii.avans.nl:3306/manschou_db2"; // location
																								// of
																								// the
																								// database
	final private static String USER = "manschou"; // is the database user
	final private static String USERPASS = "Mschouten92"; // the password of the
															// user

	private PreparedStatement statement = null;
	private ResultSet result = null;

	private Connection con;

	private static java.sql.Timestamp getCurrentTimeStamp() {
		java.util.Date today = new java.util.Date();
		return new java.sql.Timestamp(today.getTime());
	}

	private static DatabaseHandler databaseHandler = new DatabaseHandler();

	public static DatabaseHandler getInstance() {
		return databaseHandler;
	}

	private DatabaseHandler() {
		// Accessing driver from the JAR file
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// e.printStackTrace();
			System.out.println("Driver not found");
		}
	}

	public void connection() {
		int retry = 0;
		boolean connected = false;
		while (!connected && retry < 100) {
			// Creating a variable for the connection
			try {
				con = DriverManager.getConnection(URL, USER, USERPASS);
				// System.out.println("verbonden");
				connected = true;
			} catch (SQLException e) {
				e.printStackTrace();
				retry++;
				System.out.println("kan geen verbinding maken");
			}
		}
	}

	public void closeConnection() // closes the connection to the database
	{
		try {
			// closes the connection
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("ERROR in closing connection");
		}
	}

	public synchronized boolean login(String username, String password) // return
																		// statement
																		// for
																		// login
																		// in/correct
																		// needs
																		// to be
																		// applied
	{
		connection();
		boolean login = false;
		try {
			statement = con
					.prepareStatement("SELECT * FROM account WHERE naam = '"
							+ username + "' AND wachtwoord = '" + password
							+ "'");

			result = statement.executeQuery();

			if (result.next()) {
				System.out.println("username + password correct");
				login = true;
				result.close();
			} else {
				System.out.println("username or password incorrect");
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Query ERROR!!!!");
		} finally {
			closeConnection();
		}
		return login;
	}

	public synchronized boolean register(String username, String password)// return
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
		boolean registered = false;
		try {
			statement = con
					.prepareStatement("SELECT * FROM account WHERE naam = '"
							+ username + "'");

			result = statement.executeQuery();

			if (!result.next()) {
				System.out.println("username is available");
				result.close();
				statement.close();

				try {
					// Here we create our query where u state which fields u
					// want to insert data
					statement = con
							.prepareStatement("INSERT INTO account(naam, wachtwoord)VALUES(?,?)");
					// the ? represents anonymous values

					statement.setString(1, username);
					statement.setString(2, password);

					// execute your query
					statement.executeUpdate(); // if there is not result then u
												// use a executeUpdate()

					// closes the statement
					statement.close();

					statement = con
							.prepareStatement("INSERT INTO accountrol(account_naam, rol_type)VALUES(?,?)");

					statement.setString(1, username);
					statement.setString(2, "Player");

					statement.executeUpdate();

					statement.close();

				} catch (SQLException e) {
					e.printStackTrace();
					System.out.println("account register error");
				}
				registered = true;
			} else {
				System.out.println("username = not available");
				statement.close();
			}

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("query error!!!");
		} finally {
			closeConnection();
		}
		return registered;
	}

	public synchronized void chatSend(String username, int gameID,
			String message)// it works
	{
		connection();
		try {
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

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("chat message send error");
		} finally {
			closeConnection();
		}
	}

	public synchronized ArrayList<String> chatReceive(int gameID,
			String lastMessageTimestamp)// need to be return checked!!!
	{
		connection();
		ArrayList<String> chat = new ArrayList<String>();
		chat.clear();

		if (lastMessageTimestamp.equals("")) {
			lastMessageTimestamp = "2013-05-05 00:00:00";
		}
		Timestamp oldTimestamp = Timestamp.valueOf(lastMessageTimestamp); // converts
																			// String
																			// to
																			// Timestamp

		try {
			statement = con
					.prepareStatement("SELECT account_naam, tijdstip, bericht FROM chatregel WHERE spel_id = '"
							+ gameID
							+ "' AND tijdstip >= '"
							+ oldTimestamp
							+ "' ORDER BY tijdstip ASC");

			result = statement.executeQuery();

			while (result.next()) {
				chat.add(result.getString(1) + "---"
						+ result.getTimestamp(2).toString() + "---"
						+ result.getString(3));
				// System.out.println("sender: " + sender + " Time: " + chatTime
				// + " message: " + message);
			}
			result.close();
			statement.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("query error!!!!");
		} finally {
			closeConnection();
		}
		return chat;
	}

	public synchronized int createCompetition(String username, String start,
			String end, String summary, int minParticipants, int maxParticipants) // works
	{
		connection();
		// convert string to timestamp
		java.sql.Timestamp compStart = java.sql.Timestamp.valueOf(start);
		java.sql.Timestamp compEnd = java.sql.Timestamp.valueOf(end);

		int compID = 0;

		try {
			// Here we create our query where u state which fields u want to
			// insert data
			statement = con
					.prepareStatement(
							"INSERT INTO competitie(account_naam_eigenaar, start, einde, omschrijving, minimum_aantal_deelnemers, maximum_aantal_deelnemers)VALUES(?,?,?,?,?,?)",
							PreparedStatement.RETURN_GENERATED_KEYS);
			// the ? represents anonymous values

			statement.setString(1, username);
			statement.setTimestamp(2, compStart);
			statement.setTimestamp(3, compEnd);
			statement.setString(4, summary);
			statement.setInt(5, minParticipants);
			statement.setInt(6, maxParticipants);

			// execute your query
			statement.execute(); // needs to be execute because executeQuery
									// doesn't work with PK retrieval

			result = statement.getGeneratedKeys(); // returns competitionID

			if (result.next()) {
				compID = result.getInt(1);
				// System.out.println("do i get the PK " + compID);

				statement = con
						.prepareStatement("INSERT INTO deelnemer(account_naam, competitie_id)VALUES(?,?)");

				statement.setString(1, username);
				statement.setInt(2, compID);
				statement.executeUpdate();
			}

			// closes the statement
			statement.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("create error");
		} finally {
			closeConnection();
		}
		return compID;
	}

	public synchronized int createGame(int competitionID, String username,
			String opponent, String privacy, String language)// works
	{
		connection();
		int gameID = 0;
		try {
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

			if (result.next()) {
				gameID = result.getInt(1);
				// System.out.println("do i get the PK " + gameID);
			}
			result.close();
			// closes the statement
			statement.close();

			statement = con
					.prepareStatement("INSERT INTO beurt(id, spel_id, account_naam, aktie_type)VALUES(?,?,?,?)");

			statement.setInt(1, 1);
			statement.setInt(2, gameID);
			statement.setString(3, username);
			statement.setString(4, "Begin");

			statement.executeUpdate();

			statement.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("create error");
		} finally {
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

		try {
			statement = con
					.prepareStatement("SELECT account_naam, id FROM beurt WHERE spel_id = '"
							+ gameID + "' ORDER BY id DESC LIMIT 1");

			result = statement.executeQuery();

			if (result.next()) {
				if (result.getString(1).equals(username)) {
					// System.out.println("it's not your turn");
					turnID = result.getInt(2);
				} else {
					// System.out.println("it's your turn");
					myTurn = true;
					turnID = result.getInt(2) + 1;
				}
				turn = myTurn + "---" + turnID;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Query ERROR!!!!");
		} finally {
			closeConnection();
		}
		return turn;
	}

	public synchronized void updateTurn(int turnID, int gameID,
			String username, int score, String action)// needs to be tested,
														// probally works
	{
		connection();
		try {
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

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("insert turn ERROR!!!");
		} finally {
			closeConnection();
		}
	}

	public synchronized int score(int gameID, String username) // gets the max
																// score of the
																// user
	{
		connection();
		int score = 0;

		try {
			statement = con
					.prepareStatement("SELECT totaalscore FROM score WHERE account_naam = '"
							+ username + "' AND spel_id = '" + gameID + "'");

			result = statement.executeQuery();

			if (result.next()) {
				score = result.getInt(1);
			}
			result.close();
			statement.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Query ERROR!!!");
		} finally {
			closeConnection();
		}
		return score;
	}

	public synchronized int turnScore(int gameID, int turnID) 
	{
		connection();
		int score = 0;
		try {
			statement = con
					.prepareStatement("SELECT score FROM beurt WHERE id = '"
							+ turnID + "' AND spel_id = '" + gameID + "'");

			result = statement.executeQuery();

			if (result.next()) {
				score = result.getInt(1);
			}
			result.close();
			statement.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Query ERROR!!!");
		} finally {
			closeConnection();
		}
		return score;
	}

	public synchronized void addTileToHand(int gameID, ArrayList<Integer> tile,
			int turnID)// should work
	{
		connection();
		try {
			statement = con
					.prepareStatement("INSERT INTO letterbakjeletter(spel_id, letter_id, beurt_id)VALUES(?,?,?)");
			for (int i = 0; i < tile.size(); i++) {
				statement.setInt(1, gameID);
				statement.setInt(2, tile.get(i));
				statement.setInt(3, turnID);

				statement.addBatch();
			}
			statement.executeBatch();

			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Query Error!!!!");
		} finally {
			closeConnection();
		}
	}

	public synchronized String squareCheck(int cordX, int cordY)// should work
	{
		String squareValue = null;

		try {
			statement = con
					.prepareStatement("SELECT tegeltype_soort FROM tegel WHERE x = '"
							+ cordX + "' AND y = '" + cordY + "'");

			result = statement.executeQuery();

			if (result.next()) {
				squareValue = result.getString(1);
			}
			result.close();
			statement.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Query ERROR!!!!");
		}
		return squareValue;
	}

	public synchronized boolean checkWord(String word, String language)// check
																		// works
	{
		connection();
		boolean validWord = false;

		try {
			statement = con
					.prepareStatement("SELECT woord FROM woordenboek WHERE woord = '"
							+ word
							+ "' AND letterset_code = '"
							+ language
							+ "' AND status = 'accepted'");

			result = statement.executeQuery();

			if (result.next()) {
				validWord = true;
			}
			result.close();
			statement.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Query ERROR!!!");
		} finally {
			closeConnection();
		}
		return validWord;
	}

	public synchronized void gameStatusUpdate(int gameID, String status)// works
	{
		connection();
		try {
			statement = con
					.prepareStatement("UPDATE spel SET toestand_type = '"
							+ status + "' WHERE id ='" + gameID + "'");

			statement.executeUpdate();

			statement.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("QUERY ERROR!!!");
		} finally {
			closeConnection();
		}
	}

	public synchronized String getGameStatusValue(int gameID) {
		connection();
		String gameStatus = null;

		try {
			statement = con
					.prepareStatement("SELECT toestand_type FROM spel WHERE id ='"
							+ gameID + "'");

			result = statement.executeQuery();

			if (result.next()) {
				gameStatus = result.getString(1);
			}
			result.close();
			statement.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Query ERROR!!!");
		} finally {
			closeConnection();
		}
		return gameStatus;
	}

	public synchronized String opponentName(int gameID) {
		connection();

		String players = null;
		try {
			statement = con
					.prepareStatement("SELECT account_naam_uitdager, account_naam_tegenstander FROM spel WHERE id ='"
							+ gameID + "'");

			result = statement.executeQuery();

			if (result.next()) {
				players = result.getString(1) + "---" + result.getString(2);
			}
			result.close();
			statement.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Query error!!!");
		} finally {
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

		try {
			statement = con
					.prepareStatement("SELECT p.letter_id, lt.karakter, lt.waarde FROM pot AS p LEFT JOIN letter AS l ON p.spel_id = l.spel_id AND p.letter_id = l.id LEFT JOIN lettertype AS lt ON lt.karakter = l.lettertype_karakter AND l.lettertype_letterset_code = lt.letterset_code WHERE p.spel_id = '"
							+ gameID + "'");

			result = statement.executeQuery();

			while (result.next()) {
				jarContent.add(result.getInt(1) + "---" + result.getString(2)
						+ "---" + result.getInt(3));
			}
			result.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Query ERROR!!!!");
		} finally {
			closeConnection();
		}
		return jarContent;
	}

	public synchronized ArrayList<String> handContent(int gameID, int turnID) {
		connection();
		ArrayList<String> handContent = new ArrayList<String>();

		try {
			statement = con
					.prepareStatement("SELECT lbl.letter_id, lt.karakter, lt.waarde FROM letterbakjeletter AS lbl LEFT JOIN letter AS l ON lbl.spel_id = l.spel_id AND lbl.letter_id = l.id LEFT JOIN lettertype AS lt ON lt.karakter = l.lettertype_karakter AND l.lettertype_letterset_code = lt.letterset_code WHERE lbl.spel_id = '"
							+ gameID + "' AND lbl.beurt_id = '" + turnID + "'");

			result = statement.executeQuery();

			while (result.next()) {
				handContent.add(result.getInt(1) + "---" + result.getString(2)
						+ "---" + result.getInt(3));
			}
			result.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Query ERROR!!");
		} finally {
			closeConnection();
		}
		return handContent;
	}

	public synchronized void tileToBoard(int gameID, int turnID, int tileID,
			String blankTile, int cordX, int cordY) {
		connection();
		try {
			statement = con
					.prepareStatement("INSERT INTO gelegdeletter(letter_id, spel_id, beurt_id, tegel_x, tegel_y, tegel_bord_naam, blancoletterkarakter)VALUES(?,?,?,?,?,?,?)");

			statement.setInt(1, tileID);
			statement.setInt(2, gameID);
			statement.setInt(3, turnID);
			statement.setInt(4, cordX);
			statement.setInt(5, cordY);
			statement.setString(6, "Standard");

			if (blankTile != null) {
				statement.setString(7, blankTile);
			} else {
				statement.setString(7, null);
			}

			statement.executeUpdate();

			statement.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Query ERROR!!!");
		} finally {
			closeConnection();
		}
	}

	public synchronized void surrender(int gameID, int turnID, String username,
			String opponent)// used to surrender the game
	{
		connection();
		try {
			statement = con
					.prepareStatement("INSERT INTO beurt(id, spel_id, account_naam, score, aktie_type) VALUES(?,?,?,?,?)");

			statement.setInt(1, turnID);
			statement.setInt(2, gameID);
			statement.setString(3, username);
			statement.setInt(4, 0);
			statement.setString(5, "End");

			statement.executeUpdate();

			statement.close();

			statement = con
					.prepareStatement("UPDATE spel SET toestand_type = 'Resigned' WHERE id = '"
							+ gameID + "' AND toestand_type = 'Playing'");

			statement.executeUpdate();
			statement.close();

			statement = con
					.prepareStatement("INSERT INTO beurt(id, spel_id, account_naam, score, aktie_type) VALUES(?,?,?,?,?)");

			statement.setInt(1, turnID + 1);
			statement.setInt(2, gameID);
			statement.setString(3, opponent);
			statement.setInt(4, 0);
			statement.setString(5, "End");

			statement.executeUpdate();

			result.close();
			statement.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Query ERROR!!!");
		} finally {
			closeConnection();
		}
	}

	public synchronized void win(int gameID, int turnID, String username,
			int score, String opponent) {
		connection();
		try {

			statement = con
					.prepareStatement("INSERT INTO beurt(id, spel_id, account_naam, score, aktie_type) VALUES(?,?,?,?,?)");

			statement.setInt(1, turnID);
			statement.setInt(2, gameID);
			statement.setString(3, username);
			statement.setInt(4, score);
			statement.setString(5, "End");

			statement.executeUpdate();

			statement.close();

			statement = con.prepareStatement("SELECT * FROM spel WHERE id = '"
					+ gameID + "'");

			result = statement.executeQuery();

			statement = con
					.prepareStatement("UPDATE spel SET toestand_type = 'Finished' WHERE WHERE id = '"
							+ gameID + "' AND toestand_type = 'Playing'");

			statement.executeUpdate();

			statement.close();

			statement = con
					.prepareStatement("INSERT INTO beurt(id, spel_id, account_naam, score, aktie_type) VALUES(?,?,?,?,?)");

			statement.setInt(1, turnID + 1);
			statement.setInt(2, gameID);
			statement.setString(3, opponent);
			statement.setInt(4, 0);
			statement.setString(5, "End");

			statement.executeUpdate();

			statement.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Query ERROR!!!");
		} finally {
			closeConnection();
		}
	}

	public synchronized ArrayList<String> competitionOwner(String username)// seems
																			// to
																			// work
	{
		connection();
		ArrayList<String> myCompetitions = new ArrayList<String>();

		try {
			statement = con
					.prepareStatement("SELECT id, start, einde, omschrijving, minimum_aantal_deelnemers, maximum_aantal_deelnemers FROM competitie");

			result = statement.executeQuery();

			while (result.next()) {
				myCompetitions.add(result.getInt(1) + "---"
						+ result.getTimestamp(2).toString() + "---"
						+ result.getTimestamp(3).toString() + "---"
						+ result.getString(4) + "---" + result.getInt(5)
						+ "---" + result.getInt(6));

			}
			result.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
		return myCompetitions;
	}

	public synchronized ArrayList<String> competitionParticipant(String username)// seems
																					// to
																					// work
	{
		connection();
		ArrayList<String> myParticipations = new ArrayList<String>();

		try {
			statement = con
					.prepareStatement("SELECT d.account_naam AS me, c.id AS compID, c.account_naam_eigenaar AS competition_Owner, c.start, c.einde, c.omschrijving FROM deelnemer AS d RIGHT JOIN competitie AS c ON d.competitie_id = c.id WHERE d.account_naam = '"
							+ username
							+ "' AND c.account_naam_eigenaar NOT Like '"
							+ username + "'");

			result = statement.executeQuery();

			while (result.next()) {
				myParticipations.add(result.getString(1) + "---"
						+ result.getInt(2) + "---" + result.getString(3)
						+ "---" + result.getTimestamp(4).toString() + "---"
						+ result.getTimestamp(5).toString() + "---"
						+ result.getString(6));
			}
			result.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Query ERROR!!!!");
		} finally {
			closeConnection();
		}
		return myParticipations;
	}

	public synchronized void createJar(int gameID, String language) {
		connection();

		ArrayList<String> newPot = new ArrayList<String>();
		int numOfTiles = 0;
		String letter = null;

		try {
			statement = con
					.prepareStatement("SELECT karakter, aantal FROM lettertype WHERE letterset_code ='"
							+ language + "'");

			result = statement.executeQuery();

			while (result.next()) {
				numOfTiles = result.getInt(2);
				for (int x = 0; x < numOfTiles; x++) {
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

			for (int i = 0; i < newPot.size(); i++) {

				statement.setInt(1, (i + 1));
				statement.setInt(2, gameID);
				statement.setString(3, language);
				statement.setString(4, newPot.get(i));

				statement.addBatch();
			}
			statement.executeBatch();
			statement.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Query ERROR!!!!");
		} finally {
			closeConnection();
		}
	}

	public synchronized ArrayList<String> playedWords(int gameID)// works
	{
		connection();
		ArrayList<String> playedWord = new ArrayList<String>();

		try {
			statement = con
					.prepareStatement("SELECT * FROM gelegd WHERE spel_id = '"
							+ gameID + "'");

			result = statement.executeQuery();

			while (result.next()) {
				playedWord.add(result.getString(3) + "---"
						+ result.getString(4) + "---" + result.getString(5)
						+ "---" + result.getString(2));
			}
			result.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Query ERROR!!!!");
		} finally {
			closeConnection();
		}
		return playedWord;
	}

	public synchronized void joinCompetition(int compID, String username) {
		connection();
		try {
			statement = con
					.prepareStatement("INSERT INTO deelnemer(account_naam, competitie_id)VALUES(?,?)");

			statement.setString(1, username);
			statement.setInt(2, compID);

			statement.executeUpdate();

			statement.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Query ERROR!!!!");
		} finally {
			closeConnection();
		}
	}

	public synchronized ArrayList<String> peopleInCompetition() {
		connection();
		ArrayList<String> numOfPeopleInCompetition = new ArrayList<String>();

		try {
			statement = con
					.prepareStatement("SELECT distinct(competitie_id), count(account_naam) FROM deelnemer");

			result = statement.executeQuery();

			while (result.next()) {
				numOfPeopleInCompetition.add(result.getInt(1) + "---"
						+ result.getInt(2));
			}
			result.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("QUERRY ERROR!!!!");
		} finally {
			closeConnection();
		}
		return numOfPeopleInCompetition;
	}

	public synchronized void requestWord(String word, String language) {
		connection();
		try {
			statement = con
					.prepareStatement("INSERT INTO woordenboek(woord, letterset_code, status)VALUES(?,?,?)");

			statement.setString(1, word);
			statement.setString(2, language);
			statement.setString(3, "Pending");

			statement.executeUpdate();

			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("QUERY ERROR!!!!!");
		} finally {
			closeConnection();
		}
	}

	public synchronized void acceptDeniedWord(String word, String language,
			String status) {
		connection();
		try {
			statement = con
					.prepareStatement("UPDATE woordenboek SET status = '"
							+ status + "' WHERE woord = '" + word
							+ "' AND letterset_code = '" + language + "'");

			statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("QUERY ERROR!!!");
		} finally {
			closeConnection();
		}
	}

	public synchronized ArrayList<String> pendingWords() {
		connection();
		ArrayList<String> pendingWord = new ArrayList<String>();

		try {
			statement = con
					.prepareStatement("SELECT woord, letterset_code FROM woordenboek WHERE status = 'Pending'");

			result = statement.executeQuery();

			while (result.next()) {
				pendingWord.add(result.getString(1) + "---"
						+ result.getString(2));
			}
			result.close();
			statement.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("QUERRY ERROR!!!!");
		} finally {
			closeConnection();
		}
		return pendingWord;
	}

	public synchronized void setRole(String username, String role) {
		connection();
		try {
			statement = con
					.prepareStatement("INSERT INTO accountrol(account_naam, rol_type)VALUES(?,?)");

			statement.setString(1, username);
			statement.setString(2, role);

			statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("QUERRY ERROR!!!");
		} finally {
			closeConnection();
		}
	}

	public synchronized ArrayList<String> getRole(String username) {
		connection();
		ArrayList<String> userRoles = new ArrayList<String>();

		try {
			statement = con
					.prepareStatement("SELECT rol_type FROM accountrol WHERE account_naam = '"
							+ username + "' ORDER BY rol_type ASC");

			result = statement.executeQuery();

			while (result.next()) {
				userRoles.add(result.getString(1));
			}
			result.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("QUERRY ERRROR!!!!");
		} finally {
			closeConnection();
		}
		return userRoles;
	}

	public synchronized void banPlayer(String username) {
		connection();
		try {
			statement = con
					.prepareStatement("DELETE FROM accountrol WHERE account_naam = '"
							+ username + "' AND rol_type = 'Player'");

			statement.executeQuery();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("QUERRY ERROR!!!");
		} finally {
			closeConnection();
		}
	}

	public synchronized void acceptRejectGame(int turnID, int gameID,
			String username, String reaction) {
		connection();
		try {
			if (reaction.equalsIgnoreCase("Accepted")) {
				statement = con
						.prepareStatement("UPDATE spel SET toestand_type = 'Playing', reactie = 'Accepted', moment_reaktie = '"
								+ getCurrentTimeStamp()
								+ "' WHERE id = '"
								+ gameID + "'");

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
			} else if (reaction.equalsIgnoreCase("Rejected")) {
				statement = con
						.prepareStatement("UPDATE spel SET toestand_type = 'Resigned', reactie = 'Rejected', moment_reaktie = '"
								+ getCurrentTimeStamp()
								+ "' WHERE id = '"
								+ gameID + "'");

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
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("QUERRY ERROR!!!");
		} finally {
			closeConnection();
		}
	}

	public synchronized int letterValue(String language, String letter) {
		connection();
		int value = 0;
		try {
			statement = con
					.prepareStatement("SELECT waarde FROM lettertype WHERE karakter = '"
							+ letter
							+ "' AND letterset_code = '"
							+ language
							+ "'");

			result = statement.executeQuery();

			if (result.next()) {
				value = result.getInt(1);
			}
			result.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("QUERRY ERROR");
		} finally {
			closeConnection();
		}
		return value;
	}

	public synchronized HashMap<Integer, String> gameTiles(int gameID) {
		connection();
		HashMap<Integer, String> tileContent = new HashMap<Integer, String>();

		try {
			statement = con
					.prepareStatement("SELECT id, lettertype_karakter FROM letter WHERE spel_id = '"
							+ gameID + "'");

			result = statement.executeQuery();

			while (result.next()) {
				tileContent.put(result.getInt(1), result.getString(2));
			}
			result.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("QUERRY ERROR!!!!");
		} finally {
			closeConnection();
		}
		return tileContent;
	}

	public synchronized String turnValue(int gameID, int turnID, String username)//
	{
		connection();
		String status = null;

		try {
			statement = con
					.prepareStatement("SELECT aktie_type FROM beurt WHERE spel_id = '"
							+ gameID + "' AND id = '" + turnID + "'");

			result = statement.executeQuery();

			if (result.next()) {
				status = result.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("QUERRY ERROR!!!");
		} finally {
			closeConnection();
		}
		return status;
	}

	public synchronized ArrayList<Integer> gameScores(int gameID,
			String username) {
		connection();
		ArrayList<Integer> allScores = new ArrayList<Integer>();

		try {
			statement = con
					.prepareStatement("SELECT totaalscore FROM score WHERE account_naam = '"
							+ username + "'");

			result = statement.executeQuery();

			while (result.next()) {
				allScores.add(result.getInt(1));
			}
			result.close();
			statement.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("QUERRY ERROR!!");
		} finally {
			closeConnection();
		}
		return allScores;
	}

	public synchronized ArrayList<String> competitionRanking(int compID) {
		connection();
		ArrayList<String> compRanking = new ArrayList<String>();

		try {
			statement = con
					.prepareStatement("SELECT account_naam, wins FROM rank_winner WHERE competitie_id = '"
							+ compID + "' ORDER BY wins DESC");

			result = statement.executeQuery();

			while (result.next()) {
				compRanking.add(result.getString(1) + "---" + result.getInt(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("QUERRY ERROR!!!");
		} finally {
			closeConnection();
		}
		return compRanking;
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