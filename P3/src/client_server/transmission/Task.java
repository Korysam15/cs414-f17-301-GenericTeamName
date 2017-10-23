/**
 * 
 */
package client_server.transmission;

import java.io.IOException;

/**
 * @author pflagert
 *
 */
public abstract class Task implements Runnable {	
	
	public abstract int getTaskCode();
	
	public abstract byte[] toByteArray() throws IOException;
	
	public String toString() {
		return "Task Code: " + this.getTaskCode();
	}
	
	public abstract void run();
}
