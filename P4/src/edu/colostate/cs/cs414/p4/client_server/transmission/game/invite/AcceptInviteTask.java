package edu.colostate.cs.cs414.p4.client_server.transmission.game.invite;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import edu.colostate.cs.cs414.p4.banqi.BanqiGame;
import edu.colostate.cs.cs414.p4.client_server.transmission.Task;
import edu.colostate.cs.cs414.p4.client_server.transmission.TaskConstents;
import edu.colostate.cs.cs414.p4.client_server.transmission.game.CreateGameTask;
import edu.colostate.cs.cs414.p4.client_server.transmission.util.ForwardTask;
import edu.colostate.cs.cs414.p4.client_server.transmission.util.ReadUtils;
import edu.colostate.cs.cs414.p4.client_server.transmission.util.WriteUtils;
import edu.colostate.cs.cs414.p4.console.AbstractConsole;
import edu.colostate.cs.cs414.p4.user.ActivePlayer;
import edu.colostate.cs.cs414.p4.user.Player;

public class AcceptInviteTask extends InviteGameTask {
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
	
	@Override
	public void writeBytes(DataOutputStream dout) throws IOException {
		WriteUtils.writeString(playerWhoAccepted,dout);		
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
				console.notice(playerWhoAccepted + " has accepted your Invitation!");
			} else {
				System.out.println(playerWhoAccepted + " has accepted your Invitation!");
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
