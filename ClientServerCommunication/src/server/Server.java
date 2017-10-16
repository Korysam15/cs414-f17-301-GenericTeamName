package server;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.util.List;

import server.session.ClientSession;
import transmission.Task;

/**
 * @author pflagert
 * The Server class extends AbstractServer 
 * and provides a basic implementation for the abstract methods in AbstractServer.
 */
public class Server extends AbstractServer {

	/**
	 * Creates a new Server that will listen for incoming connections/messages on the
	 * InetSocketAddress passed into this constructor.
	 * @param address - The {@link InetSocketAddress} that the server should be bound too.
	 */
	public Server(InetSocketAddress address) {
		
	}
	
	/**
	 * Creates a new Server that will listen for incoming messages on the port number
	 * passed into this constructor.
	 * Note that this constructor creates an InetSocketAddress that will be used to 
	 * call Server(InetSocketAddress).
	 * @param port - The port number that this Server should be bound to.
	 * @see #Server(InetSocketAddress)
	 */
	public Server(int port) {
		
	}
	
	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disconnectAllClients() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isRunning() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<ClientSession> getClients() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void handleTask(Task t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected ClientSession accept(SelectionKey key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void clientConnected(ClientSession client, SelectionKey key) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void clientDisconnected(ClientSession client, SelectionKey key) {
		// TODO Auto-generated method stub
		
	}
}
