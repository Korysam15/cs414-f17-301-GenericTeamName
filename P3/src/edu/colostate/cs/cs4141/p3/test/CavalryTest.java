package edu.colostate.cs.cs4141.p3.test;



import org.junit.Before;

import org.junit.Test;

import edu.colostate.cs.cs414.p3.banqi.Cavalry;
/**
 * @author Sam Maxwell
 *
 */
public class CavalryTest {
	Cavalry cRed,cBlack;

	@Before
	public void setUp() throws Exception {
		cRed= new Cavalry(true);
		cBlack= new Cavalry(false);
	}

	@Test
	public void testCavalryRank() {
		assert(cRed.getRank()==3);
		
	}
	public void testCavalryFaceUp() {
		assert(!cRed.isFaceUp());
		
	}
	@Test
	public void testCavalryColorRed() {
		assert(cRed.isColor());
		
	}
	@Test
	public void testCavalryColorBlack() {
		assert(!cBlack.isColor());
		
	}
	@Test
	public void testCavalryIconRed() {
		assert(cRed.getIcon().equals("R3"));
		
	}
	@Test
	public void testCavalryIconBlack() {
		assert(cBlack.getIcon().equals("B3"));
		
	}
}
