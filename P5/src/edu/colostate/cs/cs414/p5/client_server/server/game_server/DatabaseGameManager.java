package edu.colostate.cs.cs414.p5.client_server.server.game_server;

import java.util.Set;

import edu.colostate.cs.cs414.p5.banqi.BanqiGame;

public class DatabaseGameManager extends GameManager {

	// leave this protected
	protected DatabaseGameManager() {
		super();
		// nick put whatever else you need here.
		// note that stuff here will not used until after #buildGameMaps is called
		// therefore if you need something else, make it a "private static final" (above this constructor) 
	}
	
	
	// this is called by the constructor in GameManager()
	// this is where you will add games
	
	// don't use the super method AKA (GameManager#addGame(BanqiGame game)
	// instead use the super method GameManager#addGameRestoredFromRecord(BanqiGame game)
	
	// see FileGameManager for examples
	@Override
	protected void buildGameMaps() {
		// TODO Auto-generated method stub

	}
	
	@Override
	protected void addRecord(BanqiGame game) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void updateRecord(BanqiGame game) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void removeRecord(BanqiGame game) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void removeRecords(Set<BanqiGame> games) {
		// TODO Auto-generated method stub

	}

}
