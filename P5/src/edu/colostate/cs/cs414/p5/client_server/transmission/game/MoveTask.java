package edu.colostate.cs.cs414.p5.client_server.transmission.game;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import edu.colostate.cs.cs414.p5.banqi.BanqiGame;
import edu.colostate.cs.cs414.p5.banqi.Square;
import edu.colostate.cs.cs414.p5.client_server.transmission.TaskConstents;
import edu.colostate.cs.cs414.p5.user.ActivePlayer;
import edu.colostate.cs.cs414.p5.user.Player;

/**
 * 
 * @author pflagert
 *
 */
public class MoveTask extends GameTask{
	
	private int gameID;
	private int fromX, fromY;
	private int toX, toY;
	
	public MoveTask(String playerWhoMadeMove,int gameID, int fromX, int fromY, int toX, int toY) {
		super(playerWhoMadeMove);
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
		super(din);
		this.gameID = din.readInt();
		this.fromX = din.readInt();
		this.fromY = din.readInt();
		this.toX = din.readInt();
		this.toY = din.readInt();
	}

	public int getTaskCode() {
		return TaskConstents.MOVE_TASK;
	}
	
	@Override
	public void writeBytes(DataOutputStream dout) throws IOException {
		super.writeBytes(dout);
		dout.writeInt(gameID);
		dout.writeInt(fromX);
		dout.writeInt(fromY);
		dout.writeInt(toX);
		dout.writeInt(toY);
	}
	
	@Override
	public int getGameID() {
		return gameID;
	}
	
	public int getFromX() {
		return fromX;
	}
	
	public int getFromY() {
		return fromY;
	}
	
	public int getToX() {
		return toX;
	}
	
	public int getToY() {
		return toY;
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
				game.promptTurn(player, getPlayerOne());
			}
		}
	}
	
	private void makeMove(BanqiGame game) {
		Square from = game.getSquare(fromX, fromY);
		Square to = game.getSquare(toX, toY);
		game.makeMove(from, to);
	}
	
}
