package edu.colostate.cs.cs414.p4.banqi;

/**
 * @author Sam Maxwell
 *
 */
public class Advisor extends Piece {

	public Advisor(boolean color) {
		super.rank=6;
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
