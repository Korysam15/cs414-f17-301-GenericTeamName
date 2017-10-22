package client_server.transmission;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import client_server.transmission.util.ReadUtils;
import client_server.transmission.util.WriteUtils;

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
		return TaskConstents.INVITE_TASK;
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
		System.out.println(this.playerFrom + this.message);
	}
}
