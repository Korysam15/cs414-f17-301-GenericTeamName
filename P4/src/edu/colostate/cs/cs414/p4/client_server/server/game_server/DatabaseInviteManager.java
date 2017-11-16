package edu.colostate.cs.cs414.p4.client_server.server.game_server;

import edu.colostate.cs.cs414.p4.client_server.transmission.game.invite.InviteTask;

public class DatabaseInviteManager extends GameInviteManager {

	public DatabaseInviteManager() {
		super();
	}
	
	@Override
	protected synchronized void buildSavedInvitations() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected synchronized void appendRecords(InviteTask t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected synchronized void removeRecord(InviteTask t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected synchronized boolean recordExists(InviteTask t) {
		// TODO Auto-generated method stub
		return false;
	}

}
