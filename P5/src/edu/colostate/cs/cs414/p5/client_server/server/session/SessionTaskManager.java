package edu.colostate.cs.cs414.p5.client_server.server.session;

import edu.colostate.cs.cs414.p5.client_server.transmission.Task;
import edu.colostate.cs.cs414.p5.client_server.transmission.TaskManager;
import edu.colostate.cs.cs414.p5.client_server.transmission.registration_login.EntryTask;
import edu.colostate.cs.cs414.p5.client_server.transmission.registration_login.ExitTask;
import edu.colostate.cs.cs414.p5.client_server.transmission.registration_login.LoginTask;
import edu.colostate.cs.cs414.p5.client_server.transmission.registration_login.LogoutTask;
import edu.colostate.cs.cs414.p5.client_server.transmission.registration_login.RegisterTask;
import edu.colostate.cs.cs414.p5.client_server.transmission.registration_login.UnregisterTask;

/**
 * 
 * @author pflagert
 *
 */
public interface SessionTaskManager extends TaskManager {
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
	
	public void handleTask(Task t, ClientSession client);
	
	public Task handleExitTask(ClientSession client, ExitTask t);
	
	public Task unregisterClient(ClientSession client, UnregisterTask t);
	
	public Task logoutClient(ClientSession client, LogoutTask t);
	
	public Task loginClient(ClientSession client, EntryTask t);
	
	public Task loginClient(ClientSession client, LoginTask t);
	
	public Task loginClient(ClientSession client, RegisterTask t);
	
	
	
}
