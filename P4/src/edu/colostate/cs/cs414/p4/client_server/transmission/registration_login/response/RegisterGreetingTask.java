package edu.colostate.cs.cs414.p4.client_server.transmission.registration_login.response;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import edu.colostate.cs.cs414.p4.client_server.transmission.Task;
import edu.colostate.cs.cs414.p4.client_server.transmission.TaskConstents;
import edu.colostate.cs.cs414.p4.client_server.transmission.util.ReadUtils;
import edu.colostate.cs.cs414.p4.client_server.transmission.util.WriteUtils;

public class RegisterGreetingTask extends Task implements EntryResponse {

	private String greeting;
	
	public RegisterGreetingTask(String greeting) {
		this.greeting = greeting;
	}
	
	public RegisterGreetingTask(DataInputStream din) throws IOException {
		this.greeting = ReadUtils.readString(din);
	}
	
	@Override
	public int getTaskCode() {
		return TaskConstents.REGISTER_GREETING_TASK;
	}

	@Override
	public byte[] toByteArray() throws IOException {
		ByteArrayOutputStream bs = WriteUtils.getByteOutputStream();
		DataOutputStream dout = WriteUtils.getDataOutputStream(bs);
		dout.writeInt(getTaskCode());
		WriteUtils.writeString(greeting,dout);
		return WriteUtils.getBytesAndCloseStreams(bs,dout);
	}

	@Override
	public void run() {
		displayMessage();
	}

	@Override
	public boolean wasSuccessful() {
		return true;
	}

	@Override
	public String getResponseMessage() {
		return greeting;
	}

}
