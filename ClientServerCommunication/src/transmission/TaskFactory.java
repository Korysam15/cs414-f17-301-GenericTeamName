/**
 * 
 */
package transmission;

import java.io.DataInputStream;

/**
 * @author pflagert
 *
 */
public final class TaskFactory {
	
	private static final TaskFactory instance = new TaskFactory();
	
	private TaskFactory() {}
	
	public static TaskFactory getInstance() {
		return instance;
	}
	
	public Task createTask(int taskCode) {
		Task t = null;
		
		return t;
	}
	
	public Task createTaskFromBytes(byte[] b) {
		Task t = null;
		
		return t;
	}
	
	public Task createTaskFromDataInputStream(DataInputStream din) {
		Task t = null;
		
		return t;
	}
}
