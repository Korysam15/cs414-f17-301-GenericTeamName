package edu.colostate.cs.cs414.p5.client_server.server.registry;

import edu.colostate.cs.cs414.p5.client_server.server.AbstractServer;

public class ActiveRegistry {
	private static AbstractRegistry instance;
	
	/**
	 * Sets the instance of AbstractServer the ActiveServer manages 
	 * @param manager - The AbstractServer that will be returned by {@link #getInstance()}.
	 */
	public static synchronized void setInstance(AbstractRegistry registry) {
		instance = registry;
	}
	
	/**
	 * Returns the AbstractServer that is currently being managed.
	 * @return - {@link AbstractServer}
	 */
	public static synchronized AbstractRegistry getInstance() {
		return instance;
	}
}
