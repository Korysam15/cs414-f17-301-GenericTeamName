package edu.colostate.cs.cs4141.p3.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.colostate.cs.cs414.p3.banqi.BanqiGame;
import edu.colostate.cs.cs414.p3.user.Player;

class PlayerTest {
	/* GLOBAL VARIABLES */
	private Player default_1;
	private Player default_2;	
	
	@BeforeEach
	void setUp() throws Exception 
	{
		this.default_1 = new Player("localhost",8080);
		this.default_2 = new Player("Korysam@comcast.net","password","KKUDDA","localhost",8080);
	}

	@Test
	void testPlayerStringIntNotNullDefault1() 
	{
		assertNotNull(this.default_1);
	}
	
	@Test
	void testPlayerStringIntNotNullDefault2() 
	{
		assertNotNull(this.default_2);
	}

	@Test
	void testRemoveGame() 
	{
		this.default_2.addGame(0, new BanqiGame(0));
		this.default_2.removeGame(0);
		assertTrue(this.default_2.getGames().size() == 0);
	}

	@Test
	void testAddGame() 
	{
		this.default_2.addGame(0, new BanqiGame(0));
		assertTrue(this.default_2.getGames().size() == 1);
	}

}
