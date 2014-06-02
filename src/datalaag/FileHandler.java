package datalaag;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class FileHandler {

	private static FileHandler fileHandler = new FileHandler();
	
	private FileHandler(){}
	
	public static FileHandler getInstance(){
		return fileHandler;
	}
	
	/*
	 * This method is used to buffer images for use in the application GUI.
	 * param = Path to the file(must be an image) in question.
	 * return = Returns the targeted image file as a BufferedImage object or returns null if an error occured.
	 */
	public BufferedImage readImage(String aFileName)
		{
			BufferedImage image = null;
			try
				{
					image = ImageIO.read(new File(aFileName));
					return image;
				}
			catch (IOException error)
				{
					System.err.println(error.getMessage());
					System.err.println(error.getStackTrace());
					System.out.println(aFileName + " not loaded");
					return null;
				}
		}	
}


/* aanmaken door:
 * DatabaseHandler dbh = DatabaseHandler.getInstance();
 * 
 * en aanroepen door:
 * dbh. --------
 */
