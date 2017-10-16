/**
 * 
 */
package pflagert.transmission;

/**
 * @author pflagert
 *
 */
public class Task implements Runnable {	
	
	public int getTaskCode() {
		return TaskConstents.TASK;
	}
	
	public byte[] toByteArray() {
		return new byte[0];
	}
	
	public String toString() {
		return TaskConstents.TASK + "";
	}
	
	public void run() {
		return;
	}
}
