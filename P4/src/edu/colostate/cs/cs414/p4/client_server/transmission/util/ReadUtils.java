/**
 * 
 */
package edu.colostate.cs.cs414.p4.client_server.transmission.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.colostate.cs.cs414.p4.client_server.transmission.Task;
import edu.colostate.cs.cs414.p4.client_server.transmission.TaskFactory;

/**
 * @author pflagert
 *
 */
public class ReadUtils {
	
	public static ByteArrayInputStream getByteInputStream(byte[] bytes) {
		return new ByteArrayInputStream(bytes);
	}
	
	public static DataInputStream getDataInputStream(ByteArrayInputStream ba) {
		return new DataInputStream(new BufferedInputStream(ba));
	}
	
	public static void closeInputStreams(ByteArrayInputStream ba, DataInputStream din) throws IOException {
		ba.close();
		din.close();
	}
	
	public static String readString(DataInputStream din) throws IOException {
		int length = din.readInt();
		byte[] stringBytes = new byte[length];
		din.readFully(stringBytes);
		return new String(stringBytes);
	}
	
	public static List<String> readStringList(DataInputStream din) throws IOException {
		int length = din.readInt();
		List<String> ret = new ArrayList<String>(length);
		for(int i=0;i<length;i++) {
			ret.add(readString(din));
		}
		return ret;
	}
	
	public static Task readTask(DataInputStream din) throws IOException {
		int length = din.readInt();
		byte[] data = new byte[length];
		din.readFully(data);
		return TaskFactory.getInstance().createTaskFromBytes(data);
	}
	
	public static List<Task> readTaskList(DataInputStream din) throws IOException {
		int length = din.readInt();
		List<Task> ret = new ArrayList<Task>(length);
		for(int i=0;i<length;i++) {
			ret.add(readTask(din));
		}
		return ret;
	}
	
	public static char[] readCharArray(DataInputStream din) throws IOException {
		int length = din.readInt();
		char ret[] = new char[length];
		for(int i=0;i<length;i++) {
			ret[i] = din.readChar();
		}
		return ret;
	}
	
	public static int[] readIntArray(DataInputStream din) throws IOException {
		int length = din.readInt();
		int ret[] = new int[length];
		for(int i=0;i<length;i++) {
			ret[i] = din.readInt();
		}
		return ret;
	}
	
}
