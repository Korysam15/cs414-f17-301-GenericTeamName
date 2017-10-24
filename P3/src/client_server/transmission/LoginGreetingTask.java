package client_server.transmission;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import client_server.server.AbstractServer;
import client_server.server.ActiveServer;
import client_server.transmission.util.ReadUtils;
import client_server.transmission.util.WriteUtils;
import console.AbstractConsole;
import user.ActivePlayer;
import user.Player;

public class LoginGreetingTask extends Task {

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
	public byte[] toByteArray() throws IOException {
		ByteArrayOutputStream bs = WriteUtils.getByteOutputStream();
		DataOutputStream dout = WriteUtils.getDataOutputStream(bs);
		dout.writeInt(getTaskCode());
		WriteUtils.writeString(greeting,dout);
		WriteUtils.writeString(playerNickname, dout);
		return WriteUtils.getBytesAndCloseStreams(bs,dout);
	}

	@Override
	public void run() 
	{
		Player player;
		AbstractServer server;
		if((player = ActivePlayer.getInstance()) != null) {
			displayToPlayer(player);
			player.setNickName(playerNickname);
		} else if((server = ActiveServer.getInstance()) !=null ) {
			displayToServer(server);
		}
	}
	
	private void displayToPlayer(Player player) {
		AbstractConsole console = player.getConsole();
		if(console != null) {
			console.display(greeting);
		} else {
			System.out.println(greeting);
		}
	}
	
	private void displayToServer(AbstractServer server) {
		System.out.println(greeting);
	}
}
