/**
 * 
 */
package edu.colostate.cs.cs414.p3.client_server.transmission.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import edu.colostate.cs.cs414.p3.client_server.transmission.Task;

/**
 * @author pflagert
 *
 */
public class WriteUtils {

	public static ByteArrayOutputStream getByteOutputStream() {
		return new ByteArrayOutputStream();
	}
	
	public static DataOutputStream getDataOutputStream(ByteArrayOutputStream ba) {
		return new DataOutputStream(new BufferedOutputStream(ba));
	}
	
	public static void closeOutputStreams(ByteArrayOutputStream ba, DataOutputStream dout) throws IOException {
		ba.close();
		dout.close();
	}
	
	public static void writeString(String s, DataOutputStream dout) throws IOException {
		dout.writeInt(s.length());
		writeByteArray(s.getBytes(), dout);
	}
	
	public static void writeStringList(List<String> strings, DataOutputStream dout) throws IOException {
		dout.writeInt(strings.size());
		for(String s: strings) {
			writeString(s, dout);
		}
	}
	
	public static void writeTask(Task t, DataOutputStream dout) throws IOException {
		byte[] data = t.toByteArray();
		dout.writeInt(data.length);
		writeByteArray(t.toByteArray(),dout);
	}
	
	public static void writeTaskList(List<Task> tasks, DataOutputStream dout) throws IOException {
		dout.writeInt(tasks.size());
		for(Task t: tasks) {
			writeTask(t, dout);
		}
	}
	
	public static void writeCharArray(char[] chars, DataOutputStream dout) throws IOException {
		dout.writeInt(chars.length);
		for(char c: chars) {
			dout.writeChar(c);
		}
	}
	
	public static void writeIntArray(int[] ints, DataOutputStream dout) throws IOException {
		dout.writeInt(ints.length);
		for(int i: ints) {
			dout.writeInt(i);
		}
	}
	
	public static void writeByteArray(byte[] bytes, DataOutputStream dout) throws IOException {
		dout.write(bytes);
	}
	
	public static byte[] getBytesAndCloseStreams(ByteArrayOutputStream ba, DataOutputStream dout) throws IOException {
		dout.flush();
		byte[] ret = ba.toByteArray();
		closeOutputStreams(ba,dout);
		return ret;
	}
}
