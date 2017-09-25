/**
 * 
 */
package transmission;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import transmission.util.ReadUtils;

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
	
	public Task createTaskFromBytes(byte[] bytes) throws IOException {
		ByteArrayInputStream ba = ReadUtils.getByteInputStream(bytes);
		DataInputStream din = ReadUtils.getDataInputStream(ba);
		Task t = createTaskFromDataInputStream(din);
		return t;
	}
	
	public Task createTaskFromDataInputStream(DataInputStream din) throws IOException {
		Task t = null;
		
		int taskCode = din.readInt();
		
		switch(taskCode) {
		case TaskConstents.TASK: 
			
			break;
		case TaskConstents.FORWARD_TASK:
			
			break;
			
		case TaskConstents.REGISTER_TASK:
			
			break;
		}
		
		return t;
	}
}
