package edu.colostate.cs.cs414.p4.client_server.transmission.game;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

import edu.colostate.cs.cs414.p4.banqi.BanqiGame;
import edu.colostate.cs.cs414.p4.client_server.transmission.TaskConstents;
import edu.colostate.cs.cs414.p4.client_server.transmission.util.ReadUtils;
import edu.colostate.cs.cs414.p4.client_server.transmission.util.WriteUtils;
import edu.colostate.cs.cs414.p4.user.ActivePlayer;
import edu.colostate.cs.cs414.p4.user.Player;

public class CreateGameTask extends GameTask {
	private int gameID;
	private String playerOne;
	private String playerTwo;
	
	public CreateGameTask(String playerOne, String playerTwo)
	{
		super();
		Random rand = new Random();
		this.gameID = rand.nextInt();
		this.playerOne = playerOne;
		this.playerTwo = playerTwo;
	}
	
	public CreateGameTask(DataInputStream din) throws IOException
	{
		gameID = din.readInt();
		playerOne = ReadUtils.readString(din);
		playerTwo = ReadUtils.readString(din);
	}
	
	public int getTaskCode() 
	{
		return TaskConstents.CREATGAME_TASK;
	}

	@Override
	public void writeBytes(DataOutputStream dout) throws IOException {
		dout.writeInt(gameID);
		WriteUtils.writeString(playerOne, dout);
		WriteUtils.writeString(playerTwo, dout);		
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
