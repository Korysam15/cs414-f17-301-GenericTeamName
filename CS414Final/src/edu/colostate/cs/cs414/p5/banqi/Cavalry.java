package edu.colostate.cs.cs414.p5.banqi;
/**
 * @author Sam Maxwell
 *
 */
public class Cavalry extends Piece {
	public static final int RANK=3;
	public Cavalry(boolean color) {
		
		super.rank=RANK;
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
