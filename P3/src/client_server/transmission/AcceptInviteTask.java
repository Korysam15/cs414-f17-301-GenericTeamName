package client_server.transmission;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import banqi.BanqiGame;
import client_server.transmission.util.ReadUtils;
import client_server.transmission.util.WriteUtils;
import user.ActivePlayer;
import user.Player;

public class AcceptInviteTask extends Task {
	private String playerWhoAccepted;

	public AcceptInviteTask(String playerWhoAccepted) {
		super();
		this.playerWhoAccepted = playerWhoAccepted;
	}

	public AcceptInviteTask(DataInputStream din) throws IOException {
		this.playerWhoAccepted = ReadUtils.readString(din);
	}

	public int getTaskCode() {
		return TaskConstents.ACCEPT_INVITE_TASK;
	}

	public byte[] toByteArray() throws IOException {
		ByteArrayOutputStream bs = WriteUtils.getByteOutputStream();
		DataOutputStream dout = WriteUtils.getDataOutputStream(bs);
		dout.writeInt(getTaskCode());
		WriteUtils.writeString(playerWhoAccepted,dout);
		return WriteUtils.getBytesAndCloseStreams(bs,dout);
	}

	public String toString() {
		return "Taskcode: " + TaskConstents.ACCEPT_INVITE_TASK+ " Message: " + playerWhoAccepted ;
	}

	public void run() {
		System.out.println(playerWhoAccepted + " has accepted your Invitiation!");
		Player player = ActivePlayer.getInstance();
		if(player != null) {
			Task gameTask = new CreateGameTask(player.getNickName(),playerWhoAccepted);
			gameTask.run();
			Task response = new ForwardTask(player.getNickName(),gameTask,playerWhoAccepted);
			try {
				player.getClient().sendToServer(response);
			} catch (IOException e) {
			}
			int gameID = ((CreateGameTask) gameTask).getGameID();
			startGame(player, gameID);
		}
	}
	
	public void startGame(Player player, int gameID) {
		BanqiGame game = player.getGame(gameID);
		if(game != null) {
			game.promptTurn(player, playerWhoAccepted);
		}
	}
}
