package edu.colostate.cs.cs4141.p3.test;



import org.junit.Before;

import org.junit.Test;
import edu.colostate.cs.cs414.p3.banqi.BanqiGame;
import edu.colostate.cs.cs414.p3.banqi.Square;
import edu.colostate.cs.cs414.p3.user.Player;
/**
 * @author Sam Maxwell
 *
 */
public class BanqiGameTest {
	
	BanqiGame bG;

	@Before
	public void setUp() throws Exception {
		
		bG= new BanqiGame(0,true); 
		
	}





	@Test
	public void testMakeMoveSquareNullSquare() {
		Square to = new Square(0,0);
		assert(bG.makeMove(null, to)==false);
		
		
	}
	@Test
	public void testMakeMoveSquareSquareNull() {
		Square from = new Square(0,0);
		assert(bG.makeMove(from, null)==false);
	}
	@Test
	public void testMakeMoveSquareNullSquareNull() {
		
		assert(bG.makeMove(null, null)==false);
		
	}
	@Test
	public void testMakeMoveSquareOnNullSquare() {
		Square from = new Square(0,0);
		from.setOn(null);
		Square to = new Square(1,0);
		assert(bG.makeMove(from, to)==false);
		
		
	}
	@Test
	public void testMakeMoveSquareSquare() {
		Square from = new Square(0,0);
		from.setOn(null);
		Square to = new Square(1,0);
		assert(bG.makeMove(from, to)==false);
	}
	
	
	@Test
	public void testMakeMoveSquare() {
		Square from = new Square(0,0);
		assert(bG.makeMove(from)==false);
	}

	@Test
	public void testFlipPiece() {
		
		Square from=bG.getGameBoard().getSquare(0, 0);
		assert(bG.flipPiece(from)==true);
	}

	@Test
	public void testGetValidMoves() {
		
		assert(bG.getValidMoves(new Square(0,0)).isEmpty());
	}

	@Test
	public void testCanOverTake() {
		Square from=bG.getGameBoard().getSquare(0, 0);
		Square to=bG.getGameBoard().getSquare(1, 0);
		assert(bG.canOverTake(from, to)==false);
	}



	

	@Test
	public void testGetSquareString() {
		assert(bG.getSquare("A1").equals(new Square(0,0)));
		
	}

	@Test
	public void testGetSquareIntInt() {
		assert(bG.getSquare(0,0).equals(new Square(0,0)));
		
		
	}

}
