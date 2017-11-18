package edu.colostate.cs.cs414.p5.client_server.transmission.game.invite;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import edu.colostate.cs.cs414.p5.client_server.server.AbstractServer;
import edu.colostate.cs.cs414.p5.client_server.server.ActiveServer;
import edu.colostate.cs.cs414.p5.client_server.server.game_server.GameInviteManager;
import edu.colostate.cs.cs414.p5.client_server.server.session.ClientSession;
import edu.colostate.cs.cs414.p5.client_server.transmission.Task;
import edu.colostate.cs.cs414.p5.client_server.transmission.TaskConstents;
import edu.colostate.cs.cs414.p5.client_server.transmission.util.MessageTask;
import edu.colostate.cs.cs414.p5.client_server.transmission.util.ReadUtils;
import edu.colostate.cs.cs414.p5.client_server.transmission.util.WriteUtils;

public class GetSentInvitesTask extends Task {

	private String fromPlayer;
	
	public GetSentInvitesTask(String fromPlayer) {
		this.fromPlayer = fromPlayer;
	}
	
	public GetSentInvitesTask(DataInputStream din) throws IOException {
		this.fromPlayer = ReadUtils.readString(din);
	}
	
	@Override
	public int getTaskCode() {
		return TaskConstents.GET_SENT_INVITES_TASK;
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
			response = new MessageTask("Failed to retrieve sent invitations from the server.",MessageTask.ERROR);
		} else {
			String invites = buildInviteListAsString(inviteManager);
			response = new DisplaySentInvitesTask(invites);
		}
		sendResponse(response);
	}
	
	private String buildInviteListAsString(GameInviteManager inviteManager) {
		List<InviteTask> actualInvites = inviteManager.getInvitationsFromUser(fromPlayer);
		StringBuilder invites = new StringBuilder();
		int size;
		if(actualInvites != null && (size=actualInvites.size()) > 0) {
			for(int i=0; i<size; i++) {
				InviteTask invite = actualInvites.get(i);
				invites.append("Invitation["+i+"]: " + invite.getPlayerTo() +
						" \"" + invite.getMessage() + "\"\n");
			}
		} else {
			invites.append("You have not sent any invitations.");
		}
		return invites.toString();
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
