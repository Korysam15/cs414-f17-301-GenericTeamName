package edu.colostate.cs.cs414.p3.test;

import org.junit.Before;

import org.junit.Test;
import edu.colostate.cs.cs414.p3.banqi.Advisor;
/**
 * @author Sam Maxwell
 *
 */
public class AdvisorTest {
	edu.colostate.cs.cs414.p3.banqi.Advisor aRed,aBlack;

	@Before
	public void setUp() throws Exception {
		aRed= new Advisor(true);
		aBlack= new Advisor(false);
	}

	@Test
	public void testAdvisorRank() {
		assert(aRed.getRank()==6);
		
	}
	public void testAdvisorFaceUp() {
		assert(!aRed.isFaceUp());
		
	}
	@Test
	public void testAdvisorColorRed() {
		assert(aRed.isColor());
		
	}
	@Test
	public void testAdvisorColorBlack() {
		assert(!aBlack.isColor());
		
	}
	@Test
	public void testAdvisorIconRed() {
		assert(aRed.getIcon().equals("R6"));
		
	}
	@Test
	public void testAdvisorIconBlack() {
		assert(aBlack.getIcon().equals("B6"));
		
	}



}
