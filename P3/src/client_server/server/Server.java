package client_server.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import client_server.server.events.ReceiveEvent;
import client_server.server.session.ClientSession;
import client_server.transmission.Task;

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
	
	/* Used for debugging */
	public static final boolean DEBUG = true;
	public static final String DEBUG_TAB = "    ";
	private static int NUM_TABS = 0;
	
	/**
	 * Keeps track of whether or not the server is running. 
	 */
	private Boolean isRunning;
	
	/**
	 * A thread pool to manage client sessions
	 */
	private ThreadPoolExecutor threadPool;

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
		} else synchronized(isRunning) {
			debugPrintln("Starting server.");
			isRunning = true;
		}

		try {
			runServer();
		} catch (IOException e) {
			debugPrintln("IOException.");
		} finally {
			synchronized(isRunning) {
				isRunning = false;
			}
		}
	}
	
	private void runServer() throws IOException {
		int eventCount;
		debugPrintHeader("runServer");
		System.out.println("Server is listening on: " +
				this.serverChannel.getLocalAddress().toString());
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
		} else if(key.isAcceptable()) {
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
		if(isRunning()) { 
			synchronized(isRunning) {
				isRunning = false;
			}
		}		
	}

	@Override
	public void disconnectAllClients() {
		stop();
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
		debugPrintln("Running TaskCode: " + t.getTaskCode());
		t.run();
		debugPrintFooter("handleTask");
	}
	
	@Override
	public void handleTask(Task t, ClientSession client) {
		debugPrintHeader("handleTask");
		debugPrintln("Running TaskCode: " + t.getTaskCode());
		t.run();
		debugPrintFooter("handleTask");
	}
	
	@Override
	public void registerClient(ClientSession client, String ID) {
		synchronized(registeredClients) {
			registeredClients.put(ID, client);
		}	
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
			System.out.println("New client connected");
		}		
	}

	@Override
	public void clientDisconnected(ClientSession client, SelectionKey key) {
		debugPrintHeader("clientDisconnected");
		debugPrintln("Client Disconnected: ");
		if(key != null) {
			key.cancel();
		}
		synchronized(clientMap) {
			if(clientMap.containsKey(key))
				clientMap.remove(key);
		}
		String id = client.getID();
		if(id != null) {			
			synchronized(registeredClients) {
				if(registeredClients.containsKey(id))
					registeredClients.remove(id);
			}
		}
		debugPrintln((id==null) ? "unkown" : id);
		debugPrintFooter("clientDisconnected");
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
	
	public static void main(String[] args) throws IOException {
		if(args.length != 1) {
			System.out.println("Expected arguments: <port>");
			return;
		}
		else {
			int port;
			try {
				port = Integer.parseInt(args[0]);
				if(port <=0 ) 
					throw new NumberFormatException();
			} catch (NumberFormatException ex) {
				System.out.println("Invalid port: " + args[0]);
				return;
			}

			InetSocketAddress address = new InetSocketAddress(port);
			Server server = null;
			server = new Server(address);
			server.start();
			ActiveServer.setInstance(server);
			
		}
	}
	
}
