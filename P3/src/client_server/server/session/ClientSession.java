/**
 * 
 */
package client_server.server.session;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import client_server.server.AbstractServer;
import client_server.server.Server;
import client_server.transmission.RegisterTask;
import client_server.transmission.Task;
import client_server.transmission.TaskConstents;
import client_server.transmission.TaskFactory;

/**
 * @author pflagert
 *
 */
public class ClientSession extends AbstractSession {

	/* Used for debugging */
	public static final boolean DEBUG = Server.DEBUG;
	public static final String DEBUG_TAB = "    ";
	private static int NUM_TABS = 0;
	
	private SocketChannel channel;

	// prevents two threads from writing to the same channel at the same time
	private Object writeLock;
	// prevents two from reading from the same channel at the same time
	private Object readLock;

	public ClientSession(AbstractServer server, SelectionKey key, String ID) throws IOException {
		super(server, key, ID);
		channel = (SocketChannel) key.channel();
		writeLock = new Object();
		readLock = new Object();
	}

	public ClientSession(AbstractSession s) throws IOException {
		this(s.server,s.key,s.ID);
	}

	@Override
	public void receive() throws IOException {
		debugPrintHeader("receive");
		ByteBuffer localWrite = null;

		ByteBuffer readBuffer = ByteBuffer.allocate(7000);
		int temp = 0, read = 0, total = 0;
		int size = -1;
		synchronized(readLock) {
			while( (temp = channel.read(readBuffer)) > 0 ) {
				read+=temp;
				if(read >= 4) {
					readBuffer.flip();
					size = readBuffer.getInt();
					debugPrintln("READ " + size + " For the amount of required bytes");
					if(size >= 1) {
						localWrite = ByteBuffer.allocate(size);
						if(read > 4) {
							localWrite.put(readBuffer);
						}
						total += receiveTask(localWrite,size, read);
						read = 0;
						readBuffer.clear();
					} else {
						debugPrintln("SIZE: " + size + " is an invalid ammount of bytes.");
						debugPrintln("returning");
						return;
					}
				} else {
					continue;
				}
			}
			if(temp == -1 || !channel.isConnected()) {
				server.clientDisconnected(this, key);
			} else {
				debugPrintln("DONE READING BYTES: READ " + total + " TOTAL BYTES");
			}
		}
		debugPrintFooter("receive");
	}

	private int receiveTask(ByteBuffer local, int size, int currentRead) throws IOException {
		int temp = 0,read = currentRead;
		
		if(read < size) {
			while(local.hasRemaining() && (temp = channel.read(local)) > -1) {
				read += temp;
				if(read >= size) {
					break;
				}
			}
		}
		
		debugPrintln("Done reading the required bytes: " + (read - 4));
		
		if(temp == -1) {
			server.clientDisconnected(this, key);
		} else {
			createTask(local);
		}
		
		return read;
	}

	private void createTask(ByteBuffer local) throws IOException {
		Task t = TaskFactory.getInstance().createTaskFromBytes(local.array());
		if(t != null) {
			switch(t.getTaskCode()) {
			case TaskConstents.LOGIN_TASK:
				// handle login
				// for now just send to server
				server.handleTask(t);
				break;
			case TaskConstents.REGISTER_TASK:
				if(t instanceof RegisterTask) {
					registerWithServer((RegisterTask)t);
				}
			default:
				server.handleTask(t);
			}
		}
	}

	@Override
	public void send(Task t) throws IOException {
		debugPrintHeader("send");
		synchronized(writeLock) {
			byte[] data = t.toByteArray();
			int dataLength = data.length;
			debugPrintln("Sending: " + dataLength + " bytes for TaskCode: " + t.getTaskCode());
			ByteBuffer writeBuffer = ByteBuffer.allocate(dataLength+4);
			writeBuffer.putInt(dataLength);
			writeBuffer.put(data);
			writeBuffer.flip();
			int written = 0;
			while(writeBuffer.hasRemaining()) {
				written += channel.write(writeBuffer);
			}
			debugPrintln("Sent: " + written + " total bytes");
		}
		debugPrintFooter("send");
	}

	@Override
	public void disconnect() {
		server.clientDisconnected(this, key);
	}

	@Override
	public boolean isConnected() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRegisteredWithServer() {
		return isRegistered;
	}

	@Override
	public void registerWithServer(RegisterTask t) {
		// String email = t.getEmail();
		String nickname = t.getNickname();
		// String password = t.getPassword();
		
		// Verify email is unique in DB
		// Verify nickname is unique in DB
		// Verify password meets our conditions????
		server.registerClient(this, nickname);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		return false;
	}
	
	private String getDate() {
		Date now = new Date(); // java.util.Date, NOT java.sql.Date or java.sql.Timestamp!
		return new SimpleDateFormat("HH:mm:ss.S", Locale.ENGLISH).format(now);
	}
	
	private synchronized String getTabs() {
		String ret = "";
		for(int i=0;i<NUM_TABS;i++) {
			ret+=DEBUG_TAB;
		}
		return "[" + getDate() + "]" + ret;
	}
	
	private synchronized void debugPrint(String msg) {
		if(DEBUG)
			System.out.print(getTabs()+msg);
	}
	
	private synchronized void debugPrintln(String msg) {
		debugPrint(msg+System.lineSeparator());
	}
	
	private synchronized void debugPrintHeader(String methodName) {
		debugPrintln("---------------[ " + methodName + " ]---------------");
		NUM_TABS++;
	}
	
	private synchronized void debugPrintFooter(String methodName) {
		NUM_TABS--;
		debugPrintln("===============[ " + methodName + " ]===============\n");
	}

}
