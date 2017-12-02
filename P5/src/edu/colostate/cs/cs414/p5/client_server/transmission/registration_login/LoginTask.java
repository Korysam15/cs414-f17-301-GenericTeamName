package edu.colostate.cs.cs414.p5.client_server.transmission.registration_login;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import edu.colostate.cs.cs414.p5.client_server.transmission.TaskConstents;
import edu.colostate.cs.cs414.p5.client_server.transmission.util.ReadUtils;
import edu.colostate.cs.cs414.p5.client_server.transmission.util.WriteUtils;

public class LoginTask extends EntryTask {
	private String email;
	private String password;

	public LoginTask(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public LoginTask(DataInputStream din) throws IOException {
		this.email = ReadUtils.readString(din);
		this.password = ReadUtils.readString(din);
	}

	@Override
	public int getTaskCode() {
		return TaskConstents.LOGIN_TASK;
	}
	
	@Override
	public void writeBytes(DataOutputStream dout) throws IOException {
		WriteUtils.writeString(email,dout);
		WriteUtils.writeString(password, dout);		
	}

	@Override
	public void run() {
		// No need for a run method.
		// Registration and Login require direct integration
		// run method will not be called
		return;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	@Override
	public String getNickname() {
		throw new UnsupportedOperationException(
				"Logging in does not support the use of nicknames");
	}
}
