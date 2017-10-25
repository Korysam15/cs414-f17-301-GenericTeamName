package edu.colostate.cs.cs4141.p3.test;



import org.junit.Before;

import org.junit.Test;
import edu.colostate.cs.cs414.p3.banqi.General;

/**
 * @author Sam Maxwell
 *
 */
public class GeneralTest {

	General gRed,gBlack;

	@Before
	public void setUp() throws Exception {
		gRed= new General(true);
		gBlack= new General(false);
	}

	@Test
	public void testgRedRank() {
		assert(gRed.getRank()==7);
		
	}
	public void testgRedFaceUp() {
		assert(!gRed.isFaceUp());
		
	}
	@Test
	public void testgRedColorRed() {
		assert(gRed.isColor());
		
	}
	@Test
	public void testgRedColorBlack() {
		assert(!gBlack.isColor());
		
	}
	@Test
	public void testgRedIconRed() {
		assert(gRed.getIcon().equals("R7"));
		
	}
	@Test
	public void testgRedIconBlack() {
		assert(gBlack.getIcon().equals("B7"));
		
	}

}
