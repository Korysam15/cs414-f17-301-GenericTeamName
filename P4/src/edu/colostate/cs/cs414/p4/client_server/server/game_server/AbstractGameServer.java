package edu.colostate.cs.cs414.p4.client_server.server.game_server;

import java.io.IOException;
import java.net.InetSocketAddress;

import edu.colostate.cs.cs414.p4.client_server.server.Server;

public abstract class AbstractGameServer extends Server {

	public AbstractGameServer(InetSocketAddress address) throws IOException {
		super(address);
	}
	
	public AbstractGameServer(int port) throws IOException {
		super(port);
	}

}
