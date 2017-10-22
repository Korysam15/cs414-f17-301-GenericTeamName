package client_server.transmission;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import banqi.BanqiGame;
import banqi.Square;
import client_server.transmission.util.ReadUtils;
import client_server.transmission.util.WriteUtils;
import user.ActivePlayer;
import user.Player;

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
		return WriteUtils.getBytesAndCloseStreams(bs,dout);
	}

	public String toString() {
		return "Taskcode: " + getTaskCode() + "\n" + playerWhoMadeMove 
				+ " is flipping the piece on [" + fromX + "][" + fromY + "]";
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
