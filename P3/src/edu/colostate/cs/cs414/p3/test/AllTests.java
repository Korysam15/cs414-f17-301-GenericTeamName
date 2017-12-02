package edu.colostate.cs.cs414.p3.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import junit.framework.JUnit4TestAdapter;

@RunWith(Suite.class)
@SuiteClasses({ AdvisorTest.class, BanqiGameTest.class, CannonTest.class, CavalryTest.class, ChariotTest.class,
		ElephantTest.class, GameBoardTest.class, GameRecordTest.class, GeneralTest.class, HistoryTest.class,
		PieceTest.class, PlayerTest.class, ProfileTest.class, SoldierTest.class, SquareTest.class })
public class AllTests {
	public static void main (String[] args)
	{
		junit.textui.TestRunner.run (suite());
	}
	
	public static junit.framework.Test suite()
	{
		return new JUnit4TestAdapter(AllTests.class);
	}

}
