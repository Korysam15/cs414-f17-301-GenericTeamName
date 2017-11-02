package edu.colostate.cs.cs414.p4.client_server.transmission.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import edu.colostate.cs.cs414.p4.client_server.server.AbstractServer;
import edu.colostate.cs.cs414.p4.client_server.server.ActiveServer;
import edu.colostate.cs.cs414.p4.client_server.server.session.ClientSession;
import edu.colostate.cs.cs414.p4.client_server.transmission.Task;
import edu.colostate.cs.cs414.p4.client_server.transmission.TaskConstents;

public class ForwardTask extends UtilityTask {
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
	
	@Override
	public void writeBytes(DataOutputStream dout) throws IOException {
		WriteUtils.writeString(fromPlayer,dout);
		WriteUtils.writeString(toPlayer,dout);
		WriteUtils.writeTask(task, dout);		
	}

	public String toString() {
		return "[ForwardTask, Taskcode: " + TaskConstents.MESSAGE_TASK + 
				", Contents: " + task + "]";
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
					clientFrom.send(new MessageTask(response,MessageTask.ERROR));
				} catch (IOException e2) {
					
				}
			}
		}
	}
}
