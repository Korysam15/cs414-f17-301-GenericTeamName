package client_server.transmission;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import client_server.server.AbstractServer;
import client_server.server.ActiveServer;
import client_server.server.session.ClientSession;
import client_server.transmission.util.ReadUtils;
import client_server.transmission.util.WriteUtils;

public class ForwardTask extends Task {
	private String fromPlayer;
	private String toPlayer;
	private Task task;

	public ForwardTask(String fromPlayer, Task task, String toPlayer) {
		this.fromPlayer = fromPlayer;
		this.task = task;
		this.toPlayer = toPlayer;
	}

	public ForwardTask(DataInputStream din) throws IOException {
		this.fromPlayer = ReadUtils.readString(din);
		this.toPlayer = ReadUtils.readString(din);
		task = ReadUtils.readTask(din);
	}

	public int getTaskCode() {
		return TaskConstents.FORWARD_TASK;
	}

	public byte[] toByteArray() throws IOException {
		ByteArrayOutputStream bs = WriteUtils.getByteOutputStream();
		DataOutputStream dout = WriteUtils.getDataOutputStream(bs);
		dout.writeInt(getTaskCode());
		WriteUtils.writeString(fromPlayer,dout);
		WriteUtils.writeString(toPlayer,dout);
		WriteUtils.writeTask(task, dout);
		return WriteUtils.getBytesAndCloseStreams(bs,dout);
	}

	public String toString() {
		return "Taskcode: " + TaskConstents.MESSAGE_TASK + 
				" Forwarded Task code: " + task.getTaskCode();
	}

	public void run() {
		AbstractServer server = ActiveServer.getInstance();	
		if(server != null) {
			forwardTheTask(server);
		}
	}
	
	private void forwardTheTask(AbstractServer server) {
		boolean success = false;
		String response = "";
		ClientSession clientFrom = server.getRegisteredClient(fromPlayer);
		ClientSession clientTo = server.getRegisteredClient(toPlayer);
		
		if(clientFrom != null) { // ensures that the client sending is registered
			if(clientTo != null) {
				try {
					clientTo.send(task);
					success = true;
				} catch (IOException e) {
					success = false;
					response = "Your message to: " + "'" + toPlayer + "' did not send";
				}
			} else {
				success = false;
				response = "No such user: " + "'" + toPlayer + "'";
			}
			
			if(!success) {
				try {
					clientFrom.send(new MessageTask(response));
				} catch (IOException e2) {
					
				}
			}
		}
	}
}
