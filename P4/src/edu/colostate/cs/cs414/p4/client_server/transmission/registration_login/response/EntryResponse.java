package edu.colostate.cs.cs414.p4.client_server.transmission.registration_login.response;

import edu.colostate.cs.cs414.p4.console.AbstractConsole;
import edu.colostate.cs.cs414.p4.user.ActivePlayer;
import edu.colostate.cs.cs414.p4.user.Player;

/**
 * 
 * @author pflagert
 * A simple Interface to distinguish 
 */
public interface EntryResponse {
	
	/**
	 * Returns true if the player's/user's {@link EntryAble} {@link Task} was successful.
	 * @return true if the player was logged in, otherwise false.
	 */
	public boolean wasSuccessful();
	
	/**
	 * Retrieves the response message from the server.
	 * @return A {@link String} response from the server.
	 */
	public String getResponseMessage();
	
	public default void displayMessage() {
		Player player;
		if((player = ActivePlayer.getInstance()) != null) {
			displayMessageToPlayer(player);
		}
	}
	
	public default void displayMessageToPlayer(Player player) {
		AbstractConsole console = player.getConsole();
		if(console != null) {
			if(wasSuccessful()) {
				console.notice(getResponseMessage());
			} else {
				console.error(getResponseMessage());
			}
		} else {
			System.out.println(getResponseMessage());
		}
	}
	
}
