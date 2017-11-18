package edu.colostate.cs.cs414.p5.client_server.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import edu.colostate.cs.cs414.p5.client_server.server.events.ReceiveEvent;
import edu.colostate.cs.cs414.p5.client_server.server.events.SendEvent;
import edu.colostate.cs.cs414.p5.client_server.server.registry.ActiveRegistry;
import edu.colostate.cs.cs414.p5.client_server.server.registry.FileRegistry;
import edu.colostate.cs.cs414.p5.client_server.server.session.ClientSession;
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
	
	/* Used for debugging */
	public static final boolean DEBUG = false;
	public static final String DEBUG_TAB = "    ";
	private static int NUM_TABS = 0;
	
	/**
	 * Used for sending registered clients the nicknames of all the players 
	 */
	private List<String> playerNicknames;
	
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
			playerNicknames = new LinkedList<String>();
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

	@Override
	public void start() {
		debugPrintHeader("start");
		if(isRunning()) {
			debugPrintln("Server is already running.");
			return;
		} else {
			debugPrintln("Starting server.");
			synchronized(isRunning) {
				isRunning = true;
			}
			synchronized(isListening) {
				isListening = true;
			}
			try {
				runServer();
			} catch (IOException e) {
				debugPrintln("IOException.");
			} finally {
				stop();
			}
		}
	}
	
	@Override
	public void startWithNewThread() {
		debugPrintHeader("startWithNewThread");
		if(isRunning()) {
			debugPrintln("Server is already running.");
			return;
		} else {
			debugPrintln("Starting server.");
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
		debugPrintHeader("runServer");
		log("Server is listening on: " +
				this.serverChannel.socket().getLocalPort());
		while(isRunning()) {
			eventCount = 0;
			debugPrintln("Selecting keys");
			selector.select(SELECT_TIME_OUT);
			Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();
			debugPrintHeader("Iterating keys");
			while(selectedKeys.hasNext()) {
				SelectionKey key = (SelectionKey) selectedKeys.next();
				eventCount += handleKey(key);
				selectedKeys.remove();
			}
			debugPrintFooter("Done Iterating keys: " + eventCount + " total events");
		}
		debugPrintFooter("runServer");
	}
	
	private int handleKey(SelectionKey key) {
		if(!key.isValid()) {
			debugPrintln("key is Invalid");
		} else if(key.isAcceptable() && isListening()) {
			debugPrintln("key is Acceptable");
			ClientSession client = accept(key);
			if(client != null) {
				debugPrintln("Client is not null, adding to clientMap");
				clientConnected(client,client.getKey());
			} else {
				debugPrintln("Client is null, NOT adding to clientMap");
			}
		} else if(key.isReadable()) {
			debugPrintln("key is Readable");
			ClientSession client = getClient(key);
			if(client == null) {
				debugPrintln("Client is null");
			} else {
				debugPrintln("Client is not null");
				debugPrintln("Adding task to thread pool");
				threadPool.execute(new ReceiveEvent(client));
				return 1;
			}
		} else {
			debugPrintln("key is in unhandled state");
		}
		return 0;
	}
	
	private ClientSession getClient(SelectionKey key) {
		synchronized(clientMap) {
			return clientMap.get(key);
		}
	}

	@Override
	public void stop() {
		stopListening();
		threadPool.purge();
		synchronized(isRunning) {
			if(isRunning()) { 
				isRunning = false;
				log("Server is stopping");
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
		List<ClientSession> sessions = getClients();
		for(ClientSession client: sessions) {
			client.disconnect();
		}		
	}
	
	@Override
	public ClientSession getRegisteredClient(String ID) {
		synchronized(registeredClients) {
			return registeredClients.get(ID);
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
					log("Server is now listening for incoming connections");
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
				log("Server is no longer listening for incoming connections");
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
		List<ClientSession> sessions = getClients();
		log("broadcasting: " + t + " to: ");
		for(ClientSession session: sessions) {
			threadPool.execute(new SendEvent(session,t));
			log("\t"+session);
		}
		try {
			threadPool.awaitTermination(BROADCAST_TIME_OUT, BROADCAST_TIME_OUT_UNIT);
		} catch (InterruptedException e) {
			log("Interrupted while broadcasting task: " + t);
		}
	}

	@Override
	public List<String> getClientNicknames() {
		synchronized(playerNicknames) {
			return new ArrayList<String>(playerNicknames);
		}
	}
	
	@Override
	public List<ClientSession> getClients() {
		List<ClientSession> sessions = new LinkedList<ClientSession>();
		synchronized(clientMap) {
			for(ClientSession session: clientMap.values()) {
				sessions.add(session);
			}
		}
		return sessions;
	}
	
	@Override
	public List<ClientSession> getRegisteredClients() {
		List<ClientSession> sessions = new LinkedList<ClientSession>();
		synchronized(registeredClients) {
			for(ClientSession session: registeredClients.values()) {
				sessions.add(session);
			}
		}
		return sessions;
	}

	@Override
	public void handleTask(Task t) {
		debugPrintHeader("handleTask");
		runTaskIfValid(t);
		debugPrintFooter("handleTask");
	}
	
	@Override
	public void handleTask(Task t, ClientSession client) {
		debugPrintHeader("handleTask");
		runTaskIfValid(t,client);
		debugPrintFooter("handleTask");
	}
	
	private boolean taskIsValid(Task t) {
		if(t instanceof EntryTask) { // should be handled by session
			return false;
		} else if(t instanceof EntryResponseTask) { // clients should not send EntryResponses
			return false;
		} else if(t instanceof ExitTask) { // should be handled by session
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
	
	// Differs from taskIsValid(Task t) by insuring forwarded Tasks "fromPlayer" are not spoofed.
	private boolean taskIsValid(Task t, ClientSession client) {
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
	
	@Deprecated
	private void runTaskIfValid(Task t) {
		if(taskIsValid(t)) {
			debugPrintln("Running TaskCode: " + t.getTaskCode());
			log("Performing Task: " + t);
			t.run();
		} else {
			log("A client submitted miliscious task: " + t);
		}
	}
	
	private void runTaskIfValid(Task t, ClientSession client) {
		if(taskIsValid(t,client)) {
			debugPrintln("Running TaskCode: " + t.getTaskCode());
			log("Performing Task: " + t + " for " + client.getID() + " [" + client + "].");
			t.run();
		} else {
			log(client.getID() + " [" + client + "] submitted miliscious task: " + t);
		}
	}
	
	@Override
	public void registerClient(ClientSession client, String ID) {
		synchronized(registeredClients) {
			registeredClients.put(ID, client);
		}
		synchronized(playerNicknames) {
			playerNicknames.add(ID);
		}
		log(ID + " has logged in from [" + client + "].");
	}

	@Override
	public void unregisterClient(ClientSession client, String ID) {
		synchronized(registeredClients) {
			registeredClients.remove(ID);
		}
		synchronized(playerNicknames) {
			playerNicknames.remove(ID);
		}
		log(ID + " has logged off from [" + client + "].");
	}
	
	@Override
	protected ClientSession accept(SelectionKey key) {
		debugPrintHeader("accept");
		debugPrintln("Accepting connection to new client");
		ClientSession ret = null;
		if(key != null) {
			SocketChannel channel = null;
			SelectionKey selectedKey = null;

			try {
				channel = ((ServerSocketChannel)key.channel()).accept();
				if(channel != null) {
					debugPrintln("Channel is not null");
					channel.configureBlocking(false);
					selectedKey = channel.register(selector, SelectionKey.OP_READ);
					debugPrintln("Connected to: " + channel.socket().toString());
				} else {
					debugPrintln("Channel is null");
				}
			} catch (ClosedChannelException e) {
				debugPrintln("Channel is closed");
				selectedKey = null;
			} catch (IOException e) {
				debugPrintln("IOException");
				channel = null;
			} 

			if(selectedKey != null) {
				try {
					selectedKey.interestOps(SelectionKey.OP_READ);
					ret = new ClientSession(this,selectedKey,null);
					debugPrintln("Constructed new ClientSession");
				} catch (IOException e) {
					debugPrintln("IOException#2");
					ret = null;
				}
			} else {
				// first try failed
			}
			
		} else {
			debugPrintln("The key is null");
		}
		debugPrintFooter("accept");
		return ret;
	}

	@Override
	protected void clientConnected(ClientSession client, SelectionKey key) {
		synchronized(clientMap) {
			clientMap.put(key, client);
			log("New client connected from [" + client + "].");
		}		
	}

	@Override
	public void clientDisconnected(ClientSession client, SelectionKey key) {
		debugPrintHeader("clientDisconnected");
		if(key != null) {
			key.cancel();
		}
		String id = client.getID();
		synchronized(clientMap) {
			if(clientMap.containsKey(key)) { 
				clientMap.remove(key);
				log("Client [" + client +"] disconnected.");
			}
		}
		if(id != null) {			
			synchronized(registeredClients) {
				if(registeredClients.containsKey(id))
					registeredClients.remove(id);
			}
		}
		debugPrintFooter("clientDisconnected.");
	}
	
	private String getDate() {
		Date now = new Date(); // java.util.Date, NOT java.sql.Date or java.sql.Timestamp!
		return new SimpleDateFormat("HH:mm:ss.S", Locale.ENGLISH).format(now);
	}
	
	protected void log(String msg) {
		System.out.println("["+getDate()+"]: "+msg);
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
	
	public static void main(String[] args) throws IOException {
		if(args.length != 2) {
			System.out.println("Expected arguments: <port> <password-filename>");
			return;
		}
		else {
			int port;
			String passwordFile;
			try {
				port = Integer.parseInt(args[0]);
				if(port <=0 ) 
					throw new NumberFormatException();
			} catch (NumberFormatException ex) {
				System.out.println("Invalid port: " + args[0]);
				return;
			}
			
			passwordFile = args[1];

			InetSocketAddress address = new InetSocketAddress(port);
			Server server = null;
			server = new Server(address);
			ActiveServer.setInstance(server);
			
			FileRegistry registry = new FileRegistry(passwordFile);
			ActiveRegistry.setInstance(registry);
			
			server.start();
			
		}
	}
	
	private class ServerThread extends Thread {
		public void run() {
			try {
				runServer();
			} catch (IOException e) {
				debugPrintln("IOException.");
			} catch (Exception e) {
				// for catching interrupt
			} finally {
				Server.this.stop();
				log("ServerThread has stopped");
			}
		}
	}
}
