package client_server.server.registry;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;

import client_server.transmission.LoginTask;
import client_server.transmission.RegisterTask;

public class FileRegistry extends AbstractRegistry {

	/**
	 * used to build the set of {@link User}s
	 */
	private File inputFile;

	/**
	 * Creates a new FileRegistry, using the registrationFile to read/write registered users.
	 * Note that this is the same as: "new FileRegistry(new FileReader(registrationFile))"
	 * @param registrationFile
	 * @throws IllegalArgumentException if the File is null, or doesn't have read / write permissions 
	 * @see #FileRegistry(FileReader)
	 */
	public FileRegistry(File registrationFile) {
		super();
		if(registrationFile == null) {
			throw new IllegalArgumentException("registration file can not be null");
		} else if(registrationFile.exists() && !registrationFile.canRead()) {
			throw new IllegalArgumentException("registration file must have read permissions");
		} else if(registrationFile.exists() && !registrationFile.canWrite()) {
			throw new IllegalArgumentException("registration file must have write permissions");
		} else if(registrationFile.exists() && !registrationFile.isFile()) {
			throw new IllegalArgumentException("registration file must be a true file");
		}
		else {
			inputFile = registrationFile;
			readUsers();
		}
	}

	/**
	 * Creates a new FileRegistry, using the registrationFile to read/write registered users.
	 * Note that this is the same as: "new FileRegistry(new File(registrationFile))"
	 * @param registrationFile
	 * @see #FileRegistry(File)
	 */
	public FileRegistry(String registrationFileName) {
		this(new File(registrationFileName));
	}

	private synchronized void readUsers() {
		try {
			if(!inputFile.exists()) {
				inputFile.createNewFile();
				return;
			} else {
				BufferedReader br = new BufferedReader(new FileReader(inputFile));
				String line = null;
				while((line = br.readLine()) != null) {
					try {
						User u = User.createUserFromString(line);
						addUser(u);
					} catch(NullPointerException e) {
						// corrupted file
					} catch(IllegalArgumentException e) {
						// outdated file per the specs of User
					}
				}
				br.close();			
			}
		} catch (FileNotFoundException e) {
			// shouldn't happen because of first if statements
		} catch (IOException e) {
		}
	}

	private synchronized void updateFile() {
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(inputFile));
			for(User u: validUsers.values()) {
				pw.println(u);
				pw.flush();
			}
			pw.close();
		} catch (IOException e) {
			System.out.println("Error when updating password file");
		}
	}

	private synchronized void updateFile(User u) {
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(inputFile,true));
			pw.println(u);
			pw.flush();
			pw.close();
		} catch (IOException e) {
			System.out.println("Error when updating password file");
		}
	}

	private synchronized void addUser(User u) {
		validUsers.put(u.getEmail(),u);
		takenEmails.add(u.getEmail());
		takenNicknames.add(u.getNickname());
	}

	private synchronized void addUserAndUpdate(User u) {
		addUser(u);
		updateFile(u);
	}

	private synchronized void removeUser(User u) {
		validUsers.remove(u.getEmail());
		takenEmails.remove(u.getEmail());
		takenNicknames.remove(u.getNickname());
	}

	private synchronized void removeUserAndUpdate(User u) {
		removeUser(u);
		updateFile();
	}

	private User createUser(RegisterTask register) {
		if(register == null) {
			return null;
		} else {
			User u;
			try {
				u = new User(register);
			} catch (IllegalArgumentException e) {
				u = null;
			}
			return u;
		}
	}

	private User createUser(LoginTask login) {
		if(login == null) {
			return null;
		} else {
			User u;
			try {
				u = new User(login);
			} catch (IllegalArgumentException e) {
				u = null;
			}
			return u;
		}
	}

	public synchronized boolean isEmailTaken(String email) {
		return takenEmails.contains(email);
	}

	public synchronized boolean isNicknameTaken(String nickname) {
		return takenNicknames.contains(nickname);
	}

	@Override
	public synchronized boolean isValidRegistration(RegisterTask register) {
		User u = createUser(register);
		if(u == null) {
			return false;
		} else if(isEmailTaken(u.getEmail())) {
			return false;
		} else if(isNicknameTaken(u.getNickname())) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public synchronized boolean isValidLogin(LoginTask login) {
		User copy = createUser(login);
		if(copy != null) {
			User stored = getUser(copy.getEmail());
			if(stored != null) {
				copy.setSalt(stored.getSalt());
				return copy.equals(stored);
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public synchronized boolean registerNewUser(RegisterTask register) {
		User u = createUser(register);
		if(u != null && isValidRegistration(register)) {
			u.generateSalt();
			addUserAndUpdate(u);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public synchronized boolean unregisterUser(User u) {
		if(u == null) {
			return false;
		} else {
			return unregisterUser(u.getEmail());
		}
	}

	@Override
	public synchronized boolean unregisterUser(String email) {
		User user = getUser(email);
		if(user == null) {
			return false;
		} else {
			removeUserAndUpdate(user);
			return true;
		}
	}

	@Override
	public synchronized User getUser(String email) {
		return validUsers.get(email);
	}

}
