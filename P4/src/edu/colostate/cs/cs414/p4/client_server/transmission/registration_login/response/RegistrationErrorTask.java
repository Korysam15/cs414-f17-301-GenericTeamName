package edu.colostate.cs.cs414.p4.client_server.transmission.registration_login.response;

import java.io.IOException;

import edu.colostate.cs.cs414.p4.client_server.transmission.Task;

public class RegistrationErrorTask extends Task implements EntryResponse {

	@Override
	public int getTaskCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public byte[] toByteArray() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
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
