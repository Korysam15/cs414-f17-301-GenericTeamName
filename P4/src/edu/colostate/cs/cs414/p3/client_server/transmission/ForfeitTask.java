package edu.colostate.cs.cs414.p3.client_server.transmission;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import edu.colostate.cs.cs414.p3.client_server.transmission.util.ReadUtils;
import edu.colostate.cs.cs414.p3.client_server.transmission.util.WriteUtils;

public class ForfeitTask extends Task {
	private int gameID;
	private UpdateRecordTask update;
	private String message;
	public ForfeitTask(int gameID, UpdateRecordTask update, String message)
	{
		this.gameID = gameID;
		this.update = update;
		this.message = message;
	}
	
	
	public ForfeitTask(DataInputStream din) throws IOException 
	{
		this.gameID = din.readInt();
		this.update = (UpdateRecordTask) ReadUtils.readTask(din);
		this.message = ReadUtils.readString(din);
	}

	public int getTaskCode() 
	{
		return TaskConstents.FORFEIT_TASK;
	}

	public byte[] toByteArray() throws IOException 
	{
		ByteArrayOutputStream bs = WriteUtils.getByteOutputStream();
		DataOutputStream dout = WriteUtils.getDataOutputStream(bs);
		dout.writeInt(getTaskCode());
		dout.writeInt(this.gameID);
		WriteUtils.writeTask(this.update, dout);
		WriteUtils.writeString(this.message, dout);
		return WriteUtils.getBytesAndCloseStreams(bs,dout);
	}
	
	public String toString() {
		return "[ForfeitTask, Taskcode: " + getTaskCode() + ", Contents: " +
				gameID+","+update+","+message+"]";
	}
	
	public UpdateRecordTask getUpdateRecordTask()
	{
		return this.update;
	}

	public void run() 
	{
		this.update.run();
		System.out.println(this.message);
	}

}
