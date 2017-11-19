package edu.colostate.cs.cs414.p5.client_server.server.registry;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.colostate.cs.cs414.p5.client_server.transmission.registration_login.LoginTask;
import edu.colostate.cs.cs414.p5.client_server.transmission.registration_login.RegisterTask;

/**
 * AbstractRegistry provides methods that should be defined by a "registry".
 * The responsiblities of a "registry" include:
 * registering users via storing emails, nicknames, and passwords
 * validating that users emails/nicknames are not taken / exist in the system
 * validating that passwords are correct (for logins)
 * @author pflagert
 *
 */
public abstract class AbstractRegistry {
	/**
	 * A map of emails associated with users
	 */
	protected Map<String,User> validUsers;
	
	/**
	 * A set of emails that are in use by users
	 */
	protected Set<String> takenEmails;
	
	/**
	 * A set of nicknames that are in use by users
	 */
	protected Set<String> takenNicknames;
	
	/**
	 * Initializes instance variables.
	 * However, all the instance variables remain empty.
	 */
	protected AbstractRegistry() {
		validUsers = new HashMap<String,User>();
		takenEmails = new HashSet<String>();
		takenNicknames = new HashSet<String>();
	}
	
	public List<String> getAllUserNicknames() {
		List<String> ret = new LinkedList<String>();
		for(String nickname: takenNicknames) {
			ret.add(nickname);
		}
		return ret;
	}
	
	/**
	 * Checks if a nickname is already used in the system.
	 * @param nickname - The nickname to check.
	 * @return True if the nickname is already taken.
	 */
	public abstract boolean isNicknameTaken(String nickname);
	
	/**
	 * Checks if an email address is already used in the system.
	 * @param email - The email to check.
	 * @return True if the email is already taken.
	 */
	public abstract boolean isEmailTaken(String email);
	
	/**
	 * Returns an error message if the registration is INVALID otherwise returns null. 
	 * @param register - A {@link RegisterTask} that represents the users Registration info.
	 * @return - An error message or null
	 */
	public abstract String isValidRegistration(RegisterTask register);
	
	/**
	 * Returns an error message if the login is invalid, otherwise null;
	 * @param login - The login submitted
	 * @return An error message or null
	 */
	public abstract String isValidLogin(LoginTask login);
	
	/**
	 * Registers a new user with the system
	 * @param register - A {@link RegisterTask} that represents the users Registration info.
	 * @return a String that represents an error or null if registration is successful.
	 */
	public abstract String registerNewUser(RegisterTask register);
	
	/**
	 * Unregisters the User u from the system.
	 * @param u - The {@link User} to unregister.
	 * @return True if successful
	 */
	public abstract boolean unregisterUser(User u);
	
	/**
	 * Unregisters the User associated with email. 
	 * @param email - The email of the {@link User} to unregister
	 * @return - True if successful
	 */
	public abstract boolean unregisterUser(String email);
	
	/**
	 * Returns the User with the associated email
	 * @param email
	 */
	public abstract User getUser(String email);
}