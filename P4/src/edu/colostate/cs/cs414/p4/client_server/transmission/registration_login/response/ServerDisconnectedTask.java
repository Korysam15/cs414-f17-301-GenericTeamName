package edu.colostate.cs.cs414.p4.client_server.transmission.registration_login.response;

import java.io.DataInputStream;
import java.io.IOException;

import edu.colostate.cs.cs414.p4.client_server.transmission.TaskConstents;

public class ServerDisconnectedTask extends ExitResponseTask {

	public ServerDisconnectedTask(String message) {
		super(message);
	}
	
	public ServerDisconnectedTask(DataInputStream din) throws IOException {
		super(din);
	}
	
	@Override
	public int getTaskCode() {
		return TaskConstents.SERVER_DISCONNECTED_TASK;
	}
	
	@Override
	public void run() {
		super.run();
		try { // grace before shutting down a client.
			Thread.sleep(1000);
		} catch (InterruptedException e) {}
		
		System.exit(0);
	}

}
