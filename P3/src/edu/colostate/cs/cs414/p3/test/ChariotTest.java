package edu.colostate.cs.cs414.p3.test;



import org.junit.Before;

import org.junit.Test;
import edu.colostate.cs.cs414.p3.banqi.Chariot;
/**
 * @author Sam Maxwell
 *
 */
public class ChariotTest {

	Chariot cRed,cBlack;

	@Before
	public	void setUp() throws Exception {
		cRed= new Chariot(true);
		cBlack= new Chariot(false);
	}

	@Test
	public void testChariotRank() {
		assert(cRed.getRank()==4);
		
	}
	public void testChariotFaceUp() {
		assert(!cRed.isFaceUp());
		
	}
	@Test
	public	void testChariotColorRed() {
		assert(cRed.isColor());
		
	}
	@Test
	public	void testChariotColorBlack() {
		assert(!cBlack.isColor());
		
	}
	@Test
	public void testChariotIconRed() {
		assert(cRed.getIcon().equals("R4"));
		
	}
	@Test
	public	void testChariotIconBlack() {
		assert(cBlack.getIcon().equals("B4"));
		
	}
}
