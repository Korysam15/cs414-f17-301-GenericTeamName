package pflagert.transmission;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import pflagert.transmission.util.ReadUtils;
import pflagert.transmission.util.WriteUtils;

/**
 * @author pflagert
 * Just a Test for the server / client
 */
public class MessageTask extends Task {

	private String msg;

	public MessageTask() 
	{
		this("");
	}
	
	public MessageTask(String msg) 
	{
		super();
		this.msg = msg;
	}
	
	public MessageTask(DataInputStream din) throws IOException 
	{
		this.msg = ReadUtils.readString(din);
	}

	public int getTaskCode() 
	{
		return TaskConstents.TEST_TASK;
	}

	public byte[] toByteArray() throws IOException 
	{
		ByteArrayOutputStream bs = WriteUtils.getByteOutputStream();
		DataOutputStream dout = WriteUtils.getDataOutputStream(bs);
		dout.writeInt(getTaskCode());
		WriteUtils.writeString(msg,dout);
		return WriteUtils.getBytesAndCloseStreams(bs,dout);
	}

	public String toString() 
	{
		return "Taskcode: " + TaskConstents.TEST_TASK + " Message: " + msg ;
	}

	public void run() 
	{
		System.out.println(msg);
	}

}
