package edu.colostate.cs.cs414.p4.client_server.transmission.game.invite;

import edu.colostate.cs.cs414.p4.client_server.transmission.game.GameTask;

public interface InviteGameTask extends GameTask {
	
	/**
	 * Returns a static integer that represents a {@link GameTask} that doesn't
	 * have a game assigned to it.
	 * @return {@link GameTask#UNASSIGNED_GAME_ID}
	 */
	@Override
	public default int getGameID() {
		return GameTask.UNASSIGNED_GAME_ID;
	}
}
