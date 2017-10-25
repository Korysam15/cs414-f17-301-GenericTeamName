package edu.colostate.cs.cs4141.p3.test;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.colostate.cs.cs414.p4.user.History;

public class HistoryTest {

	@Test
	public void testHistory() {
		History h = new History();
	}

	@Test
	public void testAddDraw() {
		History h = new History();
		h.addDraw();
		assertEquals(1, h.getDraws());
	}

	@Test
	public void testReset() {
		History h = new History();
		h.addWin();
		h.addDraw();
		h.reset();
		assertEquals(0, h.getLosses());
	}

	@Test
	public void testAddWin() {
		History h = new History();
		h.addWin();
		assertEquals(h.getWins(), 1);
	}

	@Test
	public void testAddLoss() {
		History h = new History();
		h.addLoss();
		assertEquals(h.getLosses(), 1);
	}

	@Test
	public void testGetWins() {
		History h = new History();
		h.addWin();
		assertEquals(h.getWins(), 1);
	}

	@Test
	public void testGetLosses() {
		History h = new History();
		h.addLoss();
		assertEquals(h.getLosses(), 1);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGetWinRatio() {
		History h = new History();
		h.addWin();
		h.addLoss();
		h.addWin();
		h.addWin();
		assert(h.getWinRatio()==0.75);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGetLossRatio() {
		History h = new History();
		h.addWin();
		h.addLoss();
		h.addWin();
		h.addWin();
		assert(h.getLossRatio()==0.25);
	}

	@Test
	public void testGetGamesPlayed() {
		History h = new History();
		h.addDraw();
		h.addDraw();
		h.addDraw();
		assertEquals(h.getGamesPlayed(), 3);
	}

	

}
