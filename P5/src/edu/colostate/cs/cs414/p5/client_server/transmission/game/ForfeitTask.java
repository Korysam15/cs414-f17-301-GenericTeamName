package edu.colostate.cs.cs414.p5.client_server.transmission.game;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import edu.colostate.cs.cs414.p5.client_server.transmission.TaskConstents;
import edu.colostate.cs.cs414.p5.client_server.transmission.util.ReadUtils;
import edu.colostate.cs.cs414.p5.client_server.transmission.util.WriteUtils;

public class ForfeitTask extends GameTask {
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
		super(din);
		this.gameID = din.readInt();
		this.update = (UpdateRecordTask) ReadUtils.readTask(din);
		this.message = ReadUtils.readString(din);
	}

	public int getTaskCode() 
	{
		return TaskConstents.FORFEIT_TASK;
	}
	
	@Override
	public void writeBytes(DataOutputStream dout) throws IOException {
		super.writeBytes(dout);
		dout.writeInt(this.gameID);
		WriteUtils.writeTask(this.update, dout);
		WriteUtils.writeString(this.message, dout);
	}
	
	@Override
	public int getGameID() {
		return gameID;
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
