package edu.colostate.cs.cs4141.p3.test;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.colostate.cs.cs414.p4.user.History;
import edu.colostate.cs.cs414.p4.user.Profile;

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
