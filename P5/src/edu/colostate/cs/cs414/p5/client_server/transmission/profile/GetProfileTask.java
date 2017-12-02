package edu.colostate.cs.cs414.p5.client_server.transmission.profile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import edu.colostate.cs.cs414.p5.client_server.transmission.Task;
import edu.colostate.cs.cs414.p5.client_server.transmission.TaskConstents;
import edu.colostate.cs.cs414.p5.client_server.transmission.util.ForwardTask;
import edu.colostate.cs.cs414.p5.client_server.transmission.util.ReadUtils;
import edu.colostate.cs.cs414.p5.client_server.transmission.util.WriteUtils;
import edu.colostate.cs.cs414.p5.user.ActivePlayer;
import edu.colostate.cs.cs414.p5.user.Player;

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
		Player player = ActivePlayer.getInstance();
		if(player != null) {
			String profile = player.getProfile().toString();
			Task display = new DisplayProfileTask(profile);
			Task forward = new ForwardTask(playerWhoHasIt,display,getRequester());
			try {
				player.getClient().sendToServer(forward);
			} catch (IOException e) {

			}
		}

	}

}
