package edu.colostate.cs.cs414.p5.client_server.server.profile_manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.colostate.cs.cs414.p5.client_server.logger.Logger;
import edu.colostate.cs.cs414.p5.client_server.server.registry.AbstractRegistry;
import edu.colostate.cs.cs414.p5.client_server.server.registry.ActiveRegistry;
import edu.colostate.cs.cs414.p5.user.History;
import edu.colostate.cs.cs414.p5.user.Profile;

public abstract class ProfileManager {
	
	private static final ProfileManager instance = new FileProfileManager();
	
	public static ProfileManager getInstance() {
		return instance;
	}
	
	protected static final Logger LOG = Logger.getInstance();
	
	protected final Map<String,Profile> profileMap;
	
	protected ProfileManager() {
		this.profileMap = new HashMap<String,Profile>();
		buildProfileMapFromRecords();
		createProfilesForRegisteredUsers();
	}
	
	private void createProfilesForRegisteredUsers() {
		AbstractRegistry registry = ActiveRegistry.getInstance();
		if(registry != null) {
			List<String> userNicknames = registry.getAllUserNicknames();
			for(String playersName: userNicknames) {
				addProfileIfAbsent(playersName);
			}
		}
	}
	
	protected void addProfileRestoredFromRecords(String playersName, Profile profile) {
		synchronized(profileMap) {
			if(profileMap.containsKey(playersName)) {
				LOG.error("Profile Map already contains a record for: " + playersName);
			} else {
				profileMap.put(playersName, profile);
			}
		}
	}
	
	public Profile getProfile(String playersName) {
		synchronized(profileMap) {
			return profileMap.get(playersName);
		}
	}
	
	public void addProfile(String playersName) {
		synchronized(profileMap) {
			if(profileMap.containsKey(playersName)) {
				LOG.error("Profile Map already contains a record for: " + playersName);
			} else {
				Profile p = new Profile(playersName);
				profileMap.put(playersName, p);
				addRecord(p);
			}
		}
	}
	
	public void addProfileIfAbsent(String playersName) {
		synchronized(profileMap) {
			if(profileMap.containsKey(playersName)) {
				return;
			} else {
				Profile p = new Profile(playersName);
				profileMap.put(playersName, p);
				addRecord(p);
			}
		}
	}
	
	public void removeProfile(String playersName) {
		synchronized(profileMap) {
			if(profileMap.containsKey(playersName)) {
				Profile removed = profileMap.remove(playersName);
				removeRecord(removed);
			} else {
				LOG.error("Can not remove the profile for: " + playersName + " as it does not exist");
			}
		}
	}
	
	public void addWin(String playersName) {
		synchronized(profileMap) {
			if(profileMap.containsKey(playersName)) {
				Profile playersProfile = profileMap.get(playersName);
				History playersHistory = playersProfile.getHistory();
				playersHistory.addWin();
				updateRecord(playersProfile);
			} else {
				LOG.error("Can not add a win to: " + playersName + " profile as it does not exists");
			}
		}
	}
	
	public void addDraw(String playersName) {
		synchronized(profileMap) {
			if(profileMap.containsKey(playersName)) {
				Profile playersProfile = profileMap.get(playersName);
				History playersHistory = playersProfile.getHistory();
				playersHistory.addDraw();
				updateRecord(playersProfile);
			} else {
				LOG.error("Can not add a draw to: " + playersName + " profile as it does not exists");
			}
		}
	}
	
	public void addLoss(String playersName) {
		synchronized(profileMap) {
			if(profileMap.containsKey(playersName)) {
				Profile playersProfile = profileMap.get(playersName);
				History playersHistory = playersProfile.getHistory();
				playersHistory.addLoss();
				updateRecord(playersProfile);
			} else {
				LOG.error("Can not add a loss to: " + playersName + " profile as it does not exists");
			}
		}
	}
	
	protected abstract void buildProfileMapFromRecords();
	
	protected abstract void addRecord(Profile profile);
	
	protected abstract void updateRecord(Profile profile);
	
	protected abstract void removeRecord(Profile profile);
}
