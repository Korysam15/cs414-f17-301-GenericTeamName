package edu.colostate.cs.cs414.p4.client_server.transmission.game.invite;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import edu.colostate.cs.cs414.p4.client_server.transmission.game.GameTask;

public abstract class InviteGameTask extends GameTask {
	
	protected InviteGameTask(String playerOne, String playerTwo) {
		super(playerOne, playerTwo);
	}
	
	protected InviteGameTask(String playerOne) {
		super(playerOne);
	}
	
	protected InviteGameTask() {
		super();
	}
	
	protected InviteGameTask(DataInputStream din) throws IOException {
		super(din);
	}
	
	public void writeBytes(DataOutputStream dout) throws IOException {
		super.writeBytes(dout);
	}
	/**
	 * Returns a static integer that represents a {@link GameTask} that doesn't
	 * have a game assigned to it.
	 * @return {@link GameTask#UNASSIGNED_GAME_ID}
	 */
	public int getGameID() {
		return GameTask.UNASSIGNED_GAME_ID;
	}
}
