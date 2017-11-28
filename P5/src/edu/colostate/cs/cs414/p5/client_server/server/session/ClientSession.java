/**
 * 
 */
package edu.colostate.cs.cs414.p5.client_server.server.session;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import edu.colostate.cs.cs414.p5.client_server.logger.Logger;
import edu.colostate.cs.cs414.p5.client_server.transmission.Task;
import edu.colostate.cs.cs414.p5.client_server.transmission.TaskFactory;

/**
 * @author pflagert
 *
 */
public class ClientSession extends AbstractSession {
	private static final Logger LOG = Logger.getInstance();

	private SocketChannel channel;

	// prevents two threads from writing to the same channel at the same time
	private Object writeLock;
	// prevents two from reading from the same channel at the same time
	private Object readLock;

	private String email;
	
	private String address;

	public ClientSession(SelectionKey key, String ID) throws IOException {
		super(key, ID);
		channel = (SocketChannel) key.channel();
		writeLock = new Object();
		readLock = new Object();
		this.address = channel.socket().getRemoteSocketAddress().toString();
	}
	
	public String getEmail() {
		synchronized(address) {
			return email;
		}
	}
	
	public void setEmail(String email) {
		synchronized(address) {
			this.email = email;
		}
	}

	@Override
	public void receive() throws IOException {
		LOG.debug("Trying to read bytes to create task");
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
					LOG.debug("READ " + size + " For the amount of required bytes");
					if(size >= 1) {
						localWrite = ByteBuffer.allocate(size);
						if(read > 4) {
							fillLocal(localWrite,readBuffer);
						}
						total += receiveTask(localWrite,size, read);
						read -= size;
						//readBuffer.clear();
					} else {
						LOG.error("SIZE: " + size + " is an invalid ammount of bytes to read.");
						LOG.debug("returning");
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
				manager.clientDisconnected(this, key);
			} else {
				LOG.debug("DONE READING BYTES: READ " + total + " TOTAL BYTES");
			}
		}
	}

	private void handleOverread(ByteBuffer readBuffer) throws IOException {
		LOG.debug("Handling additional read bytes");
		while(readBuffer.remaining() >= 4) {
			int size = readBuffer.getInt();
			LOG.debug("READ " + size + " For the amount of required bytes");
			if(size >= 1) {
				ByteBuffer localWrite = ByteBuffer.allocate(size);
				fillLocal(localWrite,readBuffer);
				receiveTask(localWrite,size, 0);
			} else {
				LOG.error("SIZE: " + size + " is an invalid ammount of bytes.");
				LOG.debug("returning");
				return;
			}

		}
		//debugPrintFooter("handleOverread");
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

		LOG.debug("Done reading the required bytes: " + (read - 4));

		if(temp == -1) {
			manager.clientDisconnected(this, key);
		} else {
			createTask(local);
		}

		return read;
	}

	private void createTask(ByteBuffer local) throws IOException {
		Task t = TaskFactory.getInstance().createTaskFromBytes(local.array());
		manager.handleTask(t,this);
	}

	@Override
	public void send(Task t) throws IOException {
		//debugPrintHeader("send");
		synchronized(writeLock) {
			byte[] data = t.toByteArray();
			int dataLength = data.length;
			LOG.debug("Sending: " + dataLength + " bytes for TaskCode: " + t);
			ByteBuffer writeBuffer = ByteBuffer.allocate(dataLength+4);
			writeBuffer.putInt(dataLength);
			writeBuffer.put(data);
			writeBuffer.flip();
			int written = 0;
			while(writeBuffer.hasRemaining()) {
				written += channel.write(writeBuffer);
			}
			LOG.debug("Sent: " + written + " total bytes");
		}
	}

	@Override
	public void disconnect() {
		manager.clientDisconnected(this, key);
		if(key != null)
			key.cancel();
		try {
			if(channel != null && channel.isOpen()) {
				channel.close();
			}
		} catch(Exception e) {
			
		} finally {
			channel = null;
			email = null;
			key = null;
			address = null;
			setID(null);
		}
	}

	@Override
	public String toString() {
		return address;
	}

	@Override
	public int hashCode() {
		return (address+email+"ID").hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if(o == null || !(o instanceof ClientSession)) {
			return false;
		} else {
			// no need to cast o to ClientSession, hashCode is overwritten in ClientSession
			return this.hashCode()==o.hashCode();
		}
	}
}
