package edu.colostate.cs.cs414.p4.client_server.server.game_server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
 
import edu.colostate.cs.cs414.p4.client_server.transmission.game.invite.*;

public class FileGameInviteManager extends GameInviteManager {
	private static final File inputFile = new File("invitations");
	private static final Set<InviteTask> savedTasks = new HashSet<InviteTask>();

	public FileGameInviteManager() {
		super();
	}
	
	@Override
	protected synchronized void buildSavedInvitations() {
		try {
			if(!inputFile.exists()) {
				inputFile.createNewFile();
				return;
			} else {
				boolean corrupted = false;
				InviteTask temp;
				BufferedReader br = new BufferedReader(new FileReader(inputFile));
				String line = null;
				while((line = br.readLine()) != null) {
					temp = addRestoredInvitationFromString(line);
					if(temp == null) {
						corrupted = true;
					} else {
						savedTasks.add(temp);
					}
				}
				br.close();
				if(corrupted) {
					updateFile();
				}
			}
		} catch (FileNotFoundException e) {
			// shouldn't happen because of first if statement
		} catch (IOException e) {
		}		
	}

	@Override
	protected synchronized void appendRecords(InviteTask t) {
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(inputFile,true));
			pw.println(inviteTaskToString(t));
			pw.flush();
			pw.close();
		} catch (IOException e) {
			System.out.println("Error when updating invitation file");
		}
		
	}

	@Override
	protected synchronized void removeRecord(InviteTask t) {
		savedTasks.remove(t);
		updateFile();		
	}

	@Override
	protected synchronized boolean recordExists(InviteTask t) {
		return savedTasks.contains(t);
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
}
