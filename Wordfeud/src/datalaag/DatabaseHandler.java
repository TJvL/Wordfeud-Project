package datalaag;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseHandler {
	final private String url = "jdbc:mysql://databases.aii.avans.nl:3306/manschou_db2";	//location of the database
	final private String user = "manschou";	//is the database user
	final private String userPass = "Mschouten92"; //the password of the user
	
	private PreparedStatement statement = null;
	private ResultSet result = null;
	
	private Connection con;
	
	private static java.sql.Timestamp getCurrentTimeStamp() {
	    java.util.Date today = new java.util.Date();
	    return new java.sql.Timestamp(today.getTime());
	}
	
	private static DatabaseHandler databaseHandler = new DatabaseHandler();
	
	public static DatabaseHandler getInstance(){
		return databaseHandler;
	}
	
	public DatabaseHandler(){
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
	
	public boolean login(String username, String password) //return statement for login in/correct needs to be applied
	{
		boolean login = false;
		try
		{
			statement = con.prepareStatement("SELECT * FROM account WHERE naam = '" + username + "' AND wachtwoord = '" + password + "'");
			
			result = statement.executeQuery();
			
			if(result.next())
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
	
	public boolean register(String username, String password)//return statement for register in/correct needs to be applied
	{
		boolean registered = false;
		try
		{
			statement = con.prepareStatement("SELECT * FROM account WHERE naam = '" + username + "'");
			
			result = statement.executeQuery();
			
			if(!result.next())
			{
			 System.out.println("username is available");
			 
				try
				{
					// Here we create our query where u state which fields u want to insert data
					statement = con.prepareStatement("INSERT INTO account(naam, wachtwoord)VALUES(?,?)");
//					// the ? represents anonymous values
//							
					statement.setString(1, username);
					statement.setString(2, password);
				
					// execute your query
					statement.executeUpdate(); //if there is not result then u use a executeUpdate()
							
					// closes the statement
					statement.close();
							
					// closes the connection
					con.close();
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
			statement = con.prepareStatement("INSERT INTO chatregel(account_naam, spel_id, tijdstip, bericht)VALUES(?,?,?,?)");
//			// the ? represents anonymous values
//					
			statement.setString(1, username);
			statement.setInt(2, gameID);
			statement.setTimestamp(3, getCurrentTimeStamp());
			statement.setString(4, message);
		
			// execute your query
			statement.executeUpdate();
					
			// closes the statement
			statement.close();
					
			// closes the connection
			con.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("chat message send error");
		}
	}
	
	public void createCompetition(String username, String start, String end, String summary) // works
	{
		//convert string to timestamp
	    java.sql.Timestamp compStart = java.sql.Timestamp.valueOf(start);
	    java.sql.Timestamp compEnd = java.sql.Timestamp.valueOf(end);
		
		try
		{
			// Here we create our query where u state which fields u want to insert data
			statement = con.prepareStatement("INSERT INTO competitie(account_naam_eigenaar, start, einde, omschrijving)VALUES(?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
//			// the ? represents anonymous values
//					
			statement.setString(1, username);
			statement.setTimestamp(2, compStart);
			statement.setTimestamp(3, compEnd);
			statement.setString(4, summary);
		
			// execute your query
			statement.execute();	// needs to be execute because executeQuery doesn't work with PK retrieval
			
			result = statement.getGeneratedKeys(); //returns competitionID
			
			if(result.next())
			{
				int compID = result.getInt(1);
				System.out.println("do i get the PK " + compID);
				
			}
					
			// closes the statement
			statement.close();
					
			// closes the connection
			con.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("create error");
		}
	}
	
	public void createGame( int competitionID, String username, String opponent)// works
	{
		try
		{
			// Here we create our query where u state which fields u want to insert data
			statement = con.prepareStatement("INSERT INTO spel(competitie_id, account_naam_uitdager, account_naam_tegenstander, moment_uitdaging, bord_naam, letterset_naam)VALUES(?,?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
//			// the ? represents anonymous values
//					
			statement.setInt(1, competitionID);
			statement.setString(2, username);
			statement.setString(3, opponent);
			statement.setTimestamp(4, getCurrentTimeStamp());
			statement.setString(5,"Standard");
			statement.setString(6,"NL");
			
			// execute your query
			statement.execute();	// needs to be execute because executeQuery doesn't work with PK retrieval
			
			result = statement.getGeneratedKeys(); //returns gameID
			
			if(result.next())
			{
				int gameID = result.getInt(1);
				System.out.println("do i get the PK " + gameID);
				
			}
					
			// closes the statement
			statement.close();
					
			// closes the connection
			con.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("create error");
		}
	}
}


/* aanmaken door:
 * DatabaseHandler dbh = DatabaseHandler.getInstance();
 * 
 * en aanroepen door:
 * dbh. --------
 * 
 * aangemaakt door: Michael
 * login check
 * register check and register name and password
 * create competition, create game
 * chat send,
 * 
 * 
 * to use multiple methods con.close(); needs to be removed. otherwise u get an closed connection error.
 * 
 */
