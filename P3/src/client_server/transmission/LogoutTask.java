package client_server.transmission;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import client_server.transmission.util.ReadUtils;
import client_server.transmission.util.WriteUtils;

public class LogoutTask extends Task {

	private String email;
	
	public LogoutTask(String email) {
		this.email = email;
	}
	
	public LogoutTask(DataInputStream din) throws IOException {
		this.email = ReadUtils.readString(din);
	}
	
	@Override
	public int getTaskCode() {
		return TaskConstents.LOGOUT_TASK;
	}

	@Override
	public byte[] toByteArray() throws IOException {
		ByteArrayOutputStream bs = WriteUtils.getByteOutputStream();
		DataOutputStream dout = WriteUtils.getDataOutputStream(bs);
		dout.writeInt(getTaskCode());
		WriteUtils.writeString(email, dout);
		return WriteUtils.getBytesAndCloseStreams(bs,dout);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}
	
	public String getEmail() {
		return email;
	}

}
