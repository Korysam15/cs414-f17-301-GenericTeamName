package edu.colostate.cs.cs414.p3.client_server.server;

/**
 * ActiveServer is responsible for managing/accessing an AbstractServer.
 * @author pflagert
 *
 */
public class ActiveServer {
	private static AbstractServer instance;
	
	/**
	 * Sets the instance of AbstractServer the ActiveServer manages 
	 * @param server - The AbstractServer that will be returned by {@link #getInstance()}.
	 */
	public static synchronized void setInstance(AbstractServer server) {
		instance = server;
	}
	
	/**
	 * Returns the AbstractServer that is currently being managed.
	 * @return - {@link AbstractServer}
	 */
	public static synchronized AbstractServer getInstance() {
		return instance;
	}
}
