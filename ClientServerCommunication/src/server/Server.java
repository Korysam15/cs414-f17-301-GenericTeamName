/**
 * 
 */
package server;

import java.nio.channels.SelectionKey;
import java.util.List;

import server.session.ClientSession;
import server.session.ServerSession;
import transmission.Task;

/**
 * @author pflagert
 *
 */
public class Server extends AbstractServer {

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopListening() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disconnectAllClients() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isListening() {
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

	@Override
	protected void serverConnected(ServerSession server, SelectionKey key) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void serverDisconnected(ServerSession server, SelectionKey key) {
		// TODO Auto-generated method stub
		
	}

}
