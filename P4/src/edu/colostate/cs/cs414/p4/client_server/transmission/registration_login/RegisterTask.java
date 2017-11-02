package edu.colostate.cs.cs414.p4.client_server.transmission.registration_login;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import edu.colostate.cs.cs414.p4.client_server.transmission.TaskConstents;
import edu.colostate.cs.cs414.p4.client_server.transmission.util.ReadUtils;
import edu.colostate.cs.cs414.p4.client_server.transmission.util.WriteUtils;

public class RegisterTask extends EntryTask {
	private String email;
	private String nickname;
	private String password;

	public RegisterTask(String email, String nickname, String password) {
		this.email = email;
		this.nickname = nickname;
		this.password = password;
	}

	public RegisterTask(DataInputStream din) throws IOException {
		this.email = ReadUtils.readString(din);
		this.nickname = ReadUtils.readString(din);
		this.password = ReadUtils.readString(din);
	}

	@Override
	public int getTaskCode() {
		return TaskConstents.REGISTER_TASK;
	}
	
	@Override
	public void writeBytes(DataOutputStream dout) throws IOException {
		WriteUtils.writeString(email,dout);
		WriteUtils.writeString(nickname,dout);
		encryptPassword();
		WriteUtils.writeString(password, dout);		
	}

	/* NoSuchAlgorithm exception should never happen.
	 * That is, unless the target machine is using a none standard version of Java.
	 * As Every implementation of the Java platform is required to support the 
	 * following standard MessageDigest algorithms:
	 * MD5
	 * SHA-1
	 * SHA-256 
	 */
	private void encryptPassword() {
		byte[] toEncrypt = password.getBytes();
		MessageDigest encryptor;

		try {
			encryptor = MessageDigest.getInstance("SHA-256");
			byte[] encrypted = encryptor.digest(toEncrypt);
			password = new String(encrypted);
		} catch (NoSuchAlgorithmException e) {
			// Read comments above
		}
	}

	@Override
	public void run() {
		// No need for a run method.
		// Registration and Login require direct integration
		// run method will not be called
		return;
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
}