/**
 * 
 */
package edu.colostate.cs.cs414.p5.client_server.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import edu.colostate.cs.cs414.p5.client_server.logger.Logger;
import edu.colostate.cs.cs414.p5.client_server.transmission.Task;
import edu.colostate.cs.cs414.p5.client_server.transmission.TaskFactory;
import edu.colostate.cs.cs414.p5.client_server.transmission.registration_login.EntryTask;
import edu.colostate.cs.cs414.p5.client_server.transmission.registration_login.ExitTask;
import edu.colostate.cs.cs414.p5.client_server.transmission.registration_login.response.EntryResponseTask;
import edu.colostate.cs.cs414.p5.client_server.transmission.registration_login.response.ExitResponseTask;

/**
 * @author pflagert
 * The Client class extends AbstractClient and provides a default implementation of 
 * methods inherited from AbstractClient.
 */
public class Client extends AbstractClient {

	/*
	 * Time out variables for "select" operations Milliseconds
	 */
	public static final int SELECT_TIMEOUT = 500;

	/* Thread Pool Initialization variables */
	public static final int CORE_THREAD_POOL_SIZE = 4;
	public static final int MAX_THREAD_POOL_SIZE = 8;
	public static final int THREAD_KEEP_ALIVE_TIME = 10;
	public static final TimeUnit ALIVE_TIME_UNIT = TimeUnit.MINUTES;

	private static final Logger LOG = Logger.getInstance();
	/**
	 * A thread pool to manage tasks
	 */
	private ThreadPoolExecutor threadPool;

	private SocketChannel serverChannel;

	private Boolean isReceiving;
	private Object writeLock;
	private Object readLock;
	private Object loginLock;
	private Thread receivingThread;
	private Boolean isLoggedIn;
	private volatile boolean responseReceived;

	/**
	 * Creates a new instance of Client.
	 * @throws IOException 
	 */
	public Client() throws IOException {
		super();
		selector = Selector.open();
		channel = SocketChannel.open();
		channel.configureBlocking(false);
		channel.register(selector, SelectionKey.OP_CONNECT);
		isReceiving = false;
		serverChannel = null;
		writeLock = new Object();
		readLock = new Object();
		loginLock = new Object();
		receivingThread = null;
		isLoggedIn = false;
		responseReceived = false;
		initThreadPool();
	}

	/**
	 * Creates a new instance of client and connects the Client to a Server with
	 * the associated InetSocketAddress.
	 * Note that this is equivalent to:
	 * Client c = new Client();
	 * c.connectToServer(address);
	 * @param address - The {@link IndetSocketAddress} that represents the server's address
	 * @throws IOException 
	 */
	public Client(InetSocketAddress address) throws IOException {
		this();
		channel.connect(address);
		connect();
	}

	/**
	 * Creates a new instance of client and connects the Client to a Server
	 * with the associated address and port number.
	 * Note that this is equivalent to:
	 * Client c = new Client();
	 * c.connectToServer(address, port);
	 * @param address - The address of the server connecting too.
	 * @param port - The port number the server is listening on.
	 * @throws IOException 
	 */
	public Client(String address, int port) throws IOException {
		this(new InetSocketAddress(address,port));
	}

	private void initThreadPool() {
		threadPool = new ThreadPoolExecutor(CORE_THREAD_POOL_SIZE,
				MAX_THREAD_POOL_SIZE,
				THREAD_KEEP_ALIVE_TIME,
				ALIVE_TIME_UNIT,
				new LinkedBlockingQueue<Runnable>());
		threadPool.prestartCoreThread();
	}

	@Override
	public void connectToServer(InetSocketAddress serverAddress) throws IOException {
		LOG.debug("Connecting to: " + serverAddress);
		synchronized(writeLock) {
			synchronized(readLock) {

				if(isConnected()) {
					disconnectFromServer();
				}
				unsetLoggedIn();
				channel = SocketChannel.open();
				channel.configureBlocking(false);
				channel.connect(serverAddress);
				channel.register(selector, SelectionKey.OP_CONNECT);
				connect();
			}
		}
	}

	@Override
	public void connectToServer(String address, int port) throws IOException {
		connectToServer(new InetSocketAddress(address,port));		
	}

	public boolean isLoggedIn() {
		synchronized(isLoggedIn) {
			return isLoggedIn;
		}
	}

	public void setLoggedIn() {
		synchronized(isLoggedIn) {
			isLoggedIn = true;
		}
	}

	public void unsetLoggedIn() {
		synchronized(isLoggedIn) {
			isLoggedIn = false;
		}
	}

	@Override
	public boolean isReceiving() {
		synchronized(isReceiving) {
			return isReceiving && isConnected();
		}
	}

	@Override
	public boolean isConnected() {
		synchronized(writeLock) {
			synchronized(readLock) {
				return isChannelConnected(channel) && isChannelConnected(serverChannel);
			}
		}
	}

	private boolean isChannelConnected(SocketChannel c) {
		return c != null && c.isConnected() && c.isOpen();
	}

	@Override
	public void stopReceiving() {
		synchronized(isReceiving) {
			isReceiving = false;
		}		
	}

	@Override
	public void startReceiving() {
		synchronized(isReceiving) {
			if(!isConnected()) {
				throw new IllegalStateException("You must be connected to a server "
						+ "before receiving from a server");
			} else if(isReceiving() && receivingThread != null && receivingThread.isAlive()) {
				return;
			} else {
				isReceiving = true;
				constructReceivingThread();
				receivingThread.start();
			}
		}
	}

	private void constructReceivingThread() {
		receivingThread = new Receiver();
	}

	private void connect() {
		try {
			while(true) {
				selector.select(SELECT_TIMEOUT);
				Iterator<SelectionKey> selectedKeys = this.selector.selectedKeys().iterator();
				while(selectedKeys.hasNext()) {
					SelectionKey key = (SelectionKey) selectedKeys.next();
					selectedKeys.remove();
					if(!key.isValid())
						continue;
					else if(key.isConnectable()) {
						connect(key);
						return;
					}
				}
			}
		} catch (IOException e) {
			LOG.error("IOException occurred when attempting to establish a connection...\n"+e.getMessage());
		}
	}

	private void connect(SelectionKey key) {
		try {
			LOG.info("Attempting to connect...");
			while(!((SocketChannel) key.channel()).finishConnect());

			if(((SocketChannel) key.channel()).isConnected()) {
				LOG.info("Connected!");
				key.interestOps(SelectionKey.OP_READ);
				serverChannel = ((SocketChannel)key.channel());
			}
		} catch (IOException e) {
			LOG.error("Could not connect to server.");
		}
	}


	@Override
	public void disconnectFromServer() {
		synchronized(readLock) {
			synchronized(writeLock) {
				stopReceiving();
				try {
					if(channel != null)
						channel.close();
					if(serverChannel != null)
						serverChannel.close();
				} catch (IOException e) {

				} finally {
					channel = null;
					serverChannel = null;
					unsetLoggedIn();
				}
			}
		}
	}

	private void send(Task t) throws IOException {
		if(!isConnected()) {
			LOG.error("no longer connected");
			return;
		} else {
			synchronized(writeLock) {
				byte[] data = t.toByteArray();
				int dataLength = data.length;
				ByteBuffer writeBuffer = ByteBuffer.allocate(dataLength+4);
				writeBuffer.putInt(dataLength);
				writeBuffer.put(data);
				writeBuffer.flip();
				while(writeBuffer.hasRemaining()) {
					serverChannel.write(writeBuffer);
				}
			}
		}
	}

	@Override
	public void sendToServer(Task t) throws IOException {
		if(!isConnected()) {
			LOG.error("Tried to send Task: " + t + " but a connection hasn't been established");
			return;
		}
		else if(!isReceiving()) {
			startReceiving();
		}
		try {
			if(t instanceof EntryTask || t instanceof ExitTask) {
				this.threadPool.execute(new NotifySender(t));
				waitForResponse();
			} else {
				send(t);
			}
		} catch (IOException e) {
			LOG.error("IOException occurred while trying to send Task: " + t + "\n" + e.getMessage());
		} catch (InterruptedException e) {
			LOG.error(e.getMessage());
		}
	}

	private void waitForResponse() throws InterruptedException {
		synchronized(this.loginLock) {
			LOG.debug("Waiting for a response...");
			while(!responseReceived) {
				this.loginLock.wait();
			}
			LOG.debug("Done waiting!");
			responseReceived = false;
		}
	}

	private void notifyResponse() {
		synchronized(this.loginLock) {
			LOG.debug("Response received...");
			responseReceived = true;
			this.loginLock.notifyAll();
		}
	}

	@Override
	public void receive() throws IOException {
		ByteBuffer localWrite = null;

		ByteBuffer readBuffer = ByteBuffer.allocate(7000);
		int temp = 0, read = 0;
		int size = -1;
		synchronized(readLock) {
			while( (temp = channel.read(readBuffer)) > 0 ) {
				read+=temp;
				if(read >= 4) {
					readBuffer.flip();
					size = readBuffer.getInt();
					if(size >= 1) {
						localWrite = ByteBuffer.allocate(size);
						if(read > 4) {
							fillLocal(localWrite,readBuffer);
						}
						receiveTask(localWrite,size, read);
						read -= size;
					} else {
						LOG.error("Received a corrupted task (the size of the task is invalid)");
						return;
					}
				} else {
					continue;
				}
			}
			if(read > 0 && readBuffer.remaining() >= 4) {
				handleOverread(readBuffer);
			}
			if(temp == -1 || !isConnected()) {
				disconnectFromServer();
			} else {
				LOG.debug("Finished reading");
			}
		}
	}

	private void handleOverread(ByteBuffer readBuffer) throws IOException {
		while(readBuffer.remaining() >= 4) {
			int size = readBuffer.getInt();
			if(size >= 1) {
				ByteBuffer localWrite = ByteBuffer.allocate(size);
				fillLocal(localWrite,readBuffer);
				receiveTask(localWrite,size, 0);
			} else {
				return;
			}

		}
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

		if(temp == -1) {
			disconnectFromServer();
		} else {
			handleTask(createTask(local));
		}

		return read;
	}

	private Task createTask(ByteBuffer local) throws IOException {
		return TaskFactory.getInstance().createTaskFromBytes(local.array());
	}

	@Override
	public void handleTask(Task t) {
		LOG.info("Performing Task: " + t);
		if(t instanceof EntryResponseTask) {
			EntryResponseTask response = (EntryResponseTask) t;
			if(response.wasSuccessful()) {
				this.setLoggedIn();
			}
			threadPool.execute(t);
			notifyResponse();
		} else if(t instanceof ExitResponseTask) {
			this.unsetLoggedIn();
			threadPool.execute(t);
			notifyResponse();			
		} else {
			threadPool.execute(t);
		}
	}
	
	private class Receiver extends Thread {
		public void run() {
			try{
				while(isReceiving()) {
					selector.select(SELECT_TIMEOUT);
					Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();
					while(selectedKeys.hasNext()) {
						SelectionKey key = (SelectionKey) selectedKeys.next();
						selectedKeys.remove();
						if(!key.isValid())
							continue;
						else if(key.isReadable()) {
							receive();
						}
					}
				}
			} catch (IOException e) {
				LOG.error("IOException occurred in Receiver thread...\n" + e.getMessage());
			} finally {
				stopReceiving();
			}
		}
	}

	private class NotifySender implements Runnable {
		private final Task toSend;
		
		NotifySender(Task toSend) {
			this.toSend = toSend;
		}

		@Override
		public void run() {
			try {
				send(toSend);
			} catch (IOException e) {
				notifyResponse();
				LOG.error("IOException occurred in NotifySender thread...\n" + e.getMessage());
			}			
		}
	}
}
