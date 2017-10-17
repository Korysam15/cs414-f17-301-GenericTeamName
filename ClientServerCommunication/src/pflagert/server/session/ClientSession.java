/**
 * 
 */
package pflagert.server.session;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import pflagert.server.AbstractServer;
import pflagert.transmission.Task;
import pflagert.transmission.TaskFactory;

/**
 * @author pflagert
 *
 */
public class ClientSession extends AbstractSession {

	private SocketChannel channel;

	private Object writeLock;
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
					System.out.println("READ " + size + " For the amount of required bytes");
					if(size >= 1) {
						localWrite = ByteBuffer.allocate(size);
						if(read > 4) {
							localWrite.put(readBuffer);
						}
						total += receiveTask(localWrite,size, read);
						read = 0;
						readBuffer.clear();
					}
				}
			}
			System.out.println("DONE READING BYTES: READ " + total + " TOTAL BYTES");
		}
	}

	private int receiveTask(ByteBuffer local, int size, int currentRead) throws IOException {
		int temp = 0,read = currentRead;
		while( local.hasRemaining() && (temp = channel.read(local)) > -1) {
			read += temp;
		}
		System.out.println("Done reading the required bytes: " + (read-currentRead));
		if(temp == -1) {
			server.clientDisconnected(this, key);
		} else {
			createTask(local);
		}
		return read;
	}

	private void createTask(ByteBuffer local) throws IOException {
		/*for(byte b: local.array()) {
				System.out.println(b);
			} */
		Task t = TaskFactory.getInstance().createTaskFromBytes(local.array());
		server.handleTask(t);
	}

	@Override
	public void send(Task t) throws IOException {
		synchronized(writeLock) {
			byte[] data = t.toByteArray();
			int dataLength = data.length;
			System.out.println("Sending: " + dataLength + " bytes for TaskCode: " + t.getTaskCode());
			/*for(byte b: data) {
				System.out.println(b);
			} */
			ByteBuffer writeBuffer = ByteBuffer.allocate(dataLength+4);
			writeBuffer.putInt(dataLength);
			writeBuffer.put(data);
			writeBuffer.flip();
			int written = 0;
			while(writeBuffer.hasRemaining()) {
				written += channel.write(writeBuffer);
			}
			System.out.println("Sent: " + written + " total bytes");
		}
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
