package edu.colostate.cs.cs414.p5.client_server.transmission.game;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import edu.colostate.cs.cs414.p5.banqi.BanqiGame;
import edu.colostate.cs.cs414.p5.client_server.transmission.TaskConstents;
import edu.colostate.cs.cs414.p5.client_server.transmission.util.ReadUtils;
import edu.colostate.cs.cs414.p5.client_server.transmission.util.WriteUtils;
import edu.colostate.cs.cs414.p5.console.AbstractConsole;
import edu.colostate.cs.cs414.p5.user.ActivePlayer;
import edu.colostate.cs.cs414.p5.user.Player;

public class InvalidGameTask extends GameTask {

	private String message;
	private int gameID;
	private boolean promptTurn;
	private GameTask attachment;
	
	public InvalidGameTask(String message,int gameID,boolean promptTurn) {
		this.message = message;
		this.gameID = gameID;
		this.promptTurn = promptTurn;
		attachment = null;
	}
	
	public InvalidGameTask(String message,int gameID) {
		this(message,gameID,false);
	}
	
	public InvalidGameTask(DataInputStream din) throws IOException {
		this.message = ReadUtils.readString(din);
		this.gameID = din.readInt();
		this.promptTurn = din.readBoolean();
		boolean attach = din.readBoolean();
		if(attach) {
			this.attachment = (GameTask) ReadUtils.readTask(din);
		}
	}
	
	public void attach(GameTask attachment) {
		this.attachment = attachment;
	}
	
	@Override
	public int getGameID() {
		return gameID;
	}

	@Override
	public int getTaskCode() {
		return TaskConstents.INVALID_GAME_TASK;
	}

	@Override
	public void writeBytes(DataOutputStream dout) throws IOException {
		WriteUtils.writeString(message, dout);
		dout.writeInt(gameID);
		dout.writeBoolean(promptTurn);
		boolean attach = (attachment != null);
		dout.writeBoolean(attach);
		if(attach) {
			WriteUtils.writeTask(attachment,dout);
		}
	}

	@Override
	public void run() {
		Player player;
		if((player = ActivePlayer.getInstance()) != null) {
			displayToPlayer(player);
			runAttachment();
			checkTurn(player);
		}
	}
	
	private void displayToPlayer(Player player) {
		AbstractConsole console = player.getConsole();
		if(console != null) {
			console.error(message);
		} else {
			System.out.println(message);
		}
	}
	
	private void runAttachment() {
		if(attachment != null) {
			attachment.run();
		}
	}
	
	private void checkTurn(Player player) {
		if(promptTurn) {
			BanqiGame playersGame = player.getGame(gameID);
			if(playersGame != null) {
				String playerOne = playersGame.getPlayerOne();
				String playerTwo = playersGame.getPlayerTwo();
				String otherPlayer;
				if(player.getNickName().equals(playerOne)) {
					otherPlayer = playerTwo;
				} else {
					otherPlayer = playerOne;
				}
				playersGame.promptTurn(player, otherPlayer);
			}
		}
	}
	

}
