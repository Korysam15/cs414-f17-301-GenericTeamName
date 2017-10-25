package edu.colostate.cs.cs414.p3.test;


import org.junit.Before;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import edu.colostate.cs.cs414.p3.banqi.Cannon;
/**
 * @author Sam Maxwell
 *
 */
public class CannonTest {

	Cannon cRed,cBlack;

	@Before
	public void setUp() throws Exception {
		cRed= new Cannon(true);
		cBlack= new Cannon(false);
	}

	@Test
	public void testCannonRank() {
		assert(cRed.getRank()==2);
		
	}
	public void testCannonFaceUp() {
		assert(!cRed.isFaceUp());
		
	}
	@Test
	public void testCannonColorRed() {
		assert(cRed.isColor());
		
	}
	@Test
	public void testCannonColorBlack() {
		assert(!cBlack.isColor());
		
	}
	@Test
	public void testCannonIconRed() {
		assert(cRed.getIcon().equals("R2"));
		
	}
	@Test
	public void testCannonIconBlack() {
		assert(cBlack.getIcon().equals("B2"));
		
	}
}
