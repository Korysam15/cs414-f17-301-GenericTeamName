package edu.colostate.cs.cs414.p5.client_server.server.game_server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.colostate.cs.cs414.p5.client_server.transmission.game.invite.*;

public abstract class GameInviteManager {	
// START INSTANCE VARIABLES / METHODS
	protected final Map<String,Set<InviteTask>> fromUserInvites;
	protected final Map<String,Set<InviteTask>> toUserInvites;

	protected GameInviteManager() {
		this.fromUserInvites = new HashMap<String,Set<InviteTask>>();
		this.toUserInvites = new HashMap<String,Set<InviteTask>>();
		buildSavedInvitations();
	}
	
	// START PUBLIC METHODS
	public final synchronized List<InviteTask> getInvitationsFromUser(String userID) {
		return getInvitations(fromUserInvites,userID);
	}

	public final synchronized List<InviteTask> getInvitationsToUser(String userID) {
		return getInvitations(toUserInvites,userID);
	}
	
	public final synchronized void addInvitation(InviteTask t) {
		String fromUser = t.getPlayerFrom();
		String toUser = t.getPlayerTo();
		InviteTask copy = new InviteTask(t.getPlayerFrom(),t.getMessage(),t.getPlayerTo());
		boolean temp = addInvitation(fromUserInvites,copy,fromUser) &&
				addInvitation(toUserInvites,copy,toUser);
		if(temp && !recordExists(copy))
			appendRecords(copy);
	}
	
	public final synchronized boolean removeInvitation(String fromUser, String toUser) {
		InviteTask removedFromUser = removeInvitationFromUser(fromUser,toUser),
				removedToUser = removeInvitationToUser(fromUser,toUser);
		boolean ret = (removedFromUser != null) && (removedToUser != null);
		ret = (ret) ? ret && removedFromUser.equals(removedToUser) : false;
		if(ret) {
			if(recordExists(removedFromUser)) {
				removeRecord(removedFromUser);
			} 
		}
		return ret;
	}
	// END PUBLIC METHODS
	
	// START ABSTRACT METHODS
	protected abstract void buildSavedInvitations();

	protected abstract void appendRecords(InviteTask t);
	
	protected abstract void removeRecord(InviteTask t);
	
	protected abstract boolean recordExists(InviteTask t);
	// END ABSTRACT METHODS
	
	// START PROTECTED METHODS
	protected synchronized final InviteTask addRestoredInvitationFromString(String line) {
		try {
			InviteTask toAdd = createInviteFromString(line);
			addInvitation(toAdd);
			return toAdd;
		} catch(Exception e) {
			return null;
		}
	}
	// END PROTECTED METHODS
	
	// START PRIVATE METHODS
	private final synchronized List<InviteTask> getInvitations(Map<String,Set<InviteTask>> invites, String userID) {
		List<InviteTask> ret = null;
		if(invites.containsKey(userID)) {
			Set<InviteTask> toCopy = invites.get(userID);
			if(toCopy != null) {
				ret = new ArrayList<InviteTask>(toCopy);
			} else {
				ret = new ArrayList<InviteTask>();
			}
		} else {
			ret = new ArrayList<InviteTask>();
		}
		return ret;
	}

	private final synchronized boolean addInvitation(Map<String,Set<InviteTask>> invites, InviteTask t, String userID) {
		if(invites.containsKey(userID)) {
			Set<InviteTask> toAddTo = invites.get(userID);
			if(toAddTo.contains(t)) {
				return false;
			} else {
				invites.get(userID).add(t);
				return true;
			}
		} else {
			Set<InviteTask> toPut = new HashSet<InviteTask>();
			toPut.add(t);
			invites.put(userID, toPut);
			return true;
		}
	}

	private final InviteTask removeInvitationFromUser(String fromUser, String toUser) {
		if(fromUserInvites.containsKey(fromUser)) {
			Iterator<InviteTask> ir = fromUserInvites.get(fromUser).iterator();
			InviteTask invite;
			while(ir.hasNext()) {
				invite = ir.next();
				if(invite.getPlayerTo().equals(toUser)) {
					ir.remove();
					return invite;
				}
			}

		}
		return null;
	}
	

	private final InviteTask removeInvitationToUser(String fromUser, String toUser) {
		if(toUserInvites.containsKey(toUser)) {
			Iterator<InviteTask> ir = toUserInvites.get(toUser).iterator();
			InviteTask invite;
			while(ir.hasNext()) {
				invite = ir.next();
				if(invite.getPlayerFrom().equals(fromUser)) {
					ir.remove();
					return invite;
				}
			}
		}
		return null;
	}
	

	public final synchronized void removeAllInvitationsFromAndToUser(String user) {
		removeInvitations(fromUserInvites,user);
		removeInvitations(toUserInvites,user);
	}
	

	private final void removeInvitations(Map<String,Set<InviteTask>> map, String user) {
		for(String user2: map.keySet()) {
			Iterator<InviteTask> ir = map.get(user2).iterator();
			InviteTask invite;
			boolean shouldBeRemoved;
			while(ir.hasNext()) {
				invite = ir.next();
				shouldBeRemoved = (invite.getPlayerFrom().equals(user) || 
						invite.getPlayerTo().equals(user));
				
				if(shouldBeRemoved) {
					ir.remove();
					if(recordExists(invite)) // make sure record was removed already
						removeRecord(invite);
				}
			}
		}
	}
	// END PRIVATE METHODS
// END INSTANCE VARIABLES / METHODS	
	
// START STATIC VARIABLES / METHODS
		public static final int FILE_INVITE_MANAGER=0;
		public static final int DB_INVITE_MANAGER=1;
		// change default to DB when that portion is complete
		public static final int DEFAULT = FILE_INVITE_MANAGER;
		private static GameInviteManager instance;
		protected static final String SEPERATOR="::::";
		
	// START STATIC METHODS
		public synchronized static void setInstanceType(int type) {
			if(type != FILE_INVITE_MANAGER && type != DB_INVITE_MANAGER) {
				type = DEFAULT;
			}
			switch(type) {
			case FILE_INVITE_MANAGER:
				setInstance(new FileGameInviteManager());
				break;
			case DB_INVITE_MANAGER:
				setInstance(new DatabaseInviteManager());
				break;
			}
		}
		
		
		public synchronized static GameInviteManager getInstance() {
			if(instance != null) {
				return instance;
			} else {
				throw new IllegalStateException("The GameInviteManager has not been set.\n" + 
						"See GameInviteManager#getInstance() to see why this error occured.");
			}
		}
		
		
		protected synchronized static void setInstance(GameInviteManager newInstance) {
			instance = newInstance;
		}


		protected static InviteTask createInviteFromString(String line) {
			String input[] = line.split(SEPERATOR);
			return new InviteTask(input[0],input[1],input[2]);
		}
		

		protected static String inviteTaskToString(InviteTask t) {
			return t.getPlayerFrom()+SEPERATOR+t.getMessage()+SEPERATOR+t.getPlayerTo();
		}
		// END STATIC METHODS
// END STATIC VARIABLES / METHODS
}
