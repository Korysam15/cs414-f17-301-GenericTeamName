package edu.colostate.cs.cs414.p5.client_server.server.game_server.profile_manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import edu.colostate.cs.cs414.p5.user.Profile;
import edu.colostate.cs.cs414.p5.user.ProfileBuilder;
import edu.colostate.cs.cs414.p5.util.FileUtils;

public class FileProfileManager extends ProfileManager {
	protected static final String SEPERATOR=",";
	private static final File inputFile = new File("profiles");
	private static final Set<Profile> savedProfiles = new HashSet<Profile>();
	private static final ProfileBuilder builder = new ProfileBuilder(SEPERATOR);
	
	protected FileProfileManager() {
		super();
	}
	
	@Override
	protected synchronized void buildProfileMapFromRecords() {
		try {
			if(!inputFile.exists()) {
				inputFile.createNewFile();
				LOG.info("Created new file to store players profiles: " + inputFile.getAbsolutePath());
				return;
			} else {
				boolean corrupted = false;
				Profile temp;
				BufferedReader br = new BufferedReader(new FileReader(inputFile));
				String line = null;
				while((line = br.readLine()) != null) {
					try {
						temp = builder.createProfileFromString(line);
					} catch (Exception e) {
						LOG.error("An error occurred trying to recreate a players profile: " + e.getMessage());
						temp = null;
					}
					if(temp == null) {
						corrupted = true;
					} else if(savedProfiles.contains(temp)) {
						corrupted = true;
					} else {
						super.addProfileRestoredFromRecords(temp.getName(),temp);
						savedProfiles.add(temp);
					}
				}
				br.close();
				if(corrupted) {
					LOG.info(inputFile.getAbsolutePath() + " may be corrupted. ");
					backupFile();
					updateFile();
				}
			}
		} catch (FileNotFoundException e) {
			LOG.error("Could not find file: " + inputFile.getAbsolutePath());
		} catch (IOException e) {
			LOG.error("IOException occurred when trying read: " + inputFile.getAbsolutePath());
		}
	}

	@Override
	protected synchronized void addRecord(Profile profile) {
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(inputFile,true));
			pw.println(builder.profileToString(profile));
			pw.flush();
			pw.close();
			savedProfiles.add(profile);
		} catch (IOException e) {
			LOG.error("Error when updating profiles file");
		}
	}

	@Override
	protected synchronized void updateRecord(Profile profile) {
		removeRecord(profile);
		addRecord(profile);
	}

	@Override
	protected synchronized void removeRecord(Profile profile) {
		savedProfiles.remove(profile);
		updateFile();
	}
	
	private synchronized void backupFile() {
		int i = 0;
		File backup = new File(inputFile.getName()+i);

		while(backup.exists()) 
			backup = new File(inputFile.getName() + (++i));
		
		LOG.info("Backing up: " + inputFile.getAbsolutePath() + " to " + backup.getAbsolutePath());
		FileUtils.copyFileUsingStream(inputFile,backup);
	}
	
	private synchronized void updateFile() {
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(inputFile));
			for(Profile profile: savedProfiles) {
				pw.println(builder.profileToString(profile));
			}
			pw.close();
		} catch(IOException e) {
			LOG.error(e.getMessage());
		}
	}

}
