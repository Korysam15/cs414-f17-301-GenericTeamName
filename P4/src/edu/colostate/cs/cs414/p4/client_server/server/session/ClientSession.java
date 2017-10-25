/**
 * 
 */
package edu.colostate.cs.cs414.p4.client_server.server.session;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import edu.colostate.cs.cs414.p4.client_server.server.AbstractServer;
import edu.colostate.cs.cs414.p4.client_server.server.Server;
import edu.colostate.cs.cs414.p4.client_server.server.registry.AbstractRegistry;
import edu.colostate.cs.cs414.p4.client_server.server.registry.ActiveRegistry;
import edu.colostate.cs.cs414.p4.client_server.transmission.LoginGreetingTask;
import edu.colostate.cs.cs414.p4.client_server.transmission.LoginTask;
import edu.colostate.cs.cs414.p4.client_server.transmission.LogoutTask;
import edu.colostate.cs.cs414.p4.client_server.transmission.MessageTask;
import edu.colostate.cs.cs414.p4.client_server.transmission.RegisterGreetingTask;
import edu.colostate.cs.cs414.p4.client_server.transmission.RegisterTask;
import edu.colostate.cs.cs414.p4.client_server.transmission.Task;
import edu.colostate.cs.cs414.p4.client_server.transmission.TaskConstents;
import edu.colostate.cs.cs414.p4.client_server.transmission.TaskFactory;
import edu.colostate.cs.cs414.p4.client_server.transmission.UnregisterTask;

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

	private String email;
	
	private String address;

	public ClientSession(AbstractServer server, SelectionKey key, String ID) throws IOException {
		super(server, key, ID);
		channel = (SocketChannel) key.channel();
		writeLock = new Object();
		readLock = new Object();
		this.address = channel.socket().getRemoteSocketAddress().toString();
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
							fillLocal(localWrite,readBuffer);
						}
						total += receiveTask(localWrite,size, read);
						read -= size;
						//readBuffer.clear();
					} else {
						debugPrintln("SIZE: " + size + " is an invalid ammount of bytes.");
						debugPrintln("returning");
						return;
					}
				} else {
					continue;
				}
			}
			if(read > 0 && readBuffer.remaining() >= 4) {
				handleOverread(readBuffer);
			}
			if(temp == -1 || !channel.isConnected()) {
				server.clientDisconnected(this, key);
			} else {
				debugPrintln("DONE READING BYTES: READ " + total + " TOTAL BYTES");
			}
		}
		debugPrintFooter("receive");
	}

	private void handleOverread(ByteBuffer readBuffer) throws IOException {
		debugPrintHeader("handleOverread");
		while(readBuffer.remaining() >= 4) {
			int size = readBuffer.getInt();
			debugPrintln("READ " + size + " For the amount of required bytes");
			if(size >= 1) {
				ByteBuffer localWrite = ByteBuffer.allocate(size);
				fillLocal(localWrite,readBuffer);
				receiveTask(localWrite,size, 0);
			} else {
				debugPrintln("SIZE: " + size + " is an invalid ammount of bytes.");
				debugPrintln("returning");
				return;
			}

		}
		debugPrintFooter("handleOverread");
	}

	private void fillLocal(ByteBuffer local, ByteBuffer readBuffer) {
		while(local.hasRemaining() && readBuffer.hasRemaining()) {
			local.put(readBuffer.get());
		}
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
				if(t instanceof LoginTask) {
					registerWithServer((LoginTask)t);
				}
				break;
			case TaskConstents.REGISTER_TASK:
				if(t instanceof RegisterTask) {
					registerWithServer((RegisterTask)t);
				}
				break;
			default:
				if(isRegisteredWithServer()) {
					handleTask(t);
				} else {

				}
			}
		}
	}

	private void handleTask(Task t) throws IOException {
		if(t instanceof UnregisterTask) {
			unregister((UnregisterTask)t);
			send(new UnregisterTask("","",""));
			setID(null);
		} else if(t instanceof LogoutTask) {
			logout();
			send(new LogoutTask(""));
			setID(null);
		} else {
			server.handleTask(t,this);
		}
	}

	private void unregister(UnregisterTask t) throws IOException {
		AbstractRegistry registry = ActiveRegistry.getInstance();
		if(registry != null) {
			registry.unregisterUser(email);
			logout();
		}
	}

	private void logout() {
		server.unregisterClient(this, this.ID);
		setUnregistered();
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
		synchronized(isRegistered) {
			return isRegistered;
		}
	}

	private void setRegistered() {
		synchronized(isRegistered) {
			isRegistered = true;
		}
	}

	private void setUnregistered() {
		synchronized(isRegistered) {
			isRegistered = false;
		}
	}

	@Override
	public void registerWithServer(RegisterTask t) {
		AbstractRegistry registry = ActiveRegistry.getInstance();
		Task response = null;
		if(registry != null) {
			String msg = registry.registerNewUser(t);
			if(msg == null) {
				String nickname = t.getNickname();
				setID(nickname);
				this.email = t.getEmail();
				server.registerClient(this, nickname);
				setRegistered();
				response = new RegisterGreetingTask("Welcome " + nickname + "!");
			} else {
				int type = (msg.contains("in use")) ? MessageTask.WARNING : MessageTask.ERROR;
				response = new MessageTask(msg,type);
			}
		} else {
			response = new MessageTask("Error occured while logging in.",MessageTask.ERROR);
		}
		
		try {
			send(response);
		} catch(IOException e) {

		}
	}

	@Override
	public void registerWithServer(LoginTask t) {
		AbstractRegistry registry = ActiveRegistry.getInstance();
		Task response = null;
		if(registry != null) {
			String msg = registry.isValidLogin(t);
			if(msg == null) {
				String nickname = registry.getUser(t.getEmail()).getNickname();
				setID(nickname);
				this.email = t.getEmail();
				server.registerClient(this, nickname);
				setRegistered();
				response = new LoginGreetingTask("Welcome Back " + nickname + "!",nickname);
			} else {
				response = new MessageTask(msg,MessageTask.ERROR);
			}
		} else {
			response = new MessageTask("Error occured while registering.",MessageTask.ERROR);
		}

		try {
			send(response);
		} catch(IOException e) {

		}
	}

	@Override
	public String toString() {
		return address;
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
