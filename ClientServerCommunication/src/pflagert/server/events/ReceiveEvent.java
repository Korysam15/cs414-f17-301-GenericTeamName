package pflagert.server.events;

import java.io.IOException;

import pflagert.server.session.ClientSession;

public class ReceiveEvent implements Event{

	private ClientSession client;
	
	public ReceiveEvent(ClientSession client) {
		if(client == null)
			throw new IllegalArgumentException("The client in a ReceiveEvent can not be null");
		else
			this.client = client;
	}
	
	@Override
	public void run() {
		try {
			client.receive();
		} catch (IOException e) {
			System.out.println("Receive failed");
		}
	}

}
