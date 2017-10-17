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
public class TestTask extends Task {

	private String msg;

	public TestTask() {
		super();
		msg = null;
	}
	
	public TestTask(DataInputStream din) throws IOException {
		/* assume this is done already */
		// int someTaskCode = din.readInt() 
		
		this.msg = ReadUtils.readString(din);
	
		
		/* let the caller close the DataInputStream */
		// din.close() 
	}

	public int getTaskCode() {
		return TaskConstents.TEST_TASK;
	}

	public byte[] toByteArray() throws IOException {
		ByteArrayOutputStream bs = WriteUtils.getByteOutputStream();
		DataOutputStream dout = WriteUtils.getDataOutputStream(bs);

		WriteUtils.writeString(msg,dout);
		byte[] data = WriteUtils.getBytesAndCloseStreams(bs,dout);
		return data;
	}

	public String toString() {
		return "Taskcode: " + TaskConstents.TEST_TASK + " Message: " + msg ;
	}

	public void run() {
		System.out.println("It worked.... Kinda, I think.  I guess we will see..." );
		for(int i=0; i<10; i++) {
			System.out.println(msg);
		}
		System.out.println("Well did it work????");
	}

}
