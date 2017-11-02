package edu.colostate.cs.cs414.p4.client_server.transmission.game.invite;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import edu.colostate.cs.cs414.p4.client_server.transmission.TaskConstents;
import edu.colostate.cs.cs414.p4.client_server.transmission.util.ReadUtils;
import edu.colostate.cs.cs414.p4.client_server.transmission.util.WriteUtils;
import edu.colostate.cs.cs414.p4.console.AbstractConsole;
import edu.colostate.cs.cs414.p4.user.ActivePlayer;
import edu.colostate.cs.cs414.p4.user.Player;

public class RejectInviteTask extends InviteGameTask {
	private String message;
	private String playerFrom;
	
	public RejectInviteTask(String playerFrom,String message)
	{
		super();
		this.message = message;
		this.playerFrom = playerFrom;
	}
	
	public RejectInviteTask(DataInputStream din) throws IOException
	{
		this.message = ReadUtils.readString(din);
		this.playerFrom = ReadUtils.readString(din);
	}
	
	public int getTaskCode() 
	{
		return TaskConstents.REJECTINVITE_TASK;
	}
	
	@Override
	public void writeBytes(DataOutputStream dout) throws IOException {
		WriteUtils.writeString(message, dout);
		WriteUtils.writeString(playerFrom, dout);		
	}
	
	public String toString() {
		return "[RejectInviteTask, Taskcode: " + getTaskCode() +
		", Contents: " + playerFrom + "," + message + "]";
	}
	
	public void run()
	{
		Player player = ActivePlayer.getInstance();
		if(player != null) {
			AbstractConsole console = player.getConsole();
			if(console != null) {
				console.notice(this.playerFrom + this.message);
			} else {
				System.out.println(this.playerFrom + this.message);
			}
		}
	}
}
