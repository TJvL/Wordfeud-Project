package domein;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import datalaag.DatabaseHandler;

public class CompetitionManager {
	
	private HashMap<String, Competition> joinedCompetitions;
	private HashMap<String, Competition> competitions;

	public CompetitionManager(WordFeud wf) {
		
		competitions = new HashMap<String, Competition>();
		joinedCompetitions = new HashMap<String, Competition>();
	}

	public void loadJoinedCompetitions(String username) {
		
		joinedCompetitions.clear();
		System.out.println("Loading joined competitions...");

		ArrayList<String> comps = DatabaseHandler.getInstance().fetchJoinedCompetitions(username);
		if (!comps.isEmpty()) {
			for (String comp : comps) {
				String[] compData = comp.split("---");

				// --------TEST CODE---------
				System.out.println("RAW DATA PRINT COMP: " + compData[0] + " "
						+ compData[1] + " " + compData[2] + " " + compData[3]
						+ " " + compData[4] + " " + compData[5] + " " + compData[6]);
				// --------TEST CODE---------

				joinedCompetitions.put(compData[0], new Competition(Integer
						.parseInt(compData[0]), compData[1], compData[2], compData[3],
						compData[4], Integer.parseInt(compData[5]), Integer
								.parseInt(compData[6])));
			}
			
			System.out.println("Succesfully loaded all joined competitions.");
		} else {
			System.out.println("No joined competitions to load.");
		}
	}
	
	public void loadAllCompetitions(String username){
		competitions.clear();
		System.out.println("Loading all other competitions...");

		ArrayList<String> comps = DatabaseHandler.getInstance().fetchAllCompetitions(username);
		if (!comps.isEmpty()) {
			for (String comp : comps) {
				String[] compData = comp.split("---");

				// --------TEST CODE---------
				System.out.println("RAW DATA PRINT COMP: " + compData[0] + " "
						+ compData[1] + " " + compData[2] + " " + compData[3]
						+ " " + compData[4] + " " + compData[5] + " " + compData[6]);
				// --------TEST CODE---------

				competitions.put(compData[0], new Competition(Integer
						.parseInt(compData[0]), compData[1], compData[2], compData[3],
						compData[4], Integer.parseInt(compData[5]), Integer
								.parseInt(compData[6])));
			}
			
			System.out.println("Succesfully loaded all other competitions.");
		} else {
			System.out.println("No other competitions to load.");
		}
	}
	
	public boolean joinCompetition(int compID, String username){
		boolean actionSuccesfull = false;
		if (competitions.get(compID).isRoomForMore()){
			DatabaseHandler.getInstance().joinCompetition(compID, username);
			Competition comp = competitions.get(compID);
			joinedCompetitions.put(Integer.toString(compID), comp);
			competitions.remove(compID);
			actionSuccesfull = true;
		}
		else{
			System.err.println("ERROR: Could not join selected competition");
		}
		return actionSuccesfull;
	}

	public void createCompetition(String currentUsername, String summary, String endDate, int minParticipants, int maxParticipants) {
		
		DatabaseHandler.getInstance().createCompetition(currentUsername, endDate, summary, minParticipants, maxParticipants);
		this.loadJoinedCompetitions(currentUsername);
		this.loadAllCompetitions(currentUsername);
	}
	
	public void updateEachParticipants(){
		Iterator<Entry<String, Competition>> it = joinedCompetitions.entrySet().iterator();
		while (it.hasNext()){
			Map.Entry<String, Competition>  pair = (Map.Entry<String, Competition>)it.next();
			pair.getValue().updateParticipants();
		}
		
		it = competitions.entrySet().iterator();
		while (it.hasNext()){
			Map.Entry<String, Competition>  pair = (Map.Entry<String, Competition>)it.next();
			pair.getValue().updateParticipants();
		}
	}
	
	public HashMap<String, Competition>  getCompetitionsMap(){
		return competitions;
	}

	public ArrayList<String> getParticipantList(int compID) {
		ArrayList<String> participantList;
		
		if (joinedCompetitions.containsKey(compID)){
			participantList = joinedCompetitions.get(compID).getParticipants();
			return participantList;
		}
		else if (competitions.containsKey(compID)){
			participantList = competitions.get(compID).getParticipants();
			return participantList;
		}
		else{
		return null;
		}
	}
}
