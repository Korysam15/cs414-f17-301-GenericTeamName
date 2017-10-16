import java.util.ArrayList;

public class Player {
	/* GLOBAL VARIABLES */
	private String email; // Unique
	private String password;
	private String nickName; // Unique
	private Invitation invite;
	private BanqiGame game;
//	private Profile profile;
//	private ArrayList<Piece> pieces;

	/* Constructor */
	public Player(String email,String password,String nickName)
	{
		if(checkIfRegistered(email,nickName))
		{
			System.out.println("The email address or nickname you entered is already in use.\n Please try again with a different email and nickname.");
		}
		else
		{
			this.email = email;
			this.password = password;
			this.nickName = nickName;
		}
	}

	/* Checks if a new player is already registered to the system */
	public boolean checkIfRegistered(String email, String nickName)
	{
		// Query to server and check if the player has already been made
		return false;
	}

	/* Allows a player to make a new Banqi Game */
	public void createNewGame(BanqiGame game)
	{
		game.startNewGame();
	}

	/* Creates a new invitation with a message and an arraylist of players to send it to */
	public void createInvitation(String message,ArrayList<Player> playersToInvite)
	{
		invite = new Invitation(message,playersToInvite);
	}

	public void sendInvitation()
	{
		invite.sendInvite();
	}

	public void handleMessageFromServer(Object message) 
	{
		if(message.toString().contains("#invite"))
		{
			// accept or reject
		}
		else
		{
//		x	clientUserInterface.display(message.toString());
		}
	}

	public void acceptInvitation()
	{

	}

	public void rejectInvitation()
	{

	}

	public String toString()
	{
		return "Email: " + this.email + " Nickname: " + this.nickName + " Password: " + this.password;
	}
}
