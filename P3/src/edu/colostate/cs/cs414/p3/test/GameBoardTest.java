package edu.colostate.cs.cs414.p3.test;



import org.junit.Before;
import org.junit.Test;

import edu.colostate.cs.cs414.p3.banqi.GameBoard;
import edu.colostate.cs.cs414.p3.banqi.Square;
/**
 * @author Sam Maxwell
 *
 */
public class GameBoardTest {
	GameBoard gB;
	@Before
	public	void setUp() throws Exception {
		gB= new GameBoard();
	}

	

	@Test
	public	void testCreateSquares() {
		Square squares[]= gB.createSquares();
		for(int i=0; i<squares.length;i++) {
			assert(gB.getSquaresOnBoard()[i].equals(squares[i]));
		}
		
		
	}

	@Test
	public	void testGetSquare0() {
		Square first= new Square(0,0);
		Square firstgB=gB.getSquare(0, 0);
		assert(first.equals(firstgB));
		
		
	}
	@Test
	public	void testGetSquareNullX() {
		Square n=gB.getSquare(8, 0);
		assert(n==null);
	}
	@Test
	public	void testGetSquareNullY() {
		Square n=gB.getSquare(0, 4);
		assert(n==null);
	}
	@Test
	public void testGetSquareNullXY() {
		Square n=gB.getSquare(8, 4);
		assert(n==null);
	}

	

}
