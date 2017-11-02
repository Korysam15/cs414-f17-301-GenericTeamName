package edu.colostate.cs.cs414.p4.client_server.transmission.registration_login;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import edu.colostate.cs.cs414.p4.client_server.transmission.Task;
import edu.colostate.cs.cs414.p4.client_server.transmission.TaskConstents;
import edu.colostate.cs.cs414.p4.client_server.transmission.util.ReadUtils;
import edu.colostate.cs.cs414.p4.client_server.transmission.util.WriteUtils;

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
	public void writeBytes(DataOutputStream dout) throws IOException {
		WriteUtils.writeString(email, dout);		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}
	
	public String getEmail() {
		return email;
	}

}
