package edu.colostate.cs.cs414.p3.test;



import org.junit.Before;

import org.junit.Test;
import edu.colostate.cs.cs414.p3.banqi.Piece;
import edu.colostate.cs.cs414.p3.banqi.Soldier;
/**
 * @author Sam Maxwell
 *
 */
public class PieceTest {

	Piece p;
	@Before
	public void setUp() throws Exception {
		p= new Soldier(true);
	}

	
	

	@Test
	public void testFlipPiece() {
		p.flipPiece();
		assert(p.isFaceUp()==true);
	}

}
