package user;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProfileTest {
	
	/* Global Variables */
	Profile default_;
	Profile null_;
	Profile profile1;		
	Profile profile2;
	Profile profile3;

	@BeforeEach
	void setUp() throws Exception {
		default_ = new Profile("default");
		null_ = null;
		profile1 = new Profile("Kory");
		profile2 = new Profile("Tanner");
		profile3 = new Profile("Sam");
	}

	@Test
	void testProfile() 
	{
		assert(!this.default_.equals(null));
		assert(this.null_ == null);
		assert(!this.profile1.equals(null));
		assert(!this.profile2.equals(null));
		assert(!this.profile3.equals(null));
	}

	@Test
	void testGetHistory() 
	{
		assertTrue(this.default_.getHistory().equals(this.default_.history));
		assertTrue(this.profile1.getHistory().equals(this.profile1.history));
		assertFalse(this.profile2.getHistory().equals(this.profile1.history));
		assertFalse(this.profile3.getHistory().equals(this.profile2.history));
	}

	@Test
	void testToString() 
	{
	 	assertTrue(this.default_.toString().equals("default\n" + this.default_.history.toString()));
		assertTrue(this.profile1.toString().equals("Kory\n" + this.profile1.history.toString()));
		assertTrue(this.profile2.toString().equals("Tanner\n" + this.profile2.history.toString()));
		assertFalse(this.profile3.toString().equals("Tanner\n" + this.profile2.history.toString()));
	}

}
