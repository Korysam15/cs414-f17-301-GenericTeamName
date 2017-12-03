package edu.colostate.cs.cs414.p5.client_server.transmission.registration_login.response;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import edu.colostate.cs.cs414.p5.client_server.transmission.TaskConstents;
import edu.colostate.cs.cs414.p5.client_server.transmission.util.ReadUtils;
import edu.colostate.cs.cs414.p5.client_server.transmission.util.WriteUtils;
import edu.colostate.cs.cs414.p5.user.ActivePlayer;
import edu.colostate.cs.cs414.p5.user.Player;

public class LoginGreetingTask extends EntryResponseTask {

	private String greeting;
	private String playerNickname;
	
	public LoginGreetingTask(String greeting, String playerNickname) {
		this.greeting = greeting;
		this.playerNickname = playerNickname;
	}
	
	public LoginGreetingTask(DataInputStream din) throws IOException {
		this.greeting = ReadUtils.readString(din);
		this.playerNickname = ReadUtils.readString(din);
	}
	
	@Override
	public int getTaskCode() {
		return TaskConstents.LOGIN_GREETING_TASK;
	}
	
	@Override
	public void writeBytes(DataOutputStream dout) throws IOException {
		WriteUtils.writeString(greeting,dout);
		WriteUtils.writeString(playerNickname, dout);		
	}

	@Override
	public void run() 
	{
		Player player;
		if((player = ActivePlayer.getInstance()) != null) {
			player.setNickName(playerNickname);
			displayMessageToPlayer(player);
		}
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
