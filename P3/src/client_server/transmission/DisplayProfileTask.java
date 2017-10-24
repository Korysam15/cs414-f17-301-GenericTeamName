package client_server.transmission;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import client_server.transmission.util.ReadUtils;
import client_server.transmission.util.WriteUtils;
import console.AbstractConsole;
import user.ActivePlayer;
import user.Player;

public class DisplayProfileTask extends Task {

	private String profile;
	
	public DisplayProfileTask(String profile) {
		this.profile = profile;
	}
	
	public DisplayProfileTask(DataInputStream din) throws IOException {
		profile = ReadUtils.readString(din);
	}
	
	@Override
	public int getTaskCode() {
		return TaskConstents.DISPLAY_PROFILE_TASK;
	}

	@Override
	public byte[] toByteArray() throws IOException {
		ByteArrayOutputStream bs = WriteUtils.getByteOutputStream();
		DataOutputStream dout = WriteUtils.getDataOutputStream(bs);
		dout.writeInt(getTaskCode());
		WriteUtils.writeString(profile, dout);
		return WriteUtils.getBytesAndCloseStreams(bs,dout);
	}
	
	public String toString() {
		return "DisplayProfileTask, Taskcode: " + getTaskCode();
	}

	@Override
	public void run() {
		Player player = ActivePlayer.getInstance();
		if(player != null) {
			AbstractConsole console = player.getConsole();
			if(console != null) {
				console.notice(profile);
			} else {
				System.out.println(profile);
			}
		}

	}

}
