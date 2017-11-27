package edu.colostate.cs.cs414.p5.client_server.transmission.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import edu.colostate.cs.cs414.p5.client_server.server.registry.AbstractRegistry;
import edu.colostate.cs.cs414.p5.client_server.server.registry.ActiveRegistry;
import edu.colostate.cs.cs414.p5.client_server.server.session.ClientSession;
import edu.colostate.cs.cs414.p5.client_server.server.session.SessionManager;
import edu.colostate.cs.cs414.p5.client_server.transmission.Task;
import edu.colostate.cs.cs414.p5.client_server.transmission.TaskConstents;

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
	
	public String getPlayerFrom() {
		return fromPlayer;
	}
	
	public String getPlayerTo() {
		return toPlayer;
	}

	public String toString() {
		return "[ForwardTask, Taskcode: " + TaskConstents.MESSAGE_TASK + 
				", Contents: " + task + "]";
	}

	public void run() {
		SessionManager server = SessionManager.getInstance();	
		if(server != null) {
			forwardTheTask(server);
		}
	}
	
	public Task getTask() {
		return task;
	}
	
	private void forwardTheTask(SessionManager server) {
		boolean success = false;
		String response = "";
		ClientSession clientFrom = server.getLoggedInClient(fromPlayer);
		ClientSession clientTo = server.getLoggedInClient(toPlayer);
		
		if(clientFrom != null) { // ensures that the client sending is registered
			if(clientTo != null) {
				try {
					clientTo.send(task);
					success = true;
				} catch (IOException e) {
					success = false;
					response = "Your message to: " + "'" + toPlayer + "' did not send.";
				}
			} else {
				success = false;
				response = getPlayerToStatus();
			}
			
			if(!success) {
				try {
					clientFrom.send(new MessageTask(response,MessageTask.ERROR));
				} catch (IOException e2) {
					
				}
			}
		}
	}
	
	// 
	// returns either "No such user: " + "'" + toPlayer + "'"
	// or "'" + toPlayer + "' is not online."
	private String getPlayerToStatus() {
		AbstractRegistry registry = ActiveRegistry.getInstance();
		if(registry != null && registry.isNicknameTaken(toPlayer)) {
			return "'" + toPlayer + "' is not online.";
		} else {
			return "No such user: " + "'" + toPlayer + "'";
		}
	}
}
