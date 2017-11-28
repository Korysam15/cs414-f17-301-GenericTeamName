package edu.colostate.cs.cs414.p5.client_server.transmission.game;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

import edu.colostate.cs.cs414.p5.banqi.BanqiGame;
import edu.colostate.cs.cs414.p5.client_server.transmission.TaskConstents;
import edu.colostate.cs.cs414.p5.user.ActivePlayer;
import edu.colostate.cs.cs414.p5.user.Player;

/**
 * 
 * @author pflagert
 *
 */
public class CreateGameTask extends GameTask {
	private int gameID;
	
	public CreateGameTask(String playerOne, String playerTwo)
	{
		super(playerOne,playerTwo);
		Random rand = new Random();
		this.gameID = rand.nextInt();
	}
	
	public CreateGameTask(int gameID, String playerOne, String playerTwo) {
		super(playerOne,playerTwo);
		this.gameID = gameID;
	}
	
	public CreateGameTask(DataInputStream din) throws IOException
	{
		super(din);
		gameID = din.readInt();
	}
	
	public int getTaskCode() 
	{
		return TaskConstents.CREATGAME_TASK;
	}

	@Override
	public void writeBytes(DataOutputStream dout) throws IOException {
		super.writeBytes(dout);
		dout.writeInt(gameID);		
	}
	
	public String toString() {
		return "[CreateGameTask, Taskcode: " + getTaskCode() + ", Contents: " 
				+ gameID + "," + playerOne + "," + playerTwo + "]";
	}
	
	public int getGameID() {
		return gameID;
	}
	
	public void run()
	{
		Player player = ActivePlayer.getInstance();
		if(player != null) {
			createGame(player);
		}
	}
	
	private void createGame(Player player) {
		BanqiGame game = new BanqiGame(gameID);
		player.addGame(gameID, game);
	}
}
