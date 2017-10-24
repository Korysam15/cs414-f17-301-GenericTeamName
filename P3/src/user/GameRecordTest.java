package user;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

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
		assertEquals(g.getWinner(), p1);
		assertEquals(g.wasWinner(p1), true);
	}

	@Test
	public void testWasLoser() {
		GameRecord g = new GameRecord(p1, p2, EndGameState.PLAYER1WON);
		assertEquals(g.getLoser(), p2);
		assert(g.wasLoser(p2));
	}

	@Test
	public void testHadProfileInGame() {
		GameRecord g = new GameRecord(p1, p2, EndGameState.PLAYER1WON);
		assert(g.hadProfileInGame(p1));
		assert(g.hadProfileInGame(p2));
		assert(g.hadProfileInGame(new Profile("pete"))==false);
	}

}
