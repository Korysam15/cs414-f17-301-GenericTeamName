package edu.colostate.cs.cs414.p5.client_server.server.game_server;

import java.io.IOException;
import java.net.InetSocketAddress;

import edu.colostate.cs.cs414.p5.client_server.server.registry.AbstractRegistry;
import edu.colostate.cs.cs414.p5.client_server.server.registry.ActiveRegistry;
import edu.colostate.cs.cs414.p5.client_server.server.session.ClientSession;
import edu.colostate.cs.cs414.p5.client_server.transmission.game.CreateGameTask;
import edu.colostate.cs.cs414.p5.client_server.transmission.game.FlipPieceTask;
import edu.colostate.cs.cs414.p5.client_server.transmission.game.ForfeitTask;
import edu.colostate.cs.cs414.p5.client_server.transmission.game.GameTask;
import edu.colostate.cs.cs414.p5.client_server.transmission.game.InvalidGameTask;
import edu.colostate.cs.cs414.p5.client_server.transmission.game.MoveTask;
import edu.colostate.cs.cs414.p5.client_server.transmission.game.OpenGameTask;
import edu.colostate.cs.cs414.p5.client_server.transmission.game.invite.AcceptInviteTask;
import edu.colostate.cs.cs414.p5.client_server.transmission.game.invite.InviteTask;
import edu.colostate.cs.cs414.p5.client_server.transmission.game.invite.RejectInviteTask;

/**
 * 
 * @author pflagert
 *
 */
public class GameServer extends AbstractGameServer {

	private final GameInviteManager inviteManager;
	private final GameManager gameManager;

	public GameServer(InetSocketAddress address) throws IOException {
		super(address);
		this.inviteManager = GameInviteManager.getInstance();
		gameManager = GameManager.getInstance();
	}

	public GameServer(int port) throws IOException {
		this(new InetSocketAddress(port));
	}

	private boolean playerExists(String playerID) {
		AbstractRegistry registry = ActiveRegistry.getInstance();
		// if a registry exists
		// then the player exists if and only if the playerID (nickname) is taken
		if(registry != null) {
			return registry.isNicknameTaken(playerID);
		} else { // if a registry does not exist then the player only exists if they are logged in.
			return sessionManager.isClientOnline(playerID);
		}
	}
	
	private void sendInvalidGameTaskResponse(InvalidGameTask response, ClientSession client) {
		try {
			client.send(response);
		} catch(IOException e) {
			LOG.error("Failed to send InvalidGameTask to: " + client);
		}
	}

	private void sendGameTask(GameTask t, String userID) {
		sessionManager.sendToClient(t, userID);
	}

	private void sendGameTaskIfOnline(GameTask t, String player) {
		if(sessionManager.isClientOnline(player)) {
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
			LOG.info(fromClient.getID() + " on [" + fromClient + "] tried to send GameTask: "
					+ t + " but " + message);
			try {
				fromClient.send(response);
			} catch(IOException e) {
				LOG.info("Failed to notify: " + fromClient + " that " + message);
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
		if(gameManager.isValidMove(t)) {
			// send FlipPieceTask to playerTwo (if online)
			String playerTwo = t.getPlayerTwo();
			checkAndSend(t,playerTwo,client);
		} else {
			InvalidGameTask response;
			if(gameManager.getGame(t.getGameID()) == null) {
				response = new InvalidGameTask("That move is not valid",t.getGameID());
			} else {
				response = new InvalidGameTask("That move is not valid",t.getGameID(),true);
				GameTask attachment = new OpenGameTask(gameManager.getGame(t.getGameID()));
				response.attach(attachment);
			}
			this.sendInvalidGameTaskResponse(response, client);
		}
	}

	@Override
	public void handleGameTask(MoveTask t, ClientSession client) {
		if(gameManager.isValidMove(t)) {
			String playerTwo = t.getPlayerTwo();
			checkAndSend(t,playerTwo,client);
		} else {
			InvalidGameTask response;
			if(gameManager.getGame(t.getGameID()) == null) {
				response = new InvalidGameTask("That move is not valid",t.getGameID());
			} else {
				response = new InvalidGameTask("That move is not valid",t.getGameID(),true);
				GameTask attachment = new OpenGameTask(gameManager.getGame(t.getGameID()));
				response.attach(attachment);
			}
			this.sendInvalidGameTaskResponse(response, client);
		}

	}

	@Override
	public void handleGameTask(ForfeitTask t, ClientSession client) {
		// TODO validate ForfeitTask?
		// update playerOne's record to show an additional loss
		// update playerTwo's record to show an additional win
		gameManager.closeGame(t.getGameID());

		// send playerTwo a notification about the forfeit (if online)
		String playerTwo = t.getPlayerTwo();
		checkAndSend(t,playerTwo,client);
	}

	@Override
	public void handleInviteGameTask(InviteTask t, ClientSession client) {
		// Send InviteTask to playerTwo (if online)
		String playerOne = t.getPlayerOne();
		String playerTwo = t.getPlayerTwo();
		if(playerOne.equals(playerTwo)) {
			LOG.info(playerOne + " on [" + client + "] attempted to invite himself to a game.");
			try {
				client.send(new InvalidGameTask("You can not invite your self to a game...",t.getGameID()));
			} catch (IOException e) {
				LOG.error("Failed to send message to: " + playerOne + " that they invited them selves to a game");
			}
		} else if(checkAndSend(t,playerTwo,client)) {		
			// add invitation
			inviteManager.addInvitation(t);
		}
	}

	@Override
	public void handleInviteGameTask(AcceptInviteTask t, ClientSession client) {
		// Remove / Check if their was invitation from PlayerTwo
		String playerOne = t.getPlayerOne();
		String playerTwo = t.getPlayerTwo();
		if(inviteManager.removeInvitation(playerTwo, playerOne)) {
			int gameID = gameManager.createGame(playerTwo,playerOne);
			CreateGameTask createGame = new CreateGameTask(gameID,playerTwo,playerOne);
			t.setCreateGameTask(createGame);

			checkAndSend(t,playerTwo,client);
		} else {
			GameTask response = new InvalidGameTask("It appears that '"
					+ t.getPlayerTwo() + 
					"' never sent you an invitation.",t.getGameID());
			this.sendGameTaskResponse(response, client);
			LOG.info(client.getID() + " on [" + client + "] "
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
			LOG.info(client.getID() + " on [" + client + "] "
					+ "never received an invitation, but they rejected it.");
		}
	}

}
