package user;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import client_server.transmission.InviteTask;
import client_server.transmission.MultiForwardTask;

class InvitationTest {
	/* Global Variables */
	Invitation null_, default_, invite1;

	@BeforeEach
	void setUp() throws Exception 
	{
		this.null_ = null;
		this.default_ = new Invitation("default","Hey There!",new ArrayList<String>(Arrays.asList("Player1")));
		this.invite1 = new Invitation("Kory","Hey! Let's play a game of Banqi!",new ArrayList<String>(Arrays.asList("Tanner","Sam","Nick")));
	}

	@Test
	void testInvitation() 
	{
		assertNull(this.null_);
		assertNotNull(this.default_);
		assertNotNull(this.invite1);
	}

	@Test
	void testGetPlayerFrom() 
	{
		assertTrue(this.default_.getPlayerFrom().equals("default"));
		assertTrue(this.invite1.getPlayerFrom().equals("Kory"));
		assertFalse(this.default_.getPlayerFrom().equals("Kory"));
	}

	@Test
	void testGetMessage() 
	{
		assertTrue(this.default_.getMessage().equals("Hey There!"));
		assertTrue(this.invite1.getMessage().equals("Hey! Let's play a game of Banqi!"));
		assertFalse(this.invite1.getMessage().equals(this.default_.getMessage()));
	}

	@Test
	void testToTask() 
	{
		assertNotNull(this.default_.toTask());
		assertNotNull(this.invite1.toTask());
		assertFalse(this.default_.toTask().equals(this.invite1.toTask()));
	}

	@Test
	void testGetPlayersToInvite() 
	{
		assertTrue(this.default_.getPlayersToInvite().equals(new ArrayList<String>(Arrays.asList("Player1"))));
		ArrayList<String> invite1_players = new ArrayList<String>(Arrays.asList("Tanner","Sam","Nick"));
		assertTrue(this.invite1.getPlayersToInvite().equals(invite1_players));
		assertFalse(this.default_.getPlayersToInvite().equals(invite1_players));
	}

}
