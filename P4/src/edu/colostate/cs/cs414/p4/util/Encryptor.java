package edu.colostate.cs.cs414.p4.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encryptor {	
	/* NoSuchAlgorithm exception should never happen.
	 * That is, unless the target machine is using a none standard version of Java.
	 * As Every implementation of the Java platform is required to support the 
	 * following standard MessageDigest algorithms:
	 * MD5
	 * SHA-1
	 * SHA-256 
	 */
	public static String encryptPassword(String password) {
		byte[] toEncrypt = password.getBytes();
		MessageDigest encryptor;

		try {
			encryptor = MessageDigest.getInstance("SHA-256");
			byte[] encrypted = encryptor.digest(toEncrypt);
			return new String(encrypted);
		} catch (NoSuchAlgorithmException e) {
			// read comments above, this shouldn't happen.
			throw new UnsupportedOperationException("You are using a non-standard jvm.\n"
					+ "Your password will not be encrypted under this jvm");
		}
	}
}
