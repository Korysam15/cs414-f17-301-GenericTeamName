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
	
	public RejectInviteTask(String playerFrom,String message)
	{
		super(playerFrom);
		this.message = message;
	}
	
	public RejectInviteTask(DataInputStream din) throws IOException
	{
		super(din);
		this.message = ReadUtils.readString(din);
	}
	
	public int getTaskCode() 
	{
		return TaskConstents.REJECTINVITE_TASK;
	}
	
	@Override
	public void writeBytes(DataOutputStream dout) throws IOException {
		super.writeBytes(dout);
		WriteUtils.writeString(message, dout);		
	}
	
	public String toString() {
		return "[RejectInviteTask, Taskcode: " + getTaskCode() +
		", Contents: " + getPlayerOne() + "," + message + "]";
	}
	
	public void run()
	{
		Player player = ActivePlayer.getInstance();
		if(player != null) {
			AbstractConsole console = player.getConsole();
			if(console != null) {
				console.notice(getPlayerOne() + this.message);
			} else {
				System.out.println(getPlayerOne() + this.message);
			}
		}
	}
}
