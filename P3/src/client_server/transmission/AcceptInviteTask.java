package client_server.transmission;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import banqi.BanqiGame;
import client_server.transmission.util.ReadUtils;
import client_server.transmission.util.WriteUtils;
import console.AbstractConsole;
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
		return "[AcceptInviteTask, Taskcode: " + getTaskCode() + 
				", Contents: " + playerWhoAccepted + "]" ;
	}

	public void run() {
		Player player = ActivePlayer.getInstance();
		if(player != null) {
			displayMessage(player);
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
	
	private void displayMessage(Player player) {
		if(player != null) {
			AbstractConsole console = player.getConsole();
			if(console != null) {
				console.notice(playerWhoAccepted + " has accepted your Invitiation!");
			} else {
				System.out.println(playerWhoAccepted + " has accepted your Invitiation!");
			}
		}
	}
	
	public void startGame(Player player, int gameID) {
		BanqiGame game = player.getGame(gameID);
		if(game != null) {
			game.promptTurn(player, playerWhoAccepted);
		}
	}
}
