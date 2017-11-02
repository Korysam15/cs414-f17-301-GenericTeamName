package edu.colostate.cs.cs414.p4.client_server.transmission.registration_login;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import edu.colostate.cs.cs414.p4.client_server.transmission.Task;
import edu.colostate.cs.cs414.p4.client_server.transmission.TaskConstents;
import edu.colostate.cs.cs414.p4.client_server.transmission.util.ReadUtils;
import edu.colostate.cs.cs414.p4.client_server.transmission.util.WriteUtils;

public class LoginTask extends Task implements EntryAble {
	private String email;
	private String password;

	public LoginTask(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public LoginTask(DataInputStream din) throws IOException {
		this.email = ReadUtils.readString(din);
		this.password = ReadUtils.readString(din);
	}

	@Override
	public byte[] toByteArray() throws IOException {
		ByteArrayOutputStream bs = WriteUtils.getByteOutputStream();
		DataOutputStream dout = WriteUtils.getDataOutputStream(bs);
		dout.writeInt(getTaskCode());
		WriteUtils.writeString(email,dout);
		encryptPassword();
		WriteUtils.writeString(password, dout);
		return WriteUtils.getBytesAndCloseStreams(bs,dout);
	}

	@Override
	public int getTaskCode() {
		return TaskConstents.LOGIN_TASK;
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

	public String getPassword() {
		return password;
	}

	@Override
	public String getNickname() {
		throw new UnsupportedOperationException(
				"Logging in does not support the use of nicknames");
	}
}
