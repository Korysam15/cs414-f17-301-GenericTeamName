/**
 * 
 */
package pflagert.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import pflagert.transmission.Task;

/**
 * @author pflagert
 * The AbstractClient class lists methods that a client should have.
 */
public abstract class AbstractClient {
	
	/**
	 * The selector instance variable is used to determine which SelectionKey is ready to
	 * read/write/connect to. 
	 * Note that the Selector class is imported from: java.nio.channels.Selector
	 * @see Selector
	 */
	protected Selector selector;
	
	/**
	 * The {@link SocketChannel} that represents the connection to the server.
	 */
	protected SocketChannel channel;
	
	protected AbstractClient() {
		
	}
	
	/**
	 * Creates a connection to a server with the associated InetSocketAddress.
	 * Note that calling this method will disconnect the current channel (if it is not null)
	 * and then overwrite the channel with a new SocketChannel.
	 * @param serverAddress - The address of the server to connect to.
	 * @throws IOException 
	 */
	public abstract void connectToServer(InetSocketAddress serverAddress) throws IOException;
	
	/**
	 * Creates a connection to a server with the associated address and port number.
	 * Note that this method creates a InetSocketAddress and then calls connectToServer(InetSocketAddress).
	 * Thus this method provides the same behavior as connectToServer(InetSocketAddress).
	 * @param address - The ip address associated with the server.
	 * @param port - The port number the server is listening on.
	 * @throws IOException 
	 */
	public abstract void connectToServer(String address, int port) throws IOException;
	
	/**
	 * @return True if the {@link AbstractClient} is listening for incoming messages from the Server,
	 * Otherwise returns false.
	 */
	public abstract boolean isReceiving();
	
	/**
	 * The startReceiving method will make the client listen to the server that it is connected to. 
	 */
	public abstract void startReceiving();
	
	/**
	 * The stopReceiving method will essentially cause the client to ignore messages
	 * received from the server.
	 */
	public abstract void stopReceiving();
	
	/**
	 * The disconnectFromServer method will close the SocketChannel (channel). After a call to this method is made, 
	 * all communications between the server and an instance of this class will cease.
	 * @throws IOException 
	 */
	public abstract void disconnectFromServer() throws IOException;
	
	/**
	 * The send method constructs a byte Array by calling t.toByteArray().
	 * After the byte array is constructed, the bytes are then “sent” to the server by writing the bytes
	 * to the SocketChannel (channel) created via the call to connectToServer().
	 * @param t - The {@link Task} to send to the server.
	 * @throws IOException 
	 */
	public abstract void sendToServer(Task t) throws IOException;
	
	/**
	 * The receive method constructs a new Task through the TaskFactory
	 * from the bytes received from the server. 
	 * This method then calls client.handleTask() with the newly created task as the parameter.
	 * @return - The constructed {@link Task}
	 * @throws IOException 
	 */
	public abstract void receive() throws IOException;
	
	/**
	 * The default implementation of handleTask simply executes the Task that was passed into this method.
	 * If a different behavior is desired from this method,
	 * this class should be extended then this method can be overwritten.
	 * @param t
	 */
	public abstract void handleTask(Task t);
	
}
