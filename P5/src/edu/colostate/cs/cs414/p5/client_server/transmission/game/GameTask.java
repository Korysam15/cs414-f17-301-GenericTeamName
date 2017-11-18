/**
 * 
 */
package edu.colostate.cs.cs414.p5.client_server.transmission.game;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import edu.colostate.cs.cs414.p5.client_server.transmission.Task;
import edu.colostate.cs.cs414.p5.client_server.transmission.game.invite.AcceptInviteTask;
import edu.colostate.cs.cs414.p5.client_server.transmission.game.invite.InviteTask;
import edu.colostate.cs.cs414.p5.client_server.transmission.game.invite.RejectInviteTask;
import edu.colostate.cs.cs414.p5.client_server.transmission.util.ReadUtils;
import edu.colostate.cs.cs414.p5.client_server.transmission.util.WriteUtils;

/**
 * @author pflagert
 * A simple interface to allow {@link Task}s related to game play / creation to
 * distinguish themselves from other Tasks. 
 */
public abstract class GameTask extends Task {
	
	public static final String PLAYER_NOT_SET = " ";
	protected String playerOne;
	protected String playerTwo;
	
	protected GameTask(String playerOne, String playerTwo) {
		this.playerOne = playerOne;
		this.playerTwo = playerTwo;
	}
	
	protected GameTask(String playerWhoMade) {
		this(playerWhoMade, PLAYER_NOT_SET);
	}
	
	protected GameTask() {
		this(PLAYER_NOT_SET);
	}
	
	protected GameTask(DataInputStream din) throws IOException {
		this.playerOne = ReadUtils.readString(din);
		this.playerTwo = ReadUtils.readString(din);
	}
	
	@Override
	public void writeBytes(DataOutputStream dout) throws IOException {
		WriteUtils.writeString(playerOne, dout);
		WriteUtils.writeString(playerTwo, dout);
	}
	
	public String getPlayerOne() {
		return playerOne;
	}
	
	public String getPlayerTwo() {
		return playerTwo;
	}	
	
	public void setPlayerOne(String player) {
		this.playerOne = player;
	}
	
	public void setPlayerTwo(String player) {
		this.playerTwo = player;
	}
	
	/**
	 * The value in which a Game's ID should be in the following tasks:
	 * {@link InviteTask}
	 * {@link RejectInviteTask}
	 * {@link AcceptInviteTask}
	 */
	public static final int UNASSIGNED_GAME_ID = Integer.MIN_VALUE;
	
	/**
	 * Returns the identifier for the game that an instance of GameTask is referring to.
	 * @return Either the unique identifier assigned to the game, 
	 * or {@link #UNASSIGNED_GAME_ID}
	 */
	public abstract int getGameID();
}
