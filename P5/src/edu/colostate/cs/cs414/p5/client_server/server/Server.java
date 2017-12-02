package edu.colostate.cs.cs414.p5.client_server.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import edu.colostate.cs.cs414.p5.client_server.server.events.ReceiveEvent;
import edu.colostate.cs.cs414.p5.client_server.server.events.SendEvent;
import edu.colostate.cs.cs414.p5.client_server.server.session.ClientSession;
import edu.colostate.cs.cs414.p5.client_server.server.session.SessionManager;
import edu.colostate.cs.cs414.p5.client_server.transmission.Task;
import edu.colostate.cs.cs414.p5.client_server.transmission.registration_login.EntryTask;
import edu.colostate.cs.cs414.p5.client_server.transmission.registration_login.ExitTask;
import edu.colostate.cs.cs414.p5.client_server.transmission.registration_login.response.EntryResponseTask;
import edu.colostate.cs.cs414.p5.client_server.transmission.registration_login.response.ExitResponseTask;
import edu.colostate.cs.cs414.p5.client_server.transmission.registration_login.response.ServerDisconnectedTask;
import edu.colostate.cs.cs414.p5.client_server.transmission.util.ForwardTask;
import edu.colostate.cs.cs414.p5.client_server.transmission.util.MultiForwardTask;

/**
 * @author pflagert
 * The Server class extends AbstractServer 
 * and provides a basic implementation for the abstract methods in AbstractServer.
 */
public class Server extends AbstractServer {

	/* Thread Pool Initialization variables */
	public static final int CORE_THREAD_POOL_SIZE = 8;
	public static final int MAX_THREAD_POOL_SIZE = 16;
	public static final int THREAD_KEEP_ALIVE_TIME = 10;
	public static final TimeUnit ALIVE_TIME_UNIT = TimeUnit.MINUTES;

	/* Server Settings variables */
	public static final int EVENT_TIME_OUT = 30;
	public static final TimeUnit TIME_OUT_UNIT = TimeUnit.SECONDS;
	public static final int SELECT_TIME_OUT = 3000; // Milliseconds
	public static final int BROADCAST_TIME_OUT = 3;
	public static final TimeUnit BROADCAST_TIME_OUT_UNIT = TimeUnit.SECONDS;

	/**
	 * Keeps track of whether or not the server is running. 
	 */
	private Boolean isRunning;

	/**
	 * Keeps track of whether or not the server is listening for new connections.
	 */
	private Boolean isListening;

	/**
	 * A thread pool to manage client sessions
	 */
	private ThreadPoolExecutor threadPool;

	/**
	 * A thread that may be used to start the server.
	 */
	private ServerThread listener;

	/**
	 * Creates a new Server that will listen for incoming connections/messages on the
	 * InetSocketAddress passed into this constructor.
	 * @param address - The {@link InetSocketAddress} that the server should be bound too.
	 * @throws IOException
	 * @see {@link ServerSocketChannel#open()}
	 */
	public Server(InetSocketAddress address) throws IOException {
		super();
		if(address == null) {
			throw new IllegalArgumentException("The InetSocketAddress can not be null, "
					+ "It is needed to bind a socket to a specific address");
		} else {
			serverChannel.configureBlocking(false);
			serverChannel.socket().bind(address);
			serverChannel.register(selector, SelectionKey.OP_ACCEPT);
			isRunning = false;
			isListening = false;
			initThreadPool();
		}		
	}

	private void initThreadPool() {
		threadPool = new ThreadPoolExecutor(CORE_THREAD_POOL_SIZE,
				MAX_THREAD_POOL_SIZE,
				THREAD_KEEP_ALIVE_TIME,
				ALIVE_TIME_UNIT,
				new LinkedBlockingQueue<Runnable>());
		threadPool.prestartCoreThread();
	}

	/**
	 * Creates a new Server that will listen for incoming messages on the port number
	 * passed into this constructor.
	 * Note that this constructor creates an InetSocketAddress that will be used to 
	 * call Server(InetSocketAddress).
	 * @param port - The port number that this Server should be bound to.
	 * @throws IOException 
	 * @see #Server(InetSocketAddress)
	 */
	public Server(int port) throws IOException {
		this(new InetSocketAddress(port));
	}
	
	public String getStatus() {
		StringBuilder ret = new StringBuilder();
		
		int port = this.serverChannel.socket().getLocalPort();
		
		boolean running = isRunning();
		boolean listening = isListening();
		
		if(running) {
			ret.append("The server is accepting messages from clients on port: " + port);
		} else {
			ret.append("The server is NOT accepting messages from clients");
		}
		ret.append(".\n");
		
		if(listening) {
			ret.append("The server is listening for new clients on port: " + port);
		} else {
			ret.append(" is NOT listening for new clients");
		}
		ret.append(".\n");
		
		ret.append(getThreadPoolStatus());
		
		
		return ret.toString();
	}
	
	private String getThreadPoolStatus() {
		return "The threadpool has performed: " + threadPool.getCompletedTaskCount() + 
				" tasks with a maximum thread count of: " + threadPool.getLargestPoolSize() +
				".\n" +
				"Currently there are: " + threadPool.getActiveCount() +
				" active threads in the threadpool.";
	}

	@Override
	public void start() {
		if(isRunning()) {
			LOG.debug("Server is already running.");
			return;
		} else {
			LOG.debug("Starting server.");
			synchronized(isRunning) {
				isRunning = true;
			}
			synchronized(isListening) {
				isListening = true;
			}
			try {
				runServer();
			} catch (IOException e) {
				LOG.error("IOException occurred when running the server");
				LOG.debug(e.getMessage());
			} finally {
				stop();
			}
		}
	}

	@Override
	public void startWithNewThread() {
		if(isRunning()) {
			LOG.debug("Server is already running.");
			return;
		} else {
			LOG.debug("Starting server.");
			synchronized(isRunning) {
				isRunning = true;
			}
			synchronized(isListening) {
				isListening = true;
			}
			listener = new ServerThread();
			listener.start();
		}
	}

	private void runServer() throws IOException {
		int eventCount;
		LOG.info("Server is listening on: " +
				this.serverChannel.socket().getLocalPort());
		while(isRunning()) {
			eventCount = 0;
			selector.select(SELECT_TIME_OUT);
			Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();
			while(selectedKeys.hasNext()) {
				SelectionKey key = (SelectionKey) selectedKeys.next();
				eventCount += handleKey(key);
				selectedKeys.remove();
			}
			LOG.debug("Done Iterating keys: " + eventCount + " total events");
		}
	}

	private int handleKey(SelectionKey key) {
		try {
			if(!key.isValid()) {
				LOG.debug("key is Invalid");
			} else if(key.isAcceptable() && isListening()) {
				LOG.debug("key is Acceptable");
				ClientSession client = accept(key);
				if(client != null) {
					LOG.debug("Client is not null, adding to clientMap");
					sessionManager.clientConnected(client,client.getKey());
				} else {
					LOG.debug("Client is null, NOT adding to clientMap");
				}
			} else if(key.isReadable()) {
				LOG.debug("key is Readable");
				ClientSession client = sessionManager.getClient(key);
				if(client == null) {
					LOG.debug("Client is null");
				} else {
					LOG.debug("Client is not null");
					LOG.debug("Adding ReceiveEvent to thread pool");
					threadPool.execute(new ReceiveEvent(client));
					return 1;
				}
			} else {
				LOG.debug("key is in unhandled state");
			}
		} catch(CancelledKeyException e) {
			LOG.error("Key cancelled: " + e.getCause());
		}
		return 0;
	}

	@Override
	public void stop() {
		stopListening();
		threadPool.purge();
		synchronized(isRunning) {
			if(isRunning()) { 
				isRunning = false;
				LOG.info("Server is stopping");
			}
		}
		if(Thread.currentThread().equals(listener)) {
			return;
		} else if(listener != null && listener.isAlive()) {
			listener.interrupt();
		}
	}

	@Override
	public void disconnectAllClients() {
		stop();
		ServerDisconnectedTask shutDown = 
				new ServerDisconnectedTask("The server is shutting down. Please try again later.");
		broadcast(shutDown);
		List<ClientSession> sessions = SessionManager.getInstance().getClients();
		for(ClientSession client: sessions) {
			client.disconnect();
		}		
	}

	@Override
	public boolean isRunning() {
		synchronized(isRunning) {
			return isRunning;
		}
	}

	@Override
	public void startListening() {
		synchronized (isRunning) {
			synchronized (isListening) {
				if(isRunning) {
					isListening = true;
					LOG.info("Server is now listening for incoming connections");
				} else {
					throw new IllegalStateException("Server has to be running, to start listening");
				}
			}
		}
	}

	@Override
	public void stopListening() {
		synchronized(isListening) {
			if(isListening) {
				isListening = false;
				LOG.info("Server is no longer listening for incoming connections");
			}
		}
	}

	@Override
	public boolean isListening() {
		synchronized(isListening) {
			return isListening;
		}
	}

	@Override
	public void broadcast(Task t) {
		List<ClientSession> sessions = SessionManager.getInstance().getClients();
		LOG.info("broadcasting: " + t + " to: ");
		for(ClientSession session: sessions) {
			threadPool.execute(new SendEvent(session,t));
			LOG.info("\t"+session);
		}
		try {
			threadPool.awaitTermination(BROADCAST_TIME_OUT, BROADCAST_TIME_OUT_UNIT);
		} catch (InterruptedException e) {
			LOG.error("Interrupted while broadcasting task: " + t);
		}
	}

	private boolean taskIsValid(Task t) {
		if(t instanceof EntryTask) { // should be handled by sessionManager
			return false;
		} else if(t instanceof EntryResponseTask) { // clients should not send EntryResponses
			return false;
		} else if(t instanceof ExitTask) { // should be handled by sessionManager
			return false;
		} else if(t instanceof ExitResponseTask) { // clients definitely should NOT send these
			return false;
		} else if(t instanceof ForwardTask) { // A forward task is valid if it's "forwarded task" is valid.
			return taskIsValid(((ForwardTask)t).getTask());
		} else if(t instanceof MultiForwardTask) { // same as forward task
			return taskIsValid(((MultiForwardTask)t).getTask());
		} else { // all is good
			return true;
		}
	}

	public boolean taskIsValid(Task t, ClientSession client) {
		if(t instanceof ForwardTask) { 
			ForwardTask forward = (ForwardTask) t;
			String clientNickname = client.getID();
			String submittedNickname = forward.getPlayerFrom();

			// a forward task is invalid if the "getPlayerFrom" doesn't match the ID of the session
			return submittedNickname.equals(clientNickname) &&
					taskIsValid(forward.getTask(),client); // A forward task is valid if it's "forwarded task" is valid.

		} else if(t instanceof MultiForwardTask) {
			MultiForwardTask forward = (MultiForwardTask) t;
			String clientNickname = client.getID();
			String submittedNickname = forward.getPlayerFrom();

			// a forward task is invalid if the "getPlayerFrom" doesn't match the ID of the session
			return submittedNickname.equals(clientNickname) &&
					taskIsValid(forward.getTask(),client); // A forward task is valid if it's "forwarded task" is valid.
		} else {
			return taskIsValid(t);
		}
	}

	public void runTaskIfValid(Task t, ClientSession client) {
		if(taskIsValid(t,client)) {
			LOG.debug("Running TaskCode: " + t.getTaskCode());
			LOG.info("Performing Task: " + t + " for " + client.getID() + " [" + client + "].");
			t.run();
		} else {
			LOG.info(client.getID() + " [" + client + "] submitted miliscious task: " + t);
		}
	}

	@Override
	protected ClientSession accept(SelectionKey key) {
		ClientSession ret = null;
		if(key != null) {
			SocketChannel channel = null;
			SelectionKey selectedKey = null;

			try {
				channel = ((ServerSocketChannel)key.channel()).accept();
				if(channel != null) {
					LOG.debug("Channel is not null");
					channel.configureBlocking(false);
					selectedKey = channel.register(selector, SelectionKey.OP_READ);
					LOG.debug("Connected to: " + channel.socket().toString());
				} else {
					LOG.debug("Channel is null");
				}
			} catch (ClosedChannelException e) {
				LOG.error(e.getMessage());
				selectedKey = null;
			} catch (IOException e) {
				LOG.error(e.getMessage());
				channel = null;
			} 

			if(selectedKey != null) {
				try {
					selectedKey.interestOps(SelectionKey.OP_READ);
					ret = new ClientSession(selectedKey,null);
					LOG.debug("Constructed new ClientSession");
				} catch (IOException e) {
					LOG.error(e.getMessage());
					ret = null;
				}
			} else {
				// first try failed
			}

		} else {
			LOG.debug("The key is null");
		}
		return ret;
	}

	private class ServerThread extends Thread {
		public void run() {
			try {
				runServer();
			} catch (IOException e) {
				LOG.debug(e.getMessage());
			} catch (Exception e) {
				// for catching interrupt
			} finally {
				Server.this.stop();
				LOG.info("ServerThread has stopped");
			}
		}
	}
}
