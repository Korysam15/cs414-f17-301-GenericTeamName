package edu.colostate.cs.cs414.p4.client_server.transmission.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import edu.colostate.cs.cs414.p4.client_server.server.AbstractServer;
import edu.colostate.cs.cs414.p4.client_server.server.ActiveServer;
import edu.colostate.cs.cs414.p4.client_server.transmission.TaskConstents;
import edu.colostate.cs.cs414.p4.console.AbstractConsole;
import edu.colostate.cs.cs414.p4.user.ActivePlayer;
import edu.colostate.cs.cs414.p4.user.Player;

/**
 * @author pflagert
 * Just a Test for the server / client
 */
public class MessageTask extends UtilityTask {

	public static final int DEFAULT=4;
	public static final int NOTICE=3;
	public static final int WARNING=2;
	public static final int ERROR=1;
	
	private String msg;
	private int type;
	
	public MessageTask() 
	{
		this("",DEFAULT);
	}
	
	public MessageTask(String msg) 
	{
		this("",DEFAULT);
	}
	
	public MessageTask(String msg, int type)
	{
		super();
		this.msg = msg;
		setType(type);
	}
	
	private void setType(int type) {
		switch(type) {
		case DEFAULT: case NOTICE: case WARNING: case ERROR:
			this.type = type;
			return;
		default:
			this.type = DEFAULT;
		}
	}
	
	public MessageTask(DataInputStream din) throws IOException 
	{
		msg = ReadUtils.readString(din);
		type = din.readInt();
		setType(type);
	}

	public int getTaskCode() 
	{
		return TaskConstents.MESSAGE_TASK;
	}
	
	@Override
	public void writeBytes(DataOutputStream dout) throws IOException {
		WriteUtils.writeString(msg,dout);
		dout.writeInt(type);
	}

	public String toString() 
	{
		return "[MessageTask, Taskcode: " + getTaskCode() +
		", Contents: " + msg + "]";
	}

	public void run() 
	{
		Player player;
		AbstractServer server;
		if((player = ActivePlayer.getInstance()) != null) {
			displayToPlayer(player);
		} else if((server = ActiveServer.getInstance()) !=null ) {
			displayToServer(server);
		}
	}
	
	private void displayToPlayer(Player player) {
		AbstractConsole console = player.getConsole();
		if(console != null) {
			switch(type) {
			case DEFAULT:
				console.display(msg);
				break;
			case NOTICE:
				console.notice(msg);
				break;
			case WARNING:
				console.warning(msg);
				break;
			case ERROR:
				console.error(msg);
				break;
			default:
				console.display(msg);
			}
		} else {
			System.out.println(msg);
		}
	}
	
	private void displayToServer(AbstractServer server) {
		System.out.println(msg);
	}

}
