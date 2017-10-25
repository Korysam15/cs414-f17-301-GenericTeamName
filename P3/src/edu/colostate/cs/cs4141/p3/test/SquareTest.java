package edu.colostate.cs.cs4141.p3.test;



import org.junit.Before;

import org.junit.Test;
import edu.colostate.cs.cs414.p3.banqi.Piece;
import edu.colostate.cs.cs414.p3.banqi.Soldier;
import edu.colostate.cs.cs414.p3.banqi.Square;
/**
 * @author Sam Maxwell
 *
 */
public class SquareTest {
	Square s;
	@Before
	public void setUp() throws Exception {
		s= new Square(0,0);
	}

	@Test
	public void testSetOn() {
		Piece p = new Soldier(true);
		s.setOn(p);
		assert(s.getOn().equals(p));
		
	}

	@Test
	public void testGetX() {
		assert(s.getX()==0);
	}

	@Test
	public void testSetX() {
		s.setX(1);
		assert(s.getX()==1);
	}

	@Test
	public void testGetY() {
		assert(s.getY()==0);
	}

	@Test
	public void testSetY() {
		s.setY(1);
		assert(s.getY()==1);
	}

	@Test
	public void testGetOn() {
		Piece p = new Soldier(true);
		s.setOn(p);
		assert(s.getOn().equals(p));
	}

	@Test
	public void testIsEmpty() {
		assert(s.isEmpty());
	}
	@Test
	public void testEqualsTrue() {
		Square s0= new Square(0,0);
		assert(s.equals(s0));
		
	}
	@Test
	public void testEqualsFalseX() {
		Square s0= new Square(1,0);
		assert(!s.equals(s0));
		
	}
	@Test
	public void testEqualsFalseY() {
		Square s0= new Square(0,1);
		assert(!s.equals(s0));
		
	}
	@Test
	public void testEqualsFalseObj() {
		Piece p= new Soldier(true);
		assert(!s.equals(p));
		
	}

}
