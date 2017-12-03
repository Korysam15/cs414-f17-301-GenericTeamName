package edu.colostate.cs.cs414.p5.client_server.server.game_server.profile_manager;

import java.util.HashMap;
import java.util.Map;

import edu.colostate.cs.cs414.p5.client_server.logger.Logger;
import edu.colostate.cs.cs414.p5.user.History;
import edu.colostate.cs.cs414.p5.user.Profile;

public abstract class ProfileManager {
	protected static final Logger LOG = Logger.getInstance();
	
	protected final Map<String,Profile> profileMap;
	
	protected ProfileManager() {
		this.profileMap = new HashMap<String,Profile>();
	}
	
	protected void addProfileRestoredFromRecords(String playersName, Profile profile) {
		synchronized(profileMap) {
			if(profileMap.containsKey(playersName)) {
				LOG.error("Profile Map already contains a record for: " + playersName);
			} else {
				profileMap.put(playersName, new Profile(playersName));
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
				profileMap.put(playersName, new Profile(playersName));
			}
		}
	}
	
	public void removeProfile(String playersName) {
		synchronized(profileMap) {
			if(profileMap.containsKey(playersName)) {
				profileMap.remove(playersName);
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
