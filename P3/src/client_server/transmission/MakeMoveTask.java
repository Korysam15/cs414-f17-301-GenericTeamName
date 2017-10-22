package client_server.transmission;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import client_server.transmission.util.ReadUtils;
import client_server.transmission.util.WriteUtils;
import user.ActivePlayer;
import user.Player;
import banqi.Square;

public class MakeMoveTask extends Task {
	private int [] move;
	private String playerFrom;
	
	
	
	public MakeMoveTask(int[] move, String playerFrom)
	{
		super();
		this.move = move;
		this.playerFrom = playerFrom;
		
		
	}
	
	public MakeMoveTask(DataInputStream din) throws IOException
	{
		this.move = ReadUtils.readIntArray(din);
		this.playerFrom = ReadUtils.readString(din);
		
	}
	
//	public int getTaskCode() 
	{
//		return TaskConstents.MAKEMOVE_TASK;
	}
	
	public byte[] toByteArray() throws IOException 
	{
		ByteArrayOutputStream bs = WriteUtils.getByteOutputStream();
		DataOutputStream dout = WriteUtils.getDataOutputStream(bs);
		dout.writeInt(getTaskCode());
		WriteUtils.writeIntArray(move, dout);
		WriteUtils.writeString(playerFrom, dout);
		return WriteUtils.getBytesAndCloseStreams(bs,dout);
	}
	
	public void run()
	{
		if(move.length==2){
			System.out.print(this.playerFrom + " has flipped the piece: "+move[0]+""+move[1]);
		}
		else if(move.length==4){
			System.out.print(this.playerFrom + " has made the move: "+move[0]+""+move[1]+" to "+move[2]+""+move[3]);
		}
//		BufferedReader fromConsole = 
//				new BufferedReader(new InputStreamReader(System.in));
//		String choice = "no";
//		try {
//			choice = fromConsole.readLine();
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		if(choice.toLowerCase().equals("yes"))
//		{
//			System.out.println("You entered in yes!");
//			Player player = ActivePlayer.getInstance();
//			try {
//				player.getClient().sendToServer(new ForwardTask(player.getNickName(),new MessageTask(player.getNickName() + " has accepted your invitation!"),playerFrom));
//			} catch (Exception e) {
//			}
//		}
//		else
//		{
//			System.out.println("You entered in no!");
//			Player player = ActivePlayer.getInstance();
//			try {
//				player.getClient().sendToServer(new ForwardTask(player.getNickName(),new MessageTask(player.getNickName() + " has rejected your invitation!"),playerFrom));
//			} catch (Exception e) {
//			}
//		}
	}
}