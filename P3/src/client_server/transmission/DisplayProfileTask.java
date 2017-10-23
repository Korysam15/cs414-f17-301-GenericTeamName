package client_server.transmission;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import client_server.transmission.util.ReadUtils;
import client_server.transmission.util.WriteUtils;

public class DisplayProfileTask extends Task {

	private String profile;
	
	public DisplayProfileTask(String profile) {
		this.profile = profile;
	}
	
	public DisplayProfileTask(DataInputStream din) throws IOException {
		profile = ReadUtils.readString(din);
	}
	
	@Override
	public int getTaskCode() {
		return TaskConstents.DISPLAY_PROFILE_TASK;
	}

	@Override
	public byte[] toByteArray() throws IOException {
		ByteArrayOutputStream bs = WriteUtils.getByteOutputStream();
		DataOutputStream dout = WriteUtils.getDataOutputStream(bs);
		dout.writeInt(getTaskCode());
		WriteUtils.writeString(profile, dout);
		return WriteUtils.getBytesAndCloseStreams(bs,dout);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}
