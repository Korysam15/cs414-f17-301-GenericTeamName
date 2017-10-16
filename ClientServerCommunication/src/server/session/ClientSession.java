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
public class ClientSession extends AbstractSession {

	public ClientSession(AbstractServer server, SelectionKey key, String ID) {
		super(server, key, ID);
		// TODO Auto-generated constructor stub
	}
	
	public ClientSession(AbstractSession s) {
		this(s.server,s.key,s.ID);
	}

	@Override
	public void receive() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void send(Task t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isConnected() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRegisteredWithServer() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void registerWithServer() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

}
