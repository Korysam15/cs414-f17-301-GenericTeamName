package user;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlayerTest {
	/* Global Variables */
	Player default_, null_, player1, player2;
	@BeforeEach
	void setUp() throws Exception 
	{
		this.default_ = new Player("default","default","default","host",8080);
		this.null_ = null;
		this.player1 = new Player("Korysam@comcast.net","password","KKUDDA","host",8080);
		this.player2 = new Player("pflagert@gmail.com","password2","pflagert","host",8080);
	}

	@Test
	void testPlayer() 
	{
		assertNull(this.null_);
		assertNotNull(this.default_);
		assertNotNull(this.player1);
		assertNotNull(this.player2);
	}

	@Test
	void testGetNickName() 
	{
		assertTrue(this.default_.getNickName().equals("default"));
		assertTrue(this.player1.getNickName().equals("KKUDDA"));
		assertTrue(this.player2.getNickName().equals("pflagert"));
		assertFalse(this.player1.getNickName().equals(this.player2.getNickName()));
	}

	@Test
	void testGetProfileInformation() 
	{
		
	}

	@Test
	void testGetGame() 
	{
		
	}

	@Test
	void testAddGame() 
	{
		
	}

	@Test
	void testSendInvitation() 
	{
		fail("Not yet implemented");
	}

	@Test
	void testGetClient() 
	{
		fail("Not yet implemented");
	}

	@Test
	void testToString() 
	{
		fail("Not yet implemented");
	}

}
