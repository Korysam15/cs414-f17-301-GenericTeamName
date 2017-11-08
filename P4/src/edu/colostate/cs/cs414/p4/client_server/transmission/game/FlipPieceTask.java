package edu.colostate.cs.cs414.p4.client_server.transmission.game;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import edu.colostate.cs.cs414.p4.banqi.BanqiGame;
import edu.colostate.cs.cs414.p4.banqi.Square;
import edu.colostate.cs.cs414.p4.client_server.transmission.TaskConstents;
import edu.colostate.cs.cs414.p4.user.ActivePlayer;
import edu.colostate.cs.cs414.p4.user.Player;

public class FlipPieceTask extends GameTask {
	private int gameID;
	private int fromX, fromY;
	
	public FlipPieceTask(String playerWhoMadeMove,int gameID, int fromX, int fromY) {
		super(playerWhoMadeMove);
		this.gameID = gameID;
		this.fromX = fromX;
		this.fromY = fromY;
	}
	
	public FlipPieceTask(String playerWhoMadeMove,int gameID, Square from) {
		this(playerWhoMadeMove,gameID,from.getX(),from.getY());
	}
	
	public FlipPieceTask(DataInputStream din) throws IOException {
		super(din);
		this.gameID = din.readInt();
		this.fromX = din.readInt();
		this.fromY = din.readInt();
	}

	public int getTaskCode() {
		return TaskConstents.FLIP_PIECE_TASK;
	}
	
	@Override
	public void writeBytes(DataOutputStream dout) throws IOException {
		super.writeBytes(dout);
		dout.writeInt(gameID);
		dout.writeInt(fromX);
		dout.writeInt(fromY);
	}
	
	@Override
	public int getGameID() {
		return gameID;
	}

	public String toString() {
		return "[FlipPieceTask, Taskcode: " + getTaskCode() + ", Contents: " + getPlayerOne() 
				+ ", " + fromX + ", " + fromY +"]";
	}

	public void run() {
		Player player = ActivePlayer.getInstance();
		if(player != null) {
			BanqiGame game = player.getGame(gameID);
			if(game != null) {
				makeMove(game);
				game.promptTurn(player, getPlayerOne());
			}
		}
	}
	
	private void makeMove(BanqiGame game) {
		Square from = game.getSquare(fromX, fromY);
		game.flipPiece(from);
	}

}
