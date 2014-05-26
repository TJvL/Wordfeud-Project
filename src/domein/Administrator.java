package domein;

import datalaag.DatabaseHandler;

public class Administrator  extends Role
{
	
	private DatabaseHandler dbh = DatabaseHandler.getInstance();

	
	public Administrator(boolean hasPermissions)
		{
			super(hasPermissions);
		}
	
	public String adminRegister(String usernameField, char[] passInputField, char[] confirmPassInputField, boolean admin, boolean mod, boolean player)
	{	
		boolean adminSelected = admin;
		boolean modSelected = mod;
		boolean playerSelected = player;
		boolean allFieldFilled = false;
		
		String username = usernameField;
		char[] passInput = passInputField;
		char[] confirmPassInput = confirmPassInputField;
		
		String retValue = "U need to fill in all the fields";

		String password = "";
		for (char c : passInput){
			password = password + c;
		}
		String passConfirm = "";
		for (char c : confirmPassInput){
			passConfirm = passConfirm + c;
		}

		if((!username.isEmpty() && !password.isEmpty() && !passConfirm.isEmpty()))
		{
			
			if((adminSelected || modSelected || playerSelected))
			{
				if (username.length() < 3 || username.length() > 15) 
				{
					retValue = "Username must be between 3 and 15 characters.";
				}
				else if (password.length() < 6) 
				{
					retValue = "The password must contain at least 6 characters.";
				}
				else if (!password.equals(passConfirm)) 
				{
					retValue = "The given passwords do not match.";
				}
				else
				{	
					allFieldFilled = true;
					dbh.adminRegister(username, password, adminSelected, modSelected, playerSelected);
					retValue = "Register confirmed";
				}
			}
			else
			{
				retValue = "U need to select at least 1 role";
			}
		}
		return retValue + "---" + allFieldFilled;
	}
	
}
