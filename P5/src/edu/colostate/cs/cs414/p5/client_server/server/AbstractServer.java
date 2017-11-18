package edu.colostate.cs.cs414.p5.client_server.server;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.colostate.cs.cs414.p5.client_server.server.session.ClientSession;
import edu.colostate.cs.cs414.p5.client_server.transmission.Task;

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
	 * Keeps track of registered clients
	 */
	protected Map<String,ClientSession> registeredClients;
	
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
	
	protected AbstractServer() throws IOException {
		clientMap = new HashMap<SelectionKey,ClientSession>();
		registeredClients = new HashMap<String, ClientSession>();
		serverChannel = ServerSocketChannel.open();
		selector = Selector.open();
	}
	
	/**
	 * The start method starts the server.  And the calling {@link Thread} will be the listener.
	 * Thus, instructions called after this method will only be executed when the server stops.
	 * Once the server is started, the server will connect/send/receive messages/requests
	 * to or from clients.
	 * @see #startWithNewThread()
	 */
	public abstract void start();
	
	/**
	 * The startWithNewThread method starts the server, but the method returns.
	 * Once the server is started, the server will connect/send/receive messages/requests
	 * to or from clients.
	 * @see #start()
	 */
	public abstract void startWithNewThread();
	
	/**
	 * Allows the server to start listening for new connections.
	 * Note that this method only works if {@link #isRunning()} returns true.
	 * Another way to consider the use of this method is to first, {@link #start()}
	 * then {@link #stopListening()}, then and only then you can startListening.
	 * If the server is not started, and this method is called an IllegalStateException is thrown.
	 */
	public abstract void startListening();
	
	/**
	 * Stops the server from accepting new connections.
	 */
	public abstract void stopListening();
	
	/**
	 * The stop method stops the server.  However, clients will remain connected.
	 * Implies {@link #stopListening()}.
	 */
	public abstract void stop();
	
	/**
	 * The disconnectAllClients method implies stop()
	 * but the server will force a disconnection to all of the clients.
	 */
	public abstract void disconnectAllClients();
	
	/**
	 * The isRunning method returns the current status of the server.
	 * @return true if the server is currently excepting messages from clients.
	 * Returns false if the is not excepting messages from client.
	 */
	public abstract boolean isRunning();

	/**
	 * The isListening method returns whether the server is listening for incoming connections.
	 * @return True if the server is listening for incoming connections.
	 * Returns false if the server is not listening for incoming connections.
	 */
	public abstract boolean isListening();
	
	/**
	 * Sends the {@link Task} to all connected clients.
	 * @param t - The task to send to all clients.
	 */
	public abstract void broadcast(Task t);
	
	/**
	 * The getRegisteredClient method returns a ClientSession associated with param ID.
	 * @param ID - The ID for the ClientSession.
	 * @return The {@link ClientSession} associated with the ID.  Or null if there are no
	 * ClientSessions with that ID.
	 */
	public abstract ClientSession getRegisteredClient(String ID);
	
	/**
	 * The getClientNicknames method returns a list of Strings that represents
	 * all of the registered clients.
	 * @return A list of player nicknames
	 */
	public abstract List<String> getClientNicknames();
	
	
	/**
	 * The getClients method returns a List of ClientSessions. 
	 * Note that the getClients method constructs the list of clients from the clientMap.
	 * @return A list of ClientSessions
	 */
	public abstract List<ClientSession> getClients();
	
	/**
	 * The getRegisterClients method returns a List of ClientSessions. 
	 * Note that the getRegisteredClients method constructs the list of clients from the registeredClients map.
	 * @return A list of ClientSessions
	 */
	public abstract List<ClientSession> getRegisteredClients();
	
	/**
	 * The default implementation of handleTask simply executes the Task that was passed into this method.
	 * If a different behavior is desired from this method, this class should be extended then this method
	 * can be overwritten
	 * @param t - The {@link Task} to be handled.
	 */
	@Deprecated
	public abstract void handleTask(Task t);
	
	/**
	 * The default implementation of handleTask simply executes the Task that was passed into this method.
	 * If a different behavior is desired from this method, this class should be extended then this method
	 * can be overwritten
	 * @param t - The {@link Task} to be handled.
	 */
	public abstract void handleTask(Task t, ClientSession client);
	
	/**
	 * The registerClient method adds a new entry into the registeredClients map.
	 * Note that registering client implies logging in or the creation of a new account. 
	 */
	public abstract void registerClient(ClientSession client, String ID);
	
	/**
	 * The unregisterClient method removes an entry from the registeredClients map.
	 * Note that unregistering client implies logging off or the deletion of an account. 
	 */
	public abstract void unregisterClient(ClientSession client, String ID);
	
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
	public abstract void clientDisconnected(ClientSession client, SelectionKey key);
	
}
