package edu.colostate.cs.cs414.p5.client_server.transmission;

public interface TaskManager {
	public void handleTask(Task t);
	public void handleTask(Task t, Object attachment);
}
