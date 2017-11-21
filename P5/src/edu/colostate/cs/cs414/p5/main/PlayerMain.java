package edu.colostate.cs.cs414.p5.main;

import java.io.IOException;

import edu.colostate.cs.cs414.p5.client_server.client.AbstractClient;
import edu.colostate.cs.cs414.p5.client_server.client.Client;
import edu.colostate.cs.cs414.p5.client_server.logger.Logger;
import edu.colostate.cs.cs414.p5.client_server.logger.Logger.LOG_LEVEL;
import edu.colostate.cs.cs414.p5.console.AbstractConsole;
import edu.colostate.cs.cs414.p5.console.PlayerConsole;
import edu.colostate.cs.cs414.p5.user.ActivePlayer;
import edu.colostate.cs.cs414.p5.user.Player;

public class PlayerMain {
	public static void main(String[] args) throws IOException, InterruptedException {
		if(args.length != 2) 
		{
			System.out.println("Expected 2 arguments: <server-host> <server-port>");
			return;
		}
		else 
		{
			String host = args[0]; 
			int port;

			try {
				port = Integer.parseInt(args[1]);
				if(port <=0 )
				{
					throw new NumberFormatException();
				}
			} catch (NumberFormatException ex) { 
				System.out.println("Invalid port: " + args[1]);
				return;
			}
			
			Logger.getInstance().setLogLevel(LOG_LEVEL.OFF);
			
			AbstractClient client = new Client(host,port);
			client.startReceiving();
			Player player = new Player();
			ActivePlayer.setInstance(player);
			AbstractConsole console = new PlayerConsole(client,player);
			player.setConsole(console);
			console.accept();
		}
	}
}
