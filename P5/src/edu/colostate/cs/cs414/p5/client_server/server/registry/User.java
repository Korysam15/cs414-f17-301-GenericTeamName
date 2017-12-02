package edu.colostate.cs.cs414.p5.client_server.server.registry;

import java.security.SecureRandom;

import edu.colostate.cs.cs414.p5.client_server.transmission.registration_login.LoginTask;
import edu.colostate.cs.cs414.p5.client_server.transmission.registration_login.RegisterTask;
import edu.colostate.cs.cs414.p5.util.Encryptor;

/**
 * The User class provides a simple wrapping of User login data.
 * @author pflagert
 *
 */
public class User {
	private static final int SALT_HASH_ITERATIONS = 11;
	private static final String FIELD_SEPERATOR = "::";

	private String email;
	private String nickname;
	private String salt;
	private String password;

	public static final User createUserFromString(String in) {
		if(in == null) {
			return null;
		} else {
			String input[] = in.split(FIELD_SEPERATOR);
			return new User(input[0],input[1],input[2],input[3]);
		}
	}

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

	public User(String email, String nickname, String password, String salt) {
		this(email,nickname,password);
		this.salt = salt;
	}

	public User(RegisterTask register) {
		this(register.getEmail(),register.getNickname(),register.getPassword());
	}

	public User(LoginTask login) {
		this(login.getEmail()," ",login.getPassword());
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

	public String getSalt() {
		return salt;
	}

	public void generateSalt() {
		SecureRandom random = new SecureRandom();
		byte bytes[] = new byte[20];
		random.nextBytes(bytes);
		this.salt = new String(bytes);
		rehashPass();
	}

	private void rehashPass() {
		for(int i=0;i<SALT_HASH_ITERATIONS;i++) {
			password = encrypt(salt,password);
		}
	}

	private String encrypt(String salt, String password) {
		String saltyPassword = (salt+password);
		return Encryptor.encryptPassword(saltyPassword);
	}

	public void setSalt(String salt) {
		this.salt = salt;
		rehashPass();
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

	@Override
	public String toString() {
		return email+FIELD_SEPERATOR+nickname+FIELD_SEPERATOR+password+FIELD_SEPERATOR+salt;
	}

}
