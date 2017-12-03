package edu.colostate.cs.cs414.p4.main;

import java.io.IOException;

import edu.colostate.cs.cs414.p4.console.AbstractConsole;
import edu.colostate.cs.cs414.p4.console.PlayerConsole;
import edu.colostate.cs.cs414.p4.user.Player;

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
			Player p = new Player(host,port);
			p.getClient().startReceiving();
			AbstractConsole console = new PlayerConsole(p);
			p.setConsole(console);
			console.accept();
		}
	}
}
