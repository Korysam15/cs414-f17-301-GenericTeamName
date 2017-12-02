package edu.colostate.cs.cs414.p5.client_server.transmission.registration_login.response;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import edu.colostate.cs.cs414.p5.client_server.transmission.TaskConstents;
import edu.colostate.cs.cs414.p5.client_server.transmission.util.ReadUtils;
import edu.colostate.cs.cs414.p5.client_server.transmission.util.WriteUtils;

public class RegisterGreetingTask extends EntryResponseTask {

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
	public void writeBytes(DataOutputStream dout) throws IOException {
		WriteUtils.writeString(greeting,dout);
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
