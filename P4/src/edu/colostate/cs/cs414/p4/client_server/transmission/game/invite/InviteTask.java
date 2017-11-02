package edu.colostate.cs.cs414.p4.client_server.transmission.game.invite;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.colostate.cs.cs414.p4.client_server.transmission.TaskConstents;
import edu.colostate.cs.cs414.p4.client_server.transmission.util.ForwardTask;
import edu.colostate.cs.cs414.p4.client_server.transmission.util.ReadUtils;
import edu.colostate.cs.cs414.p4.client_server.transmission.util.WriteUtils;
import edu.colostate.cs.cs414.p4.user.ActivePlayer;
import edu.colostate.cs.cs414.p4.user.Player;

public class InviteTask extends InviteGameTask {
	private String message;
	private String playerFrom;
	//private String playerTo;

	public InviteTask(String playerFrom,String message/*, String playerTo*/)
	{
		super();
		this.message = message;
		this.playerFrom = playerFrom;
		//this.playerTo = playerTo;
	}

	public InviteTask(DataInputStream din) throws IOException
	{
		this.message = ReadUtils.readString(din);
		this.playerFrom = ReadUtils.readString(din);
		//this.playerTo = ReadUtils.readString(din);
	}

	public int getTaskCode() 
	{
		return TaskConstents.INVITE_TASK;
	}
	
	@Override
	public void writeBytes(DataOutputStream dout) throws IOException {
		// TODO Auto-generated method stub
		
	}
	
	public String toString() {
		return "[InviteTask, Taskcode: " + getTaskCode() +
				", Contents: " + message + playerFrom + "]";
	}

	public byte[] toByteArray() throws IOException 
	{
		ByteArrayOutputStream bs = WriteUtils.getByteOutputStream();
		DataOutputStream dout = WriteUtils.getDataOutputStream(bs);
		dout.writeInt(getTaskCode());
		WriteUtils.writeString(message, dout);
		WriteUtils.writeString(playerFrom, dout);
		//WriteUtils.writeString(playerTo, dout);
		return WriteUtils.getBytesAndCloseStreams(bs,dout);
	}

	public void oldrun()
	{
		System.out.print(this.playerFrom + " wants to play a game of Banqi! Do you wish to accept or reject?\n");
		System.out.print("Type 'accept' to accept and 'reject' to reject: ");
		BufferedReader fromConsole = new BufferedReader(new InputStreamReader(System.in));
		String choice = "reject";
		try {
			choice = fromConsole.readLine();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(choice.toLowerCase().equals("accept"))
		{
			System.out.println("You entered in yes!");
			Player player = ActivePlayer.getInstance();
			try {
				player.getClient().sendToServer(new ForwardTask(player.getNickName(),new AcceptInviteTask(player.getNickName()),playerFrom));
			} catch (Exception e) {
			}
		}
		else
		{
			System.out.println("You entered in no!");
			Player player = ActivePlayer.getInstance();
			try {
				player.getClient().sendToServer(new ForwardTask(player.getNickName(),new RejectInviteTask(player.getNickName()," has rejected your invitation!"),playerFrom));
			} catch (Exception e) {
			}
		}
	}

	public void run() {
		getResponse();
	}

	private void getResponse() {
		JFrame frame = new JFrame("Game Invitation From " + playerFrom);
		frame.setVisible(true);
		frame.setSize(300, 300);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JLabel label = new JLabel(message);
		JPanel panel = new JPanel();
		frame.add(panel);
		panel.add(label);
		label.setLocation(20,20);

		JButton declineButton = new JButton("Decline");
		panel.add(declineButton);

		JButton acceptButton = new JButton("Accept");
		panel.add(acceptButton);
		
		
		declineButton.addActionListener(new ActionListener() { // The action listener which notices when the button is pressed
			public void actionPerformed(ActionEvent e) {
				Player player = ActivePlayer.getInstance();
				try {
					player.getClient().sendToServer(new ForwardTask(player.getNickName(),new RejectInviteTask(player.getNickName()," has rejected your invitation!"),playerFrom));
				} catch (Exception ex) {
				}
				frame.dispose();
			}
		});
		
		acceptButton.addActionListener(new ActionListener() { // The action listener which notices when the button is pressed
			public void actionPerformed(ActionEvent e) {
				Player player = ActivePlayer.getInstance();
				try {
					player.getClient().sendToServer(new ForwardTask(player.getNickName(),new AcceptInviteTask(player.getNickName()),playerFrom));
				} catch (Exception ex) {
				}
				frame.dispose();
			}
		});
	}
}

