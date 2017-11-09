package edu.colostate.cs.cs414.p4.client_server.transmission.game.invite;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import edu.colostate.cs.cs414.p4.banqi.BanqiGame;
import edu.colostate.cs.cs414.p4.client_server.transmission.Task;
import edu.colostate.cs.cs414.p4.client_server.transmission.TaskConstents;
import edu.colostate.cs.cs414.p4.client_server.transmission.game.CreateGameTask;
import edu.colostate.cs.cs414.p4.client_server.transmission.util.ForwardTask;
import edu.colostate.cs.cs414.p4.console.AbstractConsole;
import edu.colostate.cs.cs414.p4.user.ActivePlayer;
import edu.colostate.cs.cs414.p4.user.Player;

public class AcceptInviteTask extends InviteGameTask {

	public AcceptInviteTask(String playerWhoAccepted) {
		super(playerWhoAccepted);
	}

	public AcceptInviteTask(DataInputStream din) throws IOException {
		super(din);
	}

	public int getTaskCode() {
		return TaskConstents.ACCEPT_INVITE_TASK;
	}
	
	@Override
	public void writeBytes(DataOutputStream dout) throws IOException {
		super.writeBytes(dout);		
	}

	public String toString() {
		return "[AcceptInviteTask, Taskcode: " + getTaskCode() + 
				", Contents: " + getPlayerOne() + "]" ;
	}

	public void run() {
		Player player = ActivePlayer.getInstance();
		if(player != null) {
			displayMessage(player);
			Task gameTask = new CreateGameTask(player.getNickName(),getPlayerOne());
			gameTask.run();
			Task response = new ForwardTask(player.getNickName(),gameTask,getPlayerOne());
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
				console.notice(getPlayerOne() + " has accepted your Invitation!");
			} else {
				System.out.println(getPlayerOne() + " has accepted your Invitation!");
			}
		}
	}
	
	public void startGame(Player player, int gameID) {
		BanqiGame game = player.getGame(gameID);
		if(game != null) {
			game.promptTurn(player, getPlayerOne());
		}
	}
}
