package edu.colostate.cs.cs414.p5.client_server.server.events;

import java.io.IOException;

import edu.colostate.cs.cs414.p5.client_server.server.session.ClientSession;

public class ReceiveEvent implements Event{

	private ClientSession client;
	
	public ReceiveEvent(ClientSession client) {
		this.client = client;
	}
	
	@Override
	public void run() {
		try {
			client.receive();
		} catch (IOException e) {
			System.out.println("Receive failed");
			e.printStackTrace();
		}
	}

}
