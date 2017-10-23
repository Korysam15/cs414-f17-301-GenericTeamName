package client_server.server.registry;

import java.util.HashSet;
import java.util.Set;

import client_server.transmission.LoginTask;
import client_server.transmission.RegisterTask;

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
	 * A set of users who have registered with the system.
	 * Note that {@link User#hashCode()} hashes with the email associated with a User.
	 */
	protected Set<User> validUsers;
	
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
		validUsers = new HashSet<User>();
		takenEmails = new HashSet<String>();
		takenNicknames = new HashSet<String>();
	}
	
	/**
	 * Returns true if and only if the email / nickname are not taken 
	 * @param register - A {@link RegisterTask} that represents the users Registration info.
	 * @return - True if the registration is valid
	 */
	public abstract boolean isValidRegistration(RegisterTask register);
	
	/**
	 * Returns true if and only if the email/password associated with the login, is the same as
	 * the password stored.
	 * @param login - The login submitted
	 * @return True if the email / password are the same.
	 */
	public abstract boolean isValidLogin(LoginTask login);
	
	/**
	 * Registers a new user with the system
	 * @param register - A {@link RegisterTask} that represents the users Registration info.
	 * @return True if registration is successful.
	 */
	public abstract boolean registerNewUser(RegisterTask register);
	
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
}