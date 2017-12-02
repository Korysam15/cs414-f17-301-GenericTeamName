package edu.colostate.cs.cs414.p5.banqi;
/**
 * @author Sam Maxwell
 *
 */
public class Cannon extends Piece {
	public static final int RANK=2;
	public Cannon(boolean color) {
		super.rank=RANK;
		super.color=color;
		if(color){
			super.icon="R2";
		}
		else{
			super.icon="B2";
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
