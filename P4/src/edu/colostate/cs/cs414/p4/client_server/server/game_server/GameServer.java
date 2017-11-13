package edu.colostate.cs.cs414.p4.client_server.server.game_server;

import java.io.IOException;
import java.net.InetSocketAddress;

import edu.colostate.cs.cs414.p4.client_server.server.registry.AbstractRegistry;
import edu.colostate.cs.cs414.p4.client_server.server.registry.ActiveRegistry;
import edu.colostate.cs.cs414.p4.client_server.server.session.ClientSession;
import edu.colostate.cs.cs414.p4.client_server.transmission.game.CreateGameTask;
import edu.colostate.cs.cs414.p4.client_server.transmission.game.FlipPieceTask;
import edu.colostate.cs.cs414.p4.client_server.transmission.game.ForfeitTask;
import edu.colostate.cs.cs414.p4.client_server.transmission.game.GameTask;
import edu.colostate.cs.cs414.p4.client_server.transmission.game.InvalidGameTask;
import edu.colostate.cs.cs414.p4.client_server.transmission.game.MoveTask;
import edu.colostate.cs.cs414.p4.client_server.transmission.game.invite.AcceptInviteTask;
import edu.colostate.cs.cs414.p4.client_server.transmission.game.invite.InviteTask;
import edu.colostate.cs.cs414.p4.client_server.transmission.game.invite.RejectInviteTask;

public class GameServer extends AbstractGameServer {
	
	private final GameInviteManager inviteManager;
	
	public GameServer(InetSocketAddress address) throws IOException {
		super(address);
		this.inviteManager = GameInviteManager.getInstance();
		
	}
	
	public GameServer(int port) throws IOException {
		super(port);
		this.inviteManager = GameInviteManager.getInstance();
	}
	
	private boolean isPlayerOnline(String playerID) {
		return this.getRegisteredClient(playerID) != null;
	}
	
	private boolean playerExists(String playerID) {
		AbstractRegistry registry = ActiveRegistry.getInstance();
		// if a registry exists
		// then the player exists if and only if the playerID (nickname) is taken
		if(registry != null) {
			return registry.isNicknameTaken(playerID);
		} else { // if a registry does not exist then the player only exists if they are logged in.
			return isPlayerOnline(playerID);
		}
	}
	
	private void sendGameTask(GameTask t, ClientSession client) {
		if(client != null) {
			try {
				client.send(t);
			} catch (IOException e) {
				log("Error occured while sending: " + t + " to [" + client + "]");
			}
		}
	}
	
	private void sendGameTask(GameTask t, String userID) {
		sendGameTask(t,getRegisteredClient(userID));
	}
	
	private void sendGameTaskIfOnline(GameTask t, String player) {
		if(isPlayerOnline(player)) {
			sendGameTask(t,player);
		}
	}
	
	private boolean checkAndSend(GameTask t, String toPlayer, ClientSession fromClient) {
		if(playerExists(toPlayer)) {
			sendGameTaskIfOnline(t,toPlayer);
			return true;
		} else {
			String message = toPlayer+" has removed their account";
			GameTask response = new InvalidGameTask(message,t.getGameID());
			log(fromClient.getID() + " on [" + fromClient + "] tried to send GameTask: "
					+ t + " but " + message);
			try {
				fromClient.send(response);
			} catch(IOException e) {
				log("Failed to notify: " + fromClient + " that " + message);
			}
			return false;
		}
	}
	
	@Override
	public void handleGameTask(CreateGameTask t, ClientSession client) {
		// This shouldn't have to be here
		String playerTwo = t.getPlayerTwo();
		checkAndSend(t,playerTwo,client);
	}

	@Override
	public void handleGameTask(FlipPieceTask t, ClientSession client) {
		// TODO validate FlipPieceTask
		
		// update game state
		// send FlipPieceTask to playerTwo (if online)
		String playerTwo = t.getPlayerTwo();
		checkAndSend(t,playerTwo,client);
	}

	@Override
	public void handleGameTask(MoveTask t, ClientSession client) {
		// TODO validate MoveTask
		// update game state
		
		// send MoveTask to playerTwo (if online)
		String playerTwo = t.getPlayerTwo();
		checkAndSend(t,playerTwo,client);

	}

	@Override
	public void handleGameTask(ForfeitTask t, ClientSession client) {
		// TODO validate ForfeitTask?
		// update playerOne's record to show an additional loss
		// update playerTwo's record to show an additional win
		
		// send playerTwo a notification about the forfeit (if online)
		String playerTwo = t.getPlayerTwo();
		checkAndSend(t,playerTwo,client);
	}

	@Override
	public void handleInviteGameTask(InviteTask t, ClientSession client) {
		// Send InviteTask to playerTwo (if online)
		String playerTwo = t.getPlayerTwo();
		if(checkAndSend(t,playerTwo,client)) {		
			// add invitation
			inviteManager.addInvitation(t);
		}
	}

	@Override
	public void handleInviteGameTask(AcceptInviteTask t, ClientSession client) {
		// Remove / Check if their was invitation from PlayerTwo
		if(inviteManager.removeInvitation(t.getPlayerTwo(), t.getPlayerOne())) {
			// create a game


			// send AcceptInviteTask to playerTwo (if online)
			String playerTwo = t.getPlayerTwo();
			checkAndSend(t,playerTwo,client);
		} else {
			GameTask response = new InvalidGameTask("It appears that '"
					+ t.getPlayerTwo() + 
					"' never sent you an invitation.",t.getGameID());
			this.sendGameTaskResponse(response, client);
			log(client.getID() + " on [" + client + "] "
					+ "never received an invitation, but they accepted it.");
		}
		

	}

	@Override
	public void handleInviteGameTask(RejectInviteTask t, ClientSession client) {
		// Remove / Check if their was invitation from PlayerTwo
		if(inviteManager.removeInvitation(t.getPlayerTwo(), t.getPlayerOne())) {
			// create a game
			// send RejectInviteTask to playerTwo (if online)
			String playerTwo = t.getPlayerTwo();
			checkAndSend(t,playerTwo,client);
		} else {
			// ignore the fact that there wasn't an invitation, they rejected it anyway.
			log(client.getID() + " on [" + client + "] "
					+ "never received an invitation, but they rejected it.");
		}
		

	}

}
