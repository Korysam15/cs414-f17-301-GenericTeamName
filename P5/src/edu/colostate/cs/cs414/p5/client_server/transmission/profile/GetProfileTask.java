package edu.colostate.cs.cs414.p5.client_server.transmission.profile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import edu.colostate.cs.cs414.p5.client_server.logger.Logger;
import edu.colostate.cs.cs414.p5.client_server.server.profile_manager.ProfileManager;
import edu.colostate.cs.cs414.p5.client_server.server.session.SessionManager;
import edu.colostate.cs.cs414.p5.client_server.transmission.Task;
import edu.colostate.cs.cs414.p5.client_server.transmission.TaskConstents;
import edu.colostate.cs.cs414.p5.client_server.transmission.util.MessageTask;
import edu.colostate.cs.cs414.p5.client_server.transmission.util.ReadUtils;
import edu.colostate.cs.cs414.p5.client_server.transmission.util.WriteUtils;
import edu.colostate.cs.cs414.p5.user.Profile;

public class GetProfileTask extends ProfileTask {

	private String playerWhoHasIt;

	public GetProfileTask(String playerWhoWantsIt, String playerWhoHasIt) {
		super(playerWhoWantsIt);
		this.playerWhoHasIt = playerWhoHasIt;
	}

	public GetProfileTask(DataInputStream din) throws IOException {
		super(din);
		playerWhoHasIt = ReadUtils.readString(din);
	}

	@Override
	public int getTaskCode() {
		return TaskConstents.GET_PROFILE_TASK;
	}

	@Override
	public void writeBytes(DataOutputStream dout) throws IOException {
		super.writeBytes(dout);
		WriteUtils.writeString(playerWhoHasIt, dout);		
	}

	public String toString() {
		return "[GetProfileTask, Taskcode: " + getTaskCode() +
				", Contents: " + getRequester() + "," + playerWhoHasIt + "]";
	}

	@Override
	public void run() {
		SessionManager server = SessionManager.getInstance();
		ProfileManager profileManager = ProfileManager.getInstance();

		if(server != null && server != null) {
			Task response;
			Profile profile = profileManager.getProfile(playerWhoHasIt);
			if(profile != null) {
				response = new DisplayProfileTask(profile.toString());
			} else {
				Logger.getInstance().info(super.getRequester() + " requested to see a profile for: " +
						playerWhoHasIt + " but that profile does not exists.");
				response = new MessageTask(playerWhoHasIt + " does not appear to have a profile...", MessageTask.ERROR);
			}
			server.sendToClient(response, super.getRequester());
		} else {
			Logger.getInstance().error("Failed to retrieve " + playerWhoHasIt +
					" profile for " + super.getRequester() + ".\n" + 
					" This error is due to the SessionManager or ProfileManager being null.");
		}
	}

}

