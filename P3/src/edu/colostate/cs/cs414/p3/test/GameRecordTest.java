package edu.colostate.cs.cs414.p3.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import edu.colostate.cs.cs414.p3.user.*;
import edu.colostate.cs.cs414.p3.user.GameRecord;
import edu.colostate.cs.cs414.p3.user.Profile;

public class GameRecordTest {
	Profile p1 = new Profile("nick"), p2 = new Profile("james");
	
	@Test
	public void testGameRecord() {
		
		GameRecord g = new GameRecord(p1, p2, EndGameState.PLAYER1WON);

	}

	
	

	@Test
	public void testGetLoser() {
		GameRecord g = new GameRecord(p1, p2, EndGameState.PLAYER1WON);
		assert(g.getLoser().equals(p2));
	}

	@Test
	public void testGetWinner() {
		GameRecord g = new GameRecord(p1, p2, EndGameState.PLAYER1WON);
		assertEquals(g.getWinner(), p1);
	}

	@Test
	public void testWasWinner() {
		GameRecord g = new GameRecord(p1, p2, EndGameState.PLAYER1WON);
		assertEquals(g.wasWinner(p1), true);
	}

	@Test
	public void testWasLoser() {
		GameRecord g = new GameRecord(p1, p2, EndGameState.PLAYER1WON);
		assert(g.wasLoser(p2));
	}

	@Test
	public void testHadProfileInGame() {
		GameRecord g = new GameRecord(p1, p2, EndGameState.PLAYER1WON);
		assert(g.hadProfileInGame(p1));
	}

}
