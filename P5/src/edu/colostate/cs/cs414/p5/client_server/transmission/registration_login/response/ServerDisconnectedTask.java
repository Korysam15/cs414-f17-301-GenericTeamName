package edu.colostate.cs.cs414.p5.client_server.transmission.registration_login.response;

import java.io.DataInputStream;
import java.io.IOException;

import edu.colostate.cs.cs414.p5.client_server.client.AbstractClient;
import edu.colostate.cs.cs414.p5.client_server.transmission.TaskConstents;
import edu.colostate.cs.cs414.p5.console.AbstractConsole;
import edu.colostate.cs.cs414.p5.user.ActivePlayer;
import edu.colostate.cs.cs414.p5.user.Player;

public class ServerDisconnectedTask extends ExitResponseTask {

	public ServerDisconnectedTask(String message) {
		super("\033[0;31m" + message);
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
		try {
			displayMessage();
			Thread.sleep(1000); // grace before shutting down a client.
		} catch (InterruptedException e) {
			// ignore
		} finally {
			//System.exit(0);
		}
	}

	protected void displayMessage() {
		Player player;
		if((player = ActivePlayer.getInstance()) != null) {
			displayMessageToPlayer(player);
			logoffClient(player);
		} else {
			System.out.println(getResponseMessage());
		}
	}

	protected void displayMessageToPlayer(Player player) {
		AbstractConsole console = player.getConsole();
		if(console != null) {
			console.warning(getResponseMessage());
		} else {
			System.out.println(getResponseMessage());
		}
	}
	
	protected void logoffClient(Player player) {
		AbstractClient client = player.getClient();
		if(client != null) {
			client.unsetLoggedIn();
		}
	}
	

}
