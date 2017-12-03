package edu.colostate.cs.cs414.p5.main;

import java.io.IOException;
import java.net.InetSocketAddress;

import edu.colostate.cs.cs414.p5.client_server.logger.Logger;
import edu.colostate.cs.cs414.p5.client_server.server.AbstractServer;
import edu.colostate.cs.cs414.p5.client_server.server.ActiveServer;
import edu.colostate.cs.cs414.p5.client_server.server.game_server.GameServer;
import edu.colostate.cs.cs414.p5.client_server.server.game_server.invite_manager.GameInviteManager;
import edu.colostate.cs.cs414.p5.client_server.server.registry.ActiveRegistry;
import edu.colostate.cs.cs414.p5.client_server.server.registry.FileRegistry;

public class ServerMain {
	
	public static void main(String args[]) throws IOException, InterruptedException {
		if(args.length != 2) {
			System.out.println("Expected arguments: <port> <password-filename>");
			return;
		}
		else {
			int port;
			String passwordFile;
			try {
				port = Integer.parseInt(args[0]);
				if(port <=0 ) 
					throw new NumberFormatException();
			} catch (NumberFormatException ex) {
				System.out.println("Invalid port: " + args[0]);
				return;
			}

			passwordFile = args[1];
			Logger.getInstance().setLogLevel(Logger.LOG_LEVEL.INFO_ERROR);
			InetSocketAddress address = new InetSocketAddress(port);
			AbstractServer server = null;
			GameInviteManager.setInstanceType(GameInviteManager.FILE_INVITE_MANAGER);
			server = new GameServer(address);
			ActiveServer.setInstance(server);

			FileRegistry registry = new FileRegistry(passwordFile);
			ActiveRegistry.setInstance(registry);

			server.start();

		}
	}
}
