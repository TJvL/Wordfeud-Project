package datalaag;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseHandler {
	final private String url = "jdbc:mysql://databases.aii.avans.nl:3306/manschou_db2";	//location of the database
	final private String user = "";	//is the database user
	final private String password = ""; //the password of the user
	private Connection con;
	
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
			con = DriverManager.getConnection(url, user, password);
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
			PreparedStatement statement = con.prepareStatement("SELECT * FROM account WHERE naam = '" + username + "' AND wachtwoord = '" + password + "'");
			
			ResultSet result = statement.executeQuery();
			
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
			PreparedStatement statement = con.prepareStatement("SELECT * FROM account WHERE naam = '" + username + "'");
			
			ResultSet result = statement.executeQuery();
			
			if(!result.next())
			{
			 System.out.println("username is available");
			 
				try
				{
					// Here we create our query where u state which fields u want to insert data
//					statement = con.prepareStatement("INSERT INTO account(naam.wachtwoord)VALUES(?.?)");
//					// the ? represents anonymous values
//							
//					statement.setString(1, username);
//					statement.setString(2, password);
					
					String sql = "INSERT INTO account " + "VALUES ('" + username + "', '" + password + "')";
					
					// execute your query
					statement.executeUpdate(sql);
							
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
 * 
 */
