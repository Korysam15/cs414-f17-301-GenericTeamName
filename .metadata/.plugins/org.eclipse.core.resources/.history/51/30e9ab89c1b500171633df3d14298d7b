/**
 * 
 */
package pflagert.transmission;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import pflagert.transmission.util.ReadUtils;

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
	
	public Task createTaskFromBytes(byte[] bytes) throws IOException {
		ByteArrayInputStream ba = ReadUtils.getByteInputStream(bytes);
		DataInputStream din = ReadUtils.getDataInputStream(ba);
		Task t = createTaskFromDataInputStream(din);
		ReadUtils.closeInputStreams(ba, din);
		return t;
	}
	
	public Task createTaskFromDataInputStream(DataInputStream din) throws IOException {
		Task t = null;
		
		int taskCode = din.readInt();
		System.out.println("Task code is: " + taskCode);
		switch(taskCode) {
		case TaskConstents.TASK: 
			
			break;
		case TaskConstents.FORWARD_TASK:
			
			break;
			
		case TaskConstents.REGISTER_TASK:
			
			break;
		case TaskConstents.TEST_TASK:
			t = new MessageTask(din);
			break;
			
		case TaskConstents.INVITE_TASK:
			t = new InviteTask(din);
			break;
			
		case TaskConstents.CREATGAME_TASK:
			t = new CreateGameTask(din);
			break;
		}
		
		return t;
	}
}
