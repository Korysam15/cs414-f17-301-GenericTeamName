package edu.colostate.cs.cs414.p5.client_server.transmission.game.invite;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.colostate.cs.cs414.p5.client_server.transmission.TaskConstents;
import edu.colostate.cs.cs414.p5.client_server.transmission.util.ForwardTask;
import edu.colostate.cs.cs414.p5.client_server.transmission.util.ReadUtils;
import edu.colostate.cs.cs414.p5.client_server.transmission.util.WriteUtils;
import edu.colostate.cs.cs414.p5.user.ActivePlayer;
import edu.colostate.cs.cs414.p5.user.Player;

public class InviteTask extends InviteGameTask {
	private String message;
	
	public InviteTask(String playerFrom, String message, String playerTo) {
		super(playerFrom,playerTo);
		this.message = message;
	}

	public InviteTask(String playerFrom,String message)
	{
		super(playerFrom);
		this.message = message;
	}

	public InviteTask(DataInputStream din) throws IOException
	{
		super(din);
		this.message = ReadUtils.readString(din);
	}

	public int getTaskCode() 
	{
		return TaskConstents.INVITE_TASK;
	}
	
	public String getPlayerFrom() {
		return getPlayerOne();
	}
	
	public void setPlayerTo(String playerTo) {
		setPlayerTwo(playerTo);
	}
	
	public String getPlayerTo() {
		return getPlayerTwo();
	}
	
	public String getMessage() {
		return message;
	}
	
	@Override
	public int hashCode() {
		return (getPlayerOne()+getPlayerTwo()+message).hashCode();
	}
	
	@Override
	public boolean equals(Object other) {
		if(other == null || !(other instanceof InviteTask)) {
			return false;
		} else {
			return this.hashCode() == other.hashCode();
		}
	}
	
	@Override
	public void writeBytes(DataOutputStream dout) throws IOException {
		super.writeBytes(dout);
		WriteUtils.writeString(message, dout);
	}
	
	public String toString() {
		return "[InviteTask, Taskcode: " + getTaskCode() +
				", Contents: " + message + " from " + getPlayerFrom() + " to " + getPlayerTo() + " ]";
	}

	public void run() {
		getResponse();
	}

	private void getResponse() {
		JFrame frame = new JFrame("Game Invitation From " + getPlayerFrom());
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
					player.getClient().sendToServer(new ForwardTask(player.getNickName(),new RejectInviteTask(player.getNickName()," has rejected your invitation!"),getPlayerFrom()));
				} catch (Exception ex) {
				}
				frame.dispose();
			}
		});
		
		acceptButton.addActionListener(new ActionListener() { // The action listener which notices when the button is pressed
			public void actionPerformed(ActionEvent e) {
				Player player = ActivePlayer.getInstance();
				try {
					player.getClient().sendToServer(new ForwardTask(player.getNickName(),new AcceptInviteTask(player.getNickName()),getPlayerFrom()));
				} catch (Exception ex) {
				}
				frame.dispose();
			}
		});
	}
}

