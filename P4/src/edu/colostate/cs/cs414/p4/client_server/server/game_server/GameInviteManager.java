package edu.colostate.cs.cs414.p4.client_server.server.game_server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
	private File inputFile;
	private static final String SEPERATOR="::::";

	private GameInviteManager() {
		this.fromUserInvites = new HashMap<String,Set<InviteTask>>();
		this.toUserInvites = new HashMap<String,Set<InviteTask>>();
		inputFile = new File("invitations");
		readInvitations();
	}

	private static InviteTask createInviteFromString(String line) {
		String input[] = line.split(SEPERATOR);
		return new InviteTask(input[0],input[1],input[2]);
	}

	private static String inviteTaskToString(InviteTask t) {
		return t.getPlayerFrom()+SEPERATOR+t.getMessage()+SEPERATOR+t.getPlayerTo();
	}

	private synchronized void readInvitations() {
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
						InviteTask t = createInviteFromString(line);
						String fromUser = t.getPlayerFrom();
						String toUser = t.getPlayerTo();
						addInvitation(fromUserInvites,t,fromUser);
						addInvitation(toUserInvites,t,toUser);
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
			for(String nickname: fromUserInvites.keySet()) {
				List<InviteTask> invites = getInvitationsFromUser(nickname);
				for(InviteTask invite: invites) {
					pw.println(inviteTaskToString(invite));
				}
			}
			pw.close();
		} catch (IOException e) {
			System.out.println("Error when updating invitation file");
		}
	}

	// only appends a new invitation to the file
	private synchronized void updateFile(InviteTask t) {
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(inputFile,true));
			pw.println(inviteTaskToString(t));
			pw.flush();
			pw.close();
		} catch (IOException e) {
			System.out.println("Error when updating invitation file");
		}
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
			} else {
				ret = new ArrayList<InviteTask>();
			}
		} else {
			ret = new ArrayList<InviteTask>();
		}
		return ret;
	}

	public synchronized void addInvitation(InviteTask t) {
		String fromUser = t.getPlayerFrom();
		String toUser = t.getPlayerTo();
		InviteTask copy = new InviteTask(t.getPlayerFrom(),t.getMessage(),t.getPlayerTo());
		addInvitation(fromUserInvites,copy,fromUser);
		addInvitation(toUserInvites,copy,toUser);
		updateFile(copy);
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
		boolean removeFromUser = removeInvitationFromUser(fromUser,toUser),
				removeToUser = removeInvitationToUser(fromUser,toUser);
		updateFile();
		return (removeFromUser && removeToUser);
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

	public synchronized void removeAllInvitationsFromAndToUser(String user) {
		removeInvitations(fromUserInvites,user);
		removeInvitations(toUserInvites,user);
		updateFile();

	}

	private void removeInvitations(Map<String,Set<InviteTask>> map, String user) {
		for(String user2: map.keySet()) {
			Iterator<InviteTask> ir = map.get(user2).iterator();
			InviteTask invite;
			while(ir.hasNext()) {
				invite = ir.next();
				if(invite.getPlayerFrom().equals(user)) {
					ir.remove();
				} else if(invite.getPlayerTo().equals(user)) {
					ir.remove();
				}
			}
		}
	}

}
