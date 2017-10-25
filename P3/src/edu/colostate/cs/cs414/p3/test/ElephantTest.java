package edu.colostate.cs.cs414.p3.test;



import org.junit.Before;

import org.junit.Test;
import edu.colostate.cs.cs414.p3.banqi.Elephant;
/**
 * @author Sam Maxwell
 *
 */
public class ElephantTest {
	Elephant eRed,eBlack;

	@Before
	public void setUp() throws Exception {
		eRed= new Elephant(true);
		eBlack= new Elephant(false);
	}

	@Test
	public	void testElephantRank() {
		assert(eRed.getRank()==5);
		
	}
	public	void testElephantFaceUp() {
		assert(!eRed.isFaceUp());
		
	}
	@Test
	public	void testElephantColorRed() {
		assert(eRed.isColor());
		
	}
	@Test
	public	void testElephantColorBlack() {
		assert(!eBlack.isColor());
		
	}
	@Test
	public	void testElephantIconRed() {
		assert(eRed.getIcon().equals("R5"));
		
	}
	@Test
	public	void testElephantIconBlack() {
		assert(eBlack.getIcon().equals("B5"));
		
	}

}
