package edu.colostate.cs.cs414.p5.client_server.transmission.game;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import edu.colostate.cs.cs414.p5.client_server.transmission.TaskConstents;
import edu.colostate.cs.cs414.p5.user.ActivePlayer;
import edu.colostate.cs.cs414.p5.user.Player;

public class UpdateRecordTask extends GameTask {
	private int gameID;
	private boolean won, loss, draw;
	
	public UpdateRecordTask(boolean won, boolean loss, boolean draw, int gameID) {
		this.won = won;
		this.loss = loss;
		this.draw = draw;
		this.gameID = gameID;
	}

	public UpdateRecordTask(DataInputStream din) throws IOException {
		this.won = din.readBoolean();
		this.loss = din.readBoolean();
		this.draw = din.readBoolean();
		this.gameID = din.readInt();
	}

	public int getTaskCode() {
		return TaskConstents.UPDATERECORD_TASK;
	}
	
	@Override
	public void writeBytes(DataOutputStream dout) throws IOException {
		dout.writeBoolean(won);
		dout.writeBoolean(loss);
		dout.writeBoolean(draw);
		dout.writeInt(gameID);
	}
	
	public String toString() {
		return "[UpdateRecordTask, Taskcode: " + getTaskCode() +
		", Contents: " + won + "," + loss + "," + draw +"]";
	}
	
	public void setWon(boolean won)
	{
		this.won = won;
	}
	
	public void setLoss(boolean loss)
	{
		this.loss = loss;
	}
	
	public void setDraw(boolean draw)
	{
		this.draw = draw;
	}
	
	@Override
	public int getGameID() {
		return gameID;
	}

	public void run() 
	{
		Player player = ActivePlayer.getInstance();
		if(won)
		{
			player.getProfile().getHistory().addWin();
		}
		if(loss)
		{
			player.getProfile().getHistory().addLoss();
		}
		if(draw)
		{
			player.getProfile().getHistory().addDraw();
		}
		System.out.println("RECORD: " + player.getProfile().getHistory().toString());
	}
}
