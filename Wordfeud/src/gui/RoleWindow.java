package gui;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;


public class RoleWindow extends JFrame
{

	
	public String ShowchangeRole()
	{

		Object[] possibilities = {"Administrator", "Moderator", "Spectator", "Player"};
		String s = (String)JOptionPane.showInputDialog(
		                    this,
		                    "What role would you like to choose?",
		                    null, JOptionPane.PLAIN_MESSAGE,
		                    
		                    null, possibilities,
		                    "Player");
		
		
		if ((s != null) && (s.length() > 0)) {
		    System.out.println(s);
		    return s;
		}
		else
		{
			return null;
		}
		   
	}
	
	
	//private JDialog testBox = new JDialog();
	private JOptionPane testOP = new JOptionPane("What role?", JOptionPane.QUESTION_MESSAGE,JOptionPane.OK_CANCEL_OPTION);
	
	
	
	
	
	
	
	
	
}
