package edu.colostate.cs.cs414.p4.client_server.transmission.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.colostate.cs.cs414.p4.client_server.server.AbstractServer;
import edu.colostate.cs.cs414.p4.client_server.server.ActiveServer;
import edu.colostate.cs.cs414.p4.client_server.server.registry.AbstractRegistry;
import edu.colostate.cs.cs414.p4.client_server.server.registry.ActiveRegistry;
import edu.colostate.cs.cs414.p4.client_server.server.session.ClientSession;
import edu.colostate.cs.cs414.p4.client_server.transmission.Task;
import edu.colostate.cs.cs414.p4.client_server.transmission.TaskConstents;

public class MultiForwardTask extends UtilityTask {
	private String fromPlayer;
	private List<String> toPlayers;
	private Task task;

	public MultiForwardTask(String fromPlayer, Task task, List<String> toPlayer) {
		this.fromPlayer = fromPlayer;
		this.task = task;
		this.toPlayers = new ArrayList<String>(toPlayer);
	}

	public MultiForwardTask(DataInputStream din) throws IOException {
		this.fromPlayer = ReadUtils.readString(din);
		this.toPlayers = ReadUtils.readStringList(din);
		task = ReadUtils.readTask(din);
	}

	public int getTaskCode() {
		return TaskConstents.MULTI_FORWARD_TASK;
	}

	@Override
	public void writeBytes(DataOutputStream dout) throws IOException {
		WriteUtils.writeString(fromPlayer,dout);
		WriteUtils.writeStringList(toPlayers,dout);
		WriteUtils.writeTask(task, dout);		
	}

	public String toString() {
		return "[MultiForwardTask, Taskcode: " + getTaskCode() +
				", Contents: " + task + ",sent to " + toPlayers.size() + " clients]";
	}

	public List<String> getPlayersTo() {
		return new ArrayList<String>(toPlayers);
	}

	public Task getTask() {
		return task;
	}

	public void run() {
		AbstractServer server = ActiveServer.getInstance();	
		if(server != null) {
			for(String toPlayer: toPlayers)
				forwardTheTask(server,toPlayer);
		}
	}

	private void forwardTheTask(AbstractServer server, String toPlayer) {
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
				response = getPlayerToStatus(toPlayer);
			}

			if(!success) {
				try {
					clientFrom.send(new MessageTask(response));
				} catch (IOException e2) {

				}
			}
		}
	}

	// returns either "No such user: " + "'" + toPlayer + "'"
	// or "'" + toPlayer + "' is not online."
	private String getPlayerToStatus(String toPlayer) {
		AbstractRegistry registry = ActiveRegistry.getInstance();
		if(registry != null && registry.isNicknameTaken(toPlayer)) {
			return "'" + toPlayer + "' is not online.";
		} else {
			return "No such user: " + "'" + toPlayer + "'";
		}
	}
}
