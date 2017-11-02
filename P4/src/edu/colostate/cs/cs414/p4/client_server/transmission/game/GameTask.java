/**
 * 
 */
package edu.colostate.cs.cs414.p4.client_server.transmission.game;

import edu.colostate.cs.cs414.p4.client_server.transmission.Task;
import edu.colostate.cs.cs414.p4.client_server.transmission.game.invite.AcceptInviteTask;
import edu.colostate.cs.cs414.p4.client_server.transmission.game.invite.InviteTask;
import edu.colostate.cs.cs414.p4.client_server.transmission.game.invite.RejectInviteTask;

/**
 * @author pflagert
 * A simple interface to allow {@link Task}s related to game play / creation to
 * distinguish themselves from other Tasks. 
 */
public abstract class GameTask extends Task {
	
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
