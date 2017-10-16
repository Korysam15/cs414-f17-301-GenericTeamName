/**
 * 
 */
package pflagert.client;

import java.net.InetSocketAddress;

import pflagert.transmission.Task;

/**
 * @author pflagert
 *
 */
public class Client extends AbstractClient {
	
	public Client() {
		super();
	}
	
	public Client(InetSocketAddress address) {
		
	}
	
	public Client(String address, int port) {
		
	}

	@Override
	public void connectToServer(InetSocketAddress serverAddress) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startReceiving() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopReceiving() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disconnectFromServer() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendToServer(Task t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Task receive() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void handleTask(Task t) {
		// TODO Auto-generated method stub
		
	}

}
