package edu.colostate.cs.cs414.p4.client_server.server.game_server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.colostate.cs.cs414.p4.client_server.transmission.game.invite.*;

public class GameInviteManager {
	private static final GameInviteManager instance = new GameInviteManager();
	private final Map<String,Set<InviteTask>> fromUserInvites;
	private final Map<String,Set<InviteTask>> toUserInvites;
	
	private GameInviteManager() {
		this.fromUserInvites = new HashMap<String,Set<InviteTask>>();
		this.toUserInvites = new HashMap<String,Set<InviteTask>>();
	}
	
	public static GameInviteManager getInstance() {
		return instance;
	}
	
	public synchronized List<InviteTask> getInvitationsFromUser(String userID) {
		return getInvitations(fromUserInvites,userID);
	}
	
	public synchronized List<InviteTask> getInvitationsToUser(String userID) {
		return getInvitations(toUserInvites,userID);
	}
	
	private List<InviteTask> getInvitations(Map<String,Set<InviteTask>> invites, String userID) {
		List<InviteTask> ret = null;
		if(invites.containsKey(userID)) {
			Set<InviteTask> toCopy = invites.get(userID);
			if(toCopy != null) {
				ret = new ArrayList<InviteTask>(toCopy);
			}
		}			
		return ret;
	}
	
	public synchronized void addInvitation(InviteTask t) {
		String fromUser = t.getPlayerFrom();
		String toUser = t.getPlayerTo();
		addInvitation(fromUserInvites,t,fromUser);
		addInvitation(toUserInvites,t,toUser);
	}
	
	private void addInvitation(Map<String,Set<InviteTask>> invites, InviteTask t, String userID) {
		if(invites.containsKey(userID)) {
			invites.get(userID).add(t);	// may overwrite existing invitation if present		
		} else {
			Set<InviteTask> toPut = new HashSet<InviteTask>();
			toPut.add(t);
			invites.put(userID, toPut);
		}
	}
	
	public synchronized boolean removeInvitation(String fromUser, String toUser) {
		return (removeInvitationFromUser(fromUser,toUser) &&
				removeInvitationToUser(fromUser,toUser));
	}
	
	private boolean removeInvitationFromUser(String fromUser, String toUser) {
		if(fromUserInvites.containsKey(fromUser)) {
			Iterator<InviteTask> ir = fromUserInvites.get(fromUser).iterator();
			InviteTask invite;
			while(ir.hasNext()) {
				invite = ir.next();
				if(invite.getPlayerTo().equals(toUser)) {
					ir.remove();
					return true;
				}
			}
			
		}
		return false;
	}
	
	private boolean removeInvitationToUser(String fromUser, String toUser) {
		if(toUserInvites.containsKey(toUser)) {
			Iterator<InviteTask> ir = toUserInvites.get(toUser).iterator();
			InviteTask invite;
			while(ir.hasNext()) {
				invite = ir.next();
				if(invite.getPlayerFrom().equals(fromUser)) {
					ir.remove();
					return true;
				}
			}
		}
		return false;
	}
	
}
