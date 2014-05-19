package gui;
import javax.swing.JFrame;
import javax.swing.JOptionPane;



@SuppressWarnings({ "serial" })
public class RoleWindow extends JFrame
{
	/////this method returns a string with the chosen value, if th eplayer chooses cancel, nothing is returned
	public String showChangeRole()
	{

		Object[] possibilities = {"Administrator", "Moderator", "Spectator", "Player"};
		String s = (String)JOptionPane.showInputDialog(
		                    this,
		                    "What role would you like to choose?",
		                    null, JOptionPane.PLAIN_MESSAGE,
		                    
		                    null, possibilities,
		                    "Player");
		
		
		if ((s != null) && (s.length() > 0)) {
		    System.out.println("Attempting to change role to: " + s);
		    return s;
		}
		else
		{
			return null;
		}
		   
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
