package client_server.server.registry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

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
				boolean corrupted = false;
				BufferedReader br = new BufferedReader(new FileReader(inputFile));
				String line = null;
				while((line = br.readLine()) != null) {
					try {
						User u = User.createUserFromString(line);
						addUser(u);
					} catch(NullPointerException e) {
						corrupted = true;
					} catch(IllegalArgumentException e) {
						corrupted = true;
					} catch(Exception ex) {
						corrupted = true;
					}
				}
				br.close();
				if(corrupted) {
					updateFile();
				}
			}
		} catch (FileNotFoundException e) {
			// shouldn't happen because of first if statements
		} catch (IOException e) {
		}
	}

	// completely overwrites file
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

	// only appends a new user to the file
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

	// adds users to instance variables
	private synchronized void addUser(User u) {
		validUsers.put(u.getEmail(),u);
		takenEmails.add(u.getEmail());
		takenNicknames.add(u.getNickname());
	}

	// adds users to instance variables and updates the password file
	private synchronized void addUserAndUpdate(User u) {
		addUser(u);
		updateFile(u);
	}

	// removes user from instance variables
	private synchronized void removeUser(User u) {
		validUsers.remove(u.getEmail());
		takenEmails.remove(u.getEmail());
		takenNicknames.remove(u.getNickname());
	}

	// removes user from instance variables and rewrites the password file
	private synchronized void removeUserAndUpdate(User u) {
		removeUser(u);
		updateFile();
	}

	// creates a user from a register task
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

	// creates a user from a login task
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

	// checks if the email address is already in use.
	public synchronized boolean isEmailTaken(String email) {
		return takenEmails.contains(email);
	}
	
	// checks if the nickname is already in use.
	public synchronized boolean isNicknameTaken(String nickname) {
		return takenNicknames.contains(nickname);
	}

	@Override
	public synchronized String isValidRegistration(RegisterTask register) {
		User u = createUser(register);
		if(u == null) {
			return "Invalid registration.";
		} else if(isEmailTaken(u.getEmail())) {
			return "'" + u.getEmail() + "'" + " is already in use.";
		} else if(isNicknameTaken(u.getNickname())) {
			return "'" + u.getNickname() + "'" + " is already in use.";
		} else if(u.getEmail().contains(":")) {
			return "':'" + " is not allowed in the email address.";
		} else if(u.getNickname().contains(":")) {
			return "':'" + " is not allowed in the nickname.";
		} else {
			return null;
		}
	}

	@Override
	public synchronized String isValidLogin(LoginTask login) {
		User copy = createUser(login);
		if(copy == null) {
			return "Invalid login.";
		}
		User stored = getUser(copy.getEmail());
		if(stored != null) {
			copy.setSalt(stored.getSalt());
			if(copy.getPassword().equals(stored.getPassword())) {
				return null;
			} else {
				//return "Bad password: Salt: " + copy.getSalt() + " Salt: " + stored.getSalt();
			}
		} else {
			//return "Bad email";
		}
		return "Invalid login credentials.";
	}

	@Override
	public synchronized String registerNewUser(RegisterTask register) {
		User u = createUser(register);
		String msg = isValidRegistration(register);
		if(u != null && msg == null) {
			u.generateSalt();
			addUserAndUpdate(u);
			return null;
		} else {
			return msg;
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
