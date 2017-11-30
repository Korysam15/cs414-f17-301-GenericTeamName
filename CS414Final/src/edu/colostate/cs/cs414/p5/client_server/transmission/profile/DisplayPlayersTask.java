package edu.colostate.cs.cs414.p5.client_server.transmission.profile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import edu.colostate.cs.cs414.p5.client_server.transmission.Task;
import edu.colostate.cs.cs414.p5.client_server.transmission.TaskConstents;
import edu.colostate.cs.cs414.p5.client_server.transmission.util.ReadUtils;
import edu.colostate.cs.cs414.p5.client_server.transmission.util.WriteUtils;
import edu.colostate.cs.cs414.p5.console.AbstractConsole;
import edu.colostate.cs.cs414.p5.user.ActivePlayer;
import edu.colostate.cs.cs414.p5.user.Player;

public class DisplayPlayersTask extends Task {

	private String allPlayers;
	
	public DisplayPlayersTask(String allPlayers) {
		this.allPlayers = allPlayers;
	}
	
	public DisplayPlayersTask(DataInputStream din) throws IOException {
		this.allPlayers = ReadUtils.readString(din);
	}
	
	
	@Override
	public int getTaskCode() {
		return TaskConstents.DISPLAY_PLAYERS_TASK;
	}

	@Override
	public void writeBytes(DataOutputStream dout) throws IOException {
		WriteUtils.writeString(allPlayers, dout);
	}

	@Override
	public void run() {
		Player player = ActivePlayer.getInstance();
		if(player != null) {
			AbstractConsole console = player.getConsole();
			if(console != null) {
				console.display(allPlayers);
			} else {
				System.out.println(allPlayers);
			}
		}
	}

}
