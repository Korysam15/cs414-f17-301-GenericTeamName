package user;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProfileTest {
	/* Global Variables */
	Profile default_;

	@BeforeEach
	void setUp() throws Exception 
	{
		this.default_ = new Profile("default_");
	}

	@Test
	void testProfileNotNull() 
	{
		assertNotNull(this.default_);
	}
	
	@Test
	void testGetHistoryDefault_() 
	{
		assertNotNull(this.default_.getHistory());
	}
	
	@Test
	void testToStringDefault_() 
	{
		String toString = "default_\nWins: 0		Win Ratio: NaN\nLosses: 0	Loss Ratio: NaN\nDraws: 0\nGames Played: 0";
		assertTrue(this.default_.toString().equals(toString));
	}
	
	@Test
	void testGetName() 
	{
		assertTrue(this.default_.getName().equals("default_"));
	}

	@Test
	void testEqualsObject() 
	{
		assertFalse(this.default_.equals(null));
	}
	
	@Test
	void testEqualsObjectWithAnotherProfile()
	{
		Profile korysProfile = new Profile("Kory");
		assertFalse(this.default_.equals(korysProfile));
	}
	
	@Test
	void testEqualsObjectWithItself()
	{
		assertTrue(this.default_.equals(this.default_));
	}

}
