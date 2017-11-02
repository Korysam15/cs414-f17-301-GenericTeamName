package edu.colostate.cs.cs414.p4.user;
import java.util.ArrayList;

import edu.colostate.cs.cs414.p4.client_server.transmission.MultiForwardTask;
import edu.colostate.cs.cs414.p4.client_server.transmission.Task;
import edu.colostate.cs.cs414.p4.client_server.transmission.game.InviteTask;

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
	
	public String getPlayerFrom()
	{
		return fromPlayer;
	}
	
	public String getMessage()
	{
		return message;
	}
	
	public Task toTask()
	{
		Task invite = new InviteTask(fromPlayer,message);
		return new MultiForwardTask(fromPlayer,invite,playersToInvite);
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
