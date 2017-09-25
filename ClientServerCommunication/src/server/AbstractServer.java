package server;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import server.session.ClientSession;
import server.session.ServerSession;
import transmission.Task;

/**
 * @author pflagert
 *
 */
public abstract class AbstractServer {
	
	protected Map<ClientSession,SelectionKey> clientMap;
	
	protected Map<ServerSession,SelectionKey> serverMap;
	
	protected Selector selector;
	
	protected ServerSocketChannel serverChannel;
	
	protected AbstractServer() {
		clientMap = new HashMap<ClientSession,SelectionKey>();
		serverMap = new HashMap<ServerSession,SelectionKey>();
		selector = null;
		serverChannel = null;
	}
	
	public abstract void start();
	
	public abstract void stopListening();
	
	public abstract void disconnectAllClients();
	
	public abstract boolean isListening();
	
	public abstract List<ClientSession> getClients();
	
	public abstract void handleTask(Task t);
	
	protected abstract ClientSession accept(SelectionKey key);
	
	protected abstract void clientConnected(ClientSession client, SelectionKey key);
	
	protected abstract void clientDisconnected(ClientSession client, SelectionKey key);
	
	protected abstract void serverConnected(ServerSession server, SelectionKey key);
	
	protected abstract void serverDisconnected(ServerSession server, SelectionKey key);
	
	
	
}
