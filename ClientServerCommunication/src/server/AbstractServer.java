package server;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import server.session.ClientSession;
import transmission.Task;

/**
 * @author pflagert
 * The AbstractServer class defines methods that a server should have.
 */
public abstract class AbstractServer {
	
	/**
	 * The instance variable clientMap is protected so that classes that extend AbstractServer 
	 * can access the map.
	 * The map contains a key value which is the java.nio.channels.SelectionKey 
	 * which is important for sending and receiving messages to/from clients. 
	 * The entry in the map is a ClientSession.
	 */	
	protected Map<SelectionKey,ClientSession> clientMap;
	
	/**
	 * The selector instance variable is used to determine which SelectionKey is ready to
	 * read/write/connect to. 
	 * Note that the Selector class is imported from: java.nio.channels.Selector
	 * @see Selector
	 */
	protected Selector selector;
	
	/**
	 * @see ServerSocketChannel
	 */
	protected ServerSocketChannel serverChannel;
	
	protected AbstractServer() {
		clientMap = new HashMap<SelectionKey,ClientSession>();
		selector = null;
		serverChannel = null;
	}
	
	/**
	 * The start method starts the server.
	 * Once the server is started, the server will connect/send/receive messages/requests
	 * to or from clients.
	 */
	public abstract void start();
	
	/**
	 * The stop method stops the server. 
	 * Once the server is stopped, the server will no longer connect to clients. 
	 * However, the server will continue to send/receive messages to and from clients that 
	 * are already connected.
	 */
	public abstract void stop();
	
	/**
	 * The disconnectAllClients method implies stop()
	 * but the server will no longer send/messages to and from any clients.
	 */
	public abstract void disconnectAllClients();
	
	/**
	 * The isRunning method returns the current status of the server.
	 * @return true if the server was successfully started.
	 * Returns false if the server hasn’t been started or was successfully stopped.
	 */
	public abstract boolean isRunning();
	
	/**
	 * The getClients method returns a List of ClientSessions. 
	 * Note that the getClients method constructs the list of clients from the clientMap.
	 * @return A list of ClientSessions
	 */
	public abstract List<ClientSession> getClients();
	
	/**
	 * The default implementation of handleTask simply executes the Task that was passed into this method.
	 * If a different behavior is desired from this method, this class should be extended then this method
	 * can be overwritten
	 * @param t - The {@link Task} to be handled.
	 */
	public abstract void handleTask(Task t);
	
	/**
	 * The accept method accepts a client connection request, creates a new ClientSession, 
	 * then calls clientConnected() on the newly created ClientSession. 
	 * Note: Although this method has the permissions to be overwritten,
	 * it may be better to overwrite the clientConnected method instead.
	 * @param key - The {@link SelectionKey} to be accepted.
	 * @return Returns a new ClientSession
	 */
	protected abstract ClientSession accept(SelectionKey key);
	
	/**
	 * The clientConnected method adds a new entry into the clientMap.
	 * @param client - The {@link ClientSession} that represents a new client.
	 * @param key - The {@link SelectionKey} that a client has connected through.
	 */
	protected abstract void clientConnected(ClientSession client, SelectionKey key);
	
	/**
	 * The clientDisconnected method removes a ClientSession from the clientMap.
	 * @param client - The {@link ClientSession} that has disconnected.
	 * @param key - The {@link SelectionKey} that the client was connected too.
	 */
	protected abstract void clientDisconnected(ClientSession client, SelectionKey key);
	
}
