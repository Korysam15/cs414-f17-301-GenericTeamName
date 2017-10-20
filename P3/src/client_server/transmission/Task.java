/**
 * 
 */
package client_server.transmission;

import java.io.IOException;

/**
 * @author pflagert
 *
 */
public class Task implements Runnable {	
	
	public int getTaskCode() {
		return TaskConstents.TASK;
	}
	
	public byte[] toByteArray() throws IOException {
		return new byte[0];
	}
	
	public String toString() {
		return TaskConstents.TASK + "";
	}
	
	public void run() {
		return;
	}
}
