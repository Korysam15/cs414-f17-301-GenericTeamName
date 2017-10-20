package client_server.transmission;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import banqi.BanqiGame;
import client_server.transmission.util.ReadUtils;
import client_server.transmission.util.WriteUtils;
import user.Invitation;
import user.Player;

public class CreateGameTask extends Task {
	private String banqiGameInfo;
	private BanqiGame game;
	private Player one;
	private Player two;
	
	public CreateGameTask(Player one, Player two)
	{
		super();
		this.banqiGameInfo = game.toString();
	}
	
	public CreateGameTask(DataInputStream din) throws IOException
	{
		this.banqiGameInfo = ReadUtils.readString(din);
	}
	
	public int getTaskCode() 
	{
		return TaskConstents.CREATGAME_TASK;
	}
	
	public byte[] toByteArray() throws IOException 
	{
		ByteArrayOutputStream bs = WriteUtils.getByteOutputStream();
		DataOutputStream dout = WriteUtils.getDataOutputStream(bs);
		dout.writeInt(getTaskCode());
		WriteUtils.writeString(this.banqiGameInfo, dout);
		return WriteUtils.getBytesAndCloseStreams(bs,dout);
	}
	
	public void run()
	{
		System.out.println("Banqi Game ID: " + this.banqiGameInfo);
		// Have server send game to both players
	}
}
