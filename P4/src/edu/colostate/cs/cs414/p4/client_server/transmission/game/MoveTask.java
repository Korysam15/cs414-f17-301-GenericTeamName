package edu.colostate.cs.cs414.p4.client_server.transmission.game;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import edu.colostate.cs.cs414.p4.banqi.BanqiGame;
import edu.colostate.cs.cs414.p4.banqi.Square;
import edu.colostate.cs.cs414.p4.client_server.transmission.Task;
import edu.colostate.cs.cs414.p4.client_server.transmission.TaskConstents;
import edu.colostate.cs.cs414.p4.client_server.transmission.util.ReadUtils;
import edu.colostate.cs.cs414.p4.client_server.transmission.util.WriteUtils;
import edu.colostate.cs.cs414.p4.user.ActivePlayer;
import edu.colostate.cs.cs414.p4.user.Player;

public class MoveTask extends Task implements GameTask{
	
	private String playerWhoMadeMove;
	private int gameID;
	private int fromX, fromY;
	private int toX, toY;
	
	public MoveTask(String playerWhoMadeMove,int gameID, int fromX, int fromY, int toX, int toY) {
		this.playerWhoMadeMove = playerWhoMadeMove;
		this.gameID = gameID;
		this.fromX = fromX;
		this.fromY = fromY;
		this.toX = toX;
		this.toY = toY;
	}
	
	public MoveTask(String playerWhoMadeMove,int gameID, Square from, Square to) {
		this(playerWhoMadeMove,gameID,from.getX(),from.getY(),to.getX(),to.getY());
	}
	
	public MoveTask(DataInputStream din) throws IOException {
		this.playerWhoMadeMove = ReadUtils.readString(din);
		this.gameID = din.readInt();
		this.fromX = din.readInt();
		this.fromY = din.readInt();
		this.toX = din.readInt();
		this.toY = din.readInt();
	}

	public int getTaskCode() {
		return TaskConstents.MOVE_TASK;
	}

	public byte[] toByteArray() throws IOException {
		ByteArrayOutputStream bs = WriteUtils.getByteOutputStream();
		DataOutputStream dout = WriteUtils.getDataOutputStream(bs);
		dout.writeInt(getTaskCode());
		WriteUtils.writeString(playerWhoMadeMove, dout);
		dout.writeInt(gameID);
		dout.writeInt(fromX);
		dout.writeInt(fromY);
		dout.writeInt(toX);
		dout.writeInt(toY);
		return WriteUtils.getBytesAndCloseStreams(bs,dout);
	}
	
	@Override
	public int getGameID() {
		return gameID;
	}

	public String toString() {
		return "[MoveTask, Taskcode: " + getTaskCode() +
				", Contents: " + gameID + "," + fromX + "," +
				fromY + "," + toX + "," + toY + "]";
	}

	public void run() {
		Player player = ActivePlayer.getInstance();
		if(player != null) {
			BanqiGame game = player.getGame(gameID);
			if(game != null) {
				makeMove(game);
				game.promptTurn(player, playerWhoMadeMove);
			}
		}
	}
	
	private void makeMove(BanqiGame game) {
		Square from = game.getSquare(fromX, fromY);
		Square to = game.getSquare(toX, toY);
		game.makeMove(from, to);
	}
	
}
