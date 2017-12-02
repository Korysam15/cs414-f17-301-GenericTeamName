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
	
	private static void start(String host, String port) throws IOException {
		int portNum;

		try {
			portNum = Integer.parseInt(port);
			if(portNum <=0 || portNum > 65535) {
				throw new NumberFormatException();
			} 
		} catch (NumberFormatException ex) { 
			System.out.println("Invalid port: " + port);
			return;
		}
		
		AbstractClient client = new Client(host,portNum);
		client.startReceiving();
		Player player = new Player();
		ActivePlayer.setInstance(player);
		AbstractConsole console = new PlayerConsole(client,player);
		player.setConsole(console);
		console.accept();
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {
		if(args.length < 2) {
			System.out.println("Expected 2 arguments: <server-host> <server-port>");
			return;
		} else if(args.length == 3) {
			String host = args[0]; 
			String port = args[1];
			String logLevel = args[2];
			
			LOG_LEVEL level = null;
			
			try {
				level = Logger.LOG_LEVEL.valueOf(logLevel);
			} catch(Exception e) {
				level = null;
			}
			
			if(level == null) {
				System.out.println("Invalid LOG level: " + logLevel);
				return;
			} else {
				Logger.getInstance().setLogLevel(level);
				start(host,port);
			}
		} else { // args.length == 2
			String host = args[0]; 
			String port = args[1];
			Logger.getInstance().setLogLevel(LOG_LEVEL.OFF);
			start(host,port);
		}
	}
}
