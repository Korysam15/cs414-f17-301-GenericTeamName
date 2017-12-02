package edu.colostate.cs.cs414.p3.client_server.transmission;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import edu.colostate.cs.cs414.p3.client_server.transmission.util.ReadUtils;
import edu.colostate.cs.cs414.p3.client_server.transmission.util.WriteUtils;
import edu.colostate.cs.cs414.p3.console.AbstractConsole;
import edu.colostate.cs.cs414.p3.user.ActivePlayer;
import edu.colostate.cs.cs414.p3.user.Player;

public class RejectInviteTask extends Task {
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
	
	public String toString() {
		return "[RejectInviteTask, Taskcode: " + getTaskCode() +
		", Contents: " + playerFrom + "," + message + "]";
	}
	
	public byte[] toByteArray() throws IOException 
	{
		ByteArrayOutputStream bs = WriteUtils.getByteOutputStream();
		DataOutputStream dout = WriteUtils.getDataOutputStream(bs);
		dout.writeInt(getTaskCode());
		WriteUtils.writeString(message, dout);
		WriteUtils.writeString(playerFrom, dout);
		return WriteUtils.getBytesAndCloseStreams(bs,dout);
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
