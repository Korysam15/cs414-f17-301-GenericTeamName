package client_server.server.registry;

import client_server.transmission.LoginTask;
import client_server.transmission.RegisterTask;

/**
 * The User class provides a simple wrapping of User login data.
 * @author pflagert
 *
 */
public class User {
	private String email;
	private String nickname;
	private String password;
	
	public User(String email, String nickname, String password) {
		if(email == null) {
			throw new IllegalArgumentException("Email can not be empty");
		} else if(nickname == null) {
			throw new IllegalArgumentException("Nickname can not be empty");
		} else if(password == null) {
			throw new IllegalArgumentException("Password can not be empty");
		} else {
			this.email = email;
			this.nickname = nickname;
			this.password = password;
		}
	}
	
	public User(RegisterTask register) {
		this(register.getEmail(),register.getNickname(),register.getPassword());
	}
	
	public User(LoginTask login) {
		this(login.getEmail(),login.getNickname(),login.getPassword());
	}

	public String getEmail() {
		return email;
	}

	public String getNickname() {
		return nickname;
	}

	public String getPassword() {
		return password;
	}

	// Used by equals method
	private int getHash() {
		String toHash = email+nickname+password;
		return toHash.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if(o == null || !(o instanceof User)) {
			return false;
		} else {
			User other = (User) o;
			return(this.getHash() == other.getHash());
		}
	}
	
	/**
	 * returns the hashCode of the email
	 */
	@Override
	public int hashCode() {
		return email.hashCode();
	}

}
