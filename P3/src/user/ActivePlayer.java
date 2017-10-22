package user;
import client_server.server.AbstractServer;

/**
	 * ActiveServer is responsible for managing/accessing an AbstractServer.
	 * @author pflagert
	 *
	 *asdf/
	public class ActivePlayer {
		private static Player instance;
		
		/**
		 * Sets the instance of AbstractServer the ActiveServer manages 
		 * @param server - The AbstractServer that will be returned by {@link #getInstance()}.
		 */
		public static synchronized void setInstance(Player player) {
			instance = player;
		}
		
		/**
		 * Returns the AbstractServer that is currently being managed.
		 * @return - {@link AbstractServer}
		 */
		public static synchronized Player getInstance() {
			return instance;
		}
	}