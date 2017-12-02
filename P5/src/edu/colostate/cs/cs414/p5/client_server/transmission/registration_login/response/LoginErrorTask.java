package edu.colostate.cs.cs414.p5.client_server.transmission.registration_login.response;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import edu.colostate.cs.cs414.p5.client_server.transmission.TaskConstents;
import edu.colostate.cs.cs414.p5.client_server.transmission.util.ReadUtils;
import edu.colostate.cs.cs414.p5.client_server.transmission.util.WriteUtils;

public class LoginErrorTask extends EntryResponseTask{

	private String errorMsg;
	
	public LoginErrorTask(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
	public LoginErrorTask(DataInputStream din) throws IOException {
		this.errorMsg = ReadUtils.readString(din);
	}
	
	@Override
	public int getTaskCode() {
		return TaskConstents.LOGIN_ERROR_TASK;
	}

	@Override
	public void writeBytes(DataOutputStream dout) throws IOException {
		WriteUtils.writeString(errorMsg, dout);		
	}

	@Override
	public void run() {
		displayMessage();
	}

	@Override
	public boolean wasSuccessful() {
		return false;
	}

	@Override
	public String getResponseMessage() {
		return errorMsg;
	}

}
