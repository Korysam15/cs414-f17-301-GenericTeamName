/**
 * 
 */
package server.session;

import java.nio.channels.SelectionKey;

import server.AbstractServer;
import transmission.Task;

/**
 * @author pflagert
 *
 */
public abstract class AbstractSession {
	protected AbstractServer server;
	
	protected SelectionKey key;
	
	protected String ID;
	
	protected boolean isRegistered;
	
	public AbstractSession(AbstractServer server, SelectionKey key, String ID) {
		this.server = server;
		this.key = key;
		this.ID = ID;
		isRegistered = false;
	}
	
	protected abstract void receive();
	
	public abstract void send(Task t);
	
	public abstract void disconnect();
	
	public abstract boolean isConnected();
	
	public abstract boolean isRegisteredWithServer();
	
	public abstract void registerWithServer();
	
	public abstract String toString();
	
	public abstract int hashCode();
	
	public abstract boolean equals(Object o);	
	
}
