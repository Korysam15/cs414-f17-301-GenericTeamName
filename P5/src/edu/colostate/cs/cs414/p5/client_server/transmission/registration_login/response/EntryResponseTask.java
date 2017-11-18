package edu.colostate.cs.cs414.p5.client_server.transmission.registration_login.response;

import edu.colostate.cs.cs414.p5.client_server.transmission.Task;
import edu.colostate.cs.cs414.p5.console.AbstractConsole;
import edu.colostate.cs.cs414.p5.user.ActivePlayer;
import edu.colostate.cs.cs414.p5.user.Player;

/**
 * 
 * @author pflagert
 * A simple Interface to distinguish 
 */
public abstract class EntryResponseTask extends Task {
	
	/**
	 * Returns true if the player's/user's {@link EntryTask} {@link Task} was successful.
	 * @return true if the player was logged in, otherwise false.
	 */
	public abstract boolean wasSuccessful();
	
	/**
	 * Retrieves the response message from the server.
	 * @return A {@link String} response from the server.
	 */
	public abstract String getResponseMessage();
	
	protected void displayMessage() {
		Player player;
		if((player = ActivePlayer.getInstance()) != null) {
			displayMessageToPlayer(player);
		}
	}
	
	protected void displayMessageToPlayer(Player player) {
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
