package edu.colostate.cs.cs414.p4.client_server.transmission.game.invite;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import edu.colostate.cs.cs414.p4.client_server.server.AbstractServer;
import edu.colostate.cs.cs414.p4.client_server.server.ActiveServer;
import edu.colostate.cs.cs414.p4.client_server.server.game_server.GameInviteManager;
import edu.colostate.cs.cs414.p4.client_server.server.session.ClientSession;
import edu.colostate.cs.cs414.p4.client_server.transmission.Task;
import edu.colostate.cs.cs414.p4.client_server.transmission.TaskConstents;
import edu.colostate.cs.cs414.p4.client_server.transmission.util.MessageTask;
import edu.colostate.cs.cs414.p4.client_server.transmission.util.ReadUtils;
import edu.colostate.cs.cs414.p4.client_server.transmission.util.WriteUtils;

public class GetInvitesTask extends Task {

	private String fromPlayer;
	
	public GetInvitesTask(String fromPlayer) {
		this.fromPlayer = fromPlayer;
	}
	
	public GetInvitesTask(DataInputStream din) throws IOException {
		this.fromPlayer = ReadUtils.readString(din);
	}
	
	
	@Override
	public int getTaskCode() {
		return TaskConstents.GET_INVITES_TASK;
	}

	@Override
	public void writeBytes(DataOutputStream dout) throws IOException {
		WriteUtils.writeString(fromPlayer, dout);
	}

	@Override
	public void run() {
		GameInviteManager inviteManager = GameInviteManager.getInstance();
		Task response;
		if(inviteManager == null) {
			response = new MessageTask("Failed to retrieve invitations from the server.",MessageTask.ERROR);
		} else {
			List<InviteTask> invites = inviteManager.getInvitationsToUser(fromPlayer);
			invites = (invites == null) ? new LinkedList<InviteTask>() : invites;
			response = new DisplayInvitesTask(invites);
		}
		sendResponse(response);
		
	}
	
	private void sendResponse(Task response) {
		AbstractServer server = ActiveServer.getInstance();
		if(server != null) {
			ClientSession toClient = server.getRegisteredClient(fromPlayer);
			try {
				toClient.send(response);
			} catch (Exception e) {
				// ignored
			}
		}
	}

}
