package edu.colostate.cs.cs414.p4.client_server.server.events;

import java.io.IOException;

import edu.colostate.cs.cs414.p4.client_server.server.session.ClientSession;
import edu.colostate.cs.cs414.p4.client_server.transmission.Task;

public class SendEvent implements Event {
	private final ClientSession client;
	private final Task toSend;
	
	public SendEvent(ClientSession client, Task toSend) {
		this.client = client;
		this.toSend = toSend;
	}
	
	@Override
	public void run() {
		try {
			client.send(toSend);
		} catch (IOException e) {
			// ignored, SendEvents are used for broadcast messages that likely are not crucial
		}
	}

}
