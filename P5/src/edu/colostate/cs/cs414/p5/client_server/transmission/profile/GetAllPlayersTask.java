package edu.colostate.cs.cs414.p5.client_server.transmission.profile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import edu.colostate.cs.cs414.p5.client_server.server.AbstractServer;
import edu.colostate.cs.cs414.p5.client_server.server.ActiveServer;
import edu.colostate.cs.cs414.p5.client_server.server.registry.AbstractRegistry;
import edu.colostate.cs.cs414.p5.client_server.server.registry.ActiveRegistry;
import edu.colostate.cs.cs414.p5.client_server.transmission.Task;
import edu.colostate.cs.cs414.p5.client_server.transmission.TaskConstents;
import edu.colostate.cs.cs414.p5.client_server.transmission.util.MessageTask;
import edu.colostate.cs.cs414.p5.client_server.transmission.util.ReadUtils;
import edu.colostate.cs.cs414.p5.client_server.transmission.util.WriteUtils;

public class GetAllPlayersTask extends Task {

	private static final String RED="\033[0;31m";
	private static final String YELLOW="\033[0;33m";
	private static final String WHITE="\033[0;37m";
	private static final String NORM="\033[0m";
	
	private String playerWhoWantsToSee;
	public GetAllPlayersTask(String fromPlayer) {
		this.playerWhoWantsToSee = fromPlayer;
	}
	
	public GetAllPlayersTask(DataInputStream din) throws IOException {
		this.playerWhoWantsToSee = ReadUtils.readString(din);
	}
	
	@Override
	public int getTaskCode() {
		return TaskConstents.GET_ALL_PLAYERS_TASK;
	}

	@Override
	public void writeBytes(DataOutputStream dout) throws IOException {
		WriteUtils.writeString(playerWhoWantsToSee, dout);
	}

	@Override
	public void run() {
		AbstractRegistry registry = ActiveRegistry.getInstance();
		AbstractServer server = ActiveServer.getInstance();
		Task response;
		if(registry != null) {
			List<String> allPlayers = registry.getAllUserNicknames();
			Collections.sort(allPlayers);
			StringBuilder message = new StringBuilder();
			for(String player: allPlayers) {
				message.append(getPlayerStatus(server,player)+"\n");
			}
			response = new DisplayPlayersTask(message.toString());
		} else {
			response = new MessageTask("Error: failed to retrieve a list of players", MessageTask.ERROR);
		}
		
		try {
			server.getRegisteredClient(playerWhoWantsToSee).send(response);
		} catch(Exception e) {
			
		}

	}
	
	private String getPlayerStatus(AbstractServer server, String nickname) {
		String status;
		if(server == null) {
			status = RED + " unkown";
		} else if(server.getRegisteredClient(nickname) != null) {
			status = WHITE + " online";
		} else {
			status = YELLOW +" offline";
		}
		
		return nickname + ":" + status + NORM;
		
	}

}
