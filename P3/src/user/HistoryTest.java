package user;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HistoryTest {
	/* Global Variables */
	History default_;
	History null_;
	History history1;
	History history2;
	
	
	@BeforeEach
	void setUp() throws Exception 
	{
		default_ = new History();
		null_ = null;
		history1 = new History();
	}

	@Test
	void testHistory() 
	{
//		assertNotNull(default_);
//		assertNotNull(history1);
//		assertNotNull(history2);
//		assertNull(null_);
	}

	@Test
	void testReset() 
	{
		
	}

	@Test
	void testAddWin() 
	{
		
	}

	@Test
	void testAddLoss() 
	{
		
	}

	@Test
	void testGetWins() 
	{
		
	}

	@Test
	void testGetLosses() 
	{
		
	}

	@Test
	void testGetWinRatio() 
	{
		
	}

	@Test
	void testGetLossRatio() 
	{
		
	}

	@Test
	void testGetGamesPlayed() 
	{
		
	}

	@Test
	void testToString() 
	{
		assertTrue(this.default_.toString().equals("Wins: " + 0 + "\t\tWin Ratio: NaN" + "\n" + "Losses: " + 0 + "\tLoss Ratio: NaN" + "\n" + "Games Played: " + 0));
		this.history1.addLoss();
		this.history1.addWin();
//		this.history1
		assertTrue(this.history1.toString().equals("Wins: " + 1 + "\t\tWin Ratio: NaN" + "\n" + "Losses: " + 1 + "\tLoss Ratio: NaN" + "\n" + "Games Played: " + 0));
		assertTrue(this.history2.toString().equals("Wins: " + 0 + "\t\tWin Ratio: NaN" + "\n" + "Losses: " + 0 + "\tLoss Ratio: NaN" + "\n" + "Games Played: " + 0));
	}

}
