package edu.colostate.cs.cs414.p4.client_server.transmission.registration_login.response;

import java.io.DataOutputStream;
import java.io.IOException;

public class LoginErrorTask extends EntryResponseTask{

	@Override
	public int getTaskCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeBytes(DataOutputStream dout) throws IOException {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		return null;
	}

}
