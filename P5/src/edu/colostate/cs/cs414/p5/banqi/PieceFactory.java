package edu.colostate.cs.cs414.p5.banqi;

/**
 * 
 * @author pflagert
 *
 */
public class PieceFactory {
	private static final PieceFactory instance = new PieceFactory();
	
	public static PieceFactory getInstance() {
		return instance;
	}
	
	private PieceFactory() {}
	
	public Piece createPiece(int rank, boolean color) {
		switch(rank) {
		case Advisor.RANK:
			return new Advisor(color);
		case Cannon.RANK:
			return new Cannon(color);
		case Cavalry.RANK:
			return new Cavalry(color);
		case Chariot.RANK:
			return new Chariot(color);
		case Elephant.RANK:
			return new Elephant(color);
		case General.RANK:
			return new General(color);
		case Soldier.RANK:
			return new Soldier(color);
		default:
			throw new IllegalArgumentException("Invalid piece rank: " + rank);
		}
	}
}
