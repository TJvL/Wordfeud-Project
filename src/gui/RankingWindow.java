package gui;

import java.awt.Dimension;

import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class RankingWindow extends JDialog
{

	private DefaultListModel<String> myLM = new DefaultListModel<String>();
	private JList<String> myList = new JList<String>(myLM);
	private JScrollPane mySP = new JScrollPane(myList);
	private JLabel title = new JLabel();


	public void showRanking()
	{
		this.setModal(true);
		this.setLayout(null);
		this.setPreferredSize(new Dimension(400, 550));

		for (int i = 0; i < 24; i++)
		{
			if(i < 9)
			{
				myLM.add(i, i+1 + ":   Hoi");
			}
			else
			{
				myLM.add(i, i+1 + ": Hoi");
			}
			
		}
		this.add(mySP);
		this.add(title);
		title.setText("Ranking of competition : Comp Name.");
		title.setBounds(75, 10, 225, 25);
		mySP.setBounds(100, 50,200,435);
		
		
		this.setLocationRelativeTo(null);
		this.pack();
		this.setVisible(true);
		this.setResizable(false);
	}


}
