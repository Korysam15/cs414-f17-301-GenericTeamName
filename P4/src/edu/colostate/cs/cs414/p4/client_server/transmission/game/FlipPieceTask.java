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

public class FlipPieceTask extends Task {
	private String playerWhoMadeMove;
	private int gameID;
	private int fromX, fromY;
	
	public FlipPieceTask(String playerWhoMadeMove,int gameID, int fromX, int fromY) {
		this.playerWhoMadeMove = playerWhoMadeMove;
		this.gameID = gameID;
		this.fromX = fromX;
		this.fromY = fromY;
	}
	
	public FlipPieceTask(String playerWhoMadeMove,int gameID, Square from) {
		this(playerWhoMadeMove,gameID,from.getX(),from.getY());
	}
	
	public FlipPieceTask(DataInputStream din) throws IOException {
		this.playerWhoMadeMove = ReadUtils.readString(din);
		this.gameID = din.readInt();
		this.fromX = din.readInt();
		this.fromY = din.readInt();
	}

	public int getTaskCode() {
		return TaskConstents.FLIP_PIECE_TASK;
	}

	public byte[] toByteArray() throws IOException {
		ByteArrayOutputStream bs = WriteUtils.getByteOutputStream();
		DataOutputStream dout = WriteUtils.getDataOutputStream(bs);
		dout.writeInt(getTaskCode());
		WriteUtils.writeString(playerWhoMadeMove, dout);
		dout.writeInt(gameID);
		dout.writeInt(fromX);
		dout.writeInt(fromY);
		return WriteUtils.getBytesAndCloseStreams(bs,dout);
	}

	public String toString() {
		return "[FlipPieceTask, Taskcode: " + getTaskCode() + ", Contents: " + playerWhoMadeMove 
				+ ", " + fromX + ", " + fromY +"]";
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
		game.flipPiece(from);
	}
}
