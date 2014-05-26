package gui;

public class UpdateGUIThread extends Thread{
	private MainFrame mainFrame;
	private boolean running;
	public UpdateGUIThread(MainFrame mainFrame){
		this.mainFrame = mainFrame;
		this.running = true;
	}
	
	public void run(){
		while (running){	
			mainFrame.updateGUI();
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void setRunning(boolean running){
		this.running = running;
	}
}
