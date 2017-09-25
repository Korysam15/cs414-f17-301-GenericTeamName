/**
 * 
 */
package client;

import java.net.InetSocketAddress;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import transmission.Task;

/**
 * @author pflagert
 *
 */
public abstract class AbstractClient {
	
	protected Selector selector;
	
	protected SocketChannel channel;
	
	protected AbstractClient() {
		this(null,null);
	}
	
	protected AbstractClient(Selector selector, SocketChannel channel) {
		this.selector = selector;
		this.channel = channel;
	}
	
	public abstract void connectToServer(InetSocketAddress serverAddress);
	
	public abstract void startReceiving();
	
	public abstract void stopReceiving();
	
	public abstract void disconnectFromServer();
	
	public abstract void sendToServer(Task t);
	
	public abstract Task receive();
	
	public abstract void handleTask(Task t);
	
}
