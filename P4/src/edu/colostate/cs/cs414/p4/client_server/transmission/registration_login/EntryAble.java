package edu.colostate.cs.cs414.p4.client_server.transmission.registration_login;

/**
 * 
 * @author pflagert
 * A simple interface to distinguish {@link LoginTask} and {@link RegisterTask} from,
 * other types of {@link Task}s.
 */
public interface EntryAble {
	
	/**
	 * This method should always return an email address that is NOT null
	 * @return an email address in the form of a {@link String}.
	 */
	public String getEmail();
	
	/**
	 * This method should always return a password that is NOT null.
	 * @return a password in the form of a {@link String}
	 */
	public String getPassword();
	
	/**
	 * This method should return a nickname that is NOT null in the case
	 * of Registration. When Logging in this method should not be used, and 
	 * an error should be thrown. 
	 * @return a {@link String} representing a player's/user's nickname.
	 */
	public String getNickname();
}
