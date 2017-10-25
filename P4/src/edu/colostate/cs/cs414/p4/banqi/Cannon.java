package edu.colostate.cs.cs414.p4.banqi;
/**
 * @author Sam Maxwell
 *
 */
public class Cannon extends Piece {

	public Cannon(boolean color) {
		super.rank=2;
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
