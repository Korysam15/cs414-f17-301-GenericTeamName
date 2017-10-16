/**
 * 
 */
package pflagert.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import pflagert.transmission.Task;

/**
 * @author pflagert
 * The Client class extends AbstractClient and provides a default implementation of 
 * methods inherited from AbstractClient.
 */
public class Client extends AbstractClient {
	
	private Boolean isReceiving;
	/**
	 * Creates a new instance of Client.
	 * @throws IOException 
	 */
	public Client() throws IOException {
		super();
		selector = Selector.open();
		channel = SocketChannel.open();
		channel.register(selector, SelectionKey.OP_CONNECT);
		isReceiving = false;
	}
	
	/**
	 * Creates a new instance of client and connects the Client to a Server with
	 * the associated InetSocketAddress.
	 * Note that this is equivalent to:
	 * Client c = new Client();
	 * c.connectToServer(address);
	 * @param address - The {@link IndetSocketAddress} that represents the server's address
	 * @throws IOException 
	 */
	public Client(InetSocketAddress address) throws IOException {
		this();
		channel.connect(address);
	}
	
	/**
	 * Creates a new instance of client and connects the Client to a Server
	 * with the associated address and port number.
	 * Note that this is equivalent to:
	 * Client c = new Client();
	 * c.connectToServer(address, port);
	 * @param address - The address of the server connecting too.
	 * @param port - The port number the server is listening on.
	 * @throws IOException 
	 */
	public Client(String address, int port) throws IOException {
		this(new InetSocketAddress(address,port));
	}

	@Override
	public void connectToServer(InetSocketAddress serverAddress) throws IOException {
		if(channel != null && channel.isConnected()) {
			disconnectFromServer();
		}
		channel = SocketChannel.open();
		channel.connect(serverAddress);
	}
	
	@Override
	public void connectToServer(String address, int port) throws IOException {
		connectToServer(new InetSocketAddress(address,port));		
	}
	
	@Override
	public boolean isReceiving() {
		synchronized(isReceiving) {
			return isReceiving;
		}
	}

	@Override
	public void startReceiving() {
		if(channel == null || !channel.isOpen()) {
			throw new IllegalStateException("You must be connected to a server "
					+ "before receiving from a server");
		}
		while(isReceiving()) {
			// do something
		}
	}

	@Override
	public void stopReceiving() {
		synchronized(isReceiving) {
			isReceiving = false;
		}		
	}

	@Override
	public void disconnectFromServer() throws IOException {
		stopReceiving();
		channel.close();
		channel = null;
	}

	@Override
	public void sendToServer(Task t) {
		if(!isReceiving()) {
			throw new IllegalStateException("You must be receiving from a server "
					+ "before sending to a server");
		}
	}

	@Override
	public Task receive() {
		return null;
	}

	@Override
	public void handleTask(Task t) {
		// TODO Auto-generated method stub
		
	}

}
