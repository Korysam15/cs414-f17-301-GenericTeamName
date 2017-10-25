package user;

import static org.junit.Assert.*;

import org.junit.Test;

public class ProfileTest {

	

	@Test
	public void testProfile() {
		Profile p = new Profile("nick");
	}

	@Test
	public void testGetHistory() {
		Profile p = new Profile("nick");
		History h = p.getHistory();
	}

	

	@Test
	public void testGetName() {
		Profile p = new Profile("nick");
		assertEquals(p.getName(), "nick");
	}

	@Test
	public void testEqualsObject() {
		Profile p1 = new Profile("nick");
		Profile p2 = new Profile("nick");
		assert(p1.equals(p2));
	}

}
