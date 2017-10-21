package user;
import java.util.ArrayList;

public class Invitation {
	/* GLOBAL VARIABLES */
	private String fromPlayer;
	private String message;
	private ArrayList<String> playersToInvite;
	
	
	/* Constructor */
	public Invitation(String fromPlayer,String message, ArrayList<String> playersToInvite)
	{
		this.fromPlayer = fromPlayer;
		this.message = message;
		this.playersToInvite = playersToInvite;
	}
	
	public void sendInvite()
	{
		for(String player : this.playersToInvite)
		{
			/* Use server to send invite to players */
			System.out.println(player);
		}
	}
	
	public String getPlayerFrom()
	{
		return fromPlayer;
	}
	
	public String getMessage()
	{
		return message;
	}
	
	public ArrayList<String> getPlayersToInvite() 
	{
		ArrayList<String> ret = new ArrayList<String>(playersToInvite.size());
		for(String nickName: playersToInvite)
		{
			ret.add(nickName);
		}
		return ret;
	}
}
