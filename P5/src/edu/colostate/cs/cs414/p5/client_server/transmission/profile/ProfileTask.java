/**
 * 
 */
package edu.colostate.cs.cs414.p5.client_server.transmission.profile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import edu.colostate.cs.cs414.p5.client_server.transmission.Task;
import edu.colostate.cs.cs414.p5.client_server.transmission.util.ReadUtils;
import edu.colostate.cs.cs414.p5.client_server.transmission.util.WriteUtils;

/**
 * @author pflagert
 *
 */
public abstract class ProfileTask extends Task {

	private String requester;
	
	public ProfileTask(String requester) {
		this.requester = requester;
	}
	
	public ProfileTask(DataInputStream din) throws IOException {
		this.requester = ReadUtils.readString(din);
	}

	@Override
	public void writeBytes(DataOutputStream dout) throws IOException {
		WriteUtils.writeString(requester, dout);
	}
	
	public String getRequester() {
		return this.requester;
	}

}
