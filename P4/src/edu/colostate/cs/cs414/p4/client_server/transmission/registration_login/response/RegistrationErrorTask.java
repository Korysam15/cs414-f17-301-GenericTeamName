package edu.colostate.cs.cs414.p4.client_server.transmission.registration_login.response;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import edu.colostate.cs.cs414.p4.client_server.transmission.TaskConstents;
import edu.colostate.cs.cs414.p4.client_server.transmission.util.ReadUtils;
import edu.colostate.cs.cs414.p4.client_server.transmission.util.WriteUtils;


public class RegistrationErrorTask extends EntryResponseTask {

	private String errorMsg;
	
	public RegistrationErrorTask(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
	public RegistrationErrorTask(DataInputStream din) throws IOException {
		this.errorMsg = ReadUtils.readString(din);
	}
	
	@Override
	public int getTaskCode() {
		return TaskConstents.REGISTRATION_ERROR_TASK;
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
