/**
 * 
 */
package server.session;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import server.AbstractServer;
import transmission.Task;
import transmission.TaskFactory;

/**
 * @author pflagert
 *
 */
public class ClientSession extends AbstractSession {

	private SocketChannel channel;
	private DataInputStream din;
	private DataOutputStream dout;

	public ClientSession(AbstractServer server, SelectionKey key, String ID) throws IOException {
		super(server, key, ID);
		channel = (SocketChannel) key.channel();
		din = new DataInputStream(channel.socket().getInputStream());
		dout = new DataOutputStream(channel.socket().getOutputStream());
	}

	public ClientSession(AbstractSession s) throws IOException {
		this(s.server,s.key,s.ID);
	}

	@Override
	public void receive() throws IOException {
		synchronized(din) {
			int dataLength = din.readInt();
			byte[] data = new byte[dataLength];
			din.readFully(data, 0, dataLength);
			Task t = TaskFactory.getInstance().createTaskFromBytes(data);
			server.handleTask(t);
		}
	}

	@Override
	public void send(Task t) throws IOException {
		synchronized(dout) {
			byte[] data = t.toByteArray();
			int dataLength = data.length;
			dout.writeInt(dataLength);
			dout.write(data,0,dataLength);
			dout.flush();
		}
	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub

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
	public void registerWithServer() {

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

}
