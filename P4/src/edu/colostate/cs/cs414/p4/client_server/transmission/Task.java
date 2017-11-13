/**
 * 
 */
package edu.colostate.cs.cs414.p4.client_server.transmission;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import edu.colostate.cs.cs414.p4.client_server.transmission.util.WriteUtils;

/**
 * @author pflagert
 *
 */
public abstract class Task implements Runnable {	
	
	public abstract int getTaskCode();
	
	public byte[] toByteArray() throws IOException {
		ByteArrayOutputStream bs = WriteUtils.getByteOutputStream();
		DataOutputStream dout = WriteUtils.getDataOutputStream(bs);
		dout.writeInt(getTaskCode());
		writeBytes(dout);
		return WriteUtils.getBytesAndCloseStreams(bs,dout);
	}
	
	
	public abstract void writeBytes(DataOutputStream dout) throws IOException;
	
	public String toString() {
		return getClass().getSimpleName() + ": Task Code: " + this.getTaskCode();
	}
	
	public abstract void run();
}
