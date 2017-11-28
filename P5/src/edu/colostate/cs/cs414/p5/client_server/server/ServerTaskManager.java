package edu.colostate.cs.cs414.p5.client_server.server;

import edu.colostate.cs.cs414.p5.client_server.server.session.ClientSession;
import edu.colostate.cs.cs414.p5.client_server.transmission.Task;
import edu.colostate.cs.cs414.p5.client_server.transmission.TaskManager;

/**
 * 
 * @author pflagert
 *
 */
public interface ServerTaskManager extends TaskManager {
	@Override
	public default void handleTask(Task t) {
		throw new UnsupportedOperationException("SessionTaskManager does not support handleTask(Task t)");
	}
	
	@Override
	public default void handleTask(Task t, Object attachment) {
		if(attachment instanceof ClientSession) {
			handleTask(t,(ClientSession)attachment);
		}
		throw new UnsupportedOperationException("SessionTaskManager does not support handleTask(Task t,"+
		((attachment != null) ? attachment.getClass() : "null")
		+")");
	}
	
	public default void handleTask(Task t, ClientSession client) {
		runTaskIfValid(t,client);
	}
	
	public void runTaskIfValid(Task t, ClientSession client);
}
