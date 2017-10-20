package client_server.transmission;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import client_server.transmission.util.ReadUtils;
import client_server.transmission.util.WriteUtils;

public class RegisterTask extends Task {
	private String email;
	private String nickname;
	private String password;
	
	public RegisterTask(String email, String nickname, String password) {
		this.email = email;
		this.nickname = nickname;
		this.password = password;
	}
	
	public RegisterTask(DataInputStream din) throws IOException {
		this.email = ReadUtils.readString(din);
		this.nickname = ReadUtils.readString(din);
		this.password = ReadUtils.readString(din);
	}
	
	@Override
	public byte[] toByteArray() throws IOException {
		ByteArrayOutputStream bs = WriteUtils.getByteOutputStream();
		DataOutputStream dout = WriteUtils.getDataOutputStream(bs);
		dout.writeInt(getTaskCode());
		WriteUtils.writeString(email,dout);
		WriteUtils.writeString(nickname,dout);
		WriteUtils.writeString(password, dout);
		return WriteUtils.getBytesAndCloseStreams(bs,dout);
	}
	
	@Override
	public int getTaskCode() {
		return TaskConstents.REGISTER_TASK;
	}
	
	@Override
	public void run() {
		// No need for a run method.
		// Registration and Login require direct integration
		return;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getNickname() {
		return nickname;
	}
	
	public String getPassword() {
		return password;
	}
}
