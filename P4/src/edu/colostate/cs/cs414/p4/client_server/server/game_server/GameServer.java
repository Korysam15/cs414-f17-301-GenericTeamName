package edu.colostate.cs.cs414.p4.client_server.server.game_server;

import java.io.IOException;
import java.net.InetSocketAddress;

import edu.colostate.cs.cs414.p4.client_server.server.session.ClientSession;
import edu.colostate.cs.cs414.p4.client_server.transmission.game.FlipPieceTask;
import edu.colostate.cs.cs414.p4.client_server.transmission.game.ForfeitTask;
import edu.colostate.cs.cs414.p4.client_server.transmission.game.MoveTask;
import edu.colostate.cs.cs414.p4.client_server.transmission.game.invite.AcceptInviteTask;
import edu.colostate.cs.cs414.p4.client_server.transmission.game.invite.InviteTask;
import edu.colostate.cs.cs414.p4.client_server.transmission.game.invite.RejectInviteTask;

public class GameServer extends AbstractGameServer {

	public GameServer(InetSocketAddress address) throws IOException {
		super(address);
		// TODO Auto-generated constructor stub
	}
	
	public GameServer(int port) throws IOException {
		super(port);
	}

	@Override
	public void handleGameTask(FlipPieceTask t, ClientSession client) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleGameTask(MoveTask t, ClientSession client) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleGameTask(ForfeitTask t, ClientSession client) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleInviteGameTask(InviteTask t, ClientSession client) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleInviteGameTask(AcceptInviteTask t, ClientSession client) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleInviteGameTask(RejectInviteTask t, ClientSession client) {
		// TODO Auto-generated method stub

	}

}
