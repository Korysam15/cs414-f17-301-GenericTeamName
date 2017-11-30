package edu.colostate.cs.cs414.p5.banqi;

/**
 * @author Sam Maxwell
 *
 */
public class Advisor extends Piece {
	public static final int RANK=6; 
	public Advisor(boolean color) {
		super.rank=RANK;
		super.color=color;
		if(color){
			super.icon="R6";
		}
		else{
			super.icon="B6";
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
