package edu.colostate.cs.cs414.p5.banqi;
/**
 * @author Sam Maxwell
 *
 */
public class Chariot extends Piece {

	public Chariot(boolean color) {
		super.rank=4;
		super.color=color;
		if(color){
			super.icon="R4";
		}
		else{
			super.icon="B4";
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
