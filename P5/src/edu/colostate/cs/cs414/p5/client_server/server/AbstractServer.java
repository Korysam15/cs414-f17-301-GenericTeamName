package edu.colostate.cs.cs414.p5.client_server.server;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

import edu.colostate.cs.cs414.p5.client_server.logger.Logger;
import edu.colostate.cs.cs414.p5.client_server.server.session.ClientSession;
import edu.colostate.cs.cs414.p5.client_server.server.session.SessionManager;
import edu.colostate.cs.cs414.p5.client_server.transmission.Task;

/**
 * @author pflagert
 * The AbstractServer class defines methods that a server should have.
 */
public abstract class AbstractServer implements ServerTaskManager {
	protected static final Logger LOG = Logger.getInstance();
	protected SessionManager sessionManager;
	
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
		serverChannel = ServerSocketChannel.open();
		selector = Selector.open();
		sessionManager = SessionManager.getInstance();
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
	 * The accept method accepts a client connection request, creates a new ClientSession, 
	 * then calls clientConnected() on the newly created ClientSession. 
	 * Note: Although this method has the permissions to be overwritten,
	 * it may be better to overwrite the clientConnected method instead.
	 * @param key - The {@link SelectionKey} to be accepted.
	 * @return Returns a new ClientSession
	 */
	protected abstract ClientSession accept(SelectionKey key);
	
}
