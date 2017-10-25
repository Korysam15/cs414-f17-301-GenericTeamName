package edu.colostate.cs.cs414.p3.banqi;
/**
 * @author Sam Maxwell
 *
 */
public class Cavalry extends Piece {

	public Cavalry(boolean color) {
		super.rank=3;
		super.color=color;
		if(color){
			super.icon="R3";
		}
		else{
			super.icon="B3";
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
