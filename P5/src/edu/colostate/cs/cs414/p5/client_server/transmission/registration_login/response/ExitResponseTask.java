package edu.colostate.cs.cs414.p5.client_server.transmission.registration_login.response;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import edu.colostate.cs.cs414.p5.client_server.client.AbstractClient;
import edu.colostate.cs.cs414.p5.client_server.transmission.Task;
import edu.colostate.cs.cs414.p5.client_server.transmission.TaskConstents;
import edu.colostate.cs.cs414.p5.client_server.transmission.util.ReadUtils;
import edu.colostate.cs.cs414.p5.client_server.transmission.util.WriteUtils;
import edu.colostate.cs.cs414.p5.console.AbstractConsole;
import edu.colostate.cs.cs414.p5.user.ActivePlayer;
import edu.colostate.cs.cs414.p5.user.Player;

public class ExitResponseTask extends Task {

	private String message;

	public ExitResponseTask(String message) {
		this.message = message;
	}
	
	public ExitResponseTask(DataInputStream din) throws IOException {
		this.message = ReadUtils.readString(din);
	}
	
	@Override
	public int getTaskCode() {
		return TaskConstents.EXIT_RESPONSE_TASK;
	}

	@Override
	public void writeBytes(DataOutputStream dout) throws IOException {		
		WriteUtils.writeString(message, dout);
	}

	@Override
	public void run() {
		displayMessage();		
	}

	public String getResponseMessage() {
		return message;
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
			console.notice(getResponseMessage());
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
