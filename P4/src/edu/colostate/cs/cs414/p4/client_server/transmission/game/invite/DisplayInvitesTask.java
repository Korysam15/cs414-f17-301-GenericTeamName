package edu.colostate.cs.cs414.p4.client_server.transmission.game.invite;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.colostate.cs.cs414.p4.client_server.transmission.Task;
import edu.colostate.cs.cs414.p4.client_server.transmission.TaskConstents;
import edu.colostate.cs.cs414.p4.client_server.transmission.util.ReadUtils;
import edu.colostate.cs.cs414.p4.client_server.transmission.util.WriteUtils;
import edu.colostate.cs.cs414.p4.console.AbstractConsole;
import edu.colostate.cs.cs414.p4.user.ActivePlayer;
import edu.colostate.cs.cs414.p4.user.Player;

public class DisplayInvitesTask extends Task {

	private List<InviteTask> invites;
	
	public DisplayInvitesTask(List<InviteTask> invites) {
		this.invites = invites;
	}
	
	public DisplayInvitesTask(DataInputStream din) throws IOException {
		List<? extends Task> toCheck = ReadUtils.readTaskList(din);
		this.invites = new ArrayList<InviteTask>(toCheck.size());
		
		for(Task t: toCheck) {
			if(t instanceof InviteTask) {
				this.invites.add((InviteTask)t);
			}
		}
	}
	
	@Override
	public int getTaskCode() {
		return TaskConstents.DISPLAY_INVITES_TASK;
	}

	@Override
	public void writeBytes(DataOutputStream dout) throws IOException {
		WriteUtils.writeTaskList(invites, dout);
	}

	@Override
	public void run() {
		if(invites.size() > 0) {
			for(InviteTask invite: invites) {
				invite.run();
			}
		} else {
			displayNoInvites();
		}
	}
	
	private void displayNoInvites() {
		Player player;
		if((player = ActivePlayer.getInstance()) != null) {
			displayToPlayer(player);
		} 
	}
	
	private void displayToPlayer(Player player) {
		AbstractConsole console = player.getConsole();
		String msg = "You do not have any pending invitations.";
		if(console != null) {
			console.notice(msg);
		} else {
			System.out.println(msg);
		}
	}

}
