package edu.colostate.cs.cs414.p4.client_server.server.game_server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

import edu.colostate.cs.cs414.p4.client_server.server.Server;
import edu.colostate.cs.cs414.p4.client_server.server.session.ClientSession;
import edu.colostate.cs.cs414.p4.client_server.transmission.Task;
import edu.colostate.cs.cs414.p4.client_server.transmission.game.*;
import edu.colostate.cs.cs414.p4.client_server.transmission.game.invite.*;
import edu.colostate.cs.cs414.p4.client_server.transmission.util.ForwardTask;
import edu.colostate.cs.cs414.p4.client_server.transmission.util.MultiForwardTask;

public abstract class AbstractGameServer extends Server {

	public AbstractGameServer(InetSocketAddress address) throws IOException {
		super(address);
	}
	
	public AbstractGameServer(int port) throws IOException {
		super(port);
	}
	
	protected void sendGameTaskResponse(GameTask t, ClientSession client) {
		if(client != null && t != null) {
			try {
				client.send(t);
			} catch(IOException e) {
				log("Failed to send response to: " + client);
			}
		}
	}
	
	@Override
	public void handleTask(Task t) {
		if(t instanceof GameTask) {
			handleGameTask((GameTask)t, null);
		} else {
			super.handleTask(t);
		}
	}
	
	@Override
	public void handleTask(Task t, ClientSession client) {
		if(t instanceof GameTask) {
			log("Performing Game Task: " + t + " for " + client.getID() + " [" + client + "].");
			handleGameTask((GameTask)t, client);
		} else if(t instanceof ForwardTask) {
			handleForwardTask((ForwardTask) t, client);
		} else if(t instanceof MultiForwardTask) {
			handleMultiForwardTask((MultiForwardTask) t,client);
		}
		else {
			super.handleTask(t, client);
		}
	}
	
	private void handleForwardTask(ForwardTask t, ClientSession client) {
		Task forwarded = t.getTask();
		if(forwarded instanceof GameTask) {
			GameTask gameTask = (GameTask) forwarded;
			gameTask.setPlayerOne(client.getID());
			gameTask.setPlayerTwo(t.getPlayerTo());
			handleGameTask(gameTask, client);
			// t.run()
		} else {
			super.handleTask(t,client);
		}
	}
	
	private void handleMultiForwardTask(MultiForwardTask t, ClientSession client) {
		Task forwarded = t.getTask();
		if(forwarded instanceof GameTask) {
			GameTask gameTask = (GameTask) forwarded;
			gameTask.setPlayerOne(client.getID());
			List<String> toPlayers = t.getPlayersTo();
			for(String toPlayer: toPlayers) {
				gameTask.setPlayerTwo(toPlayer);
				handleGameTask(gameTask, client);
			}
			// t.run();
		} else {
			super.handleTask(t,client);
		}
	}

	
	private void handleGameTask(GameTask t, ClientSession client) {
		if(t.getPlayerOne().equals(GameTask.PLAYER_NOT_SET)|| 
				t.getPlayerTwo().equals(GameTask.PLAYER_NOT_SET)) {
			sendGameTaskResponse(
					new InvalidGameTask("Client side code is outdated, GameTask: " + t +
							" doesn't include both playerOne and playerTwo.\n"+
							"Either wrap in a ForwardTask, or update constructors.",t.getGameID())
					,client);
			log(" Received outdated GameTask: " + t + " from: " + client.getID() + "["+ client + "]");
		} else if(t instanceof InviteGameTask) {
			handleInviteGameTask((InviteGameTask)t, client);
		} else if(t instanceof CreateGameTask) { 
			handleGameTask((CreateGameTask) t, client);
		} else if(t instanceof FlipPieceTask) {
			handleGameTask((FlipPieceTask) t, client);
		} else if(t instanceof MoveTask) {
			handleGameTask((MoveTask) t, client);
		} else if(t instanceof ForfeitTask) {
			handleGameTask((ForfeitTask) t, client);
		} else {
			String message = "Usupported GameTask: " + t;
			sendGameTaskResponse(new InvalidGameTask(message, t.getGameID()),client);
			log(message + " from: " + client.getID() + "["+ client + "]");
		}
	}
	
	private void handleInviteGameTask(InviteGameTask t, ClientSession client) {
		if(t instanceof InviteTask) {
			handleInviteGameTask((InviteTask) t, client);
		} else if(t instanceof AcceptInviteTask) {
			handleInviteGameTask((AcceptInviteTask) t, client);
		} else if(t instanceof RejectInviteTask) {
			handleInviteGameTask((RejectInviteTask) t, client);
		} else {
			String message = "Usupported InviteGameTask: " + t;
			sendGameTaskResponse(new InvalidGameTask(message, t.getGameID()),client);
			log(message + " from: " + client.getID() + "["+ client + "]");
		}
	}
	
	public abstract void handleGameTask(FlipPieceTask t, ClientSession client);
	
	public abstract void handleGameTask(MoveTask t, ClientSession client);
	
	public abstract void handleGameTask(ForfeitTask t, ClientSession client);
	
	public abstract void handleGameTask(CreateGameTask t, ClientSession client);
	
	public abstract void handleInviteGameTask(InviteTask t, ClientSession client);
	
	public abstract void handleInviteGameTask(AcceptInviteTask t, ClientSession client);
	
	public abstract void handleInviteGameTask(RejectInviteTask t, ClientSession client); 
}
