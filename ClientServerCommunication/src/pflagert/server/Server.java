package pflagert.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import pflagert.server.events.ReceiveEvent;
import pflagert.server.session.ClientSession;
import pflagert.transmission.Task;

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
		if(isRunning()) {
			return;
		} else synchronized(isRunning) {
			isRunning = true;
		}

		try {
			int eventCount;
			while(isRunning()) {
				eventCount = 0;
				selector.select();
				Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();
				while(selectedKeys.hasNext()) {
					SelectionKey key = (SelectionKey) selectedKeys.next();
					selectedKeys.remove();

					if(!key.isValid()) {
						continue;
					} else if(key.isAcceptable()) {
						ClientSession client = accept(key);
						clientConnected(client,key);
					} else if(key.isReadable()) {
						eventCount++;
						ClientSession client = clientMap.get(key);
						threadPool.execute(new ReceiveEvent(client));
					}
				}
				if(eventCount > 0) {
					try {
						threadPool.awaitTermination(EVENT_TIME_OUT, TIME_OUT_UNIT);
					} catch (InterruptedException e) {
				
					}
				}
			}
		} catch (IOException e) {

		} finally {
			synchronized(isRunning) {
				isRunning = false;
			}
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
		t.run();		
	}
	
	@Override
	public void registerClient(ClientSession client, String ID) {
		synchronized(registeredClients) {
			registeredClients.put(ID, client);
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
				selectedKey = (channel == null) ? null : 
					channel.register(selector, SelectionKey.OP_READ);

			} catch (ClosedChannelException e) {
				selectedKey = null;
			} catch (IOException e) {
				channel = null;
			} 

			if(selectedKey != null) {
				try {
					ret = new ClientSession(this,selectedKey,null);
				} catch (IOException e) {
					ret = null;
				}
			}
		}
		return ret;
	}

	@Override
	protected void clientConnected(ClientSession client, SelectionKey key) {
		synchronized(clientMap) {
			clientMap.put(key, client);
		}		
	}

	@Override
	protected void clientDisconnected(ClientSession client, SelectionKey key) {
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
	}
}
