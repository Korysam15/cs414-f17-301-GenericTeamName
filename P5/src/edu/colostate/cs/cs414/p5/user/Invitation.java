package edu.colostate.cs.cs414.p5.user;
import java.util.ArrayList;
import java.util.List;

import edu.colostate.cs.cs414.p5.client_server.transmission.Task;
import edu.colostate.cs.cs414.p5.client_server.transmission.game.invite.InviteTask;
import edu.colostate.cs.cs414.p5.client_server.transmission.util.MultiForwardTask;

public class Invitation {
	/* GLOBAL VARIABLES */
	private String fromPlayer;
	private String message;
	private List<String> playersToInvite;
	
	
	/* Constructor */
	public Invitation(String fromPlayer,String message, List<String> playersToInvite)
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
