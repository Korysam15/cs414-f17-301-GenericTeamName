package edu.colostate.cs.cs414.p4.client_server.transmission.game;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import edu.colostate.cs.cs414.p4.client_server.transmission.TaskConstents;
import edu.colostate.cs.cs414.p4.client_server.transmission.util.ReadUtils;
import edu.colostate.cs.cs414.p4.client_server.transmission.util.WriteUtils;

public class InvalidGameTask extends GameTask {

	private String message;
	private int gameID;
	
	public InvalidGameTask(String message,int gameID) {
		this.message = message;
		this.gameID = gameID;
	}
	
	public InvalidGameTask(DataInputStream din) throws IOException {
		this.message = ReadUtils.readString(din);
		this.gameID = din.readInt();
	}
	
	@Override
	public int getGameID() {
		return gameID;
	}

	@Override
	public int getTaskCode() {
		return TaskConstents.INVALID_GAME_TASK;
	}

	@Override
	public void writeBytes(DataOutputStream dout) throws IOException {
		WriteUtils.writeString(message, dout);
		dout.writeInt(gameID);
	}

	@Override
	public void run() {
		System.out.println("Need to implement run method in: " + this.getClass());
	}

}
