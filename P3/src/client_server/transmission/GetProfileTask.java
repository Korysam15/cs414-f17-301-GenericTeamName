package client_server.transmission;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import client_server.transmission.util.ReadUtils;
import client_server.transmission.util.WriteUtils;
import user.ActivePlayer;
import user.Player;

public class GetProfileTask extends Task {

	private String playerWhoWantsIt;
	private String playerWhoHasIt;
	
	public GetProfileTask(String playerWhoWantsIt, String playerWhoHasIt) {
		this.playerWhoWantsIt = playerWhoWantsIt;
		this.playerWhoHasIt = playerWhoHasIt;
	}
	
	public GetProfileTask(DataInputStream din) throws IOException {
		playerWhoWantsIt = ReadUtils.readString(din);
		playerWhoHasIt = ReadUtils.readString(din);
	}
	
	@Override
	public int getTaskCode() {
		return TaskConstents.GET_PROFILE_TASK;
	}

	public String toString() {
		return "GetProfileTask, Taskcode: " + getTaskCode() +
				", Contents: " + playerWhoWantsIt + "," + playerWhoHasIt;
	}
	@Override
	public byte[] toByteArray() throws IOException {
		ByteArrayOutputStream bs = WriteUtils.getByteOutputStream();
		DataOutputStream dout = WriteUtils.getDataOutputStream(bs);
		dout.writeInt(getTaskCode());
		WriteUtils.writeString(playerWhoWantsIt, dout);
		WriteUtils.writeString(playerWhoHasIt, dout);
		return WriteUtils.getBytesAndCloseStreams(bs,dout);
	}

	@Override
	public void run() {
		Player player = ActivePlayer.getInstance();
		if(player != null) {
			String profile = player.getProfile().toString();
			Task display = new DisplayProfileTask(profile);
			Task forward = new ForwardTask(playerWhoHasIt,display,playerWhoWantsIt);
			try {
				player.getClient().sendToServer(forward);
			} catch (IOException e) {

			}
		}

	}

}
