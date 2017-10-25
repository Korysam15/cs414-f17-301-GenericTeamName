package edu.colostate.cs.cs414.p3.test;



import org.junit.Before;

import org.junit.Test;
import edu.colostate.cs.cs414.p3.banqi.Soldier;
/**
 * @author Sam Maxwell
 *
 */
public class SoldierTest {

	Soldier sRed,sBlack;

	@Before
	public void setUp() throws Exception {
		sRed= new Soldier(true);
		sBlack= new Soldier(false);
	}

	@Test
	public void testSoldierRank() {
		assert(sRed.getRank()==1);
		
	}
	public void testSoldierFaceUp() {
		assert(!sRed.isFaceUp());
		
	}
	@Test
	public void testSoldierColorRed() {
		assert(sRed.isColor());
		
	}
	@Test
	public void testSoldierColorBlack() {
		assert(!sBlack.isColor());
		
	}
	@Test
	public void testSoldierIconRed() {
		assert(sRed.getIcon().equals("R1"));
		
	}
	@Test
	public void testSoldierIconBlack() {
		assert(sBlack.getIcon().equals("B1"));
		
	}

}
