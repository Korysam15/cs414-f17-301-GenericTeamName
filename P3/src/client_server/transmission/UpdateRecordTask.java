package client_server.transmission;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import client_server.transmission.util.WriteUtils;
import user.ActivePlayer;
import user.Player;

public class UpdateRecordTask extends Task {
	private boolean won, loss, draw;
	
	public UpdateRecordTask(boolean won, boolean loss, boolean draw) {
		this.won = won;
		this.loss = loss;
		this.draw = draw;
	}

	public UpdateRecordTask(DataInputStream din) throws IOException {
		this.won = din.readBoolean();
		this.loss = din.readBoolean();
		this.draw = din.readBoolean();
	}

	public int getTaskCode() {
		return TaskConstents.UPDATERECORD_TASK;
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

	public byte[] toByteArray() throws IOException {
		ByteArrayOutputStream bs = WriteUtils.getByteOutputStream();
		DataOutputStream dout = WriteUtils.getDataOutputStream(bs);
		dout.writeInt(getTaskCode());
		dout.writeBoolean(won);
		dout.writeBoolean(loss);
		dout.writeBoolean(draw);
		return WriteUtils.getBytesAndCloseStreams(bs,dout);
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
