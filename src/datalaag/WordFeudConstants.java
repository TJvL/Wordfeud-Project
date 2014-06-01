package datalaag;

public final class WordFeudConstants {
	public static final int PUBLIC_GAME = 0;
	public static final int PRIVATE_GAME = 1;
	
	public static final int LOGIN_FAIL = 0;
	public static final int LOGIN_SUCCES = 1;
	
	public static final int ROLE_CHANGE_FAIL = 0;
	public static final int ROLE_CHANGE_SUCCES = 1;
	
	public static final String GAME_STATUS_FINISHED = "Finished";
	public static final String GAME_STATUS_RESIGNED = "Resigned";
	public static final String GAME_STATUS_PLAYING = "Playing";
	public static final String GAME_STATUS_REQUEST = "Request";
	
	public static final String ROLE_ADMINISTRATOR = "Administrator";
	public static final String ROLE_MODERATOR = "Moderator";
	public static final String ROLE_PLAYER = "Player";
	public static final String ROLE_SPECTATOR = "Spectator";
	
	public static final String CREATE_COMP_FAIL_DEFAULT = "Something went wrong. Competition not created.";
	public static final String CREATE_COMP_FAIL_INPUT = "Incorrect input. Please input the correct numbers.";
	public static final String CREATE_COMP_FAIL_NUMBERS = "Incorrect maximum/minimum amount of participants. Check your input.";
	public static final String CREATE_COMP_SUCCES = "Competition has been created succesfully.";
	
	public static final String CHALLENGE_ACCEPTED = "Accepted";
	public static final String CHALLENGE_REJECTED = "Rejected";
	public static final String CHALLENGE_FAIL_EXISTS = "There is already an open invite for this game";
	public static final String CHALLENGE_FAIL_CLOSED = "Competition is closed Are there enough participants yet? Also check the end date.";
	public static final String CHALLENGE_SUCCES = "Succesfully challenged player and Invite has been sent!";
	
	public static final String REGISTER_FAIL_DEFAULT = "Registration error!";
	public static final String FAIL_NAME_LENGTH = "Name must be between 3 and 15 characters.";
	public static final String FAIL_PASS_LENGTH = "Password must have minimal length of 6 characters.";
	public static final String FAIL_NAME_NOT_AVAILABLE = "Username is not available for registration.";
	public static final String FAIL_NO_MATCHING_PASS = "Passwords do not match.";
	public static final String REGISTER_SUCCESS = "Succesfully registered account.";
	
	public static final String NAMECHANGE_FAIL_DEFAULT = "Failed to change username.";
	public static final String NAMECHANGE_SUCCESS = "Succesfully changed username.";
	
	private WordFeudConstants(){		
	}
}
